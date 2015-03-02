package org.fruct.oss.ghpriority;

import com.graphhopper.routing.util.FastestWeighting;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;

public class FootPriorityWeighting extends FastestWeighting {
    private final FootPriorityFlagEncoder priorityEncoder;
    private String encoder;

    public FootPriorityWeighting(FlagEncoder encoder) {
        super(encoder);
        this.encoder = encoder.toString();

        if (!(encoder instanceof FootPriorityFlagEncoder)) {
            throw new IllegalArgumentException(getClass().getName() + " can work only with " + FootPriorityFlagEncoder.class.getName());
        }

        this.priorityEncoder = (FootPriorityFlagEncoder) encoder;
    }

    @Override
    public double calcWeight( EdgeIteratorState edge, boolean reverse, int prevOrNextEdgeId ) {
        double weight = super.calcWeight(edge, reverse, prevOrNextEdgeId);

        if (Double.isInfinite(weight))
            return Double.POSITIVE_INFINITY;
        FootPriorityFlagEncoder.Priority priority = priorityEncoder.getPriority(edge.getFlags());

        return weight * priority.getRate();
    }

    @Override
    public String toString() {
        return "PRIORITY|" + encoder;
    }
}