package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.login.LoginActivity;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;
import com.quawlebs.drupp.view.ui.dialog.RateDriverDialog;
import com.quawlebs.drupp.view.ui.RateDriverFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class EndRideActivity extends MainBaseActivity {
    private int currentRideId;
    private int driverId;
    private int currentRideType;
    private String currentDriverImage;
    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

        startRateDriverFragment();



    }

    private void startRateDriverFragment() {
        Intent intent=getIntent();
        RateDriverFragment rateDriverFragment;
        currentRideId=intent.getIntExtra(AppConstants.K_RIDE_ID,1);
        driverId=intent.getIntExtra(AppConstants.K_DRIVER_ID,1);
        currentRideType=intent.getIntExtra(AppConstants.K_RIDE_TYPE,1);
        currentDriverImage=intent.getStringExtra(AppConstants.K_DRIVER_IMAGE);
        RateDriverModel model=intent.getParcelableExtra(AppConstants.K_DRIVER_MODEL);
        if(model!=null){
         rateDriverFragment=RateDriverFragment.newInstance(currentRideId,driverId,currentRideType,currentDriverImage,model);
        }
        else{
            rateDriverFragment=RateDriverFragment.newInstance(currentRideId,driverId,currentRideType,currentDriverImage);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, rateDriverFragment, RateDriverFragment.class.getSimpleName())
                .commit();
    }


}
