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

public class ShippingAddressListAdapter$ShippingAddressHolder_ViewBinding implements Unbinder {
  private ShippingAddressListAdapter.ShippingAddressHolder target;

  @UiThread
  public ShippingAddressListAdapter$ShippingAddressHolder_ViewBinding(
      ShippingAddressListAdapter.ShippingAddressHolder target, View source) {
    this.target = target;

    target.addressSelection = Utils.findRequiredViewAsType(source, R.id.rb_address, "field 'addressSelection'", RadioButton.class);
    target.completeAddress = Utils.findRequiredViewAsType(source, R.id.tv_complete_address, "field 'completeAddress'", TextView.class);
    target.completeName = Utils.findRequiredViewAsType(source, R.id.tv_complete_name, "field 'completeName'", TextView.class);
    target.phone = Utils.findRequiredViewAsType(source, R.id.tv_phone, "field 'phone'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShippingAddressListAdapter.ShippingAddressHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.addressSelection = null;
    target.completeAddress = null;
    target.completeName = null;
    target.phone = null;
  }
}
