package com.travel.application.accountservice.service;

import com.travel.application.accountservice.dto.TicketPaymentRequestDto;
import com.travel.application.accountservice.dto.TicketPaymentResponseDto;
import com.travel.application.accountservice.exception.RecordNotFoundException;
import com.travel.application.accountservice.model.Account;
import com.travel.application.accountservice.model.Currency;
import com.travel.application.accountservice.repository.AccountRepository;
import com.travel.application.accountservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    /**
     * Method for account creation for requested customer's unique identifier
     *
     * @return customer-account exchange DTO object instance
     */
    public Account addAccountForCustomer() {

        Account customerAccount = Account.builder()
                .amount(0.0)
                .currency(
                        currencyRepository.findByName("RUB")
                                .orElseThrow(() -> new RecordNotFoundException(
                                        "No currency record has been found for corresponding Name",
                                        Currency.class.getTypeName()
                                ))
                )
                .build();
        return accountRepository.save(customerAccount);
    }

    /**
     * Method for payment completion on ticket service side
     *
     * @param accountId account's unique identifier
     * @param ticketPaymentRequest ticket info that's required for payment
     * @return payment result in DTO format
     */
    public TicketPaymentResponseDto payForTicket(String accountId, TicketPaymentRequestDto ticketPaymentRequest) {

        Account customerAccount = accountRepository.findById(
                        UUID.fromString(accountId)
                ).orElseThrow(() -> new RecordNotFoundException(
                        "No customer record with requested id found",
                        Account.class.getName()
                )
        );

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

    /**
     * Method for updating account by adding customer's id value of account owner
     *
     * @param customerId customer's unique identifier
     * @param accountForUpdate account that is to be updated
     */
    public void updateAccountForCustomer(String customerId, Account accountForUpdate) {

        accountForUpdate.setCustomerId(UUID.fromString(customerId));
        accountRepository.save(accountForUpdate);
    }

    /**
     * Method for customer corresponding account deletion
     *
     * @param accountId account's unique identifier
     */
    public void deleteAccountForCustomer(String accountId) {

        accountRepository.deleteById(UUID.fromString(accountId));
    }
}
