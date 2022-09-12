package com.quawlebs.drupp.view.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FareDialog extends BottomSheetDialogFragment {
    private Unbinder unbinder;
    @BindView(R.id.tv_base_fare)
    TextView baseFare;
    @BindView(R.id.tv_distance)
    TextView distance;
    @BindView(R.id.per_km_charge)
    TextView perKmCharge;

    public static FareDialog newInstance(String baseFare, String perKmCharge, String distance) {

        Bundle args = new Bundle();
        args.putString(AppConstants.K_BASE_FARE, baseFare);
        args.putString(AppConstants.K_PER_KM, perKmCharge);
        args.putString(AppConstants.K_DISTANCE, distance);

        FareDialog fragment = new FareDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fare, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            try {
                baseFare.setText(getString(R.string.fare_in_naira, getArguments().getString(AppConstants.K_BASE_FARE)));
                perKmCharge.setText(getString(R.string.fare_in_naira, getArguments().getString(AppConstants.K_PER_KM)));
                distance.setText(getString(R.string.in_km,getArguments().getString(AppConstants.K_DISTANCE)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.btn_close)
    public void onClose() {
        dismiss();
    }
}
