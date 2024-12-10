package com.example.eventticketingsystem.repository;

import com.example.eventticketingsystem.configuration.TicketConfig;
import com.example.eventticketingsystem.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread-safe repository for managing the pool of available tickets.
 */
@Repository
public class TicketPool {
    private int totalTicketsCreated = 0;
    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    private TicketConfig config;

    /**
     * Adds new tickets to the pool, up to the total ticket limit.
     * @param newTickets The list of new tickets to add.
     * @return True if the tickets were added, false if the total ticket limit was reached.
     */
    public synchronized boolean addTickets(List<Ticket> newTickets) {
        if (totalTicketsCreated + newTickets.size() <= config.getTotalTickets()) {
            tickets.addAll(newTickets);
            totalTicketsCreated += newTickets.size();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes and returns a ticket from the pool.
     * Returns null if the pool is empty.
     */
    public synchronized Ticket removeTicket() {
        if (!tickets.isEmpty()) {
            return tickets.remove(0);
        }
        return null;
    }

    /**
     * Gets the current number of tickets in the pool.
     */
    public int getSize() {
        return tickets.size();
    }
}