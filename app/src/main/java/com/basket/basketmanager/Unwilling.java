package com.basket.basketmanager;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class Unwilling extends BaseFragment {

    private DateText startUnwilling;
    private DateText endUnwilling;
    private TextView errorsView;
    private View space;

    public static Fragment getFragment() {
        return new Unwilling();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.unwilling, container, false);
        startUnwilling = (DateText) root.findViewById(R.id.start_unwilling);
        endUnwilling = (DateText) root.findViewById(R.id.end_unwilling);
        errorsView = (TextView) root.findViewById(R.id.error_message);
        errorsView.setVisibility(View.GONE);
        space = root.findViewById(R.id.space);
        startUnwilling.setMinDate(Calendar.getInstance());
        endUnwilling.setMinDate(Calendar.getInstance());
        startUnwilling.addOnDateSetListener(new OnExternalDateSetListener() {
            @Override
            public void onExternalDateSet(int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endUnwilling.setMinDate(calendar);
                if (endUnwilling.getDate().compareTo(calendar) < 0) {
                    endUnwilling.setText(null);
                }
            }
        });

        Button okButton = (Button) root.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonClicked();
            }
        });

        Button cancelButton = (Button) root.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return root;
    }

    public void okButtonClicked() {
        int errors = 0;

        if (!isValid(startUnwilling.getText().toString())) {
            startUnwilling.setError("Inserisci la data di inizio del periodo di indisponibilità");
            startUnwilling.requestFocus();
            errors++;
        } else {
            startUnwilling.setError(null);
        }

        if (!isValid(endUnwilling.getText().toString())) {
            endUnwilling.setError("Inserisci la data di fine del periodo di indisponibilità");
            if (errors == 0) {
                endUnwilling.requestFocus();
            }
            errors++;
        } else {
            endUnwilling.setError(null);
        }

        if (errors > 0) {
            if (errors == 1) {
                errorsView.setText("Si è verificato un errore");
            } else {
                errorsView.setText("Si sono verificati " + errors + " errori");
            }

            errorsView.setVisibility(View.VISIBLE);
            space.setVisibility(View.GONE);
            return;
        }

        confirm();

    }

    private void confirm() {
        String start = startUnwilling.getText().toString();
        String end = endUnwilling.getText().toString();
        String message;
        if (start.equals(end)) {
            message = "per il giorno " + start + ".";
        } else {
            message = "dal " + start + " al " + end + ".";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Conferma invio");
        builder.setMessage("Sei sicuro di voler inviare la richiesta? " +
                "Se confermi, non ti saranno assegnate partite " + message);
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
                getBaseActivity().notifyMessage("Richiesta inviata con  successo");
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private boolean isValid(String text) {
        return text != null && !text.trim().equals("");
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack(Home.class.getName(), 0);
    }
}
