// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShoppingHomeActivity_ViewBinding implements Unbinder {
  private ShoppingHomeActivity target;

  @UiThread
  public ShoppingHomeActivity_ViewBinding(ShoppingHomeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShoppingHomeActivity_ViewBinding(ShoppingHomeActivity target, View source) {
    this.target = target;

    target.btnNoThanks = Utils.findRequiredViewAsType(source, R.id.btn_no_thanks, "field 'btnNoThanks'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShoppingHomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnNoThanks = null;
  }
}
