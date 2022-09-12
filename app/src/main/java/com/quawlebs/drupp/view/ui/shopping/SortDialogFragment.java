package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.models.SortFilterModel;
import com.quawlebs.drupp.view.adapters.SortAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class SortDialogFragment extends BottomSheetDialogFragment {
    @BindView(R.id.rv_sort_filter)
    RecyclerView sortRecyclerView;

    private SortAdapter sortAdapter;
    private ArrayList<SortFilterModel> sortFilterModels = new ArrayList<>();
    private int mSelectedSort = -1;

    private IDialogObserver<Integer> iDialogObserver;
    private Disposable sortDisposable;

    public void setiDialogObserver(IDialogObserver iDialogObserver) {
        this.iDialogObserver = iDialogObserver;
    }

    public SortDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortFilterModels.addAll(Helper.getAllFilterTypes(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sort_filter_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sortRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sortAdapter = new SortAdapter(getContext(), sortFilterModels);
        sortRecyclerView.setAdapter(sortAdapter);
        sortDisposable = sortAdapter.getItemClickObservable().subscribe(position -> {

            for (SortFilterModel sortFilterModel : sortFilterModels) {
                sortFilterModel.setChecked(false);
            }
            mSelectedSort = sortFilterModels.get(position).getId();
            sortFilterModels.get(position).setChecked(true);
            sortAdapter.notifyItemChanged(position);
            sortAdapter.notifyDataSetChanged();
            iDialogObserver.onDialogResult(sortFilterModels.get(position).getId());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sortDisposable.dispose();
    }


}
