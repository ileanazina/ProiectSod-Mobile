package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class UnitMeasurementsModel {
    @SerializedName("unitMeasurementId")
    private int unitMeasurementId;
    @SerializedName("unitMeasurementType")
    private String unitMeasurementType;

    public UnitMeasurementsModel(int unitMeasurementId, String unitMeasurementType) {
        this.unitMeasurementId = unitMeasurementId;
        this.unitMeasurementType = unitMeasurementType;
    }

    public int getUnitMeasurementId() {
        return unitMeasurementId;
    }

    public void setUnitMeasurementId(int unitMeasurementId) {
        this.unitMeasurementId = unitMeasurementId;
    }

    public String getUnitMeasurementType() {
        return unitMeasurementType;
    }

    public void setUnitMeasurementType(String unitMeasurementType) {
        this.unitMeasurementType = unitMeasurementType;
    }
}
