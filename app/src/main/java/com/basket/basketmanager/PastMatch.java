package com.basket.basketmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basket.basketmanager.model.Match;

public class PastMatch extends MatchInfo {
    public static PastMatch getFragment(Match match) {
        PastMatch instance = new PastMatch();
        instance.setMatch(match);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.past_match, container, false);
        setLongMatchViews(root);
        View mapButton = root.findViewById(R.id.map_button);
        ((ViewGroup) mapButton.getParent()).removeView(mapButton);
        ((TextView) root.findViewById(R.id.score)).setText(getMatch().getScore());
        int px = getResources().getDimensionPixelSize(R.dimen.small_padding);
        root.findViewById(R.id.place_title).setPadding(0, 0, 0, px);
        return root;
    }
}
