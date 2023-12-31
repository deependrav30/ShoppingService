/* * Copyright 2023-2024 the original author or authors. * * TBD */

package com.stickyio.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaOrderCommonConfig {

  @Value("${spring.kafka.consumer.group-id}")
  String CONSUMER_GROUPS;

  @Value("${spring.kafka.producer.bootstrap-servers}")
  String SERVER;

  @Value("${spring.kafka.producer.key-serializer}")
  String PRODUCER_KEY_SERIALIZER;

  @Value("${spring.kafka.producer.value-serializer}")
  String PRODUCER_VALUE_SERIALIZER;

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUPS);
    return props;
  }

  @Bean
  public Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, PRODUCER_KEY_SERIALIZER);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PRODUCER_VALUE_SERIALIZER);
    return props;
  }
}
