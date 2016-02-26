package com.basket.basketmanager;

import com.basket.basketmanager.model.Match;

public class Planning extends BaseMatchFragment {

    public static Planning getFragment(Match match) {
        Planning instance;
        if (match.getPlan() == null) {
            instance = new FirstPlanFragment();
        } else {
            instance = new SecondPlanFragment();
        }
        instance.setMatch(match);
        return instance;
    }
}
