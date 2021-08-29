package com.freshliver.ashistant.models.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.HashMap;
import java.util.Map;

public final class DBManager {
    private static final Map<Class<? extends RoomDatabase>, RoomDatabase> databaseMap = new HashMap<>();


    private DBManager() {
    }


    public static <T extends RoomDatabase> T getDatabase(Context context, Class<T> dbClass) {
        if (DBManager.databaseMap.get(dbClass) == null)
            DBManager.databaseMap.put(dbClass, Room.databaseBuilder(context, dbClass, dbClass.getSimpleName()).build());
        return (T) DBManager.databaseMap.get(dbClass);
    }
}
