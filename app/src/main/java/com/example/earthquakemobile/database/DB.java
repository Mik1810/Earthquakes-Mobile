package com.example.earthquakemobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.earthquakemobile.model.Station;

@Database(entities = {Station.class}, version = 1)
public abstract class DB extends RoomDatabase {
    public abstract StationDAO getStationDAO();
    private volatile static DB db=null;

    public static synchronized DB getInstance(Context context){
        if(db == null){
            synchronized (DB.class){
                if(db == null){
                    db = Room.databaseBuilder(context,DB.class,"database.db").build();
                }
            }
        }
        return db;
    }
}
