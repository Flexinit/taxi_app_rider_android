package com.quawlebs.drupp.view.ui.payments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IActivityInteractor;
import com.quawlebs.drupp.helpers.IPaymentFlowObserver;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.HashMap;

import butterknife.ButterKnife;

public class PendingFragment extends MainBaseFragment implements IPaymentFlowObserver {

    private IActivityInteractor iActivityInteractor;
    private String bankName = "";

    @Override
    protected void initViewsForFragment(View view) {
        iActivityInteractor = (BankPaymentActivity) getmActivity();
        if (getArguments() != null) {
            bankName = getArguments().getString(AppConstants.K_BANK_NAME);
        }
        if (iActivityInteractor != null) {
            iActivityInteractor.doNetworkRequest();
        }
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_pending_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResponse(HashMap<String, String> response) {
        if (response.get(AppConstants.K_STATUS).equals(AppConstants.BANK_STATUS.SEND_OTP)) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.K_BANK_NAME, bankName);
            bundle.putString(AppConstants.K_REFERENCE, response.get(AppConstants.K_REFERENCE));
            bundle.putString(AppConstants.K_DISPLAY_TEXT, response.get(AppConstants.K_DISPLAY_TEXT));
            UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_payment_flow, new OtpFragment(), OtpFragment.class.getSimpleName(), bundle, false);
        }
    }


    @Override
    public void showAlertDialog(int resId) {

    }
}
