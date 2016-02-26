package com.basket.basketmanager;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.basket.basketmanager.model.DateFormatter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DateText extends EditText implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog mDialog;
    private Calendar mCalendar;
    private Calendar mMinDate;
    private List<OnExternalDateSetListener> mListeners;


    public DateText(Context context) {
        super(context);
        registerDatePicker();
    }

    public DateText(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerDatePicker();
    }

    public DateText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        registerDatePicker();
    }

    protected void registerDatePicker() {

        this.setInputType(InputType.TYPE_NULL);
        mCalendar = Calendar.getInstance();
        mListeners = new ArrayList<>();

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && DateText.this.getError() == null) {
                    showDatePickerDialog();
                }
            }
        });

    }

    public void addOnDateSetListener(OnExternalDateSetListener listener) {
        mListeners.add(listener);
    }

    public void setMinDate(Calendar minDate) {
        if (mDialog != null) {
            mDialog.setMinDate(minDate);
        }
        mMinDate = minDate;
    }

    private void showDatePickerDialog() {
        mDialog = DatePickerDialog.newInstance(
                DateText.this,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );
        mDialog.setMinDate(mMinDate);
        mDialog.show(((Activity) getContext()).getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.setText(DateFormatter.formatDateExtended((GregorianCalendar) mCalendar));
        for (OnExternalDateSetListener listener : mListeners) {
            listener.onExternalDateSet(year, monthOfYear, dayOfMonth);
        }
    }

    public Calendar getDate() {
        return mCalendar;
    }
}
