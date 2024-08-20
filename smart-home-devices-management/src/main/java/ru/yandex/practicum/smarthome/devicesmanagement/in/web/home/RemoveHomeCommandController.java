package ru.yandex.practicum.smarthome.devicesmanagement.in.web.home;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.handler.RemoveHomeCommandHandler;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RemoveHomeCommandController {
    private final RemoveHomeCommandHandler handler;

    @DeleteMapping("/api/home")
    public void removeHome(@RequestHeader UUID userId, @RequestBody RemoveHomeCommand command) {
        handler.handle(userId, command);
    }

    public record RemoveHomeCommand(UUID id) {
    }
}
