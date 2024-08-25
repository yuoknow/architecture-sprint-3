package ru.yandex.practicum.smarthome.telemetryservice.out;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.telemetryservice.domain.sensor.Sensor;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SensorRepository {
    private final JdbcClient jdbcClient;

    public void insert(Sensor sensor) {
        jdbcClient.sql("insert into sensors (device_id, user_id, model, url) values (:deviceId, :userId, :model, :url)")
                .param("deviceId", sensor.deviceId())
                .param("userId", sensor.userId())
                .param("model", sensor.model())
                .param("url", sensor.url())
                .update();
    }

    public void deleteByDeviceId(UUID deviceId) {
        jdbcClient.sql("delete from sensors where device_id = :deviceId")
                .param("deviceId", deviceId)
                .update();
    }
}
