package ru.yandex.practicum.smarthome.devicesmanagement.out.messaging.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.device.Device;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEvent {
    private EventType eventType;
    private Device device;


    public enum EventType {
        REGISTERED, REMOVED
    }
}
