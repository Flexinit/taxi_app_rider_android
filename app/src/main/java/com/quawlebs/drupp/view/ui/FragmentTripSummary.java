package com.quawlebs.drupp.view.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.util.IDialogButtonClickListener;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import retrofit2.Response;

public class FragmentTripSummary extends MainBaseFragment implements IDialogButtonClickListener {

    private final String source;
    private final String destination;
    private final String fareAmount;
    private final int paymentMethod;
    TextView tvSource, tvDestination, tvFareAmount, tvPaymentMethod;

    FragmentTripSummary(String source,String destination,String fareAmount,int paymentMethod){


        this.source = source;
        this.destination = destination;
        this.fareAmount = fareAmount;
        this.paymentMethod = paymentMethod;
    }

    @Override
    protected void initViewsForFragment(View view) {
      tvSource =view.findViewById(R.id.tv_source);
      tvDestination =view.findViewById(R.id.tv_destination);
      tvFareAmount =view.findViewById(R.id.tvFareAmount);
      tvPaymentMethod =view.findViewById(R.id.tvPaymentMethod);

      tvSource.setText(source);
      tvDestination.setText(destination);
      tvFareAmount.setText(fareAmount);
        String method="";
        switch (paymentMethod) {
            case AppConstants.PAYMENT_METHOD.CARD:
                method = getString(R.string.debit_card);
                break;
            case AppConstants.PAYMENT_METHOD.WALLET:
                method = getString(R.string.wallet);
                break;
            case AppConstants.PAYMENT_METHOD.NET_BANKING:
                method = getString(R.string.net_banking);
                break;
            case AppConstants.PAYMENT_METHOD.CASH:
                method = getString(R.string.cash);
                break;
        }

        tvPaymentMethod.setText(method);


    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_trip_summary, container, false);
        return view;
    }


    @Override
    public void onButtonClick() {

    }

    @Override
    public void setUpDialogViews(View view) {

    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
