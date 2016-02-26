package com.basket.basketmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.basket.basketmanager.model.AppData;

public class StopOverDialog extends DialogFragment {

    public static final String TAG = StopOverDialog.class.getSimpleName();
    private StopOverDialogListener mListener;
    private AutoCompleteTextView mTextView;
    private TransportPicker mTransportPicker;
    private String mText;
    private boolean textViewEnabled = true;
    private boolean mIsDriverChecked;
    private int mPosition;
    private String mTitle;
    private boolean mSetTransportPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View root = getActivity().getLayoutInflater().inflate(R.layout.stopover, null);

        mTextView = (AutoCompleteTextView) root.findViewById(R.id.autoCompleteTextView);
        mTextView.setText(mText);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, AppData.PLACES);
        mTextView.setAdapter(adapter);
        mTextView.setEnabled(textViewEnabled);

        mTransportPicker = new TransportPicker(root);
        if (mSetTransportPicker) {
            mTransportPicker.setPosition(mPosition);
            mTransportPicker.setDriverChecked(mIsDriverChecked);
            mTransportPicker.commit();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle);
        builder.setView(root);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mListener != null) {
                    mListener.onStopOverDialogOkButton(StopOverDialog.this);
                }

            }
        }).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onStopOverDialogOkButton(StopOverDialog.this);
                    }
                });

                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       getDialog().dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    public void setListener(StopOverDialogListener listener) {
        this.mListener = listener;
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onStopOverDialogOkButton(StopOverDialog.this);
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
        }
    }

    public StopOverDialogListener getListener() {
        return mListener;
    }

    public void setTextViewEnabled(boolean textViewEnabled) {
        this.textViewEnabled = textViewEnabled;
    }

    public interface StopOverDialogListener {
        void onStopOverDialogOkButton(StopOverDialog dialog);
    }

    public void setSpinnerPosition(int position) {
        if (position >= 0) {
            if (mTransportPicker != null) {
                mTransportPicker.setPosition(position);
            }
            mPosition = position;
            mSetTransportPicker = true;
        }
    }

    public int getSpinnerPosition() {
        return mTransportPicker.getPosition();
    }

    public boolean isDriverChecked() {
        return mTransportPicker.isDriverChecked();
    }

    public void setDriverChecked(boolean checked) {
        if (mTransportPicker != null) {
            mTransportPicker.setDriverChecked(checked);
        }
        mIsDriverChecked = checked;
        mSetTransportPicker = true;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public AutoCompleteTextView getTextView() {
        return mTextView;
    }

    public String getText() {
        return mTextView.getText().toString();
    }
}
