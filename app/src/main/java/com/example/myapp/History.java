package com.example.myapp;

import java.util.List;

public class History {
    private String date;
    private List<String> details;
    private boolean isExpanded;

    public History(String date, List<String> details, boolean isExpanded) {
        this.date = date;
        this.details = details;
        this.isExpanded = isExpanded;
    }

    public String getDate() {
        return date;
    }

    public List<String> getDetails() {
        return details;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
