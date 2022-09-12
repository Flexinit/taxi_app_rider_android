// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import com.willy.ratingbar.ScaleRatingBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RateDriverFragment_ViewBinding implements Unbinder {
  private RateDriverFragment target;

  @UiThread
  public RateDriverFragment_ViewBinding(RateDriverFragment target, View source) {
    this.target = target;

    target.ratingBar = Utils.findRequiredViewAsType(source, R.id.rating_bar, "field 'ratingBar'", ScaleRatingBar.class);
    target.comment = Utils.findRequiredViewAsType(source, R.id.et_comment, "field 'comment'", EditText.class);
    target.rgDriverCareful = Utils.findRequiredViewAsType(source, R.id.rg_driver_careful, "field 'rgDriverCareful'", RadioGroup.class);
    target.rgVehicleNeat = Utils.findRequiredViewAsType(source, R.id.rg_vehicle_neat, "field 'rgVehicleNeat'", RadioGroup.class);
    target.rgDriverPunctual = Utils.findRequiredViewAsType(source, R.id.rg_driver_punctual, "field 'rgDriverPunctual'", RadioGroup.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RateDriverFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ratingBar = null;
    target.comment = null;
    target.rgDriverCareful = null;
    target.rgVehicleNeat = null;
    target.rgDriverPunctual = null;
  }
}
