package com.travel.application.orderservice.mapper;

import com.travel.application.orderservice.dto.OrderResponseDto;
import com.travel.application.orderservice.dto.OrderWithoutDetailedTicketInfoResponseDto;
import com.travel.application.orderservice.dto.TicketBookingRequestDto;
import com.travel.application.orderservice.dto.TicketResponseDto;
import com.travel.application.orderservice.model.OrderStatus;
import com.travel.application.orderservice.model.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    /**
     * Method for mapping {@code Orders.class} object instance to DTO format with additional detailed info on ticket,
     * that is being booked by this order.
     *
     * @param order
     * @param ticketInfo
     * @return order data with detailed ticket information in DTO format
     */
    public OrderResponseDto toOrderResponseDto(Orders order, TicketResponseDto ticketInfo) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        return new OrderResponseDto(
                String.valueOf(order.getOrderId()),
                order.getDate().format(formatter),
                ticketInfo
        );
    }

    /**
     * Method for mapping {@code Orders.class} object instance to DTO format with ticket ID that is being booked by this
     * order.
     *
     * @param order
     * @return order data with ticket ID in DTO format
     */
    public OrderWithoutDetailedTicketInfoResponseDto toOrderWithoutDetailedTicketInfoResponseDto(Orders order) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        return new OrderWithoutDetailedTicketInfoResponseDto(
                String.valueOf(order.getOrderId()),
                order.getDate().format(formatter),
                order.getStatus().name(),
                String.valueOf(order.getCustomerId()),
                String.valueOf(order.getTicketId())
        );
    }

    /**
     * Method for mapping order data in DTO format to order's entity format.
     *
     * @return order data in its entity format
     */
    public Orders toOrderDao(String customerId) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String dateInStringFormat = LocalDateTime.now().format(formatter);
        LocalDateTime localDateTime = LocalDateTime.parse(dateInStringFormat, formatter);

        return Orders.builder()
                .customerId(UUID.fromString(customerId))
                .status(OrderStatus.PENDING)
                .date(localDateTime)
                .build();
    }

    /**
     *
     *
     * @param order
     * @return
     */
    public TicketBookingRequestDto toTicketBookingRequestDto(Orders order) {

        return new TicketBookingRequestDto(
                String.valueOf(order.getOrderId()),
                String.valueOf(order.getCustomerId())
        );
    }
}
