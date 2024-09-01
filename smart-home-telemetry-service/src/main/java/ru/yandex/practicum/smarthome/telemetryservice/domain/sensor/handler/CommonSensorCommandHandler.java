package ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorCommand;
import ru.yandex.practicum.smarthome.telemetryservice.in.messaging.SensorEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommonSensorCommandHandler {
    private final Map<SensorCommand.CommandType, CommandHandler> commandHandlers;

    public CommonSensorCommandHandler(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers.stream()
                .collect(Collectors.toMap(CommandHandler::getCommandType, c -> c));
    }

    public SensorEvent handle(SensorCommand command) {
        final CommandHandler commandHandler = commandHandlers.get(command.getType());
        try {
            return commandHandler.handle(command);
        } catch (Exception e) {
            return SensorEvent.builder()
                    .type(SensorEvent.EventType.ERROR)
                    .build();
        }
    }
}
