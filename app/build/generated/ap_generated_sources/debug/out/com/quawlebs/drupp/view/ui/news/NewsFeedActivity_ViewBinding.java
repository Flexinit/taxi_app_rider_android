// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.news;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewsFeedActivity_ViewBinding implements Unbinder {
  private NewsFeedActivity target;

  private View view7f0b00dc;

  private View view7f0b00bf;

  @UiThread
  public NewsFeedActivity_ViewBinding(NewsFeedActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NewsFeedActivity_ViewBinding(final NewsFeedActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_no_thanks, "field 'mBtnNoThanks' and method 'onThanksClick'");
    target.mBtnNoThanks = Utils.castView(view, R.id.btn_no_thanks, "field 'mBtnNoThanks'", Button.class);
    view7f0b00dc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onThanksClick();
      }
    });
    target.mBtnCancel = Utils.findRequiredViewAsType(source, R.id.iv_cancel, "field 'mBtnCancel'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btn_access_news, "method 'accessNews'");
    view7f0b00bf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.accessNews();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    NewsFeedActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mBtnNoThanks = null;
    target.mBtnCancel = null;

    view7f0b00dc.setOnClickListener(null);
    view7f0b00dc = null;
    view7f0b00bf.setOnClickListener(null);
    view7f0b00bf = null;
  }
}
