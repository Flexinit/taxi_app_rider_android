// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.chat;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChatDialog_ViewBinding implements Unbinder {
  private ChatDialog target;

  private View view7f0b02ac;

  @UiThread
  public ChatDialog_ViewBinding(final ChatDialog target, View source) {
    this.target = target;

    View view;
    target.messagesListView = Utils.findRequiredViewAsType(source, R.id.message_list, "field 'messagesListView'", MessagesList.class);
    target.sendInput = Utils.findRequiredViewAsType(source, R.id.input_message, "field 'sendInput'", MessageInput.class);
    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.tv_toolbar_title, "field 'toolbarTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBack'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBack();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ChatDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.messagesListView = null;
    target.sendInput = null;
    target.toolbarTitle = null;

    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
  }
}
