package ru.yandex.practicum.smarthome.devicesmanagement.out.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.device.Device;

import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class DeviceRepository {
    private final JdbcClient jdbcClient;

    public void insert(Device device) {
        jdbcClient.sql("""
                        insert into devices (device_id, user_id, home_id, device_type, model, serial_number, url) \
                        values (:deviceId, :userId, :homeId, :deviceType, :model, :serialNumber, :url)
                        """)
                .param("deviceId", device.deviceId())
                .param("userId", device.userId())
                .param("homeId", device.homeId())
                .param("model", device.model())
                .param("serialNumber", device.serialNumber())
                .param("deviceType", device.deviceType().name())
                .param("url", device.url())
                .update();
    }

    public Device findById(UUID id) {
        return jdbcClient.sql("SELECT * from devices where device_id = :deviceId")
                .param("deviceId", id)
                .query(Device.class).single();
    }

    public void deleteById(UUID deviceId) {
        jdbcClient.sql("delete from devices where device_id = :deviceId")
                .param("deviceId", deviceId)
                .update();
    }
}
