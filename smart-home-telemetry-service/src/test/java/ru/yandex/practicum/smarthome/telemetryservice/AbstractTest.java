package ru.yandex.practicum.smarthome.telemetryservice;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine")
            .withUrlParam("serverTimezone", "UTC")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))
            ))
            .withTmpFs(Map.of("/var", "rw"))
            .withEnv("PGDATA", "/var/lib/postgresql/data-no-mounted")
            .withReuse(true);


    static {
        Instant start = Instant.now();
        try {
            var conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5555/postgres",
                    postgres.getUsername(),
                    postgres.getPassword()
            );
            conn.prepareStatement("""
                        DROP DATABASE IF EXISTS test;
                        CREATE DATABASE test;
                    """).execute();

        } catch (SQLException e) {
            System.out.println("🐳 " + e.getMessage());
            postgres.start();
        }
        System.out.println("🐳 TestContainers started in " + Duration.between(start, Instant.now()));
    }

}
