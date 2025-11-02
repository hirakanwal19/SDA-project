package com.moodflix.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Quote> favorites;
    private static final String FAVORITES_PATH = "src/main/resources/favorites.txt";

    public User(String name) {
        this.name = name;
        this.favorites = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // ✅ Add a quote to favorites and save to text file
    public void addFavorite(Quote quote) {
        favorites.add(quote);
        saveFavoriteToFile(quote);
    }

    // ✅ Save to file
    private void saveFavoriteToFile(Quote quote) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FAVORITES_PATH, true))) {
            bw.write(quote.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("⚠️ Failed to save favorite: " + e.getMessage());
        }
    }

    // ✅ Load favorites from file
    public void showFavorites() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FAVORITES_PATH));
            System.out.println("\n📖 Your Favorites:");
            if (lines.isEmpty()) {
                System.out.println("(No favorites yet)");
            } else {
                for (String line : lines) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Unable to read favorites: " + e.getMessage());
        }
    }
}
