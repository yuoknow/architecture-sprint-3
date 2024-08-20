package ru.yandex.practicum.smarthome.devicesmanagement.domain.home;

import java.util.UUID;

public record Home(UUID id, UUID userId, String name, String address) {
}
