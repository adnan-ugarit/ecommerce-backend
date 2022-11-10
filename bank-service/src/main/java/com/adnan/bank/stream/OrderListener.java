package com.adnan.bank.stream;

import com.adnan.bank.entity.Account;
import com.adnan.bank.service.AccountService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);
    
    @Autowired
    private OrderChecker orderChecker;
    
    @Autowired
    private AccountService accountService;
    
    private static final String OWNER_ACCOUNT_NUMBER = "12345678";
    
    @StreamListener(target = "orderChannel")
    public void processOrder(OrderStream orderStream) {
        LOGGER.info("Finding account by number: {}", orderStream.getAccountNumber());
        Optional<Account> optionalAccount = accountService.findAccountByNumber(orderStream.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            LOGGER.error("Account (number = {}) is not found", orderStream.getAccountNumber());
            orderStream.setStatus(OrderStream.Status.Declined);
        }
        else {
            Account account = optionalAccount.get();
            if (orderStream.getCost() <= account.getBalance()) {
                LOGGER.info("Accepting order: {}", orderStream);
                orderStream.setStatus(OrderStream.Status.Approved);
                account.setBalance((int) (account.getBalance() - orderStream.getCost()));
                Account ownerAccount = accountService.findAccountByNumber(OWNER_ACCOUNT_NUMBER).get();
                ownerAccount.setBalance((int) (ownerAccount.getBalance() + orderStream.getCost()));
                LOGGER.info("Updating customer account: {}", account);
                accountService.updateAccount(account);
                LOGGER.info("Updating owner account: {}", ownerAccount);
                accountService.updateAccount(ownerAccount);
            }
            else {
                LOGGER.info("Rejecting order: {}", orderStream);
                orderStream.setStatus(OrderStream.Status.Declined);
            }
        }
        Message<OrderStream> result = MessageBuilder.withPayload(orderStream).build();
        LOGGER.info("Sending stream order ({}) to RabbitMQ", orderStream);
        orderChecker.result().send(result);
    }
    
}
