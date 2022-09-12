// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.dialog;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatRatingBar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.quawlebs.drupp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RateDriverDialog_ViewBinding implements Unbinder {
  private RateDriverDialog target;

  private View view7f0b00d2;

  @UiThread
  public RateDriverDialog_ViewBinding(final RateDriverDialog target, View source) {
    this.target = target;

    View view;
    target.ratingBar = Utils.findRequiredViewAsType(source, R.id.rating_bar, "field 'ratingBar'", AppCompatRatingBar.class);
    target.comment = Utils.findRequiredViewAsType(source, R.id.et_comment, "field 'comment'", EditText.class);
    target.switchCourteous = Utils.findRequiredViewAsType(source, R.id.switch_courteous, "field 'switchCourteous'", LabeledSwitch.class);
    target.switchCareful = Utils.findRequiredViewAsType(source, R.id.switch_careful, "field 'switchCareful'", LabeledSwitch.class);
    target.switchClean = Utils.findRequiredViewAsType(source, R.id.switch_clean, "field 'switchClean'", LabeledSwitch.class);
    target.driverProfile = Utils.findRequiredViewAsType(source, R.id.iv_driver_image, "field 'driverProfile'", CircleImageView.class);
    view = Utils.findRequiredView(source, R.id.btn_done, "method 'onRate'");
    view7f0b00d2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRate();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RateDriverDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ratingBar = null;
    target.comment = null;
    target.switchCourteous = null;
    target.switchCareful = null;
    target.switchClean = null;
    target.driverProfile = null;

    view7f0b00d2.setOnClickListener(null);
    view7f0b00d2 = null;
  }
}
