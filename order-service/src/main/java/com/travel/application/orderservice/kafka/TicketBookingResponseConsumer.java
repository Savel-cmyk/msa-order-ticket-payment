package com.travel.application.orderservice.kafka;

import com.travel.application.orderservice.dto.TicketBookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TicketBookingResponseConsumer {

    private final Map<String, CompletableFuture<TicketBookingResponseDto>> ticketBookingResponsesMap =
            new ConcurrentHashMap<>();

    /**
     *
     *
     * @param ticketBookingResponse
     */
    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "ticket-topic", partitions = {"2"})},
            groupId = "order-group"
    )
    public void consumeResponseOnPaymentStatus(TicketBookingResponseDto ticketBookingResponse) {

        String orderId = ticketBookingResponse.orderId();

        CompletableFuture<TicketBookingResponseDto> ticketBookingResponseFuture =
                ticketBookingResponsesMap.remove(orderId);
        if (ticketBookingResponseFuture != null) {
            ticketBookingResponseFuture.complete(ticketBookingResponse);
        }
    }

    /**
     *
     *
     * @param orderId
     * @return
     */
    public CompletableFuture<TicketBookingResponseDto> getTicketBookingResponse(UUID orderId) {

        CompletableFuture<TicketBookingResponseDto> ticketBookingResponseFuture = new CompletableFuture<>();
        ticketBookingResponsesMap.put(String.valueOf(orderId), ticketBookingResponseFuture);

        return ticketBookingResponseFuture;
    }
}
