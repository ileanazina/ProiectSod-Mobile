package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class UserUploadModel {
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("documentId")
    private int documentId;
    @SerializedName("documentName")
    private String documentName;
    @SerializedName("file")
    private String file;

    public UserUploadModel(int accountId, int documentId, String documentName, String file) {
        this.accountId = accountId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.file = file;
    }

    public UserUploadModel(int accountId, int documentId, String documentName) {
        this.accountId = accountId;
        this.documentId = documentId;
        this.documentName = documentName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        accountId = accountId;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        documentName = documentName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
