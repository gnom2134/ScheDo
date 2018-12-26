package com.schedo.web.controller;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    public Schedule(int id, String name) {
        this.id = id;
        this.name = name;
        days = new ArrayList<Day>();
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Day> getDays() {
        return days;
    }

    public void Add(Day day) {
        days.add(day);
    }

    private String name;
    private int id;
    private List<Day> days;
    private int recommendationId;
}
