package com.moodflix.service;

import com.moodflix.model.Quote;
import java.util.ArrayList;
import java.util.List;

public class FavoriteService {

    private List<Quote> favorites = new ArrayList<>();

    // Add a quote to favorites
    public void addFavorite(Quote quote) {
        favorites.add(quote);
    }

    // Get all favorites
    public List<Quote> getFavorites() {
        return favorites;
    }
}
