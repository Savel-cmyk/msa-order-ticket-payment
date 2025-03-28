package com.travel.application.orderservice.service;

import com.travel.application.orderservice.dto.*;
import com.travel.application.orderservice.exception.RecordNotFoundException;
import com.travel.application.orderservice.exception.TimeoutWhenWaitingForKafkaRespondException;
import com.travel.application.orderservice.kafka.TicketBookingRequestProducer;
import com.travel.application.orderservice.kafka.TicketBookingResponseConsumer;
import com.travel.application.orderservice.kafka.TicketRequestProducer;
import com.travel.application.orderservice.kafka.TicketResponseConsumer;
import com.travel.application.orderservice.mapper.OrderMapper;
import com.travel.application.orderservice.model.OrderStatus;
import com.travel.application.orderservice.model.Orders;
import com.travel.application.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final TicketResponseConsumer ticketResponseConsumer;
    private final TicketRequestProducer ticketRequestProducer;
    private final TicketBookingResponseConsumer ticketBookingResponseConsumer;
    private final TicketBookingRequestProducer ticketBookingRequestProducer;
    private final OrderRepository orderRepository;

    /**
     * Service's method that sends request through message broker for ticket info,
     * tries to receive respond and maps all gathered info to OrderResponseDto class
     *
     * @param orderId order's unique identifier
     * @return order that corresponds to given id in DB in DTO format
     */
    public OrderResponseDto getOrderWithTicketInfo(String orderId) {

        Orders order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new RecordNotFoundException(
                                "There is no order record in DB with such ID",
                                Orders.class.getTypeName()
                        )
                );
        TicketInfoRequestDto ticketInfoRequest = new TicketInfoRequestDto(String.valueOf(order.getTicketId()));
        ticketRequestProducer.produceTicketRequest(ticketInfoRequest);

        try {
            TicketResponseDto ticketInfo = ticketResponseConsumer.getTicketResponse(ticketInfoRequest.ticketId())
                    .get(5, TimeUnit.SECONDS);

            return orderMapper.toOrderResponseDto(order, ticketInfo);
        } catch (TimeoutException e) {
            throw new TimeoutWhenWaitingForKafkaRespondException("Timeout when waiting respond from Kafka on message");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Method for saving requested order data for defined customer ID by
     * mapping requested order data to entity format, and then persisting
     * mapped data and returning saved data in DTO format.
     *
     * @return order data with ticket ID in an appropriate format
     */
    public OrderWithoutDetailedTicketInfoResponseDto addOrderForTicket() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        String customerId = ((Jwt) auth.getCredentials()).getClaimAsString("sub");

        Orders orderEntity = orderMapper.toOrderDao(customerId);
        Orders persistedOrder = orderRepository.save(orderEntity);
        TicketBookingRequestDto ticketBookingRequest = orderMapper.toTicketBookingRequestDto(persistedOrder);
        ticketBookingRequestProducer.produceRequestForBookingAvailableTicket(ticketBookingRequest);

        try {
            TicketBookingResponseDto ticketBookingInfo =
                    ticketBookingResponseConsumer.getTicketBookingResponse(persistedOrder.getOrderId())
                            .get(10, TimeUnit.SECONDS);

            persistedOrder.setTicketId(ticketBookingInfo.ticketId() == null ? null : UUID.fromString(ticketBookingInfo.ticketId()));
            persistedOrder.setStatus(OrderStatus.valueOf(ticketBookingInfo.bookingStatus()));
            Orders bookingResult = orderRepository.save(persistedOrder);

            return orderMapper.toOrderWithoutDetailedTicketInfoResponseDto(bookingResult);
        } catch (TimeoutException e) {
            throw new TimeoutWhenWaitingForKafkaRespondException("Timeout when waiting respond from Kafka on message");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
