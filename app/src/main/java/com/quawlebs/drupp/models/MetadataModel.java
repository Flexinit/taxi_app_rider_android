package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetadataModel {

    @SerializedName("custom_fields")
    @Expose
    private List<CustomFieldModel> customFields;

    public List<CustomFieldModel> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomFieldModel> customFields) {
        this.customFields = customFields;
    }
}
