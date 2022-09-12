package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.view.RxView;
import com.quawlebs.drupp.models.SortFilterModel;
import com.quawlebs.drupp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortHolder> {
    private ArrayList<SortFilterModel> sortFilterModels;
    private Context mContext;
    private PublishSubject<Integer> itemClickObservable = PublishSubject.create();

    public SortAdapter(Context context, ArrayList<SortFilterModel> sortFilterModels) {
        mContext = context;
        this.sortFilterModels = sortFilterModels;
    }

    @NonNull
    @Override
    public SortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SortHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sort_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SortHolder holder, int position) {
        try {
            holder.sortType.setText(sortFilterModels.get(position).getTitle());
            holder.sortSelected.setChecked(sortFilterModels.get(position).isChecked());

        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "error");
        }

    }

    public PublishSubject<Integer> getItemClickObservable() {
        return itemClickObservable;
    }

    @Override
    public int getItemCount() {
        return sortFilterModels.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        itemClickObservable.onComplete();
    }

    public class SortHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sort_type)
        TextView sortType;
        @BindView(R.id.rb_sort_selected)
        RadioButton sortSelected;

        public SortHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sortSelected.setClickable(false);
            RxView.clicks(itemView)
                    .map(integer -> getAdapterPosition())
                    .subscribe(itemClickObservable);
        }
    }
}
