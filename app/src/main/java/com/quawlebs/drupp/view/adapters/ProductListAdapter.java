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
import com.quawlebs.drupp.models.ProductModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.NewHolder> {
    private Context mContext;
    private int mResId;
    private ArrayList<ProductModel> mData;

    private PublishSubject<Integer> mItemViewObservable = PublishSubject.create();

    public Observable<Integer> getItemViewObservable() {
        return mItemViewObservable;
    }

    public ProductListAdapter(Context context, int resId, ArrayList<ProductModel> productModels) {
        mContext = context;
        mResId = resId;
        mData = productModels;
    }

    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {
        try {
            holder.productName.setText(mData.get(position).getProductName());
            holder.productPrice.setText(mContext.getString(R.string.price_in_naira, mData.get(position).getPrice()));
            Glide.with(mContext).load(AppConstants.IMAGE_URL + mData.get(position).getProductImages().get(0).getFilePath()).apply(new RequestOptions()
                    .error(R.drawable.no_image_available).placeholder(R.drawable.no_image_available)).into(holder.productImage);
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
        mItemViewObservable.onComplete();
    }

    public class NewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_product_preview_image)
        ImageView productImage;
        @BindView(R.id.tv_product_price)
        TextView productPrice;
        @BindView(R.id.tv_product_name)
        TextView productName;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(itemView)
                    .map(integer -> getAdapterPosition())
                    .subscribe(mItemViewObservable);
        }

    }
}
