package loader;
import GUI.SumoTrafficControl;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import model.MyVehicle;
import util.ConnectionManager;

import static loader.RouteGenerator2.routeidcounter;
import java.awt.*;


public class VehicleAdder {
    public static int vehCounter = 0;
    public static ConnectionManager conn;
    public VehicleAdder() {
    }

    public static void addRandomVehicle() throws Exception {
        //int randomRouteVariable = (int)(Math.random()*5);
        RouteGenerator2 routeGenerator = new RouteGenerator2();
        conn.dojobset(Vehicle.add("ourVehicle"+vehCounter,"DEFAULT_VEHTYPE", "route"+(routeidcounter-1), (int)conn.dojobget(Simulation.getCurrentTime())+1, 0.0,15.0, (byte) 0));
        vehCounter++;
    }

    public static int getVehCounter(){ return vehCounter;}

}
