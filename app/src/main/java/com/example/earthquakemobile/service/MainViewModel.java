package com.example.earthquakemobile.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.earthquakemobile.EarthquakeMobile;
import com.example.earthquakemobile.database.DB;
import com.example.earthquakemobile.model.Earthquake;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private Repository repo;
    private MutableLiveData<List<Earthquake>> earthquakes = new MutableLiveData<>();
    //private MutableLiveData<List<Station>> stations = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.repo = ((EarthquakeMobile)application).getRepository();
        new Thread(()->{
            List<Earthquake> list =  DB.getInstance(application).getEarthquakeDAO().getEarthquakes();
            if(list.isEmpty()){
                refreshData();
            }else{
                earthquakes.postValue(list);
            }
        }).start();

    }

    public void refreshData() {


        repo.downloadData(getApplication(), new Request.RequestCallback() {
            @Override
            public void onCompleted(UrlRequest request, UrlResponseInfo info, byte[] data, CronetException error) {
                List<Earthquake> temp = new ArrayList<>();
                if (data != null) {
                    String response = new String(data);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.optJSONArray("features");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.optJSONObject(i);
                            Earthquake heqk = Earthquake.parseJson(item);
                            if (heqk != null && heqk.getTitle().length() != 0) {
                                temp.add(heqk);
                            }
                        }
                        temp.sort((Earthquake earthquake1, Earthquake earthquake2) ->
                                earthquake2.getDate().compareTo(earthquake1.getDate()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                DB.getInstance(getApplication()).getEarthquakeDAO().insert(temp);
                earthquakes.postValue(temp);
            }
        });
    }


    public LiveData<List<Earthquake>> getEarthquakes(){
        return earthquakes;
    }

    public List<Earthquake> filterEarthquakesByLocation(String location){
        List<Earthquake> filteredEarthquakes = new ArrayList<Earthquake>();
        for(Earthquake eq : earthquakes.getValue()){
            String earthquakePlaceLower = eq.getPlace().toLowerCase();
            String earthquakeCountry = eq.getCountry().toLowerCase();
            String earthquakeState = eq.getState().toLowerCase();
            if(earthquakePlaceLower.contains(location) || earthquakeCountry.contains(location) || earthquakeState.contains(location))
                filteredEarthquakes.add(eq);
        }
        return filteredEarthquakes;
    }
}
