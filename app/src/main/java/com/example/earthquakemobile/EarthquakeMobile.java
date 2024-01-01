package com.example.earthquakemobile;

import android.app.Application;

import com.example.earthquakemobile.service.Repository;

public class EarthquakeMobile extends Application {
    private Repository repo;
    @Override
    public void onCreate() {
        super.onCreate();
        repo = new Repository();
    }

    public Repository getRepository(){
        return repo;
    }
}
