package com.example.earthquakemobile.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "earthquakes")
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

        /* "5 km SSE of Okrug Gornji, Croatia"
            State: Croatia,
            Country: Okrug Gornji,
            Place: 5 km SSE of Okrug Gornji

            "Sicily, Italy"
            State: Italy
            Place: Sicily

            "Thyrrenean Sea"
            Place: Thyrrenean Sea

            "null"
            Place: Unknown Place
         */

        String[] placeAndState = JSONproperties.optString("place").split(",");
        String state = placeAndState.length > 1 ? placeAndState[1] : "";
        String[] placeAndCountry = placeAndState[0].split("of");
        String country = placeAndCountry.length > 1 ? placeAndCountry[1] : "";
        earthquake.setState(state.trim());
        earthquake.setCountry(country.trim());
        earthquake.setPlace(placeAndState[0].trim());
        earthquake.setTitle(JSONproperties.optString("title"));
        earthquake.setMagnitude(Float.valueOf(JSONproperties.optString("mag")));
        earthquake.setDate(new Date(Long.parseLong(JSONproperties.optString("time"))));
        // Get the coordinates
        JSONArray coordinates = JSONgeometry.optJSONArray("coordinates");
        earthquake.setLongitudine(Double.parseDouble(coordinates.optString(0)));
        earthquake.setLatitudine(Double.parseDouble(coordinates.optString(1)));
        earthquake.setDepth(Double.parseDouble(coordinates.optString(2)));
        if(earthquake.getMagnitude() > 4) {
            earthquake.setDanger(Danger.RED);
        } else if (earthquake.getMagnitude()<=4 && earthquake.getMagnitude()>2.5) {
            earthquake.setDanger(Danger.ORANGE);
        } else {
            earthquake.setDanger(Danger.YELLOW);
        }
        if (earthquake.place.equals("null")) earthquake.setPlace("Unknown place");
        return earthquake;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", magnitude=" + magnitude +
                ", state='" + state + '\'' +
                ", place='" + place + '\'' +
                ", date=" + date +
                ", latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                '}';
    }

    public String toLocationString() {
        if (country.length() > 0 && state.length()>0)
            return country + " ," + state;
        if (state.length() > 0)
            return place + " ," + state;
        return place;
    }

    @PrimaryKey
    private Integer id;
    private String title;
    private Float magnitude;
    private String place;
    private String state;
    private String country;
    private Date date;
    private Double latitudine;
    private Double longitudine;
    private Double depth;

    private Danger danger;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Danger getDanger() { return danger; }

    public void setDanger(Danger danger) { this.danger = danger; }

    public enum Danger {
        RED,
        ORANGE,
        YELLOW
    }
}
