// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.scheduledRides;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserPostedRidesEdit_ViewBinding implements Unbinder {
  private UserPostedRidesEdit target;

  private View view7f0b02ac;

  @UiThread
  public UserPostedRidesEdit_ViewBinding(UserPostedRidesEdit target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UserPostedRidesEdit_ViewBinding(final UserPostedRidesEdit target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBack'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBack();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
