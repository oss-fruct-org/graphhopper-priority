package org.fruct.oss.ghpriority.routing;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.routing.util.BikeFlagEncoder;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.FootFlagEncoder;
import com.graphhopper.util.PointList;

import org.fruct.oss.ghpriority.FootPriorityFlagEncoder;
import org.fruct.oss.ghpriority.point.Point;
import org.fruct.oss.ghpriority.utils.LatLng;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Routing {

    private CustomGraphHopper gh;
    private ObstaclesIndex obstaclesIndex;
    private boolean isReady;

    public synchronized void loadFromPref(String path) {
        if (gh != null) {
            gh.close();
            isReady = false;
        }

        gh = (CustomGraphHopper) new CustomGraphHopper();
        gh.setEncodingManager(new EncodingManager(new ArrayList<FlagEncoder>(4) {{
//            add(new CarFlagEncoder());
//            add(new BikeFlagEncoder());
            add(new FootFlagEncoder());
            add(new FootPriorityFlagEncoder());
        }}, 8));

        gh.setCHEnabled(false);

        if (path != null) {
            if (!gh.load(path)) {
//                System.out.println("error");
                gh.close();
                gh = null;
                System.out.println("Can't initialize graphhopper in " + path);
                throw new RuntimeException("Can't initialize graphhopper in " + path);
            }
//            System.out.println("ok");
            isReady = true;
        }
    }

    public synchronized boolean isReady() {
        return isReady;
    }

    public synchronized void close() {
        if (gh != null) {
            isReady = false;
            gh.close();
            gh = null;
        }

        if (obstaclesIndex != null) {
            obstaclesIndex.clear();
        }
    }

    public synchronized ChoicePath route(LatLng from, LatLng to, RoutingType routingType) {
        if (gh == null) {
            return null;
        }

        gh.setObstaclesIndex(obstaclesIndex);

        GHRequest request = new GHRequest(from.getLatitude(), from.getLongitude(),
                to.getLatitude(), to.getLongitude());
        request.setVehicle(routingType.getVehicle());
        request.setWeighting(routingType.getWeighting());

        try {
            GHResponse response = gh.route(request);

            if (response.hasErrors()) {
                for (Throwable throwable : response.getErrors()) {
                }
                return null;
            }

            PointList pointList = response.getBest().getPoints();

            if (pointList.size() < 2) {
                return null;
            }

            for (int i = 0; i < pointList.getSize() - 1; i++) {
                obstaclesIndex.clearchosenObstacles(pointList.getLat(i), pointList.getLon(i),
                        pointList.getLat(i + 1), pointList.getLon(i + 1),
                        BlockingWeighting.BLOCK_RADIUS);
            }
            Set<Point> pointsOnPath = new HashSet<Point>();
            for(double BlockRadius = 1.0 ; BlockRadius <= BlockingWeighting.BLOCK_RADIUS; BlockRadius = BlockRadius+1.0) {
                for (int i = 0; i < pointList.getSize() - 1; i++) {
                    pointsOnPath.addAll(obstaclesIndex.queryByEdge(
                            pointList.getLat(i), pointList.getLon(i),
                            pointList.getLat(i + 1), pointList.getLon(i + 1),
                            BlockRadius));
                }
            }


            return new ChoicePath(response, routingType,
                    pointsOnPath.toArray(new Point[pointsOnPath.size()]),
                    from, to);
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized void setObstacles(List<Point> points) {

        obstaclesIndex = new ObstaclesIndex(gh.getGraphHopperStorage());
        for (Point point : points) {
            obstaclesIndex.insertPoint(point);
        }
        obstaclesIndex.initialize();
    }
}
