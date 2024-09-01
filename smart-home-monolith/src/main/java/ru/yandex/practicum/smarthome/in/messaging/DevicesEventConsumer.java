package ru.yandex.practicum.smarthome.in.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.entity.HeatingSystem;
import ru.yandex.practicum.smarthome.repository.HeatingSystemRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DevicesEventConsumer {
    private final HeatingSystemRepository heatingSystemRepository;

    @KafkaListener(topics = "device-event", groupId = "monolith-device-event",
            containerFactory = "listenerContainerFactory")
    public void listen(DeviceEvent deviceEvent) {
        log.info("Received device event: {}", deviceEvent);
        if (deviceEvent.getDevice() != null
                && deviceEvent.getDevice().deviceType().equals("HEATING_SYSTEM")) {
            var device = deviceEvent.getDevice();
            switch (deviceEvent.getEventType()) {
                case REGISTERED ->
                        heatingSystemRepository.save(new HeatingSystem(0L, device.deviceId(), false, 0.0, 0.0));
                case REMOVED -> heatingSystemRepository.deleteByExternalId(device.deviceId());
            }
        }
    }
}
