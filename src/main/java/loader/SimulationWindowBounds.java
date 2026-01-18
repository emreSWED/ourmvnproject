package loader;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.objects.SumoGeometry;
import util.ConnectionManager;

import static loader.YCoordinateFlipper.*;

public class SimulationWindowBounds {
    public static double xCoordinateBound;
    public static double yCoordinateBound;
    SumoGeometry bounds;

    public SimulationWindowBounds(ConnectionManager conn) throws Exception {
        bounds = (SumoGeometry) conn.dojobget(Simulation.getNetBoundary());
        xCoordinateBound = bounds.coords.get(1).x; //as lower bound always starts at 0,0; we only need to get the upper bounds
        yCoordinateBound = bounds.coords.get(1).y;
        yBound1 = yCoordinateBound; //Loads the static variable in YCoordinateFlipper Class
    }
}
