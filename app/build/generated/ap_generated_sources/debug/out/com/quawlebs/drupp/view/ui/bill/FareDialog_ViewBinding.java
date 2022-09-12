// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.bill;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FareDialog_ViewBinding implements Unbinder {
  private FareDialog target;

  private View view7f0b00d0;

  @UiThread
  public FareDialog_ViewBinding(final FareDialog target, View source) {
    this.target = target;

    View view;
    target.baseFare = Utils.findRequiredViewAsType(source, R.id.tv_base_fare, "field 'baseFare'", TextView.class);
    target.distance = Utils.findRequiredViewAsType(source, R.id.tv_distance, "field 'distance'", TextView.class);
    target.perKmCharge = Utils.findRequiredViewAsType(source, R.id.per_km_charge, "field 'perKmCharge'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_close, "method 'onClose'");
    view7f0b00d0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClose();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    FareDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.baseFare = null;
    target.distance = null;
    target.perKmCharge = null;

    view7f0b00d0.setOnClickListener(null);
    view7f0b00d0 = null;
  }
}
