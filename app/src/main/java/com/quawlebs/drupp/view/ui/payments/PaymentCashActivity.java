package com.quawlebs.drupp.view.ui.payments;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

public class PaymentCashActivity extends MainBaseActivity {
    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_cash_layout);
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
    }

}
