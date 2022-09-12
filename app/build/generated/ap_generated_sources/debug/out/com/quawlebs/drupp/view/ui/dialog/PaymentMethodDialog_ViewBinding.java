// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.dialog;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentMethodDialog_ViewBinding implements Unbinder {
  private PaymentMethodDialog target;

  private View view7f0b02d8;

  private View view7f0b00c2;

  @UiThread
  public PaymentMethodDialog_ViewBinding(final PaymentMethodDialog target, View source) {
    this.target = target;

    View view;
    target.paymentMethodsRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_payment_methods, "field 'paymentMethodsRecyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.iv_close, "method 'close'");
    view7f0b02d8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.close();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_add_payment_method, "method 'addPaymentMethod'");
    view7f0b00c2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addPaymentMethod();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentMethodDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.paymentMethodsRecyclerView = null;

    view7f0b02d8.setOnClickListener(null);
    view7f0b02d8 = null;
    view7f0b00c2.setOnClickListener(null);
    view7f0b00c2 = null;
  }
}
