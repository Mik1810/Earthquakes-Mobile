package com.example.earthquakemobile.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.earthquakemobile.Distributori;
import com.example.earthquakemobile.database.DB;
import com.example.earthquakemobile.model.Earthquake;
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
    private MutableLiveData<List<Earthquake>> earthquakes = new MutableLiveData<>();
    //private MutableLiveData<List<Station>> stations = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.repo = ((Distributori)application).getRepository();
        new Thread(()->{
            List<Earthquake> list =  DB.getInstance(application).getEarthquakeDAO().getEarthquakes();
            //TODO: Salvare i terremoti nel database non ha senso perchè se uno volesse vedere quelli
            // più recenti non potrebbe, si potrebbe implementare un metodo che fa comunque la fetch
            // all'API per scaricare unicamente i terremoti più recenti
            if(list.isEmpty()){
                this.repo.downloadData(application,new Request.RequestCallback(){
                    @Override
                    public void onCompleted(UrlRequest request, UrlResponseInfo info, byte[] data, CronetException error) {
                        List<Earthquake> temp = new ArrayList<Earthquake>();
                        if(data != null) {
                            String response = new String(data);
                            try{
                                JSONObject object = new JSONObject(response);
                                JSONArray array = object.optJSONArray("features");
                                for(int i = 0 ; i < array.length(); i++){
                                    JSONObject item = array.optJSONObject(i);
                                    Earthquake heqk = Earthquake.parseJson(item);
                                    if(heqk != null && heqk.getTitle().length() != 0){
                                        temp.add(heqk);
                                    }

                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        DB.getInstance(getApplication()).getEarthquakeDAO().insert(temp);
                        earthquakes.postValue(temp);
                    }
                });
            }else{
                earthquakes.postValue(list);
            }
        }).start();

    }

    public LiveData<List<Earthquake>> getEarthquakes(){
        return earthquakes;
    }


}
