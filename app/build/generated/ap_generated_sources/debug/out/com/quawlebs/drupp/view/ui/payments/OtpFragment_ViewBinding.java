// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.payments;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OtpFragment_ViewBinding implements Unbinder {
  private OtpFragment target;

  private View view7f0b00f0;

  @UiThread
  public OtpFragment_ViewBinding(final OtpFragment target, View source) {
    this.target = target;

    View view;
    target.mReference = Utils.findRequiredViewAsType(source, R.id.tv_reference_payment, "field 'mReference'", TextView.class);
    target.mBankName = Utils.findRequiredViewAsType(source, R.id.tv_bank_name, "field 'mBankName'", TextView.class);
    target.mOtpView = Utils.findRequiredViewAsType(source, R.id.tv_otp_view, "field 'mOtpView'", TextView.class);
    target.mOTP = Utils.findRequiredViewAsType(source, R.id.et_otp_code, "field 'mOTP'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_submit, "method 'onSubmit'");
    view7f0b00f0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSubmit();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    OtpFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mReference = null;
    target.mBankName = null;
    target.mOtpView = null;
    target.mOTP = null;

    view7f0b00f0.setOnClickListener(null);
    view7f0b00f0 = null;
  }
}
