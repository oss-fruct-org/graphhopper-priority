package org.fruct.oss.ghpriority.routing;

/**
 * Created by artyo on 27.04.2016.
 */
public enum RoutingType {
    SAFE("blocking", "pfoot", 1),
    NORMAL("half-blocking", "pfoot", 2),
    FASTEST("fastest", "pfoot", 3);

    private final int stringId;
    private final String weighting;
    private final String vehicle;

    private RoutingType(String weighting, String vehicle, int stringId) {
        this.weighting = weighting;
        this.vehicle = vehicle;
        this.stringId = stringId;
    }

    public String getWeighting() {
        return weighting;
    }

    public String getVehicle() {
        return vehicle;
    }

    public int getStringId() {
        return stringId;
    }
}