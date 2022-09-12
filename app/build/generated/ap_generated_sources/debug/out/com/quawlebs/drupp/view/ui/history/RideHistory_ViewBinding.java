// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.history;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.BaseRecyclerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RideHistory_ViewBinding implements Unbinder {
  private RideHistory target;

  @UiThread
  public RideHistory_ViewBinding(RideHistory target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RideHistory_ViewBinding(RideHistory target, View source) {
    this.target = target;

    target.emptyLayout = Utils.findRequiredViewAsType(source, R.id.trips_history_empty_view, "field 'emptyLayout'", LinearLayout.class);
    target.completedRideRecyclerView = Utils.findRequiredViewAsType(source, R.id.trips_history_recycler, "field 'completedRideRecyclerView'", BaseRecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RideHistory target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.emptyLayout = null;
    target.completedRideRecyclerView = null;
  }
}
