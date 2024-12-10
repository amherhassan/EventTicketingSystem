package com.example.eventticketingsystem.service;

import com.example.eventticketingsystem.configuration.TicketConfig;
import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.repository.TicketPool;
import com.example.eventticketingsystem.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Vendor implements Runnable {
    private String name;

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketConfig config;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<Ticket> newTickets = new ArrayList<>();
                for (int i = 0; i < ticketsPerRelease; i++) {
                    if (ticketPool.getTotalTicketsCreated() < config.getTotalTickets()) {
                        newTickets.add(new Ticket("Event", 50.00));
                    } else {
                        break; // Stop adding tickets if total limit is reached
                    }
                }

                if (!newTickets.isEmpty()) {
                    if (ticketPool.addTickets(newTickets)) {
                        LoggingUtil.log(formatter.format(LocalDateTime.now()) + " - " + name + " added " + newTickets.size() + " tickets. (Total in pool: " + ticketPool.getSize() + ")");
                    } else {
                        LoggingUtil.log(formatter.format(LocalDateTime.now()) + " - " + name + ": Maximum ticket capacity reached. Not adding more tickets.");
                    }
                }

                Thread.sleep(releaseInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggingUtil.logError(formatter.format(LocalDateTime.now()) + " - " + name + " interrupted.", e);
                break;
            }
        }
    }
}