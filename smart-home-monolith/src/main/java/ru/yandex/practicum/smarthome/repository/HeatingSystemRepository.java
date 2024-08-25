package ru.yandex.practicum.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.smarthome.entity.HeatingSystem;

import java.util.UUID;

@Repository
public interface HeatingSystemRepository extends JpaRepository<HeatingSystem, Long> {
    @Transactional
    void deleteByExternalId(UUID externalId);
}
