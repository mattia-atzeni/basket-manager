package com.basket.basketmanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MatchInfo extends BaseMatchFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.match_layout, container, false);

        setLongMatchViews(root);

        Button refuseButton = (Button) root.findViewById(R.id.proposed_match_refuse);
        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onFragmentSwitch(Refuse.getFragment(getMatch()));
            }
        });

        return root;
    }

    public void setLongMatchViews(View root) {
        TextView categoryView = (TextView) root.findViewById(R.id.category);
        TextView dateView = (TextView) root.findViewById(R.id.date);
        TextView timeView = (TextView) root.findViewById(R.id.time);
        TextView homeTeamView = (TextView) root.findViewById(R.id.home_team);
        TextView visitingTeamView = (TextView) root.findViewById(R.id.visiting_team);
        TextView firstRefereeView = (TextView) root.findViewById(R.id.first_referee);
        TextView secondRefereeView = (TextView) root.findViewById(R.id.second_referee);
        TextView addressView = (TextView) root.findViewById(R.id.address);
        TextView placeView = (TextView) root.findViewById(R.id.place);

        categoryView.setText(getMatch().getCategory());
        dateView.setText(getMatch().getDateExtended());
        timeView.setText(getMatch().getTime());
        homeTeamView.setText(getMatch().getHomeTeam());
        visitingTeamView.setText(getMatch().getVisitingTeam());
        firstRefereeView.setText(getMatch().getFirstReferee().toString());
        secondRefereeView.setText(getMatch().getSecondReferee().toString());
        addressView.setText(getMatch().getAddress().toString());
        placeView.setText(getMatch().getPlace());

        ImageButton mapButton = (ImageButton) root.findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra(MapsActivity.EXTRA_ADDRESS, getMatch().getPlace() + " " + getMatch().getAddress());
                startActivity(intent);
            }
        });
    }
}
