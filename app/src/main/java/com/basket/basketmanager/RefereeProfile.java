package com.basket.basketmanager;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.basket.basketmanager.model.AppData;
import com.basket.basketmanager.model.Referee;

public class RefereeProfile extends BaseFragment {

    private FrameLayout mFrameLayout;

    public static Fragment getFragment() {
        return new RefereeProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFrameLayout = new FrameLayout(getActivity());
        inflate(inflater);
        return mFrameLayout;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mFrameLayout != null) {
            mFrameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflate(inflater);
        }
    }

    private void inflate(LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.referee_profile, null);
        mFrameLayout.addView(root);
        fillValues(root);
    }

    private Referee getReferee() {
        return AppData.REFEREES[0];
    }

    private void fillValues(View root) {
        Referee referee = getReferee();

        ((TextView) root.findViewById(R.id.name)).setText(referee.toString());
        ((TextView) root.findViewById(R.id.email)).setText(referee.getEmail());
        ((TextView) root.findViewById(R.id.phone)).setText(referee.getPhone());
        ((TextView) root.findViewById(R.id.place)).setText(referee.getPlace() + ", CA");
        ((TextView) root.findViewById(R.id.birthdate)).setText(referee.getBirthDate());
        ((TextView) root.findViewById(R.id.address)).setText(referee.getAddress());
        ((TextView) root.findViewById(R.id.card_number)).setText(referee.getId());
        ((TextView) root.findViewById(R.id.category)).setText(referee.getCategory());

    }
}
