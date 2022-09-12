// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PostRideActivity_ViewBinding implements Unbinder {
  private PostRideActivity target;

  private View view7f0b0166;

  private View view7f0b015b;

  private View view7f0b044a;

  private View view7f0b0353;

  @UiThread
  public PostRideActivity_ViewBinding(PostRideActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PostRideActivity_ViewBinding(final PostRideActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.container_single_ride, "field 'containerSingleRide' and method 'onSingleRide'");
    target.containerSingleRide = Utils.castView(view, R.id.container_single_ride, "field 'containerSingleRide'", LinearLayout.class);
    view7f0b0166 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSingleRide();
      }
    });
    view = Utils.findRequiredView(source, R.id.container_pool_ride, "field 'containerPoolRide' and method 'onPoolRide'");
    target.containerPoolRide = Utils.castView(view, R.id.container_pool_ride, "field 'containerPoolRide'", LinearLayout.class);
    view7f0b015b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPoolRide();
      }
    });
    view = Utils.findRequiredView(source, R.id.regular_ride, "field 'regularRide' and method 'regularRide'");
    target.regularRide = Utils.castView(view, R.id.regular_ride, "field 'regularRide'", LinearLayout.class);
    view7f0b044a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.regularRide();
      }
    });
    view = Utils.findRequiredView(source, R.id.luxury_ride, "field 'luxuryRide' and method 'luxuryRide'");
    target.luxuryRide = Utils.castView(view, R.id.luxury_ride, "field 'luxuryRide'", LinearLayout.class);
    view7f0b0353 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.luxuryRide();
      }
    });
    target.etSource = Utils.findRequiredViewAsType(source, R.id.et_source, "field 'etSource'", EditText.class);
    target.etDestination = Utils.findRequiredViewAsType(source, R.id.et_destination, "field 'etDestination'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PostRideActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.containerSingleRide = null;
    target.containerPoolRide = null;
    target.regularRide = null;
    target.luxuryRide = null;
    target.etSource = null;
    target.etDestination = null;

    view7f0b0166.setOnClickListener(null);
    view7f0b0166 = null;
    view7f0b015b.setOnClickListener(null);
    view7f0b015b = null;
    view7f0b044a.setOnClickListener(null);
    view7f0b044a = null;
    view7f0b0353.setOnClickListener(null);
    view7f0b0353 = null;
  }
}
