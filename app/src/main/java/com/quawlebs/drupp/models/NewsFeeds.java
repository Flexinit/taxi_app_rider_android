package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsFeeds {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("news_image")
    @Expose
    private String newsImage;
    @SerializedName("news_subcategory")
    @Expose
    private String newsSubCategory;
    @SerializedName("headline")
    @Expose
    private String headLine;
    @SerializedName("news_description")
    @Expose
    private String desc;
    @SerializedName("timestamp")
    @Expose
    private long timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNewsSubCategory() {
        return newsSubCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
