package loader;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import model.MyVehicle;
import util.ConnectionManager;
import util.MySystem;

import java.awt.*;

import static loader.RouteGenerator2.routeidcounter;


public class VehicleAdder {
    public static int vehCounter = 0;
    public static ConnectionManager conn;
    public VehicleAdder() {
    }
    public static void addRandomVehicle() throws Exception {
        RouteGenerator2 routeGenerator = new RouteGenerator2();
        conn.dojobset(Vehicle.add("ourVehicle"+vehCounter,"DEFAULT_VEHTYPE", "route"+(routeidcounter-1), (int)conn.dojobget(Simulation.getCurrentTime())+1, 0.0,15.0, (byte) 0));
        vehCounter++;
    }

    public static void addVehicle(Color color) throws Exception {
        RouteGenerator2 routeGenerator = new RouteGenerator2();
        String id = "ourVehicle" + vehCounter;
        conn.dojobset(Vehicle.add(id,"DEFAULT_VEHTYPE", "route"+(routeidcounter-1), (int)conn.dojobget(Simulation.getCurrentTime())+1, 0.0,15.0, (byte) 0));
        MyVehicle myVehicle = new MyVehicle(id, ConnectionManager.traciConnection);
        myVehicle.setColor(color);
        vehCounter++;
    }
}
