package ru.yandex.practicum.smarthome.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.yandex.practicum.smarthome.in.messaging.DeviceEvent;

@Configuration
@ConditionalOnBean(KafkaProperties.class)
public class KafkaConfig {

    @Bean
    @ConditionalOnProperty("spring.kafka.bootstrap-servers")
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, DeviceEvent>>
    listenerContainerFactory(KafkaProperties kafkaProperties, ObjectProvider<SslBundles> sslBundles) {
        var defaultConsumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProperties.buildAdminProperties(sslBundles.getIfAvailable()),
                new StringDeserializer(), new DeviceEventDeserializer());
        var factory = new ConcurrentKafkaListenerContainerFactory<String, DeviceEvent>();
        factory.setConsumerFactory(defaultConsumerFactory);
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    public static class DeviceEventDeserializer extends JsonDeserializer<DeviceEvent> {
    }
}
