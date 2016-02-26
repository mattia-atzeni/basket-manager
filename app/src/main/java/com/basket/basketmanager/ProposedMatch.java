package com.basket.basketmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.basket.basketmanager.database.MatchHandler;
import com.basket.basketmanager.model.Match;

public class ProposedMatch extends MatchInfo {

    public static ProposedMatch getFragment(Match match) {
        ProposedMatch instance = new ProposedMatch();
        instance.setMatch(match);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (root != null) {
            Button acceptButton = (Button) root.findViewById(R.id.match_positive_button);
            acceptButton.setText("Accetta");
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAcceptConfirmationDialog();
                }
            });
        }
        return root;
    }

    private void showAcceptConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Accetta Partita");
        builder.setMessage("Sei sicuro di voler accettare la partita?");
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new MatchHandler(getActivity().getApplicationContext()).onMatchAccepted(getMatch());
                getBaseActivity().updateNotificationsCount();
                showPlanDialog();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Partita accettata con successo");
        builder.setMessage("Vuoi procedere ora con la pianificazione?");
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getBaseActivity().onFragmentSwitch(Planning.getFragment(getMatch()));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFragmentManager().popBackStack(Home.class.getName(), 0);
            }
        });
        builder.create().show();
    }
}
