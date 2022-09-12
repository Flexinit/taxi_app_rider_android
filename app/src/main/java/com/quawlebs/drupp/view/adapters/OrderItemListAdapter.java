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
import com.quawlebs.drupp.models.OrderDetailModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.OrderItemHolder> {

    private Context mContext;
    private int mResId;
    private ArrayList<OrderDetailModel> mData;

    public OrderItemListAdapter(Context context, int resId, ArrayList<OrderDetailModel> items) {
        mContext = context;
        mResId = resId;
        mData = items;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        try {
            Glide.with(mContext).load(AppConstants.IMAGE_URL + mData.get(position).getProduct().getProductImages().get(0).getFilePath()).apply(new RequestOptions()
                    .error(R.drawable.no_image_available)
                    .centerCrop().placeholder(R.drawable.no_image_available)).into(holder.mOrderImage);
            holder.mProductName.setText(mData.get(position).getProduct().getProductName());
            holder.mProductQty.setText(mContext.getString(R.string.product_quantity, mData.get(position).getQuantity().toString()));
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_image)
        ImageView mOrderImage;
        @BindView(R.id.tv_product_name)
        TextView mProductName;
        @BindView(R.id.tv_product_qty)
        TextView mProductQty;

        public OrderItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
