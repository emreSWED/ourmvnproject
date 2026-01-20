import GUI.SumoTrafficControl;
import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.SumoColor;

import loader.*;
import model.MyLane;
import model.MyTrafficLight;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConnectionManager;
import util.MySystem;


import model.MyVehicle;
import util.TrafficDataExporter;

public class Main {


    static GUI.SumoTrafficControl gui;
    private static final Logger LOG = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {

        LOG.info("initializing connection to SUMO");


        ConnectionManager conn = new ConnectionManager("SumoConfig/bigmap/osm.sumocfg");
        conn.startConnection();
        RouteGenerator2.conn = conn;
        MyLane.conn = conn;
        VehicleAdder.conn = conn;
        SimulationWindowBounds simulationWindowBounds = new SimulationWindowBounds(conn);

        MySystem mySystem = new MySystem(conn.traciConnection);
        LaneLoader currentLanes = new LaneLoader(conn);

        VehicleAdder vehicleAdder = new VehicleAdder();
        YCoordinateFlipper yCoordinateFlipper = new YCoordinateFlipper();
        new TrafficLightSplitter();
        TrafficLightSplitter.conn = conn;
        TrafficLightSplitter.loadTrafficLights(mySystem);
        TrafficLightSplitter.splitTrafficLight();


        List<MyTrafficLight> trafficLightsList = mySystem.getTrafficLights();
       // System.out.println("List of Traffic Lights loaded: " + trafficLightsList.size());

        LOG.info("Starting application...");

        //System.out.println("Location of lane :254384053_11_0: " + conn.dojobget(Lane.getShape(":254384053_11_0")));

        javax.swing.SwingUtilities.invokeAndWait(() -> {
            try {
                gui = new GUI.SumoTrafficControl();
                gui.setTrafficLights(trafficLightsList);
                gui.setVisible(true);
            } catch (Exception e) {
                LOG.error("Failed starting the GUI",e);
            }
        });
        /**
        * The exporter used to save vehicle data into a CSV file.
        */
        TrafficDataExporter exporter = new TrafficDataExporter();
        int step = 0;

        MySystem.stopped = true;

        while (MySystem.running) {

            synchronized (MySystem.stepLock) {
                while (MySystem.stopped) {
                    try {
                        MySystem.stepLock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            conn.step();
            step++;

            List<MyTrafficLight> currentLights = mySystem.getTrafficLights();
            // Daten für GUI und Export holen
            List<MyVehicle> vehicles = mySystem.getVehicles();


            exporter.logCurrentStep(step, vehicles);

            if (gui != null) {
                gui.refreshMap(mySystem.getVehicles(), currentLights);
            }

            System.out.println("step number " + step + ". Number of vehicles in simulation: " + mySystem.getVehicles().size());
            System.out.println("List of cars in simulation: " + mySystem.getVehicles());

            //newly added to show total number of vehicles in simulation in a Label
            SumoTrafficControl.setInfoCountVehText(SumoTrafficControl.getStringVehiclesCount(mySystem.getVehicles().size()));



            for (MyVehicle v : vehicles) {
                System.out.println(v.getX() + ", " + v.getY() + ", " + v.getSpeed() + ", " + v.getId() + ", " + v.getColor());
            }

            try {
                Thread.sleep(1000 / MySystem.ticksPerSecond);
            } catch (InterruptedException e) {
                return;
            }
        }

        //SOME TESTS
        conn.stopConnection();
        //System.out.println("Connection closed."); added als Log in Method

    }
}
