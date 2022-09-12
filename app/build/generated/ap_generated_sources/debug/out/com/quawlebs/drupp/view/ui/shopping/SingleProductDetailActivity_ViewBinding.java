// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import com.smarteist.autoimageslider.SliderView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SingleProductDetailActivity_ViewBinding implements Unbinder {
  private SingleProductDetailActivity target;

  private View view7f0b00c3;

  private View view7f0b02ac;

  private View view7f0b02d5;

  private View view7f0b021e;

  private View view7f0b00c8;

  @UiThread
  public SingleProductDetailActivity_ViewBinding(SingleProductDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SingleProductDetailActivity_ViewBinding(final SingleProductDetailActivity target,
      View source) {
    this.target = target;

    View view;
    target.mDescription = Utils.findRequiredViewAsType(source, R.id.tv_description, "field 'mDescription'", TextView.class);
    target.mBrandName = Utils.findRequiredViewAsType(source, R.id.tv_brand_name, "field 'mBrandName'", TextView.class);
    target.mProductName = Utils.findRequiredViewAsType(source, R.id.tv_product_name, "field 'mProductName'", TextView.class);
    target.mProductPrice = Utils.findRequiredViewAsType(source, R.id.tv_product_price, "field 'mProductPrice'", TextView.class);
    target.mSlider = Utils.findRequiredViewAsType(source, R.id.imageSlider, "field 'mSlider'", SliderView.class);
    view = Utils.findRequiredView(source, R.id.btn_add_to_cart, "field 'mBtnAddToCart' and method 'addToCart'");
    target.mBtnAddToCart = Utils.castView(view, R.id.btn_add_to_cart, "field 'mBtnAddToCart'", Button.class);
    view7f0b00c3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addToCart();
      }
    });
    target.bottomContainer = Utils.findRequiredViewAsType(source, R.id.bottom_container, "field 'bottomContainer'", LinearLayout.class);
    target.mBtnOutOfStock = Utils.findRequiredViewAsType(source, R.id.btn_out_of_stock, "field 'mBtnOutOfStock'", Button.class);
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBackPress'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_cart, "method 'goToCart'");
    view7f0b02d5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goToCart();
      }
    });
    view = Utils.findRequiredView(source, R.id.et_search_product, "method 'onSearch' and method 'onSearch'");
    view7f0b021e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSearch();
      }
    });
    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View p0, boolean p1) {
        target.onSearch(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_buy_now, "method 'onBuy'");
    view7f0b00c8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBuy();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SingleProductDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mDescription = null;
    target.mBrandName = null;
    target.mProductName = null;
    target.mProductPrice = null;
    target.mSlider = null;
    target.mBtnAddToCart = null;
    target.bottomContainer = null;
    target.mBtnOutOfStock = null;

    view7f0b00c3.setOnClickListener(null);
    view7f0b00c3 = null;
    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
    view7f0b02d5.setOnClickListener(null);
    view7f0b02d5 = null;
    view7f0b021e.setOnClickListener(null);
    view7f0b021e.setOnFocusChangeListener(null);
    view7f0b021e = null;
    view7f0b00c8.setOnClickListener(null);
    view7f0b00c8 = null;
  }
}
