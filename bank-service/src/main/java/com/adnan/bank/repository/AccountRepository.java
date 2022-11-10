package com.adnan.bank.repository;

import com.adnan.bank.entity.Account;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
    
    public Optional<Account> findByNumber(String number);
    
}
