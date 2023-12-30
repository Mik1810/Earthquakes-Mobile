package com.example.earthquakemobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.earthquakemobile.model.Earthquake;
import com.example.earthquakemobile.model.Station;

@Database(entities = {Earthquake.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class DB extends RoomDatabase {
    public abstract EarthquakeDAO getEarthquakeDAO();
    private volatile static DB db = null;

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
