package com.example.myapplication1.Model;
import com.google.gson.annotations.SerializedName;

public class InvoiceDetailsModel {

    @SerializedName("invoiceDetailsId")
    private int invoiceDetailsId;
    @SerializedName("invoiceId")
    private int invoiceId;
    @SerializedName("unitMeasurementType")
    private int unitMeasurementType;
    @SerializedName("vat")
    private float VAT;
    @SerializedName("valueWithVat")
    private float valueWithVat;
    @SerializedName("valueWithoutVAT")
    private float valueWithoutVAT;
    @SerializedName("sold")
    private float sold;
    @SerializedName("consumedQuantity")
    private float consumedQuantity;
    @SerializedName("pricePerUnit")
    private float pricePerUnit;

    public int getInvoiceDetailsId() {
        return invoiceDetailsId;
    }

    public void setInvoiceDetailsId(int invoiceDetailsId) {
        this.invoiceDetailsId = invoiceDetailsId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getUnitMeasurementType() {
        return unitMeasurementType;
    }

    public void setUnitMeasurementType(int unitMeasurementType) {
        this.unitMeasurementType = unitMeasurementType;
    }

    public float getVAT() {
        return VAT;
    }

    public void setVAT(float VAT) {
        this.VAT = VAT;
    }

    public float getValueWithVat() {
        return valueWithVat;
    }

    public void setValueWithVat(float valueWithVat) {
        this.valueWithVat = valueWithVat;
    }

    public float getValueWithoutVAT() {
        return valueWithoutVAT;
    }

    public void setValueWithoutVAT(float valueWithoutVAT) {
        this.valueWithoutVAT = valueWithoutVAT;
    }

    public float getSold() {
        return sold;
    }

    public void setSold(float sold) {
        this.sold = sold;
    }

    public float getConsumedQuantity() {
        return consumedQuantity;
    }

    public void setConsumedQuantity(float consumedQuantity) {
        this.consumedQuantity = consumedQuantity;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
