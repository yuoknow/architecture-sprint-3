package ru.yandex.practicum.smarthome.devicesmanagement.in.web.home;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.Home;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.HomeQueryRepository;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.HomeRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeQueryController {
    private final HomeQueryRepository homeRepository;

    @GetMapping("api/homes/{homeId}")
    public Home getHome(@RequestHeader UUID userId, @PathVariable("homeId") final UUID homeId) {
        return homeRepository.findByIdAndUserId(homeId, userId);
    }

    @GetMapping("api/homes")
    public List<Home> getAllHomes(@RequestHeader UUID userId) {
        return homeRepository.findAllByUserId(userId);
    }
}
