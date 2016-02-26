package com.basket.basketmanager.database;

import android.provider.BaseColumns;

public class BasketManagerContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";

    public static final String SQL_CREATE_SCHEMA =
            "CREATE TABLE " + MatchTable.TABLE_NAME + " (" +
                MatchTable._ID + " INTEGER PRIMARY KEY, " +
                MatchTable.CATEGORY + TEXT_TYPE + COMMA_SEP +
                MatchTable.HOME_TEAM + TEXT_TYPE + COMMA_SEP +
                MatchTable.VISITING_TEAM + TEXT_TYPE + COMMA_SEP +
                MatchTable.DATE + TEXT_TYPE + COMMA_SEP +
                MatchTable.FIRST_REFEREE + TEXT_TYPE + COMMA_SEP +
                MatchTable.SECOND_REFEREE + TEXT_TYPE + COMMA_SEP +
                MatchTable.PLACE + TEXT_TYPE + COMMA_SEP +
                MatchTable.STREET + TEXT_TYPE + COMMA_SEP +
                MatchTable.STREET_NUMBER + INTEGER_TYPE + COMMA_SEP +
                MatchTable.ACCEPTED + INTEGER_TYPE + COMMA_SEP +
                MatchTable.PLAN + TEXT_TYPE + COMMA_SEP +
                MatchTable.SCORE + TEXT_TYPE + ")";

    public static final String SQL_DELETE_SCHEMA = "DROP TABLE IF EXISTS " + MatchTable.TABLE_NAME;

    private BasketManagerContract() {}

    public static abstract class MatchTable implements BaseColumns {
        public static final String TABLE_NAME = "match";
        public static final String HOME_TEAM = "homeTeam";
        public static final String VISITING_TEAM = "visitingTeam";
        public static final String CATEGORY = "category";
        public static final String DATE = "date";
        public static final String FIRST_REFEREE = "firstReferee";
        public static final String SECOND_REFEREE = "secondReferee";
        public static final String PLACE = "place";
        public static final String STREET = "street";
        public static final String STREET_NUMBER = "streetNumber";
        public static final String ACCEPTED = "accepted";
        public static final String PLAN = "plan";
        public static final String SCORE = "score";
    }
}
