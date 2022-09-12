// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

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

public class PrivacyPolicyActivity_ViewBinding implements Unbinder {
  private PrivacyPolicyActivity target;

  private View view7f0b02ac;

  @UiThread
  public PrivacyPolicyActivity_ViewBinding(PrivacyPolicyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PrivacyPolicyActivity_ViewBinding(final PrivacyPolicyActivity target, View source) {
    this.target = target;

    View view;
    target.mPrivacyPolicy = Utils.findRequiredViewAsType(source, R.id.tv_privacy_policy, "field 'mPrivacyPolicy'", TextView.class);
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
    PrivacyPolicyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPrivacyPolicy = null;

    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
