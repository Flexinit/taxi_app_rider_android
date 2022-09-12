package com.quawlebs.drupp.view.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IDateDialogResponseObserver;
import com.quawlebs.drupp.helpers.ITimeDialogResponseObserver;
import com.quawlebs.drupp.util.IDateDialogObserver;

import java.util.Calendar;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private ITimeDialogResponseObserver iTimeDialogResponseObserver;
    private IDateDialogResponseObserver iDateDialogResponseObserver;


    public static DateDialog newInstance(Bundle bundle) {
        DateDialog dateDialog = new DateDialog();
        dateDialog.setArguments(bundle);
        return dateDialog;
    }

    public void setiDateDialogResponseObserver(IDateDialogResponseObserver iDateDialogResponseObserver) {
        this.iDateDialogResponseObserver = iDateDialogResponseObserver;
    }

    public void setiTimeDialogResponseObserver(ITimeDialogResponseObserver iTimeDialogResponseObserver) {
        this.iTimeDialogResponseObserver = iTimeDialogResponseObserver;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.CustomDatePickerDialogTheme, this, year, month, day);
        if (getArguments() != null) {
            if (getArguments().get(AppConstants.K_MIN_DATE) != null) {
                datePickerDialog.getDatePicker().setMinDate(getArguments().getLong(AppConstants.K_MIN_DATE));
            }
            if (getArguments().get(AppConstants.K_MAX_DATE) != null) {
                datePickerDialog.getDatePicker().setMaxDate(getArguments().getLong(AppConstants.K_MAX_DATE));
            }
        }

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        iDateDialogResponseObserver.onDateSelected(year, month + 1, dayOfMonth);
        if (iTimeDialogResponseObserver != null) {
            DialogFragment timeDialog = TimeDialog.newInstance(hour, minute);
            ((TimeDialog) timeDialog).setiTimeDialogResponseObserver(iTimeDialogResponseObserver);
            timeDialog.show(getFragmentManager(), TimeDialog.class.getSimpleName());
        }

    }
}
