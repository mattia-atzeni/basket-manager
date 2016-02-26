package com.basket.basketmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<String> mList;
    private View mItemView;
    private Spinner mSpinner;

    public SpinnerAdapter(List<String> list, Activity activity) {
        this.mActivity = activity;
        this.mList = list;
    }

    public SpinnerAdapter(String[] array, Activity activity) {
        this(new ArrayList<>(Arrays.asList(array)), activity);
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent, position);
            convertView = mItemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpinnerViewHolder) convertView.getTag();
        }

        onBindViewHolder(viewHolder, position);
        return convertView;
    }

    private void onOtherClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Inserisci la motivazione");
        View root = mActivity.getLayoutInflater().inflate(R.layout.motivation_edit, null);
        final EditText editText = (EditText) root.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText.getLayout() != null && editText.getLayout().getLineCount() > 3) {
                    editText.getText().delete(editText.getText().length() - 1, editText.getText().length());
                }
            }
        });

        builder.setView(root);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOkButtonClicked(editText, dialog);
                    }
                }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mSpinner.setSelection(0);
            }
        });

        editText.requestFocus();
    }

    private void onOkButtonClicked(EditText editText, AlertDialog dialog) {
        try {
            String newMotivation = editText.getText().toString().trim();
            if (newMotivation.equals("")) {
                throw new RuntimeException();
            }
            mList.add(0, editText.getText().toString());
            SpinnerAdapter.this.notifyDataSetChanged();
            dialog.dismiss();
        } catch (RuntimeException e) {
            editText.setError("Scrivi la motivazione");
        }
    }

    public void onBindViewHolder(SpinnerViewHolder viewHolder, int position) {
        String item = mList.get(position);
        viewHolder.text.setText(item);
    }

    public SpinnerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_item, viewGroup, false);
        return new SpinnerViewHolder(mItemView);
    }

    public static class SpinnerViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView text;

        public SpinnerViewHolder(View view) {
            super(view);
            text = (CheckedTextView) view.findViewById(R.id.textView_spinner);
        }
    }

    public void registerSpinner(Spinner spinner) {
        this.mSpinner = spinner;
        mSpinner.setAdapter(this);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mList.get(position).equals(mActivity.getString(R.string.other))) {
                    onOtherClicked();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

