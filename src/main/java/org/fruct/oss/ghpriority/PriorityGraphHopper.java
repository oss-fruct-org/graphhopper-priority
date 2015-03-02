package org.fruct.oss.ghpriority;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.routing.util.WeightingMap;

public class PriorityGraphHopper extends GraphHopper {
    @Override
    public Weighting createWeighting(WeightingMap wMap, FlagEncoder encoder) {
        if (encoder instanceof FootPriorityFlagEncoder) {
            return new FootPriorityWeighting(encoder);
        }

        return super.createWeighting(wMap, encoder);
    }
}
