package ru.yandex.practicum.smarthome.devicesmanagement.domain.exception;

public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
