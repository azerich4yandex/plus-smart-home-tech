package ru.yandex.practicum.service.producer;

import java.util.Properties;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.serialization.AvroSerializer;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Configuration
public class CollectorKafkaConfiguration {

    private KafkaProducer<String, SpecificRecordBase> producer;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public KafkaProducer<String, SpecificRecordBase> getProducer() {
        if (producer == null) {
            initProducer();
        }
        return producer;
    }

    private void initProducer() {
        Properties config = new Properties();
        config.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class.getName());
        producer = new KafkaProducer<>(config);
    }
}
