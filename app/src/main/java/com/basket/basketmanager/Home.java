package com.basket.basketmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.basket.basketmanager.database.MatchHandler;
import com.basket.basketmanager.model.Match;

import java.util.Collections;
import java.util.List;

public class Home extends BaseFragment {

    List<Match> mMatchList;

    public static Home getFragment() {
        return new Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home, container, false);

        ((TextView) root.findViewById(R.id.list_title)).setText(getString(R.string.future_matches));
        root.findViewById(R.id.no_matches).setVisibility(View.GONE);

        ListView listView = (ListView) root.findViewById(R.id.match_list);
        listView.setFocusable(false);
        mMatchList = new MatchHandler(getActivity().getApplicationContext()).getFutureMatches();
        Collections.sort(mMatchList);
        if (mMatchList.isEmpty()) {
            root.findViewById(R.id.next_match_info).setVisibility(View.GONE);
            root.findViewById(R.id.future_matches_list).setVisibility(View.GONE);
            return root;
        }

        root.findViewById(R.id.no_future_matches).setVisibility(View.GONE);

        final Match nextMatch = mMatchList.remove(0);

        if (mMatchList.isEmpty()) {
            root.findViewById(R.id.future_matches_list).setVisibility(View.GONE);
            return root;
        }

        setNextMatch(nextMatch, root);

        MatchAdapter adapter = new MatchAdapter(mMatchList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getListener().onFragmentSwitch(FutureMatch.getFragment(mMatchList.get(position)));
            }
        });

        Button planButton = (Button) root.findViewById(R.id.next_match_plan);
        planButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planNextMatch(nextMatch);
            }
        });


        View.OnClickListener nextMatchListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().onFragmentSwitch(FutureMatch.getFragment(nextMatch));
            }
        };

        Button detailsButton = (Button) root.findViewById(R.id.next_match_details);
        detailsButton.setOnClickListener(nextMatchListener);

        View nextMatchInfo = root.findViewById(R.id.next_match_info);
        nextMatchInfo.setOnClickListener(nextMatchListener);

        return root;
    }

    private void setNextMatch(Match nextMatch, View view) {
        ((TextView) view.findViewById(R.id.next_match_place)).setText(nextMatch.getPlace());
        ((TextView) view.findViewById(R.id.next_match_address)).setText(nextMatch.getAddress().toString());
        ((TextView) view.findViewById(R.id.next_match_date)).setText(nextMatch.getDate());
        ((TextView) view.findViewById(R.id.next_match_time)).setText(nextMatch.getTime());
    }

    public void planNextMatch(Match nextMatch) {
       getListener().onFragmentSwitch(Planning.getFragment(nextMatch));
    }

    @Override
    public void onResume() {
        getBaseActivity().setNavigationViewCheckedItem(0);
        super.onResume();
    }

}
