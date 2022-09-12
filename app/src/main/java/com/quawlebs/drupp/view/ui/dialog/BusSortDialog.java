package com.quawlebs.drupp.view.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.models.SortFilterModel;
import com.quawlebs.drupp.view.adapters.SortAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class BusSortDialog extends BottomSheetDialogFragment {
    @BindView(R.id.rv_sort_filter)
    RecyclerView sortFilterRecyclerView;

    private SortAdapter sortAdapter;
    private ArrayList<SortFilterModel> sortFilterModels = new ArrayList<>();
    private int mSelectedSort = -1;

    private IDialogObserver<Integer> iDialogObserver;
    private Disposable sortDisposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_sort_filter_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

 //       sortFilterModels.addAll(Helper.getAllFilterTypes())
    }
}
