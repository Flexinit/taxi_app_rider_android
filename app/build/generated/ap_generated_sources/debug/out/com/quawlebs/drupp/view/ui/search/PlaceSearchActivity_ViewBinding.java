// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.search;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlaceSearchActivity_ViewBinding implements Unbinder {
  private PlaceSearchActivity target;

  @UiThread
  public PlaceSearchActivity_ViewBinding(PlaceSearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlaceSearchActivity_ViewBinding(PlaceSearchActivity target, View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlaceSearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
  }
}
