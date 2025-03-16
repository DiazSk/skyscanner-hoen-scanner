package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {

    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws IOException {
        // Load rental cars from JSON
        ObjectMapper mapper = new ObjectMapper();
        List<SearchResult> searchResults = new ArrayList<>();
        
        // Load rental cars
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json")) {
            List<SearchResult> rentalCars = mapper.readValue(inputStream, new TypeReference<List<SearchResult>>() {});
            // Add kind to each rental car
            rentalCars.forEach(car -> car.setKind("rental_car"));
            searchResults.addAll(rentalCars);
        }
        
        // Load hotels
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hotels.json")) {
            List<SearchResult> hotels = mapper.readValue(inputStream, new TypeReference<List<SearchResult>>() {});
            // Add kind to each hotel
            hotels.forEach(hotel -> hotel.setKind("hotel"));
            searchResults.addAll(hotels);
        }
        
        // Register the search resource
        environment.jersey().register(new SearchResource(searchResults));
    }
}
