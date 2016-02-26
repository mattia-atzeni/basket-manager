package com.basket.basketmanager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.basket.basketmanager.model.Match;

public class FutureMatch extends MatchInfo {

    public static FutureMatch getFragment(Match match) {
        FutureMatch instance = new FutureMatch();
        instance.setMatch(match);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (root != null) {
            Button planButton = (Button) root.findViewById(R.id.match_positive_button);
            planButton.setText("Pianifica");

            planButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getListener().onFragmentSwitch(Planning.getFragment(getMatch()));
                }
            });
        }
        return root;
    }
}
