// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SavedCardListAdapter$SavedCardHolder_ViewBinding implements Unbinder {
  private SavedCardListAdapter.SavedCardHolder target;

  @UiThread
  public SavedCardListAdapter$SavedCardHolder_ViewBinding(
      SavedCardListAdapter.SavedCardHolder target, View source) {
    this.target = target;

    target.cardImage = Utils.findRequiredViewAsType(source, R.id.iv_card_image, "field 'cardImage'", ImageView.class);
    target.cardBrand = Utils.findRequiredViewAsType(source, R.id.tv_card_brand, "field 'cardBrand'", TextView.class);
    target.cardNumber = Utils.findRequiredViewAsType(source, R.id.tv_card_number, "field 'cardNumber'", TextView.class);
    target.cardSelected = Utils.findRequiredViewAsType(source, R.id.rb_select_card, "field 'cardSelected'", RadioButton.class);
    target.payButton = Utils.findRequiredViewAsType(source, R.id.btn_pay, "field 'payButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SavedCardListAdapter.SavedCardHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cardImage = null;
    target.cardBrand = null;
    target.cardNumber = null;
    target.cardSelected = null;
    target.payButton = null;
  }
}
