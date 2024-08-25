package ru.yandex.practicum.smarthome.devicesmanagement.domain.device;

import java.util.UUID;

public record Device(UUID deviceId, UUID homeId, UUID userId, DeviceType deviceType,
                     String model, String serialNumber, String url) {

    public enum DeviceType {
        HEATING_SYSTEM, CAMERA, GATE, LAMP
    }
}
