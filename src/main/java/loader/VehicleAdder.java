package loader;
import GUI.SumoTrafficControl;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.TraCIException;
import model.MyVehicle;
import util.ConnectionManager;
import util.MySystem;

import java.awt.*;

import static loader.RouteGenerator2.routeidcounter;
import java.awt.*;


public class VehicleAdder {
    public static int vehCounter = 0;
    public static ConnectionManager conn;
    public VehicleAdder() {
    }

    /**
     * adds a random vehicle into the simulation. Generates the route with the RouteGenerator2 class, which instanciates an object inside this method
     * @throws Exception
     */
    public static void addRandomVehicle() throws Exception {
        RouteGenerator2 routeGenerator = new RouteGenerator2();
        SumoColor sumoColor = new SumoColor();
        sumoColor.a = (byte)255;
        sumoColor.r = (byte)((int)(Math.random()*254+1));
        sumoColor.g = (byte)((int)(Math.random()*254+1));
        sumoColor.b = (byte)((int)(Math.random()*254+1));
        try {
            conn.dojobset(Vehicle.add("ourVehicle"+vehCounter,"DEFAULT_VEHTYPE", "route"+(routeidcounter-1), (int)conn.dojobget(Simulation.getCurrentTime())+1, 0.0,15.0, (byte) 0));
            //conn.dojobset(Vehicle.setColor("ourVehicle"+vehCounter, sumoColor));
        } catch (TraCIException ex) {
            addRandomVehicle();
        }
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
