package com.example.eventticketingsystem.controller;

import com.example.eventticketingsystem.configuration.TicketConfig;
import com.example.eventticketingsystem.service.Customer;
import com.example.eventticketingsystem.service.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Controller for managing the event simulation.
 */
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private TicketConfig config;

    @Autowired
    private ApplicationContext context;

    private ExecutorService executorService;
    private List<Thread> activeThreads = new ArrayList<>();

    /**
     * Starts the event simulation.
     * Creates and starts vendor and customer threads.
     */
    @PostMapping("/start")
    public ResponseEntity<String> startSimulation() {
        executorService = Executors.newFixedThreadPool(8);
        activeThreads.clear();

        for (int i = 0; i < 3; i++) {  // Create 3 vendors
            Vendor vendor = context.getBean(Vendor.class);
            vendor.setName("Vendor-" + (i + 1));
            vendor.setTicketsPerRelease(config.getTicketReleaseRate());
            vendor.setReleaseInterval(500);
            startThread(vendor);
        }

        for (int i = 0; i < 5; i++) {  // Create 5 customers
            Customer customer = context.getBean(Customer.class);
            customer.setName("Customer-" + (i + 1));
            customer.setRetrievalInterval(config.getCustomerRetrievalRate());
            startThread(customer);
        }

        return ResponseEntity.ok("Simulation started.");
    }

    /**
     * Starts a new thread for the given runnable.
     * @param runnable The task to run in the thread.
     */
    private void startThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        activeThreads.add(thread);
        thread.start();
    }

    /**
     * Stops the event simulation.
     * Interrupts and shuts down the executor service and active threads.
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        if (executorService != null) {
            executorService.shutdownNow();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor service did not shut down in time.");
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for executor service shutdown.");
                Thread.currentThread().interrupt();
            }
            activeThreads.clear();
        }
        return ResponseEntity.ok("Simulation stopped.");
    }
}