package org.fruct.oss.ghpriority.utils;

/**
 * Created by artyo on 27.04.2016.
 */
public class LatLng {
    private double Lat;
    private double Lng;

    public LatLng(double lat, double lng) {
        this.Lat = lat;
        this.Lng = lng;
    }
    public double getLatitude() {
        return this.Lat;
    }
    public double getLongitude() {
        return this.Lng;
    }
}
