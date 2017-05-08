package com.progdawn.collectaholic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.progdawn.collectaholic.database.CollectableBaseHelper;
import com.progdawn.collectaholic.database.CollectableCursorWrapper;
import com.progdawn.collectaholic.database.CollectableDbSchema;
import com.progdawn.collectaholic.database.CollectableDbSchema.CollectableTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class Collection {

    private static Collection sCollection;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static Collection get(Context context){
        if(sCollection == null){
            sCollection = new Collection(context);
        }
        return sCollection;
    }

    private Collection(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CollectionBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Collectable> getCrimes(){
        List<Collectable> collectables = new ArrayList<>();

        CollectableCursorWrapper cursor = queryCollectables(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                collectables.add(cursor.getCollectable());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }

        return collectables;
    }

    public Collectable getCollectable(UUID id){

        CollectableCursorWrapper cursor = queryCrimes(
                CollectableTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCollectable();
        }finally{
            cursor.close();
        }
    }

    public void addCollectable(Collectable c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(CollectableTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Collectable collectable){
        ContentValues values = new ContentValues();
        values.put(CollectableTable.Cols.UUID, collectable.getId().toString());
        values.put(CollectableTable.Cols.NAME, collectable.getName());
        values.put(CollectableTable.Cols.SERIES, collectable.getSeries());
        values.put(CollectableTable.Cols.DATE, collectable.getDate().getTime());

        return values;
    }

    public void updateCollectable(Collectable collectable){
        String uuidString = collectable.getId().toString();
        ContentValues values = getContentValues(collectable);

        mDatabase.update(CollectableTable.NAME, values, CollectableTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private CollectableCursorWrapper queryCollectables(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CollectableTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CollectableCursorWrapper(cursor);
    }

    public File getPhotoFile(Collectable collectable){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, collectable.getPhotoFilename());
    }
}
