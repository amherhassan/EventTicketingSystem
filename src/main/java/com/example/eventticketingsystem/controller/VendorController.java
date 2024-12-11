package com.example.eventticketingsystem.controller;

import com.example.eventticketingsystem.model.Ticket;
import com.example.eventticketingsystem.repository.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    private TicketPool ticketPool;

    /**
     * Endpoint to allow a vendor to sell tickets.
     * @param count Number of tickets to add to the pool.
     */
    @PostMapping("/sell")
    public String sellTickets(@RequestParam(defaultValue = "5") int count) {
        List<Ticket> newTickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newTickets.add(new Ticket("Event", 50.00));
        }

        boolean added = ticketPool.addTickets(newTickets);
        if (added) {
            return count + " tickets added successfully to the pool.";
        } else {
            return "Ticket pool is full. Cannot add more tickets.";
        }
    }
}
