package ru.yandex.practicum.smarthome.devicesmanagement.domain.home.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.Home;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.HomeRepository;

import java.util.UUID;

import static ru.yandex.practicum.smarthome.devicesmanagement.in.web.home.AddHomeCommandController.AddHomeCommand;

@Component
@RequiredArgsConstructor
public class AddHomeCommandHandler {
    private final HomeRepository homeRepository;

    public void handle(UUID userId, AddHomeCommand command) {
        homeRepository.insert(new Home(command.id(), userId, command.name(), command.address()));
    }
}
