package com.example.earthquakemobile.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.earthquakemobile.model.Station;

import java.util.List;

@Dao
public interface StationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<Station> data);
    @Query("DELETE FROM stations;")
    public void deleteAll();
    @Query("SELECT * FROM stations;")
    public List<Station> getStations();
}
