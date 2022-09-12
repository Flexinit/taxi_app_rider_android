package com.quawlebs.drupp.view.ui.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.IDialogButtonClickListener;


public class CompletedDialog extends DialogFragment {
    IDialogButtonClickListener iDialogButtonClickListener;
    int layoutResource;
    private Button complete;

    public CompletedDialog(IDialogButtonClickListener iDialogButtonClickListener,int layoutResource){
        this.iDialogButtonClickListener=iDialogButtonClickListener;
        this.layoutResource=layoutResource;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(layoutResource, container, false);
        iDialogButtonClickListener.setUpDialogViews(view);
        complete = view.findViewById(R.id.btnComplete);

        complete.setOnClickListener(v -> {
            dismiss();

            iDialogButtonClickListener.onButtonClick();

        });

        return view;
    }


}
