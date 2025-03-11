package com.travel.application.accountservice.service;

import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.dto.TicketPaymentRequestDto;
import com.travel.application.accountservice.dto.TicketPaymentResponseDto;
import com.travel.application.accountservice.exception.RecordNotFoundException;
import com.travel.application.accountservice.model.Account;
import com.travel.application.accountservice.model.Customer;
import com.travel.application.accountservice.mapper.CustomerMapper;
import com.travel.application.accountservice.repository.AccountRepository;
import com.travel.application.accountservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    /**
     * Service's method that maps received customer data in DTO format to DAO, saves mapped data
     * and maps saved data to DTO format to return
     *
     * @param customer customer data in DTO format
     * @return saved customer data in corresponding DTO format
     */
    public CustomerDto addCustomer(CustomerDto customer) {

        Customer transientCustomer = customerMapper.toCustomerDao(customer);
        transientCustomer.setAccount(accountService.addAccountForCustomer());
        Customer persistedCustomer = customerRepository.save(transientCustomer);
        return customerMapper.toCustomerDto(persistedCustomer);
    }

    /**
     * Service's method that retrieves customer data by requested id and returns it in DTO format
     *
     * @param customerId customer's unique identifier
     * @return customer's data in DTO format
     * @throws RecordNotFoundException if no data had been found for corresponding id
     */
    public CustomerDto getCustomerById(String customerId) {

        Customer persistedCustomer = customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new RecordNotFoundException(
                        "No customer record with requested id found",
                        Customer.class.getName()
                ));
        return customerMapper.toCustomerDto(persistedCustomer);
    }

    /**
     * Method for handling ticket booking process
     *
     * @param ticketPaymentRequest info that is required for producing transaction
     */
    @Transactional
    public TicketPaymentResponseDto payForTicket(TicketPaymentRequestDto ticketPaymentRequest) {

        Customer customerToPay = customerRepository
                .findById(UUID.fromString(ticketPaymentRequest.customerId()))
                .orElseThrow(() -> new RecordNotFoundException(
                        "No customer record with requested id found",
                        Customer.class.getName()
                ));
        Account customerAccount = customerToPay.getAccount();

        TicketPaymentResponseDto ticketPaymentResponse;
        if (accountRepository.withdrawIfEnough(Double.valueOf(ticketPaymentRequest.cost()), customerAccount.getId()) > 0) {

            ticketPaymentResponse = new TicketPaymentResponseDto(
                    ticketPaymentRequest.orderId(),
                    "BOOKED",
                    ticketPaymentRequest.ticketId()
            );
        } else {
            ticketPaymentResponse = new TicketPaymentResponseDto(
                    ticketPaymentRequest.orderId(),
                    "PAYMENT_FAILED",
                    ticketPaymentRequest.ticketId()
            );
        }
        return ticketPaymentResponse;
    }
}
