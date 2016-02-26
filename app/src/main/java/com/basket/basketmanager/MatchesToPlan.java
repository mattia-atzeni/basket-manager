package com.basket.basketmanager;


import com.basket.basketmanager.database.MatchHandler;


public class MatchesToPlan extends MatchListFragment {
    public static MatchesToPlan getFragment() {
        MatchesToPlan instance = new MatchesToPlan();
        final int kind = MatchHandler.TO_PLAN;
        instance.setKind(kind);
        instance.setTitle("Partite da pianificare");
        MatchListFragment.getMap().put(kind, Planning.class);
        return instance;
    }
}