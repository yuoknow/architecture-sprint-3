package ru.yandex.practicum.smarthome.devicesmanagement.out.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.Home;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeviceRepository {
    private final JdbcClient jdbcClient;

    public void insert(Home home) {
        jdbcClient.sql("insert into homes (id, name, address) values (:id, :name, :address)")
                .param("id", home.id())
                .param("name", home.name())
                .param("address", home.address())
                .update();
    }

    public void deleteById(UUID homeId) {
        jdbcClient.sql("delete from homes where id = :id")
                .update();
    }
}
