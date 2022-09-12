package com.quawlebs.drupp.helpers;

import android.view.View;

import com.google.android.libraries.places.api.model.Place;

public interface IAdapterItemClickListener {
    void onAdapterItemClick(View v, int position);

    void onAdapterItemClick(Place place);
}
