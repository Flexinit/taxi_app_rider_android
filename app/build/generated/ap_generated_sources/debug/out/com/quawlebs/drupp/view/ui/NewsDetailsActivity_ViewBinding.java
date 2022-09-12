// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewsDetailsActivity_ViewBinding implements Unbinder {
  private NewsDetailsActivity target;

  private View view7f0b02cf;

  @UiThread
  public NewsDetailsActivity_ViewBinding(NewsDetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NewsDetailsActivity_ViewBinding(final NewsDetailsActivity target, View source) {
    this.target = target;

    View view;
    target.mToolTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mToolTitle'", TextView.class);
    target.mNewsImage = Utils.findRequiredViewAsType(source, R.id.image_news, "field 'mNewsImage'", ImageView.class);
    target.mNewsFeed = Utils.findRequiredViewAsType(source, R.id.tv_news, "field 'mNewsFeed'", TextView.class);
    target.mHeader = Utils.findRequiredViewAsType(source, R.id.tv_header, "field 'mHeader'", TextView.class);
    target.mTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'mTime'", TextView.class);
    target.mCategoryName = Utils.findRequiredViewAsType(source, R.id.tv_category_name, "field 'mCategoryName'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_back, "method 'onBackPress'");
    view7f0b02cf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    NewsDetailsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolTitle = null;
    target.mNewsImage = null;
    target.mNewsFeed = null;
    target.mHeader = null;
    target.mTime = null;
    target.mCategoryName = null;

    view7f0b02cf.setOnClickListener(null);
    view7f0b02cf = null;
  }
}
