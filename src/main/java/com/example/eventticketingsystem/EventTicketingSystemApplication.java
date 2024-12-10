package com.example.eventticketingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.example.eventticketingsystem.configuration.TicketConfig;
import com.example.eventticketingsystem.controller.EventController;



import java.util.Scanner;

/**
 * Main application class for the Event Ticketing System.
 */
@SpringBootApplication
public class EventTicketingSystemApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EventTicketingSystemApplication.class, args);

		Scanner scanner = new Scanner(System.in);

		// Display the main menu for configuration
		System.out.println("--------------------------------------------------");
		System.out.println("Real-Time Event Ticketing System - Configuration");
		System.out.println("--------------------------------------------------");
		System.out.println("\nPlease enter the following configuration parameters:");

		// Prompt for configuration parameters with validation

		int ticketReleaseRate = getIntInput(scanner, "Ticket Release Rate (Tickets/Second, > 0): ", 1, Integer.MAX_VALUE);
		int customerRetrievalRate = getIntInput(scanner, "Customer Retrieval Rate (Customers/Second, > 0): ", 1, Integer.MAX_VALUE);
		int maxTicketCapacity = getIntInput(scanner, "Maximum Ticket Capacity (Integer >= Total Tickets): ", 1, Integer.MAX_VALUE);

		// Display configuration summary
		System.out.println("\nConfiguration Summary:");

		System.out.println("- Ticket Release Rate: " + ticketReleaseRate + " tickets/second");
		System.out.println("- Customer Retrieval Rate: " + customerRetrievalRate + " customers/second");
		System.out.println("- Maximum Ticket Capacity: " + maxTicketCapacity);

		// Ask if the user wants to save the configuration
		System.out.print("\nDo you want to save this configuration? (y/n): ");
		String saveConfig = scanner.nextLine();

		if (saveConfig.equalsIgnoreCase("y")) {
			// Save configuration (you can use a JSON file or other methods here)
			System.out.println("Configuration saved to config.json. (Replace this with actual saving logic)");
		}

		// Ask if the user wants to start the system
		System.out.print("\nDo you want to start the system with this configuration? (y/n): ");
		String startSystem = scanner.nextLine();

		if (startSystem.equalsIgnoreCase("y")) {
			System.out.println("\nSystem starting...\n");

			// Set the configuration in the TicketConfig bean
			TicketConfig config = context.getBean(TicketConfig.class);

			config.setTicketReleaseRate(ticketReleaseRate);
			config.setCustomerRetrievalRate(customerRetrievalRate);
			config.setMaxTicketCapacity(maxTicketCapacity);

			// Start the simulation (you'll need to adapt your EventController logic)
			EventController eventController = context.getBean(EventController.class);
			eventController.startSimulation();
		}
	}

	/**
	 * Helper function to get integer input with validation.
	 */
	private static int getIntInput(Scanner scanner, String prompt, int min, int max) {
		int value;
		do {
			System.out.print(prompt);
			while (!scanner.hasNextInt()) {
				System.err.println("Invalid input. Please enter an integer.");
				scanner.next();
				System.out.print(prompt);
			}
			value = scanner.nextInt();
			scanner.nextLine(); // Consume newline
			if (value < min || value > max) {
				System.err.println("Value must be between " + min + " and " + max + ".");
			}
		} while (value < min || value > max);
		return value;
	}
}