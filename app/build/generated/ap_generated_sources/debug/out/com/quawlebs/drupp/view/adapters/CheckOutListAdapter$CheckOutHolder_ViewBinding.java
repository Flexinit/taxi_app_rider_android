// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CheckOutListAdapter$CheckOutHolder_ViewBinding implements Unbinder {
  private CheckOutListAdapter.CheckOutHolder target;

  @UiThread
  public CheckOutListAdapter$CheckOutHolder_ViewBinding(CheckOutListAdapter.CheckOutHolder target,
      View source) {
    this.target = target;

    target.productName = Utils.findRequiredViewAsType(source, R.id.tv_product_name, "field 'productName'", TextView.class);
    target.productDesc = Utils.findRequiredViewAsType(source, R.id.tv_description, "field 'productDesc'", TextView.class);
    target.productPrice = Utils.findRequiredViewAsType(source, R.id.tv_product_price, "field 'productPrice'", TextView.class);
    target.productImage = Utils.findRequiredViewAsType(source, R.id.iv_preview_image, "field 'productImage'", ImageView.class);
    target.remove = Utils.findRequiredViewAsType(source, R.id.iv_delete, "field 'remove'", ImageView.class);
    target.spinnerQuantity = Utils.findRequiredViewAsType(source, R.id.spinner_quantity, "field 'spinnerQuantity'", Spinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CheckOutListAdapter.CheckOutHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productName = null;
    target.productDesc = null;
    target.productPrice = null;
    target.productImage = null;
    target.remove = null;
    target.spinnerQuantity = null;
  }
}
