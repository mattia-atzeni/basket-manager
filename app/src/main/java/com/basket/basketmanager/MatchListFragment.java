package com.basket.basketmanager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.basket.basketmanager.database.MatchHandler;
import com.basket.basketmanager.model.Match;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchListFragment extends BaseFragment {
    private ListView mListView;
    private List<Match> mMatchList;
    private MatchAdapter mAdapter;
    private TextView title;
    private static final String KIND = "kind";
    private static final String TITLE = "title";
    private static Map<Integer, Class<? extends BaseMatchFragment>> map = new HashMap<>();

    public static MatchListFragment getFragment(int kind, String title, Class<? extends BaseMatchFragment> fragmentClass) {
        MatchListFragment instance = new MatchListFragment();
        instance.setKind(kind);
        instance.setTitle(title);
        map.put(kind, fragmentClass);
        return instance;
    }

    public void setKind(int kind) {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(KIND, kind);
        setArguments(args);
    }

    public int getKind() {
        return getArguments().getInt(KIND);
    }

    public static Map<Integer, Class<? extends BaseMatchFragment>> getMap() {
        return map;
    }

    public void setTitle(String title) {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putString(TITLE, title);
        setArguments(args);
    }

    public String getTitle() {
        return getArguments().getString(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.match_list, container, false);
        title = (TextView) root.findViewById(R.id.list_title);
        title.setText(getTitle());

        mListView = (ListView) root.findViewById(R.id.match_list);
        mListView.setFocusable(false);
        mMatchList = new MatchHandler(getActivity().getApplicationContext()).getMatches(getKind());

        if (mMatchList == null || mMatchList.isEmpty()) {
            root.findViewById(R.id.no_matches).setVisibility(View.VISIBLE);
        } else {
            root.findViewById(R.id.no_matches).setVisibility(View.GONE);
        }

        Collections.sort(mMatchList);

        if (getKind() == MatchHandler.PAST) {
            Collections.reverse(mMatchList);
        }

        mAdapter = new MatchAdapter(mMatchList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Method method = map.get(getKind()).getMethod("getFragment", Match.class);
                    getListener().onFragmentSwitch((BaseMatchFragment) method.invoke(null, mMatchList.get(position)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return root;
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack(Home.class.getName(), 0);
    }
}
