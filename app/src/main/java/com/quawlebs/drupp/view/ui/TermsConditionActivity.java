package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class TermsConditionActivity extends MainBaseActivity {

    @BindView(R.id.pdfViewer)
    PDFView pdfView;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        ButterKnife.bind(this);
        loadTermsAndConditions();
        //getTermsAndCondition();
    }

    private void loadTermsAndConditions() {

        pdfView.setSwipeVertical(true);

        pdfView.fromAsset("Drupp Privacy Policy and Terms.pdf")
                .password(null)
                .defaultPage(0)
                .enableDoubletap(true)
                .enableSwipe(true)
                .onPageError((page, t) -> {
                    Toast.makeText(TermsConditionActivity.this,"Error loading page",Toast.LENGTH_SHORT).show();
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .onError(t -> {
                    Toast.makeText(TermsConditionActivity.this,"Error",Toast.LENGTH_SHORT).show();
                })
                .load();





    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
    }

    private void getTermsAndCondition() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        //mTermsAndCondition.setText(response.body().getResponse().get(AppConstants.K_TERMS_AND_CONDITION));
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getTermsAndCondition();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getTermsAndCondition();
    }
}
