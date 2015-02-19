package org.fruct.oss.ghpriority;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.*;
import com.graphhopper.util.CmdArgs;

public class Main {
    public static final void main(String[] args) {
        CmdArgs cmdArgs = CmdArgs.read(args);
        PriorityGraphHopper hopper = new PriorityGraphHopper();
        hopper.init(cmdArgs);
        hopper.importOrLoad();
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
