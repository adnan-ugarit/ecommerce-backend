package com.adnan.bank.service;

import com.adnan.bank.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.adnan.bank.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Optional<Account> findAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
    
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }
    
}
