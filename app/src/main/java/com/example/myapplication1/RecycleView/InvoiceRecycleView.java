package com.example.myapplication1.RecycleView;
public class InvoiceRecycleView {
    private String id;
    private String date;
    private String bill;

    public InvoiceRecycleView(String id, String date, String bill) {
        this.id = id;
        this.date = date;
        this.bill = bill;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getBill() {
        return bill;
    }
}