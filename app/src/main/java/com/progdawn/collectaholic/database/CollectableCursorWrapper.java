package com.progdawn.collectaholic.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.progdawn.collectaholic.Collectable;
import com.progdawn.collectaholic.database.CollectableDbSchema.CollectableTable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectableCursorWrapper extends CursorWrapper{

    public CollectableCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Collectable getCollectable(){
        String uuidString = getString(getColumnIndex(CollectableTable.Cols.UUID));
        String name = getString(getColumnIndex(CollectableTable.Cols.NAME));
        String series = getString(getColumnIndex(CollectableTable.Cols.SERIES));
        long date = getLong(getColumnIndex(CollectableTable.Cols.DATE));

        Collectable collectable = new Collectable(UUID.fromString(uuidString));
        collectable.setName(name);
        collectable.setSeries(series);
        collectable.setDate(new Date(date));

        return collectable;
    }
}
