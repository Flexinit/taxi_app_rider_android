package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.view.RxView;
import com.quawlebs.drupp.models.PlacedOrdersModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.OrderHolder> {
    private Context mContext;
    private int mResId;
    private List<PlacedOrdersModel> mData;
    private PublishSubject<Integer> itemClickObservable = PublishSubject.create();

    public OrdersListAdapter(Context context, int resId, LinkedList<PlacedOrdersModel> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    public PublishSubject<Integer> getItemClickObservable() {
        return itemClickObservable;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        try {
            holder.orderId.setText(mContext.getString(R.string.order_id, mData.get(position).getId()));
            String status = "";
            switch (mData.get(position).getStatus()) {
                case AppConstants.ORDER_STATUS.ORDERED:
                    status = mContext.getString(R.string.ordered);
                    break;
                case AppConstants.ORDER_STATUS.SHIPPED:
                    status = mContext.getString(R.string.shipped);
                    break;
                case AppConstants.ORDER_STATUS.DELIVERED:
                    status = mContext.getString(R.string.delivered);
                    break;
                case AppConstants.ORDER_STATUS.CANCELED:
                    status = mContext.getString(R.string.canceled);
                    break;
            }
            holder.status.setText(status);
            holder.price.setText(mContext.getString(R.string.price_in_naira, mData.get(position).getTotalPrice()));
            if (mData.get(position).getOrderDetails().size() > 0) {
                holder.products.setText(mContext.getString(R.string.product_and_more, mData.get(position).getOrderDetails().get(0).getProduct().getProductName()));
            } else {
                holder.products.setText(mData.get(position).getOrderDetails().get(0).getProduct().getProductName());
            }


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
        itemClickObservable.onComplete();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_id)
        TextView orderId;
        @BindView(R.id.tv_all_products)
        TextView products;
        @BindView(R.id.tv_status)
        TextView status;
        @BindView(R.id.tv_total_price)
        TextView price;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(itemView)
                    .map(integer -> getAdapterPosition())
                    .subscribe(itemClickObservable);
        }

    }
}
