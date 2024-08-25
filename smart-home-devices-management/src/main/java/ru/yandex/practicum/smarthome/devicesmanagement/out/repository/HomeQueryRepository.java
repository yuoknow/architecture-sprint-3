package ru.yandex.practicum.smarthome.devicesmanagement.out.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.devicesmanagement.domain.home.Home;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HomeQueryRepository {
    private final JdbcClient jdbcClient;

    public Home findByIdAndUserId(UUID homeId, UUID userId) {
        return jdbcClient.sql("SELECT * from homes where home_id = :homeId and user_id = :userId")
                .param("homeId", homeId)
                .param("userId", userId)
                .query(Home.class).single();
    }

    public List<Home> findAllByUserId(UUID userId) {
        return jdbcClient.sql("SELECT * from homes where user_id = :userId")
                .param("userId", userId)
                .query(Home.class).list();
    }
}
