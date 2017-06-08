package org.fruct.oss.ghpriority.point;

/**
 * Created by artyo on 28.04.2016.
 */
public class Point {
    private double lat;
    private double lng;
    private double difficulty;
    private String uuid;
    private int category_id;


    public double getLat() {
        return this.lat;
    }
    public  double getLon() {
        return this.lng;
    }
    public double getDifficulty() {
        return this.difficulty;
    }
    public String getUuid(){return this.uuid;}
    public int getCategory_id() {return this.category_id;}

    public void setLat(double newLat){
        this.lat = newLat;
    }

    public void setLng(double newLng){
        this.lng = newLng;
    }

    public void setDifficulty(double newDif){
        this.difficulty = newDif;
    }

    public void setUuid(String newUuid){
        this.uuid = newUuid;
    }

    public void setCategory_id(int newCategory_id){
        this.category_id = newCategory_id;
    }
}
