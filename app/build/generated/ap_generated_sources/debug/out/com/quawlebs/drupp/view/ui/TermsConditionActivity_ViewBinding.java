// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.barteksc.pdfviewer.PDFView;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TermsConditionActivity_ViewBinding implements Unbinder {
  private TermsConditionActivity target;

  private View view7f0b02ac;

  @UiThread
  public TermsConditionActivity_ViewBinding(TermsConditionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TermsConditionActivity_ViewBinding(final TermsConditionActivity target, View source) {
    this.target = target;

    View view;
    target.pdfView = Utils.findRequiredViewAsType(source, R.id.pdfViewer, "field 'pdfView'", PDFView.class);
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
    TermsConditionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pdfView = null;

    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
