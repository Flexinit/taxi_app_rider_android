package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PaymentMethod> mData;
    private int TYPE_WALLET = 1, TYPE_CARD = 2, TYPE_CASH = 3;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public PaymentMethodAdapter(Context context, ArrayList<PaymentMethod> paymentMethods) {
        mContext = context;
        mData = paymentMethods;

    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_WALLET) {
            return new WalletPaymentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_payment_method, parent, false));
        } else if (viewType == TYPE_CASH) {
            return new CashPaymentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_payment_method, parent, false));
        } else {
            return new CardPaymentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_payment_method, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CARD) {
            CardPaymentHolder cardPaymentHolder = (CardPaymentHolder) holder;
            cardPaymentHolder.setUp(mData.get(position));
        } else if (getItemViewType(position) == TYPE_WALLET) {
            WalletPaymentHolder walletPaymentHolder = (WalletPaymentHolder) holder;
            walletPaymentHolder.setUp(mData.get(position));
        } else {
            CashPaymentHolder cashPaymentHolder = (CashPaymentHolder) holder;
            cashPaymentHolder.setUp(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getType() == AppConstants.PAYMENT_METHOD.WALLET) {
            return TYPE_WALLET;
        } else if (mData.get(position).getType() == AppConstants.PAYMENT_METHOD.CASH) {
            return TYPE_CASH;
        } else {
            return TYPE_CARD;
        }

    }

    class CardPaymentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_payment_method)
        ImageView paymentMethodImage;
        @BindView(R.id.tv_payment_method)
        TextView method;

        public CardPaymentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }

        public void setUp(PaymentMethod paymentMethod) {
            method.setText(paymentMethod.getMethod());
            paymentMethodImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_visa_logo));
        }
    }

    class WalletPaymentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_payment_method)
        ImageView paymentMethodImage;
        @BindView(R.id.tv_payment_method)
        TextView method;
        @BindView(R.id.tv_wallet_balance)
        TextView walletBalance;

        public WalletPaymentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setUp(PaymentMethod paymentMethod) {
            method.setText(paymentMethod.getMethod());
            walletBalance.setText(mContext.getString(R.string.price_in_naira, paymentMethod.getAmount()));
            paymentMethodImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_wallet_24));
        }


        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }
    }

    class CashPaymentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_payment_method)
        ImageView paymentMethodImage;
        @BindView(R.id.tv_payment_method)
        TextView method;

        public CashPaymentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setUp(PaymentMethod paymentMethod) {
            method.setText(paymentMethod.getMethod());
            paymentMethodImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_cash));
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }
    }

}
