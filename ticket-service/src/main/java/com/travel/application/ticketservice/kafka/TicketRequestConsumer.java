package com.travel.application.ticketservice.kafka;

import com.travel.application.ticketservice.dto.TicketInfoRequestDto;
import com.travel.application.ticketservice.dto.TicketResponseDto;
import com.travel.application.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketRequestConsumer {

    private final TicketService ticketService;
    private final TicketResponseProducer ticketResponseProducer;

    /**
     * Method for sending respond on requested ticket data in DTO format to ticket-service topic.
     * At this moment method serves for responding on request about ticket data from order-service.
     *
     * @param ticketInfoRequest
     */
    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "order-topic", partitions = "0")},
            groupId = "ticket-group"
    )
    public void consumeTicketRequest(TicketInfoRequestDto ticketInfoRequest) {

        TicketResponseDto ticketResponseDto = ticketService.getTicketInfo(ticketInfoRequest);
        ticketResponseProducer.produceTicketResponse(ticketResponseDto);
    }
}
