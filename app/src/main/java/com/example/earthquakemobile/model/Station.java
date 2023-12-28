package com.example.earthquakemobile.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "stations")
public class Station implements Serializable {
    public static Station parseJson(JSONObject object){
        if(object == null)
            return null;
        Station station = new Station();
        station.setNome(object.optString("cnome"));
        station.setComune(object.optString("ccomune"));
        station.setProvincia(object.optString("cprovincia"));
        station.setRegione(object.optString("cregione"));
        station.setAnno(object.optString("canno_inserimento"));
        station.setLongitudine(station.getLongOrLatFromJSON(object.optString("clongitudine",null)));
        station.setLatitudine(station.getLongOrLatFromJSON(object.optString("clatitudine",null)));
        return station;
    }
    @PrimaryKey
    private Integer id;
    private String comune;
    private String provincia;
    private String regione;
    private String nome;
    private String anno;
    private double latitudine;
    private double longitudine;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
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
        return "Station{" +
                "id=" + id +
                ", comune='" + comune + '\'' +
                ", provincia='" + provincia + '\'' +
                ", regione='" + regione + '\'' +
                ", nome='" + nome + '\'' +
                ", anno='" + anno + '\'' +
                ", latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                '}';
    }
}
