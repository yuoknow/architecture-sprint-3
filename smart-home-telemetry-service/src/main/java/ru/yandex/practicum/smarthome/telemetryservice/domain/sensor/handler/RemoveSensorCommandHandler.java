package ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorCommand;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorEvent;
import ru.yandex.practicum.smarthome.telemetryservice.out.SensorRepository;

@Component
@RequiredArgsConstructor
public class RemoveSensorCommandHandler implements CommandHandler {
    private final SensorRepository sensorRepository;

    @Override
    public SensorEvent handle(SensorCommand deviceCommand) {
        var remove = deviceCommand.getRemoveDeviceCommand();
        sensorRepository
                .deleteByDeviceId(remove.deviceId());

        return SensorEvent.builder()
                .deviceId(remove.deviceId())
                .type(SensorEvent.EventType.REMOVED)
                .build();
    }

    @Override
    public SensorCommand.CommandType getCommandType() {
        return SensorCommand.CommandType.REMOVE;
    }
}
