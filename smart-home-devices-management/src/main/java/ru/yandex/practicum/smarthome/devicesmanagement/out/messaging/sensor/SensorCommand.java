package ru.yandex.practicum.smarthome.devicesmanagement.out.messaging.sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SensorCommand {
    private CommandType type;
    private RegisterSensorCommand registerSensorCommand;
    private RemoveSensorCommand removeDeviceCommand;

    public enum CommandType {
        REGISTER, REMOVE
    }

    public record RegisterSensorCommand(UUID deviceId, UUID userId, String model, String url) {}

    public record RemoveSensorCommand(UUID deviceId) {}
}
