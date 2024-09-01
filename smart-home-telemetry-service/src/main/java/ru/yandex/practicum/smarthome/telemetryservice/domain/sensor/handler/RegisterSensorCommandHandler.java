package ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.Sensor;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorCommand;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorEvent;
import ru.yandex.practicum.smarthome.telemetryservice.out.SensorRepository;

@Component
@RequiredArgsConstructor
public class RegisterSensorCommandHandler implements CommandHandler {
    private final SensorRepository sensorRepository;

    @Override
    public SensorEvent handle(SensorCommand deviceCommand) {
        var register = deviceCommand.getRegisterSensorCommand();
        sensorRepository
                .insert(new Sensor(register.deviceId(), register.userId(), register.model(), register.url()));

        return SensorEvent.builder()
                .deviceId(register.deviceId())
                .type(SensorEvent.EventType.REGISTERED)
                .build();
    }

    @Override
    public SensorCommand.CommandType getCommandType() {
        return SensorCommand.CommandType.REGISTER;
    }
}
