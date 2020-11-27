package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class DownloadFilter {
    @SerializedName("documentName")
    private String documentName;
    @SerializedName("pageNumber")
    private String pageNumber;
    @SerializedName("itemsOnPage")
    private String itemsOnPage;

    public DownloadFilter(){
        this.documentName = null;
        this.pageNumber = null;
        this.itemsOnPage = null;
    }

    public DownloadFilter(String documentName, String pageNumber, String itemsOnPage) {
        this.documentName = documentName;
        this.pageNumber = pageNumber;
        this.itemsOnPage = itemsOnPage;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getItemsOnPage() {
        return itemsOnPage;
    }

    public void setItemsOnPage(String itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }
}
