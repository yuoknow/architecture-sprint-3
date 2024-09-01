package ru.yandex.practicum.smarthome.devicesmanagement.domain.home.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.HomeRepository;

import java.util.UUID;

import static ru.yandex.practicum.smarthome.devicesmanagement.in.web.home.RemoveHomeCommandController.RemoveHomeCommand;

@Component
@RequiredArgsConstructor
public class RemoveHomeCommandHandler {
    private final HomeRepository homeRepository;

    public void handle(UUID userId, RemoveHomeCommand command) {
        homeRepository.deleteById(command.id(), userId);
    }
}
