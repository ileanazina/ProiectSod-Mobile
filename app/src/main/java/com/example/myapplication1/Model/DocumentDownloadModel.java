package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class DocumentDownloadModel implements Serializable {
    @SerializedName("documentId")
    private int documentId;
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
    @SerializedName("documentoLoad")
    private byte[] documentLoad;

    public DocumentDownloadModel( int documentType,String documentName, byte[] uploadFile) {
        this.documentType = documentType;
        this.documentName = documentName;
        this.documentLoad = uploadFile;
    }

    public DocumentDownloadModel(int documentId, String documentName, int documentType, Date uploadDate, boolean isActive, String downloadLink, String documentTypeName) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.uploadDate = uploadDate;
        this.isActive = isActive;
        this.downloadLink = downloadLink;
        this.documentTypeName = documentTypeName;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
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

    public byte[] getDocumentLoad() {
        return documentLoad;
    }

    public void setDocumentLoad(byte[] documentLoad) {
        this.documentLoad = documentLoad;
    }
}
