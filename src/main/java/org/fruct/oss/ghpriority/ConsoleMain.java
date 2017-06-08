package org.fruct.oss.ghpriority;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.*;


import com.graphhopper.util.PointList;
import org.fruct.oss.ghpriority.FootPriorityFlagEncoder;
import org.fruct.oss.ghpriority.PriorityGraphHopper;
import org.fruct.oss.ghpriority.point.Point;
import org.fruct.oss.ghpriority.routing.ChoicePath;
import org.fruct.oss.ghpriority.routing.Routing;
import org.fruct.oss.ghpriority.routing.RoutingType;
import org.fruct.oss.ghpriority.utils.LatLng;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ConsoleMain {
    public static final void main(String[] args) {
        final RoutingType[] REQUIRED_ROUTING_TYPES = {
                RoutingType.SAFE,
                RoutingType.NORMAL,
                RoutingType.FASTEST};
        Routing routing;
        double toLat, toLng, fromLat, fromLng;
        int disabilityType;
        int[] currentDisibility = {};

        if (args == null || args.length != 7) {
            System.out.println("It has to be 7 args: \n" +
                    "1. lat from \n" +
                    "2. lng from \n" +
                    "3. lat to \n" +
                    "4. lng to \n" +
                    "5. disability id \n" +
                    "6. path to temp file with points \n" +
                    "7. path to GH files \n");
            return;
        }
        try {
            fromLat = Double.parseDouble(args[0]);
            fromLng = Double.parseDouble(args[1]);
            toLat = Double.parseDouble(args[2]);
            toLng = Double.parseDouble(args[3]);
            disabilityType = Integer.parseInt(args[4]);

        }
        catch (Exception e) {
            System.out.println(e.toString());
            return;
        }

        String tmpPointsFile = args[5]; // "points.json"
        String graphHopperFolder = args[6]; // "gh"

        int[] deaf ={25,26};
        int[] blind ={23,24,25,26,29,42,41};
        int[] muscle ={23,24,27,28,29};
        int[] mental ={25,26};
        int[] wheelchair ={23,24,27,28,42,29};
        switch (disabilityType) {
            case 1:
                currentDisibility = wheelchair;
                break;
            case 2:
                currentDisibility = muscle;
                break;
            case 3:
                currentDisibility = blind;
                break;
            case 4:
                currentDisibility = deaf;
                break;
            case 5:
                currentDisibility = mental;
                break;
        }
        LatLng fromPoint = new LatLng(fromLat,fromLng);
        LatLng toPoint = new LatLng(toLat,toLng);
        routing = new Routing();
        Gson gson = new Gson();

        try {
            ArrayList<Point> uncheckedObstacles = gson.fromJson(new FileReader(tmpPointsFile), new TypeToken<ArrayList<Point>>(){}.getType());
            ArrayList<Point> obstacles = new ArrayList<Point>();
            if (uncheckedObstacles != null) {
            for(Point p: uncheckedObstacles) {
                for(int category: currentDisibility) {
                    if (category == p.getCategory_id()) {
                        obstacles.add(p);
                        break;
                    }
                }
            }
            }
            routing.loadFromPref(graphHopperFolder);
            routing.setObstacles(obstacles);
            String jsonPath = "[";
            for (final RoutingType routingType : REQUIRED_ROUTING_TYPES) {
                ChoicePath path = routing.route(fromPoint, toPoint, routingType);

                String currentType = null;
                if(routingType == routingType.FASTEST)
                    currentType = "fastest";
                if(routingType == routingType.NORMAL)
                    currentType = "normal";
                if(routingType == routingType.SAFE)
                    currentType = "safe";
                if (path != null) {
                    jsonPath += "{\"type\":\"" + currentType + "\",";
                    GHResponse ghResp = path.getResponse();
                    jsonPath += "\"distance\":" + ghResp.getBest().getDistance() + ",";
                    jsonPath += "\"weight\":" + ghResp.getBest().getRouteWeight() + ",";
                    jsonPath += "\"routePoints\": [";
                    jsonPath += "{\"lat\":" + fromLat + ",\"lng\":" + fromLng + "},";
                    PointList route = ghResp.getBest().getPoints();
                    for(int i = 0; i < route.size(); i++)
                    {
                        jsonPath += "{\"lat\":" + route.getLat(i) + ",\"lng\":" + route.getLon(i) + "},";
                    }
                    jsonPath += "{\"lat\":" + toLat + ",\"lng\":" + toLng + "}";
                    jsonPath += "], \"obstacles\":  [";
                    Point[] curpoints = path.getPoints();
                    for(Point a: curpoints)
                    {
                        jsonPath += "{\"uuid\":\"" + a.getUuid() + "\"},";
                    }
                    if(curpoints.length != 0)
                        jsonPath = jsonPath.substring(0,jsonPath.length() - 1);
                    jsonPath += "]},";
                }
            }
            if(jsonPath.length() == 1)
                jsonPath += "{\"type\":\"none\"}";
            else
                jsonPath = jsonPath.substring(0,jsonPath.length() - 1);
            jsonPath += "]";
            System.out.println(jsonPath);
            routing.close();
        }
        catch (Exception e) {
            System.out.print(e.toString());
            e.printStackTrace();
        }
    }


}
