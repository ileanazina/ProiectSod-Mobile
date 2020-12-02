package com.example.myapplication1.Model;

import java.io.File;
import com.google.gson.annotations.SerializedName;

public class UserUploadModel {
    @SerializedName("AccountId")
    private int AccountId;
    @SerializedName("DocumentId")
    private int DocumentId;
    @SerializedName("DocumentName")
    private String DocumentName;
    @SerializedName("File")
    private File File;

    public UserUploadModel(int accountId, int documentId, String documentName, File file) {
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

    public File getFile() {
        return File;
    }

    public void setFile(File file) {
        File = file;
    }
}
