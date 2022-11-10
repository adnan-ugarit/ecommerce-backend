package com.adnan.bank.bootstrap;

import com.adnan.bank.entity.Account;
import com.adnan.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankLoader {
    
    private final AccountRepository accountRepository;
    
    @Autowired
    public BankLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        loadAccounts();
    }
    
    private void loadAccounts() {
        accountRepository.save(new Account("12345678", 50000));
        accountRepository.save(new Account("999999999", 500));
        accountRepository.save(new Account("0883252", 200));
	accountRepository.save(new Account("729056", 1500));
	accountRepository.save(new Account("369170630248", 500));
	accountRepository.save(new Account("2824026344016351", 11000));
    }
    
}
