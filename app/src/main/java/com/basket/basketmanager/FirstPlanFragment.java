package com.basket.basketmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.basket.basketmanager.model.Match;
import com.basket.basketmanager.model.StopOver;

import java.util.ArrayList;
import java.util.List;

public class FirstPlanFragment extends Planning {

    private TransportPicker mTransportPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.planning_transport, container, false);
        Match match = getMatch();

        ((TextView) root.findViewById(R.id.match_place)).setText(match.getPlace());
        ((TextView) root.findViewById(R.id.match_date)).setText(match.getDate());
        ((TextView) root.findViewById(R.id.match_time)).setText(match.getTime());
        mTransportPicker = new TransportPicker(root);

        Button nextButton = (Button) root.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<StopOver> itinerary = new ArrayList<>();
                itinerary.add(new StopOver("Residenza", mTransportPicker.getPosition(), mTransportPicker.isDriverChecked()));
                itinerary.add(new StopOver(getMatch().getPlace()));
                getMatch().setPlan(itinerary);
                SecondPlanFragment next = SecondPlanFragment.getFragment(getMatch());
                next.setConfirmFlag(true);
                getListener().onFragmentSwitch(next);
            }
        });
        return root;
    }

}
