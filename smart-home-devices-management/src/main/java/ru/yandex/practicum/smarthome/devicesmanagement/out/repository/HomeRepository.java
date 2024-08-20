package ru.yandex.practicum.smarthome.devicesmanagement.out.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.Home;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HomeRepository {
    private final JdbcClient jdbcClient;

    public void insert(Home home) {
        jdbcClient.sql("insert into homes (id, user_id, name, address) values (:id, :userId, :name, :address)")
                .param("id", home.id())
                .param("userId", home.userId())
                .param("name", home.name())
                .param("address", home.address())
                .update();
    }

    public void deleteById(UUID homeId, UUID userId) {
        jdbcClient.sql("delete from homes where id = :id and user_id = :userId")
                .param("id", homeId)
                .param("userId", userId)
                .update();
    }
}
