package com.example.eventticketingsystem.service;

import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.repository.TicketPool;
import com.example.eventticketingsystem.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Customer implements Runnable {
    private String name;

    @Autowired
    private TicketPool ticketPool;

    private int retrievalInterval;

    public Customer() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRetrievalInterval(int retrievalInterval) {
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    LoggingUtil.log(formatter.format(LocalDateTime.now()) + " - " + name + " retrieved a ticket. (Total in pool: " + ticketPool.getSize() + ")");
                } else {
                    LoggingUtil.logWarning(formatter.format(LocalDateTime.now()) + " - " + name + " attempted to retrieve a ticket but the pool is empty.");
                }

                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggingUtil.logError(formatter.format(LocalDateTime.now()) + " - " + name + " interrupted.", e);
                break;
            }
        }
    }
}