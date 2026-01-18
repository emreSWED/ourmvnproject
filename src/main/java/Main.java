import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.SumoColor;

import de.tudresden.sumo.objects.SumoStringList;
import loader.*;
import model.MyLane;
import model.MyTrafficLight;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConnectionManager;
import util.MySystem;
import util.TrafficDataExporter;

import model.MyVehicle;

import static util.ConnectionManager.*;

public class Main {

    static GUI.SumoTrafficControl gui;
    public static final Logger LOG = LogManager.getLogger(Main.class.getName());
    /**
     * Main entry point of the application.
     * * @param args Command line arguments (not used).
     * @throws Exception If the connection to SUMO fails or file access errors occur.
     */
    public static void main(String[] args) throws Exception {

        LOG.info("initializing connection to SUMO");
        ConnectionManager conn = new ConnectionManager("SumoConfig/myconfig.sumocfg");
        conn.startConnection();

//ALL LOADING FOR CLASSES, CONSTRUCTORS ETC. WHICH NEEDS TO HAPPEN ONCE HAPPPENS HERE
        RouteGenerator2.conn = conn;
        MyLane.conn = conn;
        VehicleAdder.conn = conn;
        SimulationWindowBounds simulationWindowBounds = new SimulationWindowBounds(conn);
        MySystem mySystem = new MySystem(conn.traciConnection);
        LaneLoader currentLanes = new LaneLoader(conn);
        RouteGenerator2.conn = conn;
        //RouteGenerator2 routeGenerator = new RouteGenerator2();
        VehicleAdder vehicleAdder = new VehicleAdder();
        YCoordinateFlipper yCoordinateFlipper = new YCoordinateFlipper();
        new TrafficLightSplitter();
        TrafficLightSplitter.conn = conn;
        TrafficLightSplitter.loadTrafficLights(mySystem);
        TrafficLightSplitter.splitTrafficLight();


        //List<MyTrafficLight> trafficLightsList = mySystem.getTrafficLights();
        //END OF LOADING BLOCK

       // System.out.println("List of Traffic Lights loaded: " + trafficLightsList.size());

        LOG.info("Starting application...");

       // System.out.println("Location of lane :254384053_11_0: " + conn.dojobget(Lane.getShape(":254384053_11_0")));

        javax.swing.SwingUtilities.invokeAndWait(() -> {
            try {
                gui = new GUI.SumoTrafficControl();
                gui.setTrafficLights(TrafficLightSplitter.trafficLightClusters);
                gui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        /**
         * The exporter used to save vehicle data into a CSV file.
         */
        TrafficDataExporter exporter = new TrafficDataExporter("simulation_results.csv");
        int step = 0;

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

            // Daten für GUI und Export holen
            List<MyVehicle> vehicles = mySystem.getVehicles();

            // Deine neue Klasse nutzen:
            exporter.logCurrentStep(step, vehicles);

            if (gui != null) {
                gui.refreshMap(mySystem.getVehicles());
            }


            //System.out.println("step number " + step + ". Number of vehicles in simulation: " + mySystem.getVehicles().size());
            //System.out.println("List of cars in simulation: " + mySystem.getVehicles());

            //List<MyVehicle> vehicles = mySystem.getVehicles();
            for (MyVehicle v : vehicles) {
                //if (step % 10 == 0) v.setColor(new SumoColor(255, 0, 0, 255));
                //if (step % 10 == 3) v.setColor(new SumoColor(0, 255, 0, 255));
                //if (step % 10 == 7) v.setColor(new SumoColor(0, 9, 255, 255));
                //System.out.println(v.getX() + ", " + v.getY() + ", " + v.getSpeed() + ", " + v.getId() + ", " + v.getColor());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
        // Wenn die Schleife endet
        exporter.closeExport();
        conn.stopConnection();

        //SOME TESTS
        conn.stopConnection();
        System.out.println("Connection closed.");
    }
}
