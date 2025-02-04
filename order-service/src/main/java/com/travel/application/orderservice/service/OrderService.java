package com.travel.application.orderservice.service;

import com.travel.application.orderservice.dto.OrderRequestDto;
import com.travel.application.orderservice.dto.OrderResponseDto;
import com.travel.application.orderservice.dto.OrderWithoutDetailedTicketInfoResponseDto;
import com.travel.application.orderservice.dto.TicketResponseDto;
import com.travel.application.orderservice.exception.NonExistingEntityRecordException;
import com.travel.application.orderservice.exception.TimeoutWhenWaitingForKafkaRespondException;
import com.travel.application.orderservice.kafka.TicketRequestProducer;
import com.travel.application.orderservice.kafka.TicketResponseConsumer;
import com.travel.application.orderservice.mapper.OrderMapper;
import com.travel.application.orderservice.model.Orders;
import com.travel.application.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final TicketResponseConsumer ticketResponseConsumer;
    private final TicketRequestProducer ticketRequestProducer;
    private final OrderRepository orderRepository;

    /**
     * Service's method that sends request through message broker for ticket info,
     * tries to receive respond and maps all gathered info to OrderResponseDto class
     * @param orderId
     * @return order that corresponds to given id in DB in DTO format
     */
    public OrderResponseDto getOrderWithTicketInfo(String orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NonExistingEntityRecordException("There is no order record in DB with such ID"));
        String ticketId = order.getTicketId();
        ticketRequestProducer.produceTicketRequest(ticketId);

        try {
            TicketResponseDto ticketInfo = ticketResponseConsumer.getTicketResponse(ticketId)
                    .get(5, TimeUnit.SECONDS);

            return orderMapper.toOrderResponseDto(order, ticketInfo);
        } catch (TimeoutException e) {
            throw new TimeoutWhenWaitingForKafkaRespondException("Timeout when waiting respond from Kafka on message");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Method for saving requested order data for defined ticket ID by
     * mapping requested order data to entity format, and then persisting
     * mapped data and returning saved data in DTO format.
     * @param ticketId
     * @param orderRequest
     * @return order data with ticket ID in an appropriate format
     */
    public OrderWithoutDetailedTicketInfoResponseDto addOrderForTicket(String ticketId, OrderRequestDto orderRequest) {

        Orders orderEntity = orderMapper.toOrder(orderRequest);
        orderEntity.setTicketId(ticketId);
        Orders persistedOrder = orderRepository.save(orderEntity);
        return orderMapper.toOrderWithoutDetailedTicketInfoResponseDto(persistedOrder);
    }
}
