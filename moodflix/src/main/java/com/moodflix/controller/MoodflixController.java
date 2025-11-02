package com.moodflix.controller;

import com.moodflix.model.*;
import com.moodflix.service.*;
import java.util.Scanner;

public class MoodflixController {
    private final QuoteService quoteService = new QuoteService();

    public void startApp() {
        try (Scanner sc = new Scanner(System.in)) { // ✅ auto-close scanner
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            User user = new User(name);

            System.out.println("\n🎬 Welcome to Moodflix, " + name + "!");
            boolean running = true;

            while (running) {
                System.out.println("\nChoose your mood:");
                System.out.println("1. Happy");
                System.out.println("2. Sad");
                System.out.println("3. Tired");
                System.out.println("4. Stressed");
                System.out.println("5. Depressed");
                System.out.println("6. Show Favorites");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> showQuote(user, "happy", sc);
                    case 2 -> showQuote(user, "sad", sc);
                    case 3 -> showQuote(user, "tired", sc);
                    case 4 -> showQuote(user, "stressed", sc);
                    case 5 -> showQuote(user, "depressed", sc);
                    case 6 -> user.showFavorites();
                    case 7 -> {
                        running = false;
                        System.out.println("👋 Goodbye, " + name + "!");
                    }
                    default -> System.out.println("Invalid choice. Try again!");
                }
            }
        }
    }

    // ✅ Pass scanner as a parameter (so we reuse the same one)
    private void showQuote(User user, String mood, Scanner sc) {
        Quote quote = quoteService.fetchQuote(mood);
        System.out.println("\n💬 Quote for your mood (" + mood + "):");
        System.out.println(quote);

        System.out.print("\nDo you want to add this quote to favorites? (y/n): ");
        String ans = sc.nextLine();
        if (ans.equalsIgnoreCase("y")) {
            user.addFavorite(quote);
            System.out.println("✅ Added to favorites!");
        }
    }
}
