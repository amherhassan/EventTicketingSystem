package com.example.eventticketingsystem.controller;

import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.repository.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private TicketPool ticketPool;

    /**
     * Endpoint to allow a customer to buy a ticket.
     */
    @PostMapping("/buy")
    public String buyTicket() {
        Ticket ticket = ticketPool.removeTicket();
        if (ticket != null) {
            return "Ticket purchased successfully: Ticket ID - " + ticket.getId() + ", Event - " + ticket.getEventName();
        } else {
            return "No tickets available. Please try again later.";
        }
    }
}
