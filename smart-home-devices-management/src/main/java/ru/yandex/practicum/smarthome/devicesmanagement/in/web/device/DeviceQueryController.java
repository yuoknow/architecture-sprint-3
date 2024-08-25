package ru.yandex.practicum.smarthome.devicesmanagement.in.web.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.device.Device;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.DeviceQueryRepository;
import ru.yandex.practicum.smarthome.devicesmanagement.out.repository.DeviceRepository;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class DeviceQueryController {
    private final DeviceQueryRepository deviceRepository;

    @GetMapping("api/devices/{deviceId}")
    public Device getDevice(@RequestHeader UUID userId, @PathVariable("deviceId") final UUID deviceId) {
        return deviceRepository.findByIdAndUserId(deviceId, userId);
    }

    @GetMapping("api/devices")
    public List<Device> getAllDevices(@RequestHeader UUID userId) {
        return deviceRepository.findAllByUserId(userId);
    }
}
