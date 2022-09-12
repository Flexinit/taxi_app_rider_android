package com.quawlebs.drupp.helpers;

import com.quawlebs.drupp.models.FilterAttributeModel;

import java.util.HashMap;
import java.util.List;

public interface IFilterDialogObserver {
    void onFilterSelected(HashMap<String, List<FilterAttributeModel>> attributes);
}
