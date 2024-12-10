package com.example.eventticketingsystem.repository;

import com.example.eventticketingsystem.configuration.TicketConfig;
import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class TicketPool {
    private final ReentrantLock lock = new ReentrantLock(true);

    private int totalTicketsCreated = 0;
    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    private TicketConfig config;

    public  boolean addTickets(List<Ticket> newTickets) {
        lock.lock();
        if (tickets.size() + newTickets.size() <= config.getMaxTicketCapacity()) {
            tickets.addAll(newTickets);
            totalTicketsCreated += newTickets.size();
            lock.unlock();
            return true;
        } else {
            LoggingUtil.log("Maximum ticket capacity reached. Not adding more tickets.");
            lock.unlock();
            return false;


        }
    }

    public  Ticket removeTicket() {
        lock.lock();
        if (!tickets.isEmpty()) {
            lock.unlock();
            return tickets.remove(0);

        }
        lock.unlock();
        return null;
    }

    public synchronized int getSize() {
        return tickets.size();
    }

    public synchronized int getTotalTicketsCreated() {
        return totalTicketsCreated;
    }
}