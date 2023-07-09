package io.codelex.flightplanner.flights.response;

import io.codelex.flightplanner.flights.domain.Flight;

public class SearchFlightsResponse {
private int page;
private int totalItems;
private Flight[] items;

    public SearchFlightsResponse(int page, int totalItems, Flight[] items) {
        this.page = page;
        this.totalItems = totalItems;
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public Flight[] getItems() {
        return items;
    }

    public void setItems(Flight[] items) {
        this.items = items;
    }
}
