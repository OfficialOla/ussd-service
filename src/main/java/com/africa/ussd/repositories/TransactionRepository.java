package com.africa.ussd.repositories;

import com.africa.ussd.models.AppUser;
import com.africa.ussd.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAppUser(AppUser appUser);
}
