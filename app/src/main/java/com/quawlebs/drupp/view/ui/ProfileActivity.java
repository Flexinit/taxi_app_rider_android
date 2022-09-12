package com.quawlebs.drupp.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.models.MessageEvent;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.IProfileChange;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.BaseActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private CircleImageView profileImage;
    private String imageUri = "";
    private String profileImageUrl = "";
    private Button mEditProfile;
    private boolean isEditProfile = false;
    private IProfileChange iProfileChange;
    private UserInfo userInfo;


    private EditText mMobile, mEmail, mFirstName, mLastName, mCity;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setToolbarTitle(R.string.profile);
        getToolbarBack().setOnClickListener(v -> onBackPressed());
        getSwipeRefreshLayout().setEnabled(false);

        mCity = findViewById(R.id.et_city);
        mEmail = findViewById(R.id.et_email);
        profileImage = findViewById(R.id.profile_image);
        mFirstName = findViewById(R.id.et_first_name);
        mLastName = findViewById(R.id.et_last_name);
        mMobile = findViewById(R.id.et_mobile_number);
        mEditProfile = findViewById(R.id.btn_edit_profile);


        userInfo = SessionManager.getInstance().loadUser(this).getUserInfo();
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(ProfileActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        profileImage.setOnClickListener(v -> {
            if (isEditProfile) {
//                PickImageDialog.build(new PickSetup())
//                        .setOnPickResult(this::setImageForCrop).show(ProfileActivity.this);

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        });
        mEditProfile.setOnClickListener(v -> {
            isEditProfile = !isEditProfile;

            profileImage.setClickable(isEditProfile);

            disableEditText(mFirstName, isEditProfile);

            disableEditText(mLastName, isEditProfile);

            disableEditText(mEmail, isEditProfile);

            disableEditText(mCity, isEditProfile);

            mEditProfile.setText(isEditProfile ? getString(R.string.done) : getString(R.string.edit_profile));
            if (!isEditProfile && isValidate()) {
                editProfile();
            }
        });
        getProfile();

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void disableEditText(EditText editText, boolean isActive) {
        editText.setFocusable(isActive);
        editText.setFocusableInTouchMode(isActive);
        editText.setEnabled(isActive);
        editText.setCursorVisible(isActive);
//        if (isActive) {
//            requestFocus(editText);
//        }

//        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private boolean isValidate() {

        if (mFirstName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_first_name);
            mFirstName.requestFocus();
            return false;
        } else if (mLastName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_last_name);
            mLastName.requestFocus();
            return false;
        } else if (mMobile.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_mobile_no);
            mMobile.requestFocus();
            return false;
        } else if (mCity.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_city);
            mCity.requestFocus();
            return false;
        }
        return true;
    }

    private void setImageForCrop(PickResult r) {
        Intent intent = CropImage.activity(r.getUri()).getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void getProfile() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<UserInfo>() {
            @Override
            public void onResponse(Response<QualStandardResponse<UserInfo>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getProfilePicture()).apply(new RequestOptions()
                                .error(R.drawable.user_profile_icon)
                                .centerCrop().placeholder(R.drawable.user_profile_icon)).into(profileImage);
                        mFirstName.setText(response.body().getResponse().getFirstName());
                        mLastName.setText(response.body().getResponse().getLastName());
                        mMobile.setText(getString(R.string.complete_phone_number, userInfo.getCountryCode().toString(), userInfo.getPhone()));
                        mCity.setText(response.body().getResponse().getCity());
                        mEmail.setText(response.body().getResponse().getEmail());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<UserInfo>> response) {
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
                        getProfile();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getRiderProfile();
    }

    private void editProfile() {
        showLoading();
        HashMap<String, String> params = new HashMap<>();

        params.put(AppConstants.K_FIRST_NAME, mFirstName.getText().toString().trim());
        params.put(AppConstants.K_LAST_NAME, mLastName.getText().toString().trim());
        params.put(AppConstants.K_PHONE, userInfo.getPhone());
        params.put(AppConstants.K_CITY, mCity.getText().toString().trim());
        params.put(AppConstants.K_PROFILE_PIC, profileImageUrl.isEmpty() ? Helper.getProfileUrl(this) : profileImageUrl);


        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<HashMap<String, String>>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    showMessage(R.string.profile_updated_successfully);
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<HashMap<String, String>>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        editProfile();
                    });
                }
            }
        }, SessionManager.getAccessToken()).editRiderProfile(params);
    }

    private void uploadProfilePic(String filePath) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    profileImageUrl = response.body().getResponse().get(AppConstants.K_FILE_PATH);

                    Helper.saveProfileUrl(profileImageUrl, ProfileActivity.this);
                    Glide.with(ProfileActivity.this).load(imageUri).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(profileImage);

                    RxBus.getInstance().setIntentEvent(new MessageEvent(profileImageUrl));
                    showMessage(R.string.image_uploaded_successfully);
                    editProfile();
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        uploadProfilePic(filePath);
                    });
                }
            }
        }, SessionManager.getAccessToken()).uploadProfilePic(filePath, AppConstants.IMAGE_UPLOAD);
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
}
