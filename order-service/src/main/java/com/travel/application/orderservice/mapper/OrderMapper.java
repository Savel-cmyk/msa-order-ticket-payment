package com.travel.application.orderservice.mapper;

import com.travel.application.orderservice.dto.OrderRequestDto;
import com.travel.application.orderservice.dto.OrderResponseDto;
import com.travel.application.orderservice.dto.OrderWithoutDetailedTicketInfoResponseDto;
import com.travel.application.orderservice.dto.TicketResponseDto;
import com.travel.application.orderservice.model.Orders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderMapper {

    /**
     * Method for mapping {@code Orders.class} object instance to DTO format with additional detailed info on ticket,
     * that is being booked by this order.
     * @param order
     * @param ticketInfo
     * @return order data with detailed ticket information in DTO format
     */
    public OrderResponseDto toOrderResponseDto(Orders order, TicketResponseDto ticketInfo) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        return new OrderResponseDto(
                order.getOrderId(),
                order.getCustomerSNP(),
                order.getNumber(),
                order.getEmail(),
                order.getDate().format(formatter),
                ticketInfo
        );
    }

    /**
     * Method for mapping {@code Orders.class} object instance to DTO format with ticket ID that is being booked by this
     * order.
     * @param order
     * @return order data with ticket ID in DTO format
     */
    public OrderWithoutDetailedTicketInfoResponseDto toOrderWithoutDetailedTicketInfoResponseDto(Orders order) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        return new OrderWithoutDetailedTicketInfoResponseDto(
                order.getOrderId(),
                order.getCustomerSNP(),
                order.getNumber(),
                order.getEmail(),
                order.getDate().format(formatter),
                order.getTicketId()
        );
    }

    /**
     * Method for mapping order data in DTO format to order's entity format.
     * @param orderRequest
     * @return order data in its entity format
     */
    public Orders toOrder(OrderRequestDto orderRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String dateInStringFormat = LocalDateTime.now().format(formatter);
        LocalDateTime localDateTime = LocalDateTime.parse(dateInStringFormat, formatter);

        return Orders.builder()
                .customerSNP(orderRequest.customerSNP())
                .date(localDateTime)
                .email(orderRequest.email())
                .number(orderRequest.number())
                .build();
    }
}
