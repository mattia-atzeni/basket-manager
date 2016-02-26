package com.basket.basketmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.basket.basketmanager.database.MatchHandler;
import com.basket.basketmanager.model.Match;

public class Refuse extends BaseMatchFragment {

    public static Refuse getFragment(Match match) {
        Refuse instance = new Refuse();
        instance.setMatch(match);
        return instance;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.refuse, container, false);

        Spinner spinner = (Spinner) root.findViewById(R.id.motivations_spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(getResources().getStringArray(R.array.motivations), getActivity());
        adapter.registerSpinner(spinner);

        Button mRejectButton = (Button) root.findViewById(R.id.reject_button);
        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRejectButtonClicked();
            }
        });

        ((TextView) root.findViewById(R.id.match_place)).setText(getMatch().getPlace());
        ((TextView) root.findViewById(R.id.match_date)).setText(getMatch().getDate());
        ((TextView) root.findViewById(R.id.match_time)).setText(getMatch().getTime());

        return root;
    }

    public void onRejectButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Conferma Rifiuto");
        builder.setMessage("Sei sicuro di voler rifiutare la partita?");
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reject();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }


    public void reject() {
        new MatchHandler(getActivity().getApplicationContext()).rejectMatch(getMatch());
        getBaseActivity().notifyMessage("Partita Rifiutata");
        getBaseActivity().updateNotificationsCount();
        getFragmentManager().popBackStack(Home.class.getName(), 0);
    }


}
