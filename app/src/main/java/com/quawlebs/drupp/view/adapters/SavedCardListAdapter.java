package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding3.view.RxView;
import com.quawlebs.drupp.models.Authorization;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class SavedCardListAdapter extends RecyclerView.Adapter<SavedCardListAdapter.SavedCardHolder> {

    private Context mContext;
    private int mResId;
    private List<Authorization> mData;
    private PublishSubject<Integer> mItemClickSubject = PublishSubject.create();
    private IAdapterItemClickListener iAdapterItemClickListener;

    public SavedCardListAdapter(Context context, int resId, LinkedList<Authorization> cardHolderArrayList) {
        mContext = context;
        mResId = resId;
        mData = cardHolderArrayList;
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    public PublishSubject<Integer> getmItemClickSubject() {
        return mItemClickSubject;
    }

    @NonNull
    @Override
    public SavedCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedCardHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedCardHolder holder, int position) {
        try {

            holder.cardSelected.setChecked(mData.get(position).isChecked());
            holder.payButton.setVisibility(mData.get(position).isChecked() ? View.VISIBLE : View.GONE);
            holder.cardBrand.setText(mData.get(position).getBrand());
            holder.cardNumber.setText(mContext.getString(R.string.card_number_saved, mData.get(position).getLastFourDigit()));
            Glide.with(mContext).load(mData.get(position).getCardImage()).apply(new RequestOptions()
                    .error(R.drawable.no_image_available)
                    .centerCrop().placeholder(R.drawable.no_image_available)).into(holder.cardImage);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "error");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mItemClickSubject.onComplete();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SavedCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_card_image)
        ImageView cardImage;
        @BindView(R.id.tv_card_brand)
        TextView cardBrand;
        @BindView(R.id.tv_card_number)
        TextView cardNumber;
        @BindView(R.id.rb_select_card)
        RadioButton cardSelected;
        @BindView(R.id.btn_pay)
        Button payButton;

        public SavedCardHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(itemView)
                    .map(integer -> getAdapterPosition())
                    .subscribe(mItemClickSubject);
            payButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (iAdapterItemClickListener == null)
                return;
            iAdapterItemClickListener.onAdapterItemClick(view, getAdapterPosition());
        }
    }
}
