package com.example.eventticketingsystem.service;

import com.example.eventticketingsystem.configuration.TicketConfig;
import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.repository.TicketPool;
import com.example.eventticketingsystem.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Vendor implements Runnable {
    private String name;

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketConfig config; // Correctly autowire TicketConfig

    private int ticketsPerRelease;
    private int releaseInterval;

    public Vendor() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTicketsPerRelease(int ticketsPerRelease) {
        this.ticketsPerRelease = ticketsPerRelease;
    }

    public void setReleaseInterval(int releaseInterval) {
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        LoggingUtil.log(name + " run method started.");
        LoggingUtil.log(name + " config.getTotalTickets(): " + config.getTotalTickets()); // Log config values
        LoggingUtil.log(name + " ticketsPerRelease: " + ticketsPerRelease);
        LoggingUtil.log(name + " releaseInterval: " + releaseInterval);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<Ticket> newTickets = new ArrayList<>();
                for (int i = 0; i < ticketsPerRelease; i++) {
                    newTickets.add(new Ticket("Event", 50.00));
                }

                if (ticketPool.addTickets(newTickets)) {
                    LoggingUtil.log(name + " added " + newTickets.size() + " tickets. Pool size: " + ticketPool.getSize());
                } else {
                    LoggingUtil.log(name + ": Maximum ticket limit reached. Not adding more tickets.");
                    break;
                }

                Thread.sleep(releaseInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggingUtil.logError(name + " interrupted.", e);
                break;
            }
        }
    }
}