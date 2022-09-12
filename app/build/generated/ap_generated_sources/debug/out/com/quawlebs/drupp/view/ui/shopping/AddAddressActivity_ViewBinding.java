// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddAddressActivity_ViewBinding implements Unbinder {
  private AddAddressActivity target;

  private View view7f0b00e7;

  private View view7f0b02ac;

  @UiThread
  public AddAddressActivity_ViewBinding(AddAddressActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddAddressActivity_ViewBinding(final AddAddressActivity target, View source) {
    this.target = target;

    View view;
    target.mFirstName = Utils.findRequiredViewAsType(source, R.id.et_first_name, "field 'mFirstName'", EditText.class);
    target.mLastName = Utils.findRequiredViewAsType(source, R.id.et_last_namne, "field 'mLastName'", EditText.class);
    target.mStreet = Utils.findRequiredViewAsType(source, R.id.et_street, "field 'mStreet'", EditText.class);
    target.mCity = Utils.findRequiredViewAsType(source, R.id.et_city, "field 'mCity'", EditText.class);
    target.mCountry = Utils.findRequiredViewAsType(source, R.id.et_country, "field 'mCountry'", EditText.class);
    target.mState = Utils.findRequiredViewAsType(source, R.id.et_state, "field 'mState'", EditText.class);
    target.mZip = Utils.findRequiredViewAsType(source, R.id.et_zip, "field 'mZip'", EditText.class);
    target.mPhone = Utils.findRequiredViewAsType(source, R.id.et_phone, "field 'mPhone'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_save, "field 'mBtnContinue' and method 'saveAddress'");
    target.mBtnContinue = Utils.castView(view, R.id.btn_save, "field 'mBtnContinue'", Button.class);
    view7f0b00e7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.saveAddress();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBackPress'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddAddressActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mFirstName = null;
    target.mLastName = null;
    target.mStreet = null;
    target.mCity = null;
    target.mCountry = null;
    target.mState = null;
    target.mZip = null;
    target.mPhone = null;
    target.mBtnContinue = null;

    view7f0b00e7.setOnClickListener(null);
    view7f0b00e7 = null;
    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
