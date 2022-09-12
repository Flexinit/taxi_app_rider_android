// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserSettingActivity_ViewBinding implements Unbinder {
  private UserSettingActivity target;

  private View view7f0b015c;

  private View view7f0b016a;

  private View view7f0b02ac;

  private View view7f0b01f0;

  @UiThread
  public UserSettingActivity_ViewBinding(UserSettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UserSettingActivity_ViewBinding(final UserSettingActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.container_privacy_policy, "method 'onPressPrivacyPolicy'");
    view7f0b015c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPressPrivacyPolicy();
      }
    });
    view = Utils.findRequiredView(source, R.id.container_terms_and_condition, "method 'onPressTermsAndCondition'");
    view7f0b016a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPressTermsAndCondition();
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
    view = Utils.findRequiredView(source, R.id.emergency, "method 'onEmergencyPress'");
    view7f0b01f0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onEmergencyPress();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0b015c.setOnClickListener(null);
    view7f0b015c = null;
    view7f0b016a.setOnClickListener(null);
    view7f0b016a = null;
    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
    view7f0b01f0.setOnClickListener(null);
    view7f0b01f0 = null;
  }
}
