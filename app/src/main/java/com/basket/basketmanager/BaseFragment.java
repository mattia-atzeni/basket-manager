package com.basket.basketmanager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

public class BaseFragment extends Fragment {
    private OnFragmentSwitchListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onAttachToContext(activity);

    }

    private void onAttachToContext(Context context) {
        if (context instanceof OnFragmentSwitchListener) {
            mListener = (OnFragmentSwitchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentSwitchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected OnFragmentSwitchListener getListener() {
        return mListener;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }
}
