package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class MatrixRow {
    @SerializedName("elements")
    @Expose
    private List<MatrixElement> elements ;


    public List<MatrixElement> getElements() {
        return elements;
    }

    public void setElements(List<MatrixElement> elements) {
        this.elements = elements;
    }
}
