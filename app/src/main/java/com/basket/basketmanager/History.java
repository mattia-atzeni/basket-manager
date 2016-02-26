package com.basket.basketmanager;


import com.basket.basketmanager.database.MatchHandler;


public class History extends MatchListFragment {

    public static History getFragment() {
        History instance = new History();
        final int kind = MatchHandler.PAST;
        instance.setKind(kind);
        instance.setTitle("Partite Arbitrate");
        MatchListFragment.getMap().put(kind, PastMatch.class);
        return instance;
    }
}
