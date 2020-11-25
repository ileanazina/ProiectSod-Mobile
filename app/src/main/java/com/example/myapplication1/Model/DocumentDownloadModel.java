package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DocumentDownloadModel {
    @SerializedName("documentName")
    private String documentName;
    @SerializedName("documentType")
    private int documentType;
    @SerializedName("uploadDate")
    private Date uploadDate;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("downloadLink")
    private String downloadLink;
    @SerializedName("documentTypeName")
    private String documentTypeName;

    public DocumentDownloadModel(String documentName, int documentType, Date uploadDate, boolean isActive, String downloadLink, String documentTypeName) {
        this.documentName = documentName;
        this.documentType = documentType;
        this.uploadDate = uploadDate;
        this.isActive = isActive;
        this.downloadLink = downloadLink;
        this.documentTypeName = documentTypeName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }
}
