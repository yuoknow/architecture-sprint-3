package ru.yandex.practicum.smarthome.devicesmanagement.in.web.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.smarthome.devicesmanagement.out.messaging.DeviceSagas;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegisterDeviceCommandController {
    private final ProducerTemplate producerTemplate;

    @PostMapping("api/devices/{deviceId}")
    public void registerDevice(@RequestHeader UUID userId, @PathVariable UUID deviceId, @RequestBody RegisterDeviceCommandDto command) {
        log.info("Registering device {} userId {}", command, userId);
        producerTemplate.sendBody("direct:registerDeviceSaga",
                new DeviceSagas.RegisterDeviceCommand(deviceId, userId, command.homeId,
                        command.deviceType, command.serialNumber, command.model, command.url));
    }

    public record RegisterDeviceCommandDto(
                                        UUID homeId, String deviceType,
                                        String serialNumber, String model, String url) {
    }
}
