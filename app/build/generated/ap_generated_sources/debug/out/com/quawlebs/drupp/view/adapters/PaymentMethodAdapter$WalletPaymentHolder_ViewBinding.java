// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentMethodAdapter$WalletPaymentHolder_ViewBinding implements Unbinder {
  private PaymentMethodAdapter.WalletPaymentHolder target;

  @UiThread
  public PaymentMethodAdapter$WalletPaymentHolder_ViewBinding(
      PaymentMethodAdapter.WalletPaymentHolder target, View source) {
    this.target = target;

    target.paymentMethodImage = Utils.findRequiredViewAsType(source, R.id.iv_payment_method, "field 'paymentMethodImage'", ImageView.class);
    target.method = Utils.findRequiredViewAsType(source, R.id.tv_payment_method, "field 'method'", TextView.class);
    target.walletBalance = Utils.findRequiredViewAsType(source, R.id.tv_wallet_balance, "field 'walletBalance'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentMethodAdapter.WalletPaymentHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.paymentMethodImage = null;
    target.method = null;
    target.walletBalance = null;
  }
}
