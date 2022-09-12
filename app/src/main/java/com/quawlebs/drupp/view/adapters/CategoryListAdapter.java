package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding3.view.RxView;
import com.quawlebs.drupp.models.CategoryModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryHolder> {

    private Context mContext;
    private int mResId;
    private ArrayList<CategoryModel> mData;

    private PublishSubject<Integer> mPublishSubject = PublishSubject.create();

    public CategoryListAdapter(Context context, int resId, ArrayList<CategoryModel> productModels) {
        mContext = context;
        mResId = resId;
        mData = productModels;

    }

    public PublishSubject<Integer> getmPublishSubject() {
        return mPublishSubject;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        try {
            Glide.with(mContext).load(AppConstants.IMAGE_URL + mData.get(position).getImage()).apply(new RequestOptions()
                    .error(R.drawable.no_image_available).placeholder(R.drawable.no_image_available)).into(holder.categoryPreview);
            holder.categoryName.setText(mData.get(position).getCategoryName());
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mPublishSubject.onComplete();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        ImageView categoryPreview;
        TextView categoryName;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryPreview = itemView.findViewById(R.id.iv_category_preview);
            categoryName = itemView.findViewById(R.id.tv_category_name);
            RxView.clicks(itemView)
                    .map(integer -> getAdapterPosition())
                    .subscribe(mPublishSubject);
        }
    }
}
