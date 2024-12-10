package com.example.eventticketingsystem.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a ticket for an event.
 */
public class Ticket {
    private static final AtomicInteger nextId = new AtomicInteger(1);
    private int id;
    private String eventName;
    private double price;

    /**
     * Constructs a new Ticket.
     */
    public Ticket(String eventName, double price) {
        this.id = nextId.getAndIncrement();
        this.eventName = eventName;
        this.price = price;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public double getPrice() {
        return price;
    }
}
