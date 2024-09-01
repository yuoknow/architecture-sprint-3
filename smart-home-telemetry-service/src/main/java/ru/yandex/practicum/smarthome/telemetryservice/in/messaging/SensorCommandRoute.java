package ru.yandex.practicum.smarthome.telemetryservice.in.messaging;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.handler.CommonSensorCommandHandler;

@Component
@RequiredArgsConstructor
public class SensorCommandRoute extends RouteBuilder {
    private final CommonSensorCommandHandler handler;

    @Override
    public void configure() throws Exception {
        CamelSagaService sagaService = new InMemorySagaService();
        getContext().addService(sagaService);

        from("kafka:sensor-command")
                .unmarshal(new JacksonDataFormat(SensorCommand.class))
                .setBody(exchange -> {
                    var command = exchange.getIn().getBody(SensorCommand.class);
                    return handler.handle(command);
                })
                .marshal(new JacksonDataFormat(SensorEvent.class))
                .to("kafka:sensor-event");

    }
}
