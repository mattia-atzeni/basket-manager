package com.basket.basketmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String TAG = "datePicker";
    private GregorianCalendar date;
    private DatePickerFragmentListener listener;
    private DatePicker datePicker;
    private Calendar minDate;

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public GregorianCalendar getDate() {
        return this.date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (date == null) {
            date = new GregorianCalendar();
        }

        datePicker = new DatePicker(getActivity());
        if (minDate != null) {
            datePicker.setMinDate(minDate.getTimeInMillis());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(datePicker);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                date.set(Calendar.YEAR, datePicker.getYear());
                date.set(Calendar.MONTH, datePicker.getMonth());
                date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                if (listener != null) {
                    listener.onDatePickerFragmentOkButton(DatePickerFragment.this, date);
                }
            }
        }).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.onDatePickerFragmentCancelButton(DatePickerFragment.this);
                }
            }
        });


        return builder.create();
    }

    public void setOnDatePickerFragmentChanged(DatePickerFragmentListener l){
        this.listener = l;
    }


    public void setMinDate(Calendar minDate) {
        this.minDate = minDate;
        if (datePicker != null) {
            datePicker.setMinDate(minDate.getTimeInMillis());
        }
    }


    public interface DatePickerFragmentListener {
        void onDatePickerFragmentOkButton(DialogFragment dialog, GregorianCalendar date);
        void onDatePickerFragmentCancelButton(DialogFragment dialog);
    }
}
