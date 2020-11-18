package com.example.myapplication1.Model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class InvoiceModel implements Serializable {
    @SerializedName("invoiceId")
    private int invoiceId;
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("vat")
    private float VAT;
    @SerializedName("valueWithVat")
    private float valueWithVat;
    @SerializedName("valueWithoutVAT")
    private float valueWithoutVAT;
    @SerializedName("dateOfIssue")
    private Date dateOfIssue;
    @SerializedName("dueDate")
    private Date dueDate;
    @SerializedName("cuiIssuer")
    private int CUIIssuer;
    @SerializedName("issuerAddress")
    private String issuerAddress;
    @SerializedName("isPaid")
    private boolean isPaid;
    public InvoiceModel()
    {}

    public InvoiceModel( int invoiceId, int accountId, int addressId, float vat, float valueWithVat, float valueWithoutVAT,
                         Date dateOfIssue, Date dueDate, int cuiIssuer, String issuerAddress, boolean isPaid) throws ParseException {
        this.invoiceId = invoiceId;
        this.accountId = accountId;
        this.addressId = addressId;
        this.VAT = vat;
        this.valueWithVat = valueWithVat;
        this.valueWithoutVAT = valueWithoutVAT;
        this.dateOfIssue = dateOfIssue;
        this.dueDate = dueDate;
        this.CUIIssuer = cuiIssuer;
        this.issuerAddress = issuerAddress;
        this.isPaid=isPaid;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getIssuerAddress() {
        return issuerAddress;
    }

    public void setIssuerAddress(String mIssuerAddress){
        this.issuerAddress = mIssuerAddress;
    }

    public int getCUIIssuer() {
        return CUIIssuer;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getAddressId() {
        return addressId;
    }

    public float getVAT() {
        return VAT;
    }

    public float getValueWithVat() {
        return valueWithVat;
    }

    public float getValueWithoutVAT() {
        return valueWithoutVAT;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }
}