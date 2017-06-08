package org.fruct.oss.ghpriority.routing;

import com.graphhopper.GHResponse;

import org.fruct.oss.ghpriority.utils.LatLng;
import org.fruct.oss.ghpriority.point.Point;

public class ChoicePath {
    private final GHResponse response;
    private final Point[] points;
    private final RoutingType routingType;

    private final LatLng sourceLocation;
    private final LatLng destinationLocation;

    public ChoicePath(GHResponse response, RoutingType routingType, Point[] points,
                      LatLng sourceLocation, LatLng destinationLocation) {
        this.response = response;
        this.routingType = routingType;
        this.points = points;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
    }

    public GHResponse getResponse() {
        return response;
    }

    public double getDistance() {
        return response.getBest().getDistance();
    }

    public RoutingType getRoutingType() {
        return routingType;
    }

    public Point[] getPoints() {
        return points;
    }

    public LatLng getSourceLocation() {
        return sourceLocation;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }
}
