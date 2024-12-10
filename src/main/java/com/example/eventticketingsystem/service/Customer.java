package com.example.eventticketingsystem.service;

import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.repository.TicketPool;
import com.example.eventticketingsystem.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Represents a customer trying to purchase tickets.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Customer implements Runnable {
    private String name;
    @Autowired
    private TicketPool ticketPool;
    private int retrievalInterval;

    /**
     * Default constructor for the Customer.
     */
    public Customer() {
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setRetrievalInterval(int retrievalInterval) {
        this.retrievalInterval = retrievalInterval;
    }

    /**
     * Customer's run method, which continuously attempts to purchase tickets.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    LoggingUtil.log(name + " purchased ticket " + ticket.getId() + ". Pool size: " + ticketPool.getSize());
                } else {
                    LoggingUtil.log(name + " couldn't get a ticket. Pool is empty.");
                }

                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggingUtil.logError(name + " interrupted.", e);
                break;
            }
        }
    }
}