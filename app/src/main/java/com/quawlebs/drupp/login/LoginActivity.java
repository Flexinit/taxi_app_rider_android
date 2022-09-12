package com.quawlebs.drupp.login;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.util.IDialogButtonClickListener;
import com.quawlebs.drupp.view.ui.MainActivity;
import com.quawlebs.drupp.view.ui.dialog.CompletedDialog;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {



    // not in use
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        Toast.makeText(LoginActivity.this,"Log In Please",Toast.LENGTH_LONG).show();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sign_container,new SignInFragment(), SignInFragment.class.getSimpleName())
                .commit();

    }

    public static class SignInFragment extends Fragment {
        private Button btnSignIn;
        private TextView tvForgotPassword,tvSignUp;

        protected void setUpViews() {
            SpannableString spannableString=new SpannableString("New User? Sign Up Here");
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorLightBlack)),10,spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(),10,spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvSignUp.setText(spannableString);
            btnSignIn.setOnClickListener(v -> {
                Intent intent=new Intent(getActivity(),StartUpActivity.class);
                if(getActivity().getIntent().hasExtra("currentRideInfo")){
                    RideInfo currentRideInfo=getActivity().getIntent().getExtras().getParcelable("currentRideInfo");
                    intent.putExtra("currentRideInfo",currentRideInfo);
                }
                if(getActivity().getIntent().hasExtra("isDriverPosted")){
                    int isDriverPosted=getActivity().getIntent().getExtras().getInt("isDriverPosted");
                    intent.putExtra("isDriverPosted",isDriverPosted);
                }
               //if (tvForgotPassword.getText().toString().trim().length()>0) {
                   startActivity(intent);
              // }

            });
            tvForgotPassword.setOnClickListener(v -> {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(SignInFragment.class.getSimpleName(),0);
                fragmentManager .beginTransaction()
                        .replace(R.id.sign_container,new ForgotPasswordFragment(),ForgotPasswordFragment.class.getSimpleName())
                        .addToBackStack(SignInFragment.class.getSimpleName())
                        .commit();
            });
            tvSignUp.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), StartUpActivity.class));
            });

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
            ButterKnife.bind(this, view);
            btnSignIn = view.findViewById(R.id.button_sign_in);

            tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
            tvSignUp = view.findViewById(R.id.tv_sign_up);
            setUpViews();

            return view;
        }
    }


    public static class ForgotPasswordFragment extends Fragment implements IDialogButtonClickListener {
        private Button btnRecoverPassword;
        private EditText etEmail;




        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
            ButterKnife.bind(this, view);
            btnRecoverPassword = view.findViewById(R.id.button_recover_password);
            etEmail=view.findViewById(R.id.etEmail);
            setUPViews();
            return view;
        }

        private void setUPViews() {
            btnRecoverPassword.setOnClickListener(v -> {
                if(etEmail.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(),"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                CompletedDialog completedDialog=new CompletedDialog(this,R.layout.custom_dialog_password_reset);
                completedDialog.show(getActivity().getSupportFragmentManager(),CompletedDialog.class.getSimpleName());

            });

        }


        @Override
        public void onButtonClick() {

        }
        @Override
        public void setUpDialogViews(View view) {
                TextView textView=view.findViewById(R.id.tvEmail);
                textView.setText(etEmail.getText().toString());
        }

    }

}


