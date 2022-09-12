// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SortDialogFragment_ViewBinding implements Unbinder {
  private SortDialogFragment target;

  @UiThread
  public SortDialogFragment_ViewBinding(SortDialogFragment target, View source) {
    this.target = target;

    target.sortRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_sort_filter, "field 'sortRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SortDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.sortRecyclerView = null;
  }
}
