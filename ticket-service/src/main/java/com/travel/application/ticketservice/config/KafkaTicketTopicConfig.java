package com.travel.application.ticketservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTicketTopicConfig {

    @Bean
    public NewTopic ticketTopic() {
        return TopicBuilder
                .name("ticket-topic")
                .partitions(3)
                .build();
    }
}
