package com.example.myapp;

public class HistoryRecord {
    public String date;
    public String proteinSummary;
    public String carbSummary;
    public String otherSummary;

    public HistoryRecord(String date, String proteinSummary, String carbSummary, String otherSummary) {
        this.date = date;
        this.proteinSummary = proteinSummary;
        this.carbSummary = carbSummary;
        this.otherSummary = otherSummary;
    }
}
