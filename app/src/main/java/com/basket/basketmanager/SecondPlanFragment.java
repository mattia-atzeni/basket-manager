package com.basket.basketmanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.basket.basketmanager.database.MatchHandler;
import com.basket.basketmanager.model.Match;
import com.basket.basketmanager.model.StopOver;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class SecondPlanFragment extends Planning {
    private List<StopOver> mItinerary;
    private List<StopOver> mPreviousItinerary;
    private ItineraryAdapter mItineraryAdapter;
    private static final String CONFIRM_FLAG = "confirmFlag";

    public static SecondPlanFragment getFragment(Match match) {
        SecondPlanFragment instance = new SecondPlanFragment();
        instance.setMatch(match);
        return instance;
    }

    public void setConfirmFlag(boolean flag) {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putBoolean(CONFIRM_FLAG, flag);
        setArguments(args);
    }

    public boolean getConfirmFlag() {
        try {
            return getArguments().getBoolean(CONFIRM_FLAG);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.planning, container, false);
        Match match = getMatch();

        ((TextView) root.findViewById(R.id.match_place)).setText(match.getPlace());
        ((TextView) root.findViewById(R.id.match_date)).setText(match.getDate());
        ((TextView) root.findViewById(R.id.match_time)).setText(match.getTime());

        mItinerary = match.getPlan();

        cloneItinerary();

        ListView mItineraryView = (ListView) root.findViewById(R.id.itinerary);
        mItineraryAdapter = new ItineraryAdapter(mItinerary, getActivity());
        mItineraryView.setAdapter(mItineraryAdapter);
        mItineraryView.setClickable(true);
        registerForContextMenu(mItineraryView);

        final ScrollView scrollView = (ScrollView) root.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                v.clearFocus();
                return v.onTouchEvent(event);
            }
        });

        Button addButton = (Button) root.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                mItineraryAdapter.addNewStopOver();
                v.clearFocus();
                v.setFocusableInTouchMode(false);
            }
        });

        final Button confirmButton = (Button) root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                confirmPlan();
                v.clearFocus();
                v.setFocusableInTouchMode(false);
            }
        });

        return root;
    }

    private void cloneItinerary() {
        mPreviousItinerary = new ArrayList<>();
        for  (StopOver stopOver : mItinerary) {
            mPreviousItinerary.add(new StopOver(stopOver));
        }
    }

    private void confirmPlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Conferma Pianificazione");
        String date = getMatch().getDate();
        char first = date.charAt(0);
        String dateMessage;
        if (Character.isDigit(first)) {
            dateMessage = " del " + date;
        } else {
            dateMessage = " di " + date.toLowerCase();
        }
        builder.setMessage("Sei sicuro di voler confermare la pianificazione? " +
                        "Potrai comunque modificare la pianificazione fino alle ore " + getMatch().getTime() +
                dateMessage + ".");

        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.stopover_context_menu, menu);
    }

    @Override
    public void onBackPressed() {
        if (mItinerary.equals(mPreviousItinerary) && !getConfirmFlag()) {
            getFragmentManager().popBackStack(Home.class.getName(), 0);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pianificazione non salvata");
        builder.setMessage("Desideri salvare la pianificazione prima di uscire, per poterla riprendere in un secondo momento?");
        builder.setPositiveButton("Salva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        }).setNegativeButton("Esci", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFragmentManager().popBackStack(Home.class.getName(), 0);
            }
        }).setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                mItineraryAdapter.editStopOver(info.position);
                return true;
            case R.id.context_menu_delete:
                ((SwipeLayout) info.targetView).open();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void save() {
        getMatch().setPlan(mItinerary);
        new MatchHandler(getActivity().getApplicationContext()).savePlan(getMatch());
        getFragmentManager().popBackStack(Home.class.getName(), 0);
        getBaseActivity().notifyMessage("Pianificazione salvata con successo");
    }
}
