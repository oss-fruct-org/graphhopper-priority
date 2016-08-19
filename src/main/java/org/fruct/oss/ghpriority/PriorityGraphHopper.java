package org.fruct.oss.ghpriority;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.routing.util.HintsMap;

public class PriorityGraphHopper extends GraphHopper {
    @Override
    public Weighting createWeighting(HintsMap wMap, FlagEncoder encoder) {
        if (encoder instanceof FootPriorityFlagEncoder) {
            return new FootPriorityWeighting(encoder);
        }

        return super.createWeighting(wMap, encoder);
    }
}
