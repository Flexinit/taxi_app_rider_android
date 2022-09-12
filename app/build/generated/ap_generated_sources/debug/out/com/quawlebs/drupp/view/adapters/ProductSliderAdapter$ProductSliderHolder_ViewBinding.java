// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductSliderAdapter$ProductSliderHolder_ViewBinding implements Unbinder {
  private ProductSliderAdapter.ProductSliderHolder target;

  @UiThread
  public ProductSliderAdapter$ProductSliderHolder_ViewBinding(
      ProductSliderAdapter.ProductSliderHolder target, View source) {
    this.target = target;

    target.previewImage = Utils.findRequiredViewAsType(source, R.id.iv_preview_image, "field 'previewImage'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductSliderAdapter.ProductSliderHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.previewImage = null;
  }
}
