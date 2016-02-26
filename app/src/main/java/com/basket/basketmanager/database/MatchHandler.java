package com.basket.basketmanager.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.basket.basketmanager.database.BasketManagerContract.MatchTable;
import com.basket.basketmanager.model.AppData;
import com.basket.basketmanager.model.DateFormatter;
import com.basket.basketmanager.model.Match;
import com.basket.basketmanager.model.Referee;
import com.basket.basketmanager.model.StopOver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class MatchHandler {
    private BasketManagerDbHelper dbHelper;
    public static final int PAST = 0;
    private static final int NEXT = 1;
    public static final int FUTURE = 2;
    public static final int TO_PLAN = 3;
    public static final int PROPOSED = 4;

    public MatchHandler(Context context) {
        dbHelper = new BasketManagerDbHelper(context);
    }

    public static ContentValues prepareValues(Match match) {
        ContentValues values = new ContentValues();
        values.put(MatchTable.HOME_TEAM, match.getHomeTeam());
        values.put(MatchTable.VISITING_TEAM, match.getVisitingTeam());
        values.put(MatchTable.PLACE, match.getPlace());
        values.put(MatchTable.STREET, match.getAddress().getStreet());
        values.put(MatchTable.STREET_NUMBER, match.getAddress().getStreetNumber());
        values.put(MatchTable.DATE, match.getDateTime());
        values.put(MatchTable.FIRST_REFEREE, match.getFirstReferee().getId());
        values.put(MatchTable.SECOND_REFEREE, match.getSecondReferee().getId());
        values.put(MatchTable.CATEGORY, match.getCategory());
        values.put(MatchTable.ACCEPTED, 1);
        values.put(MatchTable.SCORE, match.getScore());
        return values;
    }

    public void insertMatch(Match match) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(MatchTable.TABLE_NAME, null, prepareValues(match));
        db.close();
    }

    public void insertProposedMatch(Match match) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = prepareValues(match);
        values.remove(MatchTable.ACCEPTED);
        values.put(MatchTable.ACCEPTED, 0);
        db.insert(MatchTable.TABLE_NAME, null, values);
        db.close();
    }

    public void newRandomProposedMatch() {
        insertProposedMatch(getRandomFutureMatch());
    }

    private List<Match> getAllMatches(boolean accepted) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String value = accepted ? "1" : "0";

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + MatchTable.TABLE_NAME + " WHERE " + MatchTable.ACCEPTED + " = " + value, null
        );
        List<Match> list = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                list.add(getMatchFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        dbHelper.close();
        return list;
    }

    public List<Match> getAllMatches() {
        return getAllMatches(true);
    }

    public List<Match> getAllProposedMatches() {
        return getAllMatches(false);
    }

    public List<Match> getMatches(int kind) {
        switch (kind) {
            case FUTURE: return getFutureMatches();
            case PAST: return getPastMatches();
            case TO_PLAN: return getMatchesToPlan();
            case PROPOSED: return getAllProposedMatches();
            default: throw new IllegalArgumentException();
        }
    }

    public List<Match> getFutureMatches() {
        List<Match> list = getAllMatches();
        List<Match> result = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        for (Match match : list) {
            if (now.compareTo(match.getCalendar()) < 0) {
                result.add(match);
            }
        }
        return result;
    }

    public List<Match> getPastMatches() {
        List<Match> list = getAllMatches();
        List<Match> result = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        for (Match match : list) {
            if (now.compareTo(match.getCalendar()) > 0) {
                result.add(match);
            }
        }
        return result;
    }

    public List<Match> getMatchesToPlan() {
        List<Match> list = getFutureMatches();

        List<Match> res = new ArrayList<>();

        for (Match match : list) {
            if (match.getPlan() == null) {
                res.add(match);
            }
        }
        return res;
    }

    public static Match getRandomFutureMatch() {
        return getRandomMatch(FUTURE);
    }

    public static Match getRandomNextMatch() {
        return getRandomMatch(NEXT);
    }

    public static Match getRandomPastMatch() {
        return getRandomMatch(PAST);
    }

    private static Match getRandomMatch(int kind) {
        Random prg = new Random();
        Match match = new Match();

        int i = prg.nextInt(AppData.ADDRESSES.length);
        match.setPlace(AppData.PLACES[i]);
        match.setAddress(AppData.ADDRESSES[i]);

        i = prg.nextInt(AppData.TEAMS.length);
        int j;
        do {
            j = prg.nextInt(AppData.TEAMS.length);
        } while (j == i);

        match.setHomeTeam(AppData.TEAMS[i]);
        match.setVisitingTeam(AppData.TEAMS[j]);

        switch (kind) {
            case NEXT:
                i = 0;
                break;
            case FUTURE:
                i = prg.nextInt(60) + 1;
                break;
            case PAST:
                i = (-1) - prg.nextInt(360);
                break;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, i);

        int hours = prg.nextInt(5) + 18;
        int minutes = prg.nextInt(2) * 30;

        String date = DateFormatter.formatDateExtended(calendar);
        String time = hours + ":" + String.format("%02d", minutes);
        match.setDateTime(date + " - " + time);

        i = prg.nextInt(AppData.REFEREES.length - 1) + 1;

        match.setFirstReferee(AppData.REFEREES[0]);
        match.setSecondReferee(AppData.REFEREES[i]);

        i = prg.nextInt(AppData.CATEGORIES.length);
        match.setCategory(AppData.CATEGORIES[i]);

        i = prg.nextInt(40) + 42;
        j = prg.nextInt(40) + 42;

        match.setScore(i + " - " + j);
        return match;
    }

    public void savePlan(Match match) {
        final String plan = new Gson().toJson(match.getPlan());
        final String query =
                "UPDATE " + MatchTable.TABLE_NAME +
                " SET " + MatchTable.PLAN + " = \'" + plan + "\'" +
                " WHERE " + MatchTable._ID + " = " + match.getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    private Match getMatchFromCursor(Cursor cursor) {
        Match match = new Match();
        match.setId(cursor.getInt((cursor.getColumnIndexOrThrow(MatchTable._ID))));
        match.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.CATEGORY)));
        match.setHomeTeam(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.HOME_TEAM)));
        match.setVisitingTeam(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.VISITING_TEAM)));
        match.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.DATE)));
        match.setPlace(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.PLACE)));
        match.setFirstReferee(getRefereeById(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.FIRST_REFEREE))));
        match.setSecondReferee(getRefereeById(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.SECOND_REFEREE))));
        String street = (cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.STREET)));
        int streetNumber = (cursor.getInt(cursor.getColumnIndexOrThrow(MatchTable.STREET_NUMBER)));
        match.setAddress(street, streetNumber);
        match.setScore(cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.SCORE)));

        String planJson = cursor.getString(cursor.getColumnIndexOrThrow(MatchTable.PLAN));
        if (planJson != null) {
            Type listType = new TypeToken<ArrayList<StopOver>>() {}.getType();
            List<StopOver> list = new Gson().fromJson(planJson, listType);
            match.setPlan(list);
        }
        return match;
    }

    private Referee getRefereeById(String id) {
        for (Referee referee : AppData.REFEREES) {
            if (referee.getId().equals(id)) {
                return referee;
            }
        }
        return null;
    }

    public void onMatchAccepted(Match match) {
        final String query = "UPDATE " + MatchTable.TABLE_NAME +
        " SET " + MatchTable.ACCEPTED + " = 1" +
        " WHERE " + MatchTable._ID + " = " + match.getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void rejectMatch(Match match) {
        final String query = "DELETE FROM " + MatchTable.TABLE_NAME +
                " WHERE " + MatchTable._ID + " = " + match.getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
}