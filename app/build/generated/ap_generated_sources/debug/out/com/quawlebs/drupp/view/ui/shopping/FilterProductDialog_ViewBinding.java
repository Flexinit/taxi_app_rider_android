// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FilterProductDialog_ViewBinding implements Unbinder {
  private FilterProductDialog target;

  private View view7f0b00c5;

  private View view7f0b02ac;

  @UiThread
  public FilterProductDialog_ViewBinding(final FilterProductDialog target, View source) {
    this.target = target;

    View view;
    target.mToolbarTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mToolbarTitle'", TextView.class);
    target.filterExpandableListView = Utils.findRequiredViewAsType(source, R.id.list_filter, "field 'filterExpandableListView'", ExpandableListView.class);
    view = Utils.findRequiredView(source, R.id.btn_apply, "method 'onApply'");
    view7f0b00c5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onApply();
      }
    });
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
    FilterProductDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbarTitle = null;
    target.filterExpandableListView = null;

    view7f0b00c5.setOnClickListener(null);
    view7f0b00c5 = null;
    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
