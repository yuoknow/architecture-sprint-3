package ru.yandex.practicum.smarthome.devicesmanagement.out.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class KafkaDeviceCommandSender {
    private final Map<String, Object> producerProps;

    public void send(DeviceCommand command) {
        try (var producer = createProducer()) {
            log.info("Send command to load entity: {}", command);
            producer.send(new ProducerRecord<>("device-command", command));
        }
    }

    private KafkaProducer<String, DeviceCommand> createProducer() {
        return new KafkaProducer<>(producerProps, new StringSerializer(), new RegisterDeviceCommandSerializer());
    }


}
