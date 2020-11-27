package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class UserUploadModel {
    @SerializedName("AccountId")
    private int AccountId;
    @SerializedName("DocumentId")
    private int DocumentId;
    @SerializedName("DocumentName")
    private String DocumentName;
    @SerializedName("File")
    private byte[] File;

    public UserUploadModel(int accountId, int documentId, String documentName, byte[] file) {
        AccountId = accountId;
        DocumentId = documentId;
        DocumentName = documentName;
        File = file;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }

    public int getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(int documentId) {
        DocumentId = documentId;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }

    public byte[] getFile() {
        return File;
    }

    public void setFile(byte[] file) {
        File = file;
    }
}
