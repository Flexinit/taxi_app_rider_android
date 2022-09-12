package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.login.LoginActivity;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.IDialogButtonClickListener;
import com.quawlebs.drupp.util.Token;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.dialog.CompletedDialog;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class UserRegistrationActivity extends MainBaseActivity implements IDialogButtonClickListener {

    //-------------------Views-------------------------------
    private EditText mFirstName, mLastName, mNumber, mCity, mEmail, mPassword,mConfirmPassword,edtCoupon;
    private String number, country_code, token;
    private LinearLayout mProfilePic;
    private String imageUri = "";
    private CircleImageView profileImage;
    private String profileImageUrl = "";
    private CheckBox mTermsCondition;
    private LoginDataModel userModel;
    private Button btnLogin;
    private TextView termsAndCondition;
    private RideInfo currentRideInfo;
    private int isDriverPosted;

    //-------------------Globals-----------------------------
    private UserInfo userInfo;

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_);

        btnLogin = findViewById(R.id.btn_login);
        mFirstName = findViewById(R.id.et_first_namne);
        mLastName = findViewById(R.id.et_last_namne);
        mNumber = findViewById(R.id.et_phone);
        mCity = findViewById(R.id.etCity);
        mEmail = findViewById(R.id.etEmail);
        mProfilePic = findViewById(R.id.ll_profile_container);
        profileImage = findViewById(R.id.iv_profile_image);
        mPassword = findViewById(R.id.etPassword);
        mConfirmPassword=findViewById(R.id.etConfirmPassword);
        edtCoupon = findViewById(R.id.edtCoupon);
        termsAndCondition = findViewById(R.id.tv_terms_and_condition);
        mTermsCondition = findViewById(R.id.cb_terms_conditions);

        if (getIntent() != null) {
            userModel = ((LoginDataModel) getIntent().getSerializableExtra(AppConstants.K_USER_INFO));
        }


        if (userModel != null) {
            userInfo = userModel.getUserInfo();

            if (!userModel.getToken().isEmpty()) {
                //Move to Ride Activity if has token
                startActivity(new Intent(UserRegistrationActivity.this, BottomSheetRideActivity.class));
            }

        }
        try {
            JSONObject jsonObject = new JSONObject(Helper.getRideParams(this));
            isDriverPosted = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }


        termsAndCondition.setMovementMethod(LinkMovementMethod.getInstance());

        if (userInfo != null) {
            number = userInfo.getPhone();
            country_code = userInfo.getCountryCode().toString();
            token = userModel.getToken();
        }


        mNumber.setText(getString(R.string.complete_phone_number, country_code, number));


        findViewById(R.id.btSignUp).setOnClickListener(v -> {
            if (!mTermsCondition.isChecked()) {
                showMessage(R.string.please_check_terms_and_condition);
                return;
            }
            if(mPassword.getText().toString().isEmpty()){
                showMessage(R.string.password_cannot_be_empty);
                return;
            }
            if(mConfirmPassword.getText().toString().isEmpty()){
                showMessage(R.string.confirm_password_cannot_be_empty);
                return;
            }
            if(!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
                showMessage(R.string.passwords_must_match);
                return;
            }
            userRegistration();
        });

        //  findViewById(R.id.tvHaveAccount).setOnClickListener(v -> startActivity(new Intent(UserRegistrationActivity.this, MainActivity.class)));
        findViewById(R.id.ll_profile_container).setOnClickListener(v -> PickImageDialog.build(new PickSetup()).setOnPickResult(this::setImageForCrop).show(this));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      //          UIHelper.getInstance().switchActivity(this, SplashActivity.class, null, null, false);
                Intent intent=new Intent(UserRegistrationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("isDriverPosted",isDriverPosted);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri().getPath();
                uploadProfilePic(result.getUri().getPath());
            }
        }
    }

    private void setImageForCrop(PickResult r) {
        Intent intent = CropImage.activity(r.getUri()).getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void userRegistration() {
        showLoading();
        DruppRequestHandler.clearInstance();
        HashMap<String, String> parms = new HashMap<>();
        parms.put(AppConstants.K_FIREBASE_TOKEN, SessionManager.getInstance().loadFireBaseToken(this).getFirebaseToken());
        parms.put(AppConstants.K_PLATFORM, "1");
        parms.put(AppConstants.K_FIRST_NAME, mFirstName.getText().toString().trim());
        parms.put(AppConstants.K_LAST_NAME, mLastName.getText().toString().trim());
        parms.put(AppConstants.K_EMAIL, mEmail.getText().toString().trim());
        parms.put(AppConstants.K_PASSWORD, mPassword.getText().toString().trim());
        parms.put(AppConstants.K_COUPONCODE, edtCoupon.getText().toString().trim());
        parms.put(AppConstants.K_PHONE, number);
        parms.put(AppConstants.K_COUNTRY_CODE, country_code);
        parms.put(AppConstants.K_PROFILE_PICTURE, profileImageUrl);
        parms.put(AppConstants.K_LATITUDE, "22.68610");
        parms.put(AppConstants.K_LONGITUDE, "75.86016");
        parms.put(AppConstants.K_CITY, mCity.getText().toString().trim());
        DruppRequestHandler.getInstance(new INetwork<Token>() {
            @Override
            public void onResponse(Response<QualStandardResponse<Token>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    LoginDataModel loginDataModel = SessionManager.getInstance().loadUser(UserRegistrationActivity.this);
                    loginDataModel.setToken(response.body().getResponse().getToken());
                    loginDataModel.getUserInfo().setId(response.body().getResponse().getUserId());
                    loginDataModel.getUserInfo().setFirstName(mFirstName.getText().toString().trim());
                    loginDataModel.getUserInfo().setLastName(mLastName.getText().toString().trim());
                    loginDataModel.getUserInfo().setEmail(mEmail.getText().toString().trim());
                    loginDataModel.getUserInfo().setProfilePicture(profileImageUrl);
                    loginDataModel.getUserInfo().setCity(mCity.getText().toString().trim());

                    SessionManager.getInstance().saveUser(UserRegistrationActivity.this, loginDataModel);
                    showSignUpCompleteDialog();

                    //UIHelper.getInstance().switchActivity(UserRegistrationActivity.this, BottomSheetRideActivity.class, null, null, null, true);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<Token>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        userRegistration();
                    });
                }
            }
        }).userSignin(parms);
    }

    private void showSignUpCompleteDialog() {
        CompletedDialog completedDialog=new CompletedDialog(this,R.layout.custom_dialog_complete);
        completedDialog.show(getSupportFragmentManager(),CompletedDialog.class.getSimpleName());

    }

    private void uploadProfilePic(String filePath) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    profileImageUrl = response.body().getResponse().get(AppConstants.K_FILE_PATH);
                    Helper.saveProfileUrl(profileImageUrl, UserRegistrationActivity.this);
                    Glide.with(UserRegistrationActivity.this).load(imageUri).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(profileImage);

                    showMessage(R.string.image_uploaded_successfully);
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
                showMessage(R.string.something_went_wrong);
            }
        }, SessionManager.getAccessToken()).uploadProfilePic(filePath, AppConstants.IMAGE_UPLOAD);
    }

    @Override
    public void onButtonClick() {
        Intent intent=new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("isDriverPosted",isDriverPosted);
        startActivity(intent);
    }
    @Override
    public void setUpDialogViews(View view) {

    }
}
