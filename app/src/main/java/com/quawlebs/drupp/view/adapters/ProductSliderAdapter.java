package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.models.ProductImageModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSliderAdapter extends SliderViewAdapter<ProductSliderAdapter.ProductSliderHolder> {
    private Context mContext;

    private ArrayList<ProductImageModel> mData;

    public ProductSliderAdapter(Context context, ArrayList<ProductImageModel> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ProductSliderHolder onCreateViewHolder(ViewGroup parent) {
        return new ProductSliderHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_slider, null));
    }

    @Override
    public void onBindViewHolder(ProductSliderHolder holder, int position) {
        try {
            Glide.with(mContext).load(AppConstants.IMAGE_URL + mData.get(position).getFilePath()).apply(new RequestOptions()
                    .error(R.drawable.no_image_available).placeholder(R.drawable.no_image_available)).into(holder.previewImage);
        } catch (Exception e) {

        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public class ProductSliderHolder extends SliderViewAdapter.ViewHolder {
        @BindView(R.id.iv_preview_image)
        ImageView previewImage;


        public ProductSliderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
