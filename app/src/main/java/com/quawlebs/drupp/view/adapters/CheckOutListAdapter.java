package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding3.view.RxView;
import com.quawlebs.drupp.models.CheckOutProductModel;
import com.quawlebs.drupp.models.ProductInfoModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IAdapterItemSpinnerSelectedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class CheckOutListAdapter extends RecyclerView.Adapter<CheckOutListAdapter.CheckOutHolder> {

    private Context mContext;
    private int mResId;
    private ArrayList<CheckOutProductModel> mData;

    private PublishSubject<Integer> deleteItemClickObservable = PublishSubject.create();
    private IAdapterItemSpinnerSelectedListener iAdapterItemSpinnerSelectedListener;

    public CheckOutListAdapter(Context context, int resId, ArrayList<CheckOutProductModel> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    public PublishSubject<Integer> getDeleteItemClickObservable() {
        return deleteItemClickObservable;
    }

    public void setiAdapterItemSpinnerSelectedListener(IAdapterItemSpinnerSelectedListener iAdapterItemSpinnerSelectedListener) {
        this.iAdapterItemSpinnerSelectedListener = iAdapterItemSpinnerSelectedListener;
    }

    @NonNull
    @Override
    public CheckOutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckOutHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutHolder holder, int position) {

        try {
            ProductInfoModel productModel = mData.get(position).getProduct();

            holder.productName.setText(productModel.getProductName());
            holder.productDesc.setText(productModel.getDescription());
            holder.productPrice.setText(mContext.getString(R.string.price_in_naira, productModel.getPrice()));

            Glide.with(mContext).load(AppConstants.IMAGE_URL + productModel.getProductImages().get(0).getFilePath()).apply(new RequestOptions()
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
        deleteItemClickObservable.onComplete();
    }

    public class CheckOutHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {
        @BindView(R.id.tv_product_name)
        TextView productName;
        @BindView(R.id.tv_description)
        TextView productDesc;
        @BindView(R.id.tv_product_price)
        TextView productPrice;
        @BindView(R.id.iv_preview_image)
        ImageView productImage;
        @BindView(R.id.iv_delete)
        ImageView remove;
        @BindView(R.id.spinner_quantity)
        Spinner spinnerQuantity;
        private boolean spinnerInitial = true;
        ArrayList<Integer> quantities = new ArrayList<>();

        public CheckOutHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(remove)
                    .map(integer -> getAdapterPosition())
                    .subscribe(deleteItemClickObservable);
            for (int i = 0; i < AppConstants.SPINNER_SHOP_MAX_PRODUCT_LIMIT; i++) {
                quantities.add(i + 1);
            }
            ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, quantities);
            spinnerQuantity.setAdapter(spinnerAdapter);
            spinnerQuantity.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (iAdapterItemSpinnerSelectedListener == null) return;
            if (spinnerInitial) {
                spinnerInitial = false;
                return;
            }
            iAdapterItemSpinnerSelectedListener.onItemSelected(getAdapterPosition(), position + 1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
