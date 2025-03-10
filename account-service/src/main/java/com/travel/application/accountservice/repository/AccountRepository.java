package com.travel.application.accountservice.repository;

import com.travel.application.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.amount = a.amount - :withdraw WHERE a.id = :accountId AND a.amount >= :withdraw")
    int withdrawIfEnough(@Param("withdraw") Double withdraw, @Param("accountId") UUID accountId);
}
