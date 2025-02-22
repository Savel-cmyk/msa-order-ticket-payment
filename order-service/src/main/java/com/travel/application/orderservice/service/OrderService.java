package com.travel.application.orderservice.service;

import com.travel.application.orderservice.dto.OrderResponseDto;
import com.travel.application.orderservice.dto.OrderWithoutDetailedTicketInfoResponseDto;
import com.travel.application.orderservice.dto.TicketResponseDto;
import com.travel.application.orderservice.exception.RecordNotFoundException;
import com.travel.application.orderservice.exception.TimeoutWhenWaitingForKafkaRespondException;
import com.travel.application.orderservice.kafka.TicketRequestProducer;
import com.travel.application.orderservice.kafka.TicketResponseConsumer;
import com.travel.application.orderservice.mapper.OrderMapper;
import com.travel.application.orderservice.model.Customer;
import com.travel.application.orderservice.model.Orders;
import com.travel.application.orderservice.repository.CustomerRepository;
import com.travel.application.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    /**
     * Service's method that sends request through message broker for ticket info,
     * tries to receive respond and maps all gathered info to OrderResponseDto class
     *
     * @param orderId
     * @return order that corresponds to given id in DB in DTO format
     */
    public OrderResponseDto getOrderWithTicketInfo(String orderId) {

        Orders order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new RecordNotFoundException(
                                "There is no order record in DB with such ID",
                                Orders.class.getTypeName()
                        )
                );
        String ticketId = String.valueOf(order.getTicketId());
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
     *
     * @param ticketId
     * @param customerId
     * @return order data with ticket ID in an appropriate format
     */
    public OrderWithoutDetailedTicketInfoResponseDto addOrderForTicket(
            String ticketId,
            String customerId
    ) {

        Orders orderEntity = orderMapper.toOrderDao();
        orderEntity.setTicketId(UUID.fromString(ticketId));
        orderEntity.setCustomer(
                customerRepository.findById(UUID.fromString(customerId))
                        .orElseThrow(() -> new RecordNotFoundException("No customer record that corresponds to " +
                                "requested id has been found", Customer.class.getName()))
        );
        Orders persistedOrder = orderRepository.save(orderEntity);
        return orderMapper.toOrderWithoutDetailedTicketInfoResponseDto(persistedOrder);
    }
}
