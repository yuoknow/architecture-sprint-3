package ru.yandex.practicum.smarthome.devicesmanagement.out.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.device.Device;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class DeviceQueryRepository {
    private final JdbcClient jdbcClient;

    public Device findByIdAndUserId(UUID id, UUID userId) {
        return jdbcClient.sql("SELECT * from devices where device_id = :deviceId and user_id = :userId")
                .param("deviceId", id)
                .param("userId", userId)
                .query(Device.class).single();
    }


    public List<Device> findAllByUserId(UUID userId) {
        return jdbcClient.sql("SELECT * from devices where user_id = :userId")
                .param("userId", userId)
                .query(Device.class).list();
    }
}
