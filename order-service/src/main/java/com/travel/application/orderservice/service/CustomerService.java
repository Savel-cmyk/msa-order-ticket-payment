package com.travel.application.orderservice.service;

import com.travel.application.orderservice.dto.CustomerAccountExchangeDto;
import com.travel.application.orderservice.dto.CustomerDto;
import com.travel.application.orderservice.exception.RecordNotFoundException;
import com.travel.application.orderservice.kafka.AccountRequestProducer;
import com.travel.application.orderservice.kafka.AccountResponseConsumer;
import com.travel.application.orderservice.mapper.CustomerMapper;
import com.travel.application.orderservice.model.Customer;
import com.travel.application.orderservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final AccountRequestProducer accountRequestProducer;
    private final AccountResponseConsumer accountResponseConsumer;

    /**
     * Service's method that maps received customer data in DTO format to DAO, saves mapped data
     * and maps saved data to DTO format to return
     * @param customer customer data in DTO format
     * @return saved customer data in corresponding DTO format
     */
    public CustomerDto addCustomer(CustomerDto customer) {

        Customer transientCustomer = customerMapper.toCustomerDao(customer);
        UUID customerId = UUID.randomUUID();
        accountRequestProducer.produceAccountRequest(String.valueOf(customerId));

        try {
            CustomerAccountExchangeDto exchangeDto = accountResponseConsumer.getAccountId(String.valueOf(customerId))
                    .get(5, TimeUnit.SECONDS);

            transientCustomer.setId(customerId);
            transientCustomer.setAccountId(UUID.fromString(exchangeDto.accountId()));
            Customer persistedCustomer = customerRepository.save(transientCustomer);
            return customerMapper.toCustomerDto(persistedCustomer);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Service's method that retrieves customer data by requested id and returns it in DTO format
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
}
