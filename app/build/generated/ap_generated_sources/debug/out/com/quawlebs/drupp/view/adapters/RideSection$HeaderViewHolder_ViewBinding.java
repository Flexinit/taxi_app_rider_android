// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RideSection$HeaderViewHolder_ViewBinding implements Unbinder {
  private RideSection.HeaderViewHolder target;

  @UiThread
  public RideSection$HeaderViewHolder_ViewBinding(RideSection.HeaderViewHolder target,
      View source) {
    this.target = target;

    target.header = Utils.findRequiredViewAsType(source, R.id.tv_scheduled_ride_header, "field 'header'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RideSection.HeaderViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.header = null;
  }
}
