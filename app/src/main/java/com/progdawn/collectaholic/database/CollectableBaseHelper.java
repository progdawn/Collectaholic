package com.progdawn.collectaholic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.progdawn.collectaholic.database.CollectableDbSchema.CollectableTable;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectableBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "collectableBase.db";

    public CollectableBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CollectableTable.NAME + "(" +
                CollectableTable.Cols.UUID + ", " +
                CollectableTable.Cols.NAME + ", " +
                CollectableTable.Cols.SERIES + ", " +
                CollectableTable.Cols.DATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
