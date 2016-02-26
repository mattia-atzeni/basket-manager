package com.basket.basketmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.basket.basketmanager.database.MatchHandler;
import com.basket.basketmanager.model.AppData;
import com.basket.basketmanager.model.Match;
import com.basket.basketmanager.notifications.RegistrationIntentService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentSwitchListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver broadcastReceiver;
    private GoogleApiClient client;
    private View notificationView;
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private NavigationView mNavigationView;

    public static final String EXTRA_NOTIFICATION = "com.basket.basketmanager.EXTRA_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean notificationFlag = getIntent().getBooleanExtra(EXTRA_NOTIFICATION, false);

        Toolbar toolbar;
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = sharedPreferences.getBoolean(BasketManagerPreferences.SENT_TOKEN_TO_SERVER, false);
                if (!sentToken) {
                    notifyMessage(R.string.no_connection);
                }
            }
        };

        if (checkPlayServices()) {
            RegistrationIntentService.setContext(getApplicationContext());
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        setContentView(R.layout.base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        onFragmentSwitch(Home.getFragment());
        if (notificationFlag) {
            onFragmentSwitch(ProposedMatch.getFragment(
                    new MatchHandler(getApplicationContext()).getAllProposedMatches().get(0)));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(BasketManagerPreferences.REGISTRATION_COMPLETE));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount()  > 1) {
            BaseFragment current = (BaseFragment) fragmentManager.findFragmentById(R.id.fragment_container);
            current.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_basketball);
        notificationView = MenuItemCompat.getActionView(item);
        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBasketBallIconClicked();
            }
        });
        setNotificationsCount();
        item = menu.findItem(R.id.menu_profile);
        View profileView = MenuItemCompat.getActionView(item);
        TextView name = (TextView) profileView.findViewById(R.id.name);
        name.setText(AppData.FIRST_REFEREE.toString());
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentSwitch(RefereeProfile.getFragment());
            }
        });
        return true;
    }
/*
    public void decrementNotificationsCount() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int count = sharedPreferences.getInt(BasketManagerPreferences.NOTIFICATIONS_COUNT, 0);
        sharedPreferences.edit().putInt(BasketManagerPreferences.NOTIFICATIONS_COUNT, count - 1).apply();
        Log.d("*****", "*****[decrement] count set to " + (count - 1) + "**********");
    }*/

    private void onBasketBallIconClicked() {
        final List<Match> matchList = new MatchHandler(getApplicationContext()).getAllProposedMatches();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (matchList.size() > 0) {
            MatchAdapter adapter = new MatchAdapter(matchList);

            ListView listView = new ListView(this);
            listView.setPadding(16, 16, 16, 16);
            listView.setAdapter(adapter);
            builder.setTitle("Partite proposte");
            builder.setView(listView);
            final Dialog dialog = builder.create();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onFragmentSwitch(ProposedMatch.getFragment(matchList.get(position)));
                    dialog.dismiss();
                }
            });

            dialog.show();

        } else {
            builder.setMessage("Nessuna partita proposta");
            builder.create().show();
        }

    }

    private void setNotificationsCount() {
        if (notificationView != null) {
            int notificationsCount = new MatchHandler(getApplicationContext()).getAllProposedMatches().size();
            TextView notificationsTextView = (TextView) notificationView.findViewById(R.id.notifications_count);
            if (notificationsCount > 0) {
                notificationsTextView.setText(Integer.toString(notificationsCount));
                notificationsTextView.setVisibility(View.VISIBLE);
            } else {
                notificationsTextView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void updateNotificationsCount() {
        setNotificationsCount();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            onFragmentSwitch(Home.getFragment());
        } else if (id == R.id.nav_profile) {
            onFragmentSwitch(RefereeProfile.getFragment());
        } else if (id == R.id.nav_history) {
            onFragmentSwitch(History.getFragment());
        } else if (id == R.id.nav_plan) {
            onFragmentSwitch(MatchesToPlan.getFragment());
        } else if (id == R.id.nav_unwilling) {
        	onFragmentSwitch(Unwilling.getFragment());
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Home Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.basket.basketmanager/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Home Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.basket.basketmanager/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onFragmentSwitch(Fragment fragment) {
        Fragment previous = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (previous == null || !previous.isVisible()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(fragment.getClass().getName());
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
            transaction.commit();
        }
    }

    public void notifyMessage(@StringRes int id) {
        notifyMessage(getString(id));
    }

    public void notifyMessage(String message) {
        View view = findViewById(R.id.drawer_layout);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void setNavigationViewCheckedItem(int index) {
        mNavigationView.getMenu().getItem(index).setChecked(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(BasketManagerPreferences.NOTIFICATIONS_COUNT)) {
            updateNotificationsCount();
        }
    }

}
