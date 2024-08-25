package ru.yandex.practicum.smarthome.in.messaging;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeviceEvent {
    private Device device;
    private EventType eventType;

    public record Device(UUID deviceId, UUID homeId, UUID userId, String deviceType,
                         String model, String serialNumber, String url) {
    }

    public enum EventType {
        REGISTERED, REMOVED
    }
}
