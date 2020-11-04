package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class IndexModel {
    @SerializedName("indexId")
    private int indexId;
    @SerializedName("indexValue")
    private float indexValue;
    @SerializedName("indexDate")
    private Date indexDate;
    @SerializedName("averageIndex")
    private float averageIndex;

    public int getIndexId() {
        return indexId;
    }

    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    public IndexModel(int indexId ,float indexValue, Date indexDate, float averageIndex) {
        this.indexId= indexId;
        this.indexValue = indexValue;
        this.indexDate = indexDate;
        this.averageIndex = averageIndex;
    }

    public float getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(float indexValue) {
        this.indexValue = indexValue;
    }

    public Date getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(Date indexDate) {
        this.indexDate = indexDate;
    }

    public float getAverageIndex() {
        return averageIndex;
    }

    public void setAverageIndex(float averageIndex) {
        this.averageIndex = averageIndex;
    }
}
