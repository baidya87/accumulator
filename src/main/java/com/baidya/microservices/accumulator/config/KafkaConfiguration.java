package com.baidya.microservices.accumulator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class KafkaConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfiguration.class);
    @Bean
    public KafkaMessageListenerContainer<Integer, String> kafkaMessageListenerContainer(){
        KafkaMessageListenerContainer<Integer, String> kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties());
        return kafkaMessageListenerContainer;
    }

    @Bean
    public DefaultKafkaConsumerFactory<Integer, String> consumerFactory() {
        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        DefaultKafkaConsumerFactory<Integer, String > defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        return defaultKafkaConsumerFactory;
    }

    @Bean
    public ContainerProperties containerProperties() {
        ContainerProperties containerProperties = new ContainerProperties("stock.info");
        containerProperties.setGroupId("grp-xyz");
        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL);
        containerProperties.setMessageListener(acknowledgingMessageListener());
        return containerProperties;
    }

    @Bean
    public AcknowledgingMessageListener<Integer, String> acknowledgingMessageListener(){
        return (record, ack) ->{
            LOGGER.info("Key: {} Record: {} Timestamp: {} ", record.key(), record.value(), record.timestamp());
            ack.acknowledge();
        };
    }

}
