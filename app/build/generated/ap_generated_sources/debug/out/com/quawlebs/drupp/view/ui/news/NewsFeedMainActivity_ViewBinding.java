// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.news;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewsFeedMainActivity_ViewBinding implements Unbinder {
  private NewsFeedMainActivity target;

  private View view7f0b02ac;

  @UiThread
  public NewsFeedMainActivity_ViewBinding(NewsFeedMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NewsFeedMainActivity_ViewBinding(final NewsFeedMainActivity target, View source) {
    this.target = target;

    View view;
    target.mNoResultsMessage = Utils.findRequiredViewAsType(source, R.id.tv_no_results, "field 'mNoResultsMessage'", TextView.class);
    target.newsRecyclerView = Utils.findRequiredViewAsType(source, R.id.news_feeds_Recycle, "field 'newsRecyclerView'", RecyclerView.class);
    target.newsCategories = Utils.findRequiredViewAsType(source, R.id.spinner_news_categories, "field 'newsCategories'", Spinner.class);
    target.newsSubCategories = Utils.findRequiredViewAsType(source, R.id.spinner_news_sub_categories, "field 'newsSubCategories'", Spinner.class);
    target.containerSubCategory = Utils.findRequiredViewAsType(source, R.id.container_news_sub_category, "field 'containerSubCategory'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBackPress'");
    view7f0b02ac = view;
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
    NewsFeedMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mNoResultsMessage = null;
    target.newsRecyclerView = null;
    target.newsCategories = null;
    target.newsSubCategories = null;
    target.containerSubCategory = null;

    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
