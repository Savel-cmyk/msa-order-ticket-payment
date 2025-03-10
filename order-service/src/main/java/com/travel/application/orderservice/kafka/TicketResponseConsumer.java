package com.travel.application.orderservice.kafka;

import com.travel.application.orderservice.dto.TicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TicketResponseConsumer {

    private final Map<String, CompletableFuture<TicketResponseDto>> ticketResponseFutures = new ConcurrentHashMap<>();

    /**
     * You should "complete" CompletableFuture object for receiving what it will contain where it needs.
     * Receiver gets future object instance in getTicketResponse method.
     *
     * @param ticketResponseDto
     */
    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "ticket-topic", partitions = "0")},
            groupId = "order-group"
    )
    public void consumeTicketResponse(TicketResponseDto ticketResponseDto) {

        String ticketId = ticketResponseDto.ticketId();

        CompletableFuture<TicketResponseDto> futureTicketResponse = ticketResponseFutures.remove(ticketId);
        if (futureTicketResponse != null) {
            futureTicketResponse.complete(ticketResponseDto);
        }
    }

    /**
     * Method receives id of ticket that should be requested from ticket service using message
     * broker (distributed at this moment) and returns CompletableFuture object of type TicketResponseDto
     * that should be processed to get result.
     *
     * @param ticketId
     * @return instance of a {@code CompletableFuture.class}
     */
    public CompletableFuture<TicketResponseDto> getTicketResponse(String ticketId) {

        CompletableFuture<TicketResponseDto> futureTicketResponse = new CompletableFuture<>();
        ticketResponseFutures.put(ticketId, futureTicketResponse);
        return futureTicketResponse;
    }
}
