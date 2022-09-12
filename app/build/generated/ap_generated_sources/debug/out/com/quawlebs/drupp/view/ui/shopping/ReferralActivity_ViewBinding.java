// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReferralActivity_ViewBinding implements Unbinder {
  private ReferralActivity target;

  private View view7f0b034e;

  @UiThread
  public ReferralActivity_ViewBinding(ReferralActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReferralActivity_ViewBinding(final ReferralActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.lnrshare, "method 'onClickShare'");
    view7f0b034e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickShare();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0b034e.setOnClickListener(null);
    view7f0b034e = null;
  }
}
