package org.wch.util;

public class ProductBean {

    private String title;
    private String startSellDate;
    private String merchStatus;
    private String publishType;
    private String price;
    private String onSale;
    private String style;
    private String colorCode;
    private String colorDescription;
    private String selectionEngine;
    private String imageUrl;

    private String publishedDate;
    private String lastUpdatedDate;

    private String seoSlug;

    private String id;

    private String stopSellDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartSellDate() {
        return startSellDate;
    }

    public void setStartSellDate(String startSellDate) {
        this.startSellDate = startSellDate;
    }

    public String getMerchStatus() {
        return merchStatus;
    }

    public void setMerchStatus(String merchStatus) {
        this.merchStatus = merchStatus;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOnSale() {
        return onSale;
    }

    public void setOnSale(String onSale) {
        this.onSale = onSale;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorDescription() {
        return colorDescription;
    }

    public void setColorDescription(String colorDescription) {
        this.colorDescription = colorDescription;
    }

    public String getSelectionEngine() {
        return selectionEngine;
    }

    public void setSelectionEngine(String selectionEngine) {
        this.selectionEngine = selectionEngine;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public void setSeoSlug(String seoSlug) {
        this.seoSlug = seoSlug;
    }

    public String getStopSellDate() {
        return stopSellDate;
    }

    public void setStopSellDate(String stopSellDate) {
        this.stopSellDate = stopSellDate;
    }

    @Override
    public boolean equals(Object obj) {
       // return super.equals(obj);
        return this.id.equals(((ProductBean)obj).id);
    }
}
