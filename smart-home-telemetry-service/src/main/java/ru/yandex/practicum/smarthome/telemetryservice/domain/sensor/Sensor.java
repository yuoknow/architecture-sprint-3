package ru.yandex.practicum.smarthome.telemetryservice.domain.sensor;

import java.util.UUID;

public record Sensor(UUID deviceId, UUID userId, String model, String url) {
}
