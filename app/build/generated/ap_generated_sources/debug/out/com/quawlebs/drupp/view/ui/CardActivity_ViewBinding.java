// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardActivity_ViewBinding implements Unbinder {
  private CardActivity target;

  private View view7f0b00ca;

  @UiThread
  public CardActivity_ViewBinding(CardActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CardActivity_ViewBinding(final CardActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_cancel, "method 'onCancel'");
    view7f0b00ca = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCancel();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0b00ca.setOnClickListener(null);
    view7f0b00ca = null;
  }
}
