package com.quawlebs.drupp.models;

public class SortFilterModel {
    private int id;
    private String title;
    private boolean isChecked;


    public SortFilterModel(String title, boolean isChecked, int id) {
        this.isChecked = isChecked;
        this.title = title;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
