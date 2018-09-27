package com.tiket.poc.mongokafkatestingsample.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;

/**
 * @author zakyalvan
 */
@Configuration
public class KafkaSampleConfiguration {
    public static final int TOPIC_PARTITIONS = 8;

    @Bean
    ConsumerFactory<?, ?> consumerFactory(KafkaProperties properties, ObjectMapper mapper) {
        DefaultKafkaConsumerFactory<String, SimpleMessage> factory = new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
        factory.setValueDeserializer(kafkaJsonSerde().deserializer());
        return factory;
    }

    @Bean
    ProducerFactory<?, ?> producerFactory(KafkaProperties properties, ObjectMapper objectMapper) {
        DefaultKafkaProducerFactory<String, SimpleMessage> factory = new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
        factory.setValueSerializer(kafkaJsonSerde().serializer());
        return factory;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    JsonSerde<SimpleMessage> kafkaJsonSerde() {
        return new JsonSerde<>(SimpleMessage.class, objectMapper);
    }

    @Bean
    NewTopic sampleTopic(@Value("${tiket.sample.kafka.simple-message.topic}") String topicName) {
        return new NewTopic(topicName, TOPIC_PARTITIONS, (short) 1);
    }
}
