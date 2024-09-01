package ru.yandex.practicum.smarthome.devicesmanagement.in.web.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RemoveDeviceCommandController {
    private final ProducerTemplate producerTemplate;

    @DeleteMapping("api/devices/{deviceId}")
    public void registerDevice(@RequestHeader UUID userId, @PathVariable UUID deviceId) {
        log.info("Removing device {} userId {}", deviceId, userId);
        producerTemplate.sendBody("direct:removeDeviceSaga",
                new RemoveDeviceCommand(deviceId));
    }

    public record RemoveDeviceCommand(UUID deviceId) {
    }
}
