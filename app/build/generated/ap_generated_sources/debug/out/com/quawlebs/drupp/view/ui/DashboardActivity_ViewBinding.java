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
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DashboardActivity_ViewBinding implements Unbinder {
  private DashboardActivity target;

  private View view7f0b02cf;

  private View view7f0b0112;

  private View view7f0b0109;

  private View view7f0b0170;

  private View view7f0b0161;

  @UiThread
  public DashboardActivity_ViewBinding(DashboardActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DashboardActivity_ViewBinding(final DashboardActivity target, View source) {
    this.target = target;

    View view;
    target.mProfile = Utils.findRequiredViewAsType(source, R.id.iv_profile_image, "field 'mProfile'", CircleImageView.class);
    target.mUserName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'mUserName'", TextView.class);
    target.mEmailId = Utils.findRequiredViewAsType(source, R.id.tv_email_id, "field 'mEmailId'", TextView.class);
    target.mWalletBalance = Utils.findRequiredViewAsType(source, R.id.tv_wallet_balance, "field 'mWalletBalance'", TextView.class);
    target.mScheduledRides = Utils.findRequiredViewAsType(source, R.id.tv_scheduled_rides, "field 'mScheduledRides'", TextView.class);
    target.totalRidesNumbers = Utils.findRequiredViewAsType(source, R.id.total_rides_numbers, "field 'totalRidesNumbers'", TextView.class);
    target.cancelledRidesCount = Utils.findRequiredViewAsType(source, R.id.cancelledRidesCount, "field 'cancelledRidesCount'", TextView.class);
    target.completedRidesNumbers = Utils.findRequiredViewAsType(source, R.id.completed_rides_numbers, "field 'completedRidesNumbers'", TextView.class);
    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'toolbarTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_back, "method 'onBackPress'");
    view7f0b02cf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
    view = Utils.findRequiredView(source, R.id.card_saved_locations, "method 'onSavedLocationClick'");
    view7f0b0112 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSavedLocationClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.cardReferral, "method 'onclickCardeferral'");
    view7f0b0109 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclickCardeferral();
      }
    });
    view = Utils.findRequiredView(source, R.id.container_wallet, "method 'onWalletClick'");
    view7f0b0170 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onWalletClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.container_scheduled_rides, "method 'onScheduledRidesClick'");
    view7f0b0161 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onScheduledRidesClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    DashboardActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mProfile = null;
    target.mUserName = null;
    target.mEmailId = null;
    target.mWalletBalance = null;
    target.mScheduledRides = null;
    target.totalRidesNumbers = null;
    target.cancelledRidesCount = null;
    target.completedRidesNumbers = null;
    target.toolbarTitle = null;

    view7f0b02cf.setOnClickListener(null);
    view7f0b02cf = null;
    view7f0b0112.setOnClickListener(null);
    view7f0b0112 = null;
    view7f0b0109.setOnClickListener(null);
    view7f0b0109 = null;
    view7f0b0170.setOnClickListener(null);
    view7f0b0170 = null;
    view7f0b0161.setOnClickListener(null);
    view7f0b0161 = null;
  }
}
