// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SortAdapter$SortHolder_ViewBinding implements Unbinder {
  private SortAdapter.SortHolder target;

  @UiThread
  public SortAdapter$SortHolder_ViewBinding(SortAdapter.SortHolder target, View source) {
    this.target = target;

    target.sortType = Utils.findRequiredViewAsType(source, R.id.tv_sort_type, "field 'sortType'", TextView.class);
    target.sortSelected = Utils.findRequiredViewAsType(source, R.id.rb_sort_selected, "field 'sortSelected'", RadioButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SortAdapter.SortHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.sortType = null;
    target.sortSelected = null;
  }
}
