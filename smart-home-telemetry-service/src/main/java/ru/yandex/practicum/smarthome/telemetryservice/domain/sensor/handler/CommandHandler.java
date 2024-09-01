package ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.handler;

import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorCommand;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorEvent;

public interface CommandHandler {

    SensorEvent handle(SensorCommand deviceCommand);

    SensorCommand.CommandType getCommandType();
}
