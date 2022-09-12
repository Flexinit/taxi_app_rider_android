// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.ride;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RideVehicleSingle_ViewBinding implements Unbinder {
  private RideVehicleSingle target;

  private View view7f0b015a;

  @UiThread
  public RideVehicleSingle_ViewBinding(final RideVehicleSingle target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.container_payment_method, "method 'onPaymentMethodSelection'");
    view7f0b015a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPaymentMethodSelection();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0b015a.setOnClickListener(null);
    view7f0b015a = null;
  }
}
