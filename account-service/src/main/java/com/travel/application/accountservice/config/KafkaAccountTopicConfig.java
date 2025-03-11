package com.travel.application.accountservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaAccountTopicConfig {

    @Bean
    public NewTopic ticketTopic() {
        return TopicBuilder
                .name("account-topic")
                .partitions(1)
                .build();
    }
}
