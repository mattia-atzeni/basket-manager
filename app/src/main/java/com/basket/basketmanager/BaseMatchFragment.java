package com.basket.basketmanager;

import android.os.Bundle;

import com.basket.basketmanager.model.Match;
import com.google.gson.Gson;

public class BaseMatchFragment extends BaseFragment {

    private static final String MATCH = "match";
    private Match mMatch;

    public void setMatch(Match match) {
        this.mMatch = match;
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        String matchJson = new Gson().toJson(match);
        args.putString(MATCH, matchJson);
        setArguments(args);
    }

    public Match getMatch() {
        if (mMatch != null) {
            return mMatch;
        }

        Bundle args = getArguments();
        if (args != null) {
            String matchJson = args.getString(MATCH);
            return new Gson().fromJson(matchJson, Match.class);
        }

        return null;
    }
}
