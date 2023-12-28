package com.example.earthquakemobile.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.earthquakemobile.Distributori;
import com.example.earthquakemobile.database.DB;
import com.example.earthquakemobile.model.Station;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private Repository repo;
    private MutableLiveData<List<Station>> stations = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.repo = ((Distributori)application).getRepository();
        new Thread(()->{
            List<Station> list = DB.getInstance(application).getStationDAO().getStations();
            if(list.isEmpty()){
                this.repo.downloadData(application,new Request.RequestCallback(){
                    @Override
                    public void onCompleted(UrlRequest request, UrlResponseInfo info, byte[] data, CronetException error) {
                        List<Station> temp = new ArrayList<Station>();
                        if(data != null) {
                            String response = new String(data);
                            try{
                                JSONArray array = new JSONArray(response);
                                for(int i = 0 ; i < array.length(); i++){
                                    JSONObject item = array.optJSONObject(i);
                                    Station stat = Station.parseJson(item);
                                    if(stat != null && stat.getNome().length() != 0){
                                        temp.add(stat);
                                    }

                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        DB.getInstance(getApplication()).getStationDAO().insert(temp);
                        stations.postValue(temp);
                    }
                });
            }else{
                stations.postValue(list);
            }
        }).start();

    }

    public LiveData<List<Station>> getStations(){
        return stations;
    }


}
