package com.schedo.web.controller;

public class Request {
    public Request(int user_id, int company_id, String comment) {
        this.user_id = user_id;
        this.company_id = company_id;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public int getCompany_id() {
        return company_id;
    }

    public int getUser_id() {
        return user_id;
    }

    private String comment;
    private int user_id;
    private int company_id;
}
