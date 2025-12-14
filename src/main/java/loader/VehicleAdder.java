package loader;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import model.MyVehicle;
import util.ConnectionManager;

public class VehicleAdder {
    public static int vehCounter = 0;
    public static ConnectionManager conn;
    public VehicleAdder() {

    }
    public void addRandomVehicle() throws Exception {
        int randomRouteVariable = (int)(Math.random()*5);
        conn.dojobset(Vehicle.add("ourVehicle"+vehCounter,"DEFAULT_VEHTYPE", "route"+randomRouteVariable, (int)conn.dojobget(Simulation.getCurrentTime())+1, 0.0,15.0, (byte) 0));
        vehCounter++;
    }
}
