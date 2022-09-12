package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.view.RxView;
import com.quawlebs.drupp.models.ShippingInfoModel;
import com.quawlebs.drupp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class ShippingAddressListAdapter extends RecyclerView.Adapter<ShippingAddressListAdapter.ShippingAddressHolder> {
    public int mSelectedItem = -1;
    private Context mContext;
    private int mResId;
    private ArrayList<ShippingInfoModel> mData;
    private Disposable itemViewDisposable;
    private PublishSubject<Integer> itemClickObservable = PublishSubject.create();

    public ShippingAddressListAdapter(Context context, int resId, ArrayList<ShippingInfoModel> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }


    public PublishSubject<Integer> getItemClickObservable() {
        return itemClickObservable;
    }

    @NonNull
    @Override
    public ShippingAddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShippingAddressHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressHolder holder, int position) {
        try {
            holder.addressSelection.setChecked(mData.get(position).isSelected());
            holder.completeName.setText(mContext.getString(R.string.full_name, mData.get(position).getFirstName(), mData.get(position).getLastName()));
            holder.completeAddress.setText(mContext.getString(R.string.full_address, mData.get(position).getStreet(),
                    mData.get(position).getCity(), mData.get(position).getState(), mData.get(position).getCountry(), mData.get(position).getPostalCode()));
            holder.phone.setText(mData.get(position).getPhone());
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
//        itemViewDisposable.dispose();
        itemClickObservable.onComplete();

    }

    public class ShippingAddressHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rb_address)
        RadioButton addressSelection;
        @BindView(R.id.tv_complete_address)
        TextView completeAddress;
        @BindView(R.id.tv_complete_name)
        TextView completeName;
        @BindView(R.id.tv_phone)
        TextView phone;

        public ShippingAddressHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemViewDisposable = RxView.clicks(itemView).map(integer -> getAdapterPosition()).subscribe(position -> {
//
//                for (ShippingInfoModel shippingInfoModel : mData) {
//                    shippingInfoModel.setSelected(false);
//                }
//                mData.get(getAdapterPosition()).setSelected(true);
//                mSelectedItem = getAdapterPosition();
//                notifyItemChanged(getAdapterPosition());
//                notifyDataSetChanged();
//            });
            RxView.clicks(itemView)
                    .map(integer -> getAdapterPosition())
                    .subscribe(itemClickObservable);
        }
    }
}
