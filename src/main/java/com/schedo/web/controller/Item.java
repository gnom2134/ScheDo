package com.schedo.web.controller;


public class Item implements Comparable<Item> {

    public Item(int id, String activity, String end_time) {
        this.id = id;
        this.end_time = end_time;
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public String getActivity() {
        return activity;
    }

    public String getEnd_time() {
        return end_time;
    }

    public int getMinutes() {
        String[] time = end_time.split("-");
        return Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
    }

    public static String timeWithMinutes(int arg_minutes) {
        String hours = Integer.toString(arg_minutes / 60);
        String minutes = Integer.toString(arg_minutes % 60);
        if (hours.length() < 2) hours = "0" + hours;
        if (minutes.length() < 2) minutes = "0" + minutes;
        return hours + "-" + minutes;
    }

    public int compareByTime(String time) {
        String[] separated = time.split("-");
        return this.getMinutes() - (Integer.parseInt(separated[0]) * 60 + Integer.parseInt(separated[1]));
    }

    public int compareTo(Item o) {
        return this.getMinutes() - o.getMinutes();
    }

    public static int compareTime (String lhs, String rhs) {
        String[] separatedL = lhs.split("-");
        String[] separatedR = rhs.split("-");
        return (Integer.parseInt(separatedL[0]) * 60 + Integer.parseInt(separatedL[1])) - (Integer.parseInt(separatedR[0]) * 60 + Integer.parseInt(separatedR[1]));
    }

    private String end_time;
    private String activity;
    private int id;
}
