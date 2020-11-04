package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PaymentModel {
    @SerializedName("paymentId")
    private int paymentId;
    @SerializedName("invoiceId")
    private int invoiceId;
    @SerializedName("paymentType")
    private int paymentType;
    @SerializedName("paymentAmount")
    private float paymentAmount;
    @SerializedName("paymentDate")
    private Date paymentDate;

    public PaymentModel(int paymentId, int invoiceId, int paymentType, float paymentAmount, Date paymentDate) {
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
