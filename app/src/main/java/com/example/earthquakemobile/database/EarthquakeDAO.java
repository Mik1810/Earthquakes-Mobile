package com.example.earthquakemobile.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.earthquakemobile.model.Earthquake;

import java.util.List;

@Dao
public interface EarthquakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Earthquake> data);
    @Query("DELETE FROM earthquakes;")
    void deleteAll();
    @Query("SELECT * FROM earthquakes;")
    List<Earthquake> getEarthquakes();
}
