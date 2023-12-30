package com.example.earthquakemobile.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Earthquake implements Serializable {

    /*  {
          "type": "Feature",
          "properties": {
OK          "mag": 3.5,
OK          "place": "5 km SSE of Okrug Gornji, Croatia",
OK          "time": 1703703368709,
            "updated": 1703758898674,
            "tz": null,
            "url": "https://earthquake.usgs.gov/earthquakes/eventpage/us6000m01b",
            "detail": "https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us6000m01b&format=geojson",
            "felt": 10,
            "cdi": 5.8,
            "mmi": null,
            "alert": null,
            "status": "reviewed",
            "tsunami": 0,
            "sig": 194,
            "net": "us",
            "code": "6000m01b",
            "ids": "us6000m01b",
            "sources": "us",
            "types": ",dyfi,origin,phase-data,",
            "nst": 24,
            "dmin": 1.452,
            "rms": 0.47,
            "gap": 39,
            "magType": "ml",
            "type": "earthquake",
OK          "title": "M 3.5 - 5 km SSE of Okrug Gornji, Croatia"
          },
          "geometry": {
            "type": "Point",
OK          "coordinates": [16.2809, 43.4502, 10]  (Long, Lat, Alt) So invertiti
          },
          "id": "us6000m01b"
        }
      ]
    }
    */

    public static Earthquake parseJson(JSONObject object){
        if(object == null) return null;
        Earthquake earthquake = new Earthquake();

        JSONObject JSONproperties = object.optJSONObject("properties");
        JSONObject JSONgeometry =  object.optJSONObject("geometry");

        earthquake.setTitle(JSONproperties.optString("title"));
        earthquake.setPlace(JSONproperties.optString("place"));
        earthquake.setMagnitude(Float.valueOf(JSONproperties.optString("mag")));
        earthquake.setDate(new Date(Long.parseLong(JSONproperties.optString("time"))));
        earthquake.setLongitudine(earthquake.getLongOrLatFromJSON(JSONgeometry
                .optJSONArray("coordinates")
                .optString(0)));
        earthquake.setLatitudine(earthquake.getLongOrLatFromJSON(JSONgeometry
                .optJSONArray("coordinates")
                .optString(1)));
        if (earthquake.place.equals("null")) earthquake.setPlace("Unknown place");
        return earthquake;
    }

    private double getLongOrLatFromJSON(String value){
        if(value == null)
            return 0;
        try{
            return Double.parseDouble(value);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", magnitude=" + magnitude +
                ", place='" + place + '\'' +
                ", date=" + date +
                ", latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                '}';
    }

    private Integer id;
    private String title;
    private Float magnitude;
    private String place;
    private Date date;
    private Double latitudine;
    private Double longitudine;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Float magnitude) {
        this.magnitude = magnitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }
}
