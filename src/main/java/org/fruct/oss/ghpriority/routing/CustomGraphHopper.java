package org.fruct.oss.ghpriority.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.routing.util.HintsMap;

import org.fruct.oss.ghpriority.PriorityGraphHopper;


public class CustomGraphHopper extends PriorityGraphHopper {

    private ObstaclesIndex obstaclesIndex;

    public void setObstaclesIndex(ObstaclesIndex obstaclesIndex) {
        this.obstaclesIndex = obstaclesIndex;
    }

    @Override
    public Weighting createWeighting(HintsMap wMap, FlagEncoder encoder) {
        if (wMap.getWeighting().equalsIgnoreCase("half-blocking")) {
            return new BlockingWeighting(encoder, obstaclesIndex, true);
        } else if (wMap.getWeighting().equalsIgnoreCase("blocking")) {
            return new BlockingWeighting(encoder, obstaclesIndex, false);
        } else {
            return super.createWeighting(wMap, encoder);
        }
    }
}