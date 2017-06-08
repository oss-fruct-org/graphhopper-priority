package org.fruct.oss.ghpriority.routing;


import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;

import org.fruct.oss.ghpriority.FootPriorityWeighting;


public class BlockingWeighting extends FootPriorityWeighting {
    public static final double BLOCK_RADIUS = 10;

    private final boolean half;
    private final ObstaclesIndex obstaclesIndex;
    private final String encoder;

    public BlockingWeighting(FlagEncoder encoder,
                             ObstaclesIndex obstaclesIndex,
                             boolean half) {
        super(encoder);

        this.encoder = encoder.toString();

        this.half = half;
        this.obstaclesIndex = obstaclesIndex;
    }

    @Override
    public double calcWeight(EdgeIteratorState edge, boolean reverse, int prevOrNextEdgeId) {
        if (obstaclesIndex.checkEdgeBlocked(edge, BLOCK_RADIUS, half)) {
            return Double.POSITIVE_INFINITY;
        } else {
            return super.calcWeight(edge, reverse, prevOrNextEdgeId);
        }
    }

    @Override
    public String toString() {
        return "BLOCKING" + (half ? "-HALF!" : "!") + encoder;
    }
}