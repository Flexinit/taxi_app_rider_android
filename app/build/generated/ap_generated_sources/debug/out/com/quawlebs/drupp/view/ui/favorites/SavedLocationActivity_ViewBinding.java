// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.favorites;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SavedLocationActivity_ViewBinding implements Unbinder {
  private SavedLocationActivity target;

  private View view7f0b02cf;

  @UiThread
  public SavedLocationActivity_ViewBinding(SavedLocationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SavedLocationActivity_ViewBinding(final SavedLocationActivity target, View source) {
    this.target = target;

    View view;
    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'toolbarTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_back, "method 'onBack'");
    view7f0b02cf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBack();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SavedLocationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;

    view7f0b02cf.setOnClickListener(null);
    view7f0b02cf = null;
  }
}
