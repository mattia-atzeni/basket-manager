package com.basket.basketmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.basket.basketmanager.database.BasketManagerContract.MatchTable;
import com.basket.basketmanager.model.Match;

public class BasketManagerDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BasketManager.db";

    public BasketManagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BasketManagerContract.SQL_CREATE_SCHEMA);

        Match match = MatchHandler.getRandomNextMatch();
        db.insert(MatchTable.TABLE_NAME, null, MatchHandler.prepareValues(match));

        for (int i = 0; i < 5; i++) {
            match = MatchHandler.getRandomFutureMatch();
            db.insert(MatchTable.TABLE_NAME, null, MatchHandler.prepareValues(match));
        }

        for (int i = 0; i < 15; i++) {
            match = MatchHandler.getRandomPastMatch();
            db.insert(MatchTable.TABLE_NAME, null, MatchHandler.prepareValues(match));
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BasketManagerContract.SQL_DELETE_SCHEMA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
