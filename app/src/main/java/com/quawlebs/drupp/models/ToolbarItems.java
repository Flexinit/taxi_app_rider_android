package com.quawlebs.drupp.models;

public class ToolbarItems {
    String title;
    int imgRefrence;
    int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgRefrence() {
        return imgRefrence;
    }

    public void setImgRefrence(int imgRefrence) {
        this.imgRefrence = imgRefrence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ToolbarItems(String title, int imgRefrence, int id) {
        this.title = title;
        this.imgRefrence = imgRefrence;
        this.id = id;
    }
}
