package com.basket.basketmanager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

public class TransportPicker {
    private static int mPosition = 0;
    private static boolean mIsDriverChecked = true;
    private CheckBox mCheckBox;
    private Spinner mSpinner;

    public TransportPicker(View root) {

        mCheckBox = (CheckBox) root.findViewById(R.id.checkbox);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsDriverChecked = isChecked;
            }
        });

        mSpinner = (Spinner) root.findViewById(R.id.spinner);
        mSpinner.setSelection(mPosition);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mCheckBox.setEnabled(true);
                    if (mPosition == 0) {
                        mCheckBox.setChecked(mIsDriverChecked);
                    } else {
                        mCheckBox.setChecked(true);
                    }
                } else {
                    mCheckBox.setChecked(false);
                    mCheckBox.setEnabled(false);
                }
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void commit() {
        mSpinner.setSelection(mPosition);
    }

    public boolean isDriverChecked() {
        return mCheckBox.isChecked();
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setDriverChecked(boolean driverChecked) {
        mIsDriverChecked = driverChecked;
    }
}
