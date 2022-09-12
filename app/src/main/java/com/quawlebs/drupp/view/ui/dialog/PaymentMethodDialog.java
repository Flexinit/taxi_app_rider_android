package com.quawlebs.drupp.view.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.Authorization;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.WalletModel;
import com.quawlebs.drupp.util.CommonUtils;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.PaymentMethodAdapter;
import com.quawlebs.drupp.view.ui.CardActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class PaymentMethodDialog extends BottomSheetDialogFragment implements IAdapterItemClickListener {

    @BindView(R.id.rv_payment_methods)
    RecyclerView paymentMethodsRecyclerView;
    private PaymentMethodAdapter paymentMethodAdapter;
    private ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
    private AlertDialog dialogProgress;
    private int currentRideId;
    private int postedByDriver;
    private int currentSelectionType;

    public static PaymentMethodDialog newInstance(Integer rideId, Integer postedByDriver, int selectionType) {

        Bundle args = new Bundle();
        args.putInt(AppConstants.K_TYPE, selectionType);
        args.putInt(AppConstants.K_RIDE_ID, rideId);
        args.putInt(AppConstants.K_POSTED_BY_DRIVER, postedByDriver);

        PaymentMethodDialog fragment = new PaymentMethodDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_method_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            currentSelectionType = getArguments().getInt(AppConstants.K_TYPE);
            currentRideId = getArguments().getInt(AppConstants.K_RIDE_ID);
            postedByDriver = getArguments().getInt(AppConstants.K_POSTED_BY_DRIVER);
        }
        paymentMethods.clear();
        paymentMethods.addAll(Helper.getPaymentMethods(getContext()));
        paymentMethodsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        paymentMethodAdapter = new PaymentMethodAdapter(getActivity(), paymentMethods);
        paymentMethodAdapter.setiAdapterItemClickListener(this);
        paymentMethodsRecyclerView.setAdapter(paymentMethodAdapter);

        dialogProgress = CommonUtils.showDialogProgressBar(getActivity());
        getSavedCards();
        getWalletMoney();

    }

    @OnClick(R.id.iv_close)
    public void close() {
        dismiss();
    }

    @Override
    public void onAdapterItemClick(View v, int position) {
        if (currentSelectionType == 1) {
            changePaymentOption(paymentMethods.get(position));
        } else {
            dismiss();
            Helper.savePaymentMethod(paymentMethods.get(position), getContext());
            RxBus.getInstance().setIntentEvent(paymentMethods.get(position));
        }


    }

    @OnClick(R.id.btn_add_payment_method)
    public void addPaymentMethod() {
        UIHelper.getInstance().switchActivity(getActivity(), CardActivity.class, null, null, null, false);
    }

    @Override
    public void onAdapterItemClick(Place place) {

    }


    private void getSavedCards() {
        dialogProgress.show();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Authorization>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Authorization>> response) {
                dialogProgress.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        for (Authorization authorization : response.body().getResponse()) {
                            if (authorization.getCardType().contains(AppConstants.CARD_TYPE.VISA)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.visa));
                            } else if (authorization.getCardType().contains(AppConstants.CARD_TYPE.AMERICAN_EXPRESS)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.american_express));

                            } else if (authorization.getCardType().contains(AppConstants.CARD_TYPE.DISCOVER)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.discover));
                            } else if (authorization.getCardType().contains(AppConstants.CARD_TYPE.MASTER_CARD)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.mastercard));
                            }
                            PaymentMethod paymentMethod = new PaymentMethod(authorization.getId(), getString(R.string.card_number_saved, authorization.getLastFourDigit()), AppConstants.PAYMENT_METHOD.CARD);
                            paymentMethod.setImage(authorization.getCardImage());
                            paymentMethods.add(paymentMethod);
                        }
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "Error");
                    }
                    paymentMethodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Authorization>> response) {
                dialogProgress.dismiss();
                // hideLoading();
                //   showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                dialogProgress.dismiss();
                //hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                //hideLoading();
                dialogProgress.dismiss();
            }
        }, SessionManager.getAccessToken()).getSavedCards();
    }


    private void changePaymentOption(PaymentMethod option) {
        dialogProgress.show();
        HashMap<String, Object> params = new HashMap<>();
        params.put(AppConstants.K_RIDE_ID, currentRideId);
        params.put(AppConstants.K_POSTED_BY_DRIVER, postedByDriver);
        params.put(AppConstants.K_PAYMENT_OPTION, option.getType());
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Authorization>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Authorization>> response) {
                dialogProgress.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        dismiss();
                        Helper.savePaymentMethod(option, getContext());
                        RxBus.getInstance().setIntentEvent(option);
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "Error");
                    }
                    paymentMethodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Authorization>> response) {
                dialogProgress.dismiss();
                // hideLoading();
                //   showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                dialogProgress.dismiss();
                //hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                //hideLoading();
                dialogProgress.dismiss();
            }
        }, SessionManager.getAccessToken()).changePaymentOption(params);
    }


    private void getWalletMoney() {
        try {
            DruppRequestHandler.getInstance(new INetwork<WalletModel>() {
                @Override
                public void onResponse(Response<QualStandardResponse<WalletModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        paymentMethods.get(0).setAmount(response.body().getResponse().getBalance());

                        paymentMethodAdapter.notifyDataSetChanged();
                        paymentMethodAdapter.notifyItemChanged(0);
                    }

                }

                @Override
                public void onError(Response<QualStandardResponse<WalletModel>> response) {


                }


                @Override
                public void onNullResponse() {

                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            }, SessionManager.getAccessToken()).getWalletMoney();
        } catch (Exception e) {

        }

    }
}
