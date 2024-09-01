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
public class SensorEvent {
    private UUID deviceId;
    private EventType type;

    public enum EventType {
        REGISTERED, REMOVED, ERROR
    }
}
