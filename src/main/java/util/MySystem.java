package util;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import it.polito.appeal.traci.SumoTraciConnection;

import model.MyTrafficLight;
import model.MyVehicle;

import java.util.ArrayList;
import java.util.List;

public class MySystem {
    public SumoTraciConnection traciConnection;

    public MySystem(SumoTraciConnection connection) {
        this.traciConnection = connection;
    }

    public List<MyVehicle> getVehicles() throws Exception {
        List<String> vehicles = (List<String>) traciConnection.do_job_get(Vehicle.getIDList());
        List<MyVehicle> myVehicles = new ArrayList<>();
        for (String vehicleID : vehicles) {
            MyVehicle v = new MyVehicle(vehicleID, this.traciConnection);
            myVehicles.add(v);
        }
        return myVehicles;
    }

    public List<MyTrafficLight> getTrafficLights() throws Exception {
        List<String> trafficLights = (List<String>) traciConnection.do_job_get(Trafficlight.getIDList());
        List<MyTrafficLight> mytrafficLights = new ArrayList<>();
        for (String trafficLightID : trafficLights) {
            MyTrafficLight t = new MyTrafficLight(trafficLightID, this.traciConnection);
            mytrafficLights.add(t);
        }
        return mytrafficLights;
    }
}
