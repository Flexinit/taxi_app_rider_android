// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReferralHistory_ViewBinding implements Unbinder {
  private ReferralHistory target;

  @UiThread
  public ReferralHistory_ViewBinding(ReferralHistory target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReferralHistory_ViewBinding(ReferralHistory target, View source) {
    this.target = target;

    target.tv_noData = Utils.findRequiredViewAsType(source, R.id.tv_noData, "field 'tv_noData'", TextView.class);
    target.lnrMainView = Utils.findRequiredViewAsType(source, R.id.lnrMainView, "field 'lnrMainView'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReferralHistory target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_noData = null;
    target.lnrMainView = null;
  }
}
