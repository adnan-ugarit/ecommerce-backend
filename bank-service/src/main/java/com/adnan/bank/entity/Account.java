package com.adnan.bank.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class Account {
    
    @Id
    private String id;
    
    @UniqueElements
    @Length(min = 6, max = 16)
    private String number;
    
    @NotNull
    private int balance;
    //private String customerName;

    public Account(String number, int balance) {
        this.number = number;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account [number=" + number + ", balance=" + balance + "]";
    }

}
