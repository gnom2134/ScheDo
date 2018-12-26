package com.schedo.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day {
    public Day (int id, String day_of_the_week){
        this.day_of_the_week = day_of_the_week;
        this.id = id;
        items = new ArrayList<Item>();
    }

    public void setDay_of_the_week(String day_of_the_week) {
        this.day_of_the_week = day_of_the_week;
    }

    public String getDay_of_the_week() {
        return day_of_the_week;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addItem(Item item, int index) {
        items.add(index, item);
    }

    public void sortItems() {
        Collections.sort(items);
    }

    private int id;
    private String day_of_the_week;
    private List<Item> items;
}
