package ru.yandex.practicum.smarthome.devicesmanagement.in.web.home;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.handler.AddHomeCommandHandler;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddHomeCommandController {
    private final AddHomeCommandHandler handler;

    @PostMapping("/api/home")
    public void addHome(@RequestHeader UUID userId, @RequestBody AddHomeCommand command) {
        handler.handle(userId, command);
    }

    public record AddHomeCommand(UUID id, String name, String address) {
    }
}
