package ru.yandex.practicum.smarthome.devicesmanagement.out.messaging;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.SagaCompletionMode;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.device.Device;
import ru.yandex.practicum.smarthome.devicesmanagement.in.web.device.RemoveDeviceCommandController;
import ru.yandex.practicum.smarthome.devicesmanagement.out.messaging.device.DeviceEvent;
import ru.yandex.practicum.smarthome.devicesmanagement.out.messaging.sensor.SensorCommand;
import ru.yandex.practicum.smarthome.devicesmanagement.out.messaging.sensor.SensorEvent;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.DeviceRepository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DeviceSagas extends RouteBuilder {
    private final DeviceRepository deviceRepository;

    @Override
    public void configure() throws Exception {
        CamelSagaService sagaService = new InMemorySagaService();
        getContext().addService(sagaService);

        // REGISTER DEVICE SAGA
        from("direct:registerDeviceSaga")
                .saga()
                .timeout(30, TimeUnit.SECONDS)
                .completionMode(SagaCompletionMode.MANUAL)
                .propagation(SagaPropagation.REQUIRES_NEW)
                .setBody(exchange -> {
                    var command = exchange.getIn().getBody(RegisterDeviceCommand.class);
                    deviceRepository.insert(new Device(command.deviceId(), command.homeId(),
                            command.userId(), Device.DeviceType.valueOf(command.deviceType()),
                            command.model(), command.serialNumber(), command.url()));

                    return SensorCommand.builder()
                            .type(SensorCommand.CommandType.REGISTER)
                            .registerSensorCommand(new SensorCommand.RegisterSensorCommand(
                                    command.deviceId(), command.userId(), command.model(), command.url()
                            ))
                            .build();
                })
                .compensation("direct:registerDeviceCompensation")
                .completion("direct:registerDeviceCompletion")
                .marshal(new JacksonDataFormat(SensorCommand.class))
                .to("kafka:sensor-command");
        from("direct:registerDeviceCompletion")
                .log("Device registered");

        from("direct:registerDeviceCompensation")
                .process(process -> {
                    var deviceId = process.getIn().getBody(SensorEvent.class).getDeviceId();
                    deviceRepository.deleteById(deviceId);
                })
                .log("Error on registering device");

        // REMOVE DEVICE SAGA
        from("direct:removeDeviceSaga")
                .saga()
                .timeout(30, TimeUnit.SECONDS)
                .completionMode(SagaCompletionMode.MANUAL)
                .propagation(SagaPropagation.REQUIRES_NEW)
                .setBody(exchange -> {
                    var command = exchange.getIn().getBody(RemoveDeviceCommandController.RemoveDeviceCommand.class);
                    return SensorCommand.builder()
                            .type(SensorCommand.CommandType.REMOVE)
                            .removeDeviceCommand(new SensorCommand.RemoveSensorCommand(command.deviceId()))
                            .build();
                })
                .compensation("direct:removeDeviceCompensation")
                .completion("direct:removeDeviceCompletion")
                .marshal(new JacksonDataFormat(SensorCommand.class))
                .to("kafka:sensor-command");
        from("direct:removeDeviceCompletion")
                .log("Device removed");

        from("direct:removeDeviceCompensation")
                .log("Error on removing device");

        // SENSOR EVENT PROCESSING
        from("kafka:sensor-event")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .log("Event ${body}")
                .unmarshal(new JacksonDataFormat(SensorEvent.class))
                .choice()
                .when(exchange -> exchange.getMessage().getBody(SensorEvent.class).getType() == SensorEvent.EventType.ERROR)
                .to("saga:compensate")
                .otherwise()
                .to("direct:processSensorEvent")
                .to("saga:complete")
                .end();

        from("direct:processSensorEvent")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .setBody(exchange -> {
                    var sensorEvent = exchange.getIn().getBody(SensorEvent.class);
                    var deviceId = sensorEvent.getDeviceId();
                    var device = deviceRepository.findById(deviceId);
                    switch (sensorEvent.getType()) {
                        case SensorEvent.EventType.REGISTERED -> {
                            return new DeviceEvent(DeviceEvent.EventType.REGISTERED, device);
                        }
                        case SensorEvent.EventType.REMOVED -> {
                            deviceRepository.deleteById(deviceId);
                            return new DeviceEvent(DeviceEvent.EventType.REMOVED, device);
                        }
                        default -> throw new IllegalArgumentException();
                    }
                })
                .marshal(new JacksonDataFormat(DeviceEvent.class))
                .to("kafka:device-event");
    }

    public record RegisterDeviceCommand(UUID deviceId, UUID userId,
                                        UUID homeId, String deviceType,
                                        String serialNumber, String model, String url) {
    }
}
