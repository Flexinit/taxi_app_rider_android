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

public class TripHistorySection$TripViewHolder_ViewBinding implements Unbinder {
  private TripHistorySection.TripViewHolder target;

  @UiThread
  public TripHistorySection$TripViewHolder_ViewBinding(TripHistorySection.TripViewHolder target,
      View source) {
    this.target = target;

    target.mSource = Utils.findRequiredViewAsType(source, R.id.tv_source, "field 'mSource'", TextView.class);
    target.mDestination = Utils.findRequiredViewAsType(source, R.id.tv_destination, "field 'mDestination'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TripHistorySection.TripViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mSource = null;
    target.mDestination = null;
  }
}
