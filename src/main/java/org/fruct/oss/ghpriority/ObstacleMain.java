package org.fruct.oss.ghpriority;

import com.graphhopper.routing.util.BikeFlagEncoder;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.FootFlagEncoder;
import com.graphhopper.util.CmdArgs;

import java.util.ArrayList;

public class ObstacleMain {
    public static final void main(String[] args) {
        CmdArgs cmdArgs = CmdArgs.read(args);
        PriorityGraphHopper hopper = new PriorityGraphHopper();
		hopper.setEncodingManager(new EncodingManager(new ArrayList<FlagEncoder>(2) {{
			add(new FootFlagEncoder());
			add(new FootPriorityFlagEncoder());
		}}, 8));

        hopper.init(cmdArgs);
        try {
            hopper.importOrLoad();
        } catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        hopper.close();

        /*CmdArgs cmdArgs = CmdArgs.read(
                new String[]{
                        "config=/tmp/gh/config.properties",
                        "graph.location=/tmp/gh/graph",
                        "osmreader.osm=/tmp/gh/karelia.osm.pbf"});
        graphHopper.init(cmdArgs);
        graphHopper.importOrLoad();*/
    }
}
