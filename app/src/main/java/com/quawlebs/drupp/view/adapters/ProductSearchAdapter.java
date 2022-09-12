package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.ProductSearchModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductSearchHolder> implements Filterable {
    private Context mContext;
    private int mResId;
    private ArrayList<ProductSearchModel> mData;
    private ArrayList<ProductSearchModel> mResultList = new ArrayList<>();

    public ProductSearchAdapter(Context context, int resId) {
        mContext = context;
        mResId = resId;

    }

    @NonNull
    @Override
    public ProductSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductSearchHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSearchHolder holder, int position) {
        try {
            holder.productSearch.setText(mResultList.get(position).getProductName());
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // Skip the autocomplete query if no constraints are given.


                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    //mResultList = getPredictions(constraint);
                    //mResultList.addAll(getSearchProducts(constraint.toString().trim()));
                    if (mResultList != null) {

                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
//                    if (iResponseObserver == null) return;
//                    iResponseObserver.onSuccess(AppConstants.SUCCESS);
                    notifyDataSetChanged();
                } else {
//                    if (iResponseObserver == null) return;
//                    iResponseObserver.onFailure(AppConstants.FAILURE);
                    // The API did not return any results, invalidate the data set.
                    mResultList.clear();
                    notifyDataSetChanged();
                }
            }
        };
    }


    public class ProductSearchHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_search)
        CustomTextView productSearch;

        public ProductSearchHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
