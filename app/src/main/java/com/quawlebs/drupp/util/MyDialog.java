package com.quawlebs.drupp.util;

import android.app.AlertDialog;
import android.content.Context;

public class MyDialog extends AlertDialog.Builder {
    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public AlertDialog create() {
        return super.create();
    }
}
