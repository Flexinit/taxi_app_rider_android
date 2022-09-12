package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.quawlebs.drupp.models.FilterAttributeModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import java.util.HashMap;
import java.util.List;

public class ProductFilterListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mParentList;
    private HashMap<String, List<FilterAttributeModel>> mChildList;

    public ProductFilterListAdapter(Context context, List<String> parentList, HashMap<String, List<FilterAttributeModel>> childList) {
        mContext = context;
        mParentList = parentList;
        mChildList = childList;
    }

    @Override
    public int getGroupCount() {
        return mParentList.size();
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        return mChildList.get(mParentList.get(parentPosition)).size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return mParentList.get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return mChildList.get(mParentList.get(parentPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parentPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        String listTitle = (String) getGroup(parentPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_group, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int parentPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final FilterAttributeModel childItem = (FilterAttributeModel) getChild(parentPosition, childPosition);
        String title = "";
        switch (mParentList.get(parentPosition)) {
            case AppConstants.FILTERS.K_BRAND:
                title = childItem.getBrand();
                break;
            case AppConstants.FILTERS.K_COLOR:
                title = childItem.getColor();
                break;
            case AppConstants.FILTERS.K_SIZE:
                title = childItem.getSize();
                break;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_child, null);
        }
        TextView expandedListTextView = convertView
                .findViewById(R.id.tv_child_item);
        CheckBox check = convertView.findViewById(R.id.checkbox_child_filter);
        check.setChecked(childItem.isChecked());
        expandedListTextView.setText(title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}
