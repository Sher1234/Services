package io.github.sher1234.service.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfflineBase extends SQLiteOpenHelper {

    public OfflineBase(Context context) {
        super(context, "OfflineDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Queries.CreateUser);
        db.execSQL(Queries.CreateCalls);
        db.execSQL(Queries.CreateCalls2);
        db.execSQL(Queries.CreateLogins);
        db.execSQL(Queries.CreateVisits);
        db.execSQL(Queries.CreateVisits2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Calls");
        db.execSQL("DROP TABLE IF EXISTS Calls2");
        db.execSQL("DROP TABLE IF EXISTS Logins");
        db.execSQL("DROP TABLE IF EXISTS Visits");
        db.execSQL("DROP TABLE IF EXISTS Visits2");
        onCreate(db);
    }
}
