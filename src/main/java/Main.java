import GUI.PrincipalComp;
import GUI.SumoTrafficControl;
import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoLink;
import de.tudresden.sumo.objects.SumoLinkList;
import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.util.*;
import it.polito.appeal.traci.*;

import loader.*;
import model.MyTrafficLight;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConnectionManager;
import util.MySystem;


import model.MyVehicle;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        LOG.info("initializing connection to SUMO");
        ConnectionManager conn = new ConnectionManager("SumoConfig/myconfig.sumocfg");
        conn.startConnection();
        RouteGenerator.conn = conn;
        MyLane.conn = conn;
        VehicleAdder.conn = conn;
        SimulationWindowBounds simulationWindowBounds = new SimulationWindowBounds(conn);

        MySystem mySystem = new MySystem(conn);
        LaneLoader currentLanes = new LaneLoader(conn);

        RouteGenerator routeGenerator = new RouteGenerator();

        VehicleAdder vehicleAdder = new VehicleAdder();
        YCoordinateFlipper yCoordinateFlipper = new YCoordinateFlipper();

        //get map data for UI
       // List<String> trafficLights = conn.getTrafficLights();
       // List<String> lanes = (List<String>) conn.dojobget(Lane.getIDList());
       // JunctionLoader junctions = new JunctionLoader(conn.traciConnection);

        //github test 17:32
      // System.out.println("current number of traffic lights: " + trafficLights.size());
      //  System.out.println("ID of Traffic lights:"+ conn.getTrafficLights());
       // System.out.println("ID of first Lane:"+ lanes.getFirst());
       // System.out.println(conn.dojobget(Lane.getShape(lanes.getFirst())));
      //  System.out.println("Positions of Junctions:" + junctions.JunctionPositionList);
       // System.out.println("Links:"+ conn.dojobget(Lane.getLinks(lanes.getFirst())));
       // SumoLinkList links = new SumoLinkList();
       // links = (SumoLinkList) conn.dojobget(Lane.getLinks(lanes.getFirst()));
       // System.out.println(" First list of links for first Lane:"+ links);
       // trafficLightLanes  = conn.dojobget(Trafficlight.getControlledJunctions(trafficLights.getFirst()));
       // System.out.println("Controlled links by traffic light 1: " + trafficLightLinks);
        //System.out.println("First link :"+ trafficLightLinks.getFirst());

        int numberOfTrafficLights = (int) conn.dojobget(Trafficlight.getIDCount());
        System.out.println("Number of Traffic Lights: " + numberOfTrafficLights);

        // Hier holen wir die Liste für die GUI
        List<MyTrafficLight> trafficLightsList = mySystem.getTrafficLights();
        System.out.println("List of Traffic Lights loaded: " + trafficLightsList.size());

        LOG.info("Starting application...");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SumoTrafficControl frame = new SumoTrafficControl();
                    frame.setTrafficLights(trafficLightsList);
                    frame.setVisible(true);
                } catch (Exception e) {
                    LOG.warn("Handled exception in main.");
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Location of lane :254384053_11_0: " + conn.dojobget(Lane.getShape(":254384053_11_0")));

        javax.swing.SwingUtilities.invokeLater(() -> {
            new SumoTrafficControl();
        });


       /* javax.swing.SwingUtilities.invokeLater(() -> {
            new PrincipalComp();
        });*/
        //da doppelt oben
        //int numberOfTrafficLights = (int)conn.dojobget(Trafficlight.getIDCount());
        System.out.println("Number of Traffic Lights: " + numberOfTrafficLights);

        List<MyTrafficLight> trafficLights = mySystem.getTrafficLights();
        System.out.println("List of Traffic Lights: " + trafficLights);

        System.out.println("Location of lane :254384053_11_0: " + conn.dojobget(Lane.getShape(":254384053_11_0")));
        //Last coordinates on Lanes "going from" are those where traffic lights should be placed. In this case: 85.42, 107.99 since
        //its the first in List of controlled links.

        LaneLoader.printAllLaneIDs();

        for (int step = 0; step < 10000; step++) {
            conn.step();

            System.out.println("step number " + step + ". Number of vehicles in simulation: " + mySystem.getVehicles().size());
            System.out.println("List of cars in simulation: " + mySystem.getVehicles());

            if (step == 20) {
                MyTrafficLight t1 = new MyTrafficLight("254384053", conn.traciConnection);
                //t1.setPhase();
            }

            List<MyVehicle> vehicles = mySystem.getVehicles();
            for (MyVehicle v : vehicles) {
                v.setColor(v.getId(), new SumoColor(0xFF,0xFF,0xFF,0x00));
                v.setSpeed(1.0);
                System.out.println(v.getX() + ", " + v.getY() + ", " + v.getSpeed() + ", " + v.getId());
            }

            for (MyTrafficLight t : trafficLights) {
                System.out.println("ID: " + t.getId());
                System.out.println("ControlledJunctions: " + t.getControlledJunctions());
                System.out.println("ControlledLanes: " + t.getControlledLanes());
                System.out.println("ControlledLinks: " + t.getControlledLinks());
                System.out.println("State: " + t.getState());
            }

            // Status der Ampeln prüfen (optional)
            for (MyTrafficLight t : trafficLightsList) {
                System.out.println("ID: " + t.getId() + " State: " + t.getState());
            }

            TimeUnit.MILLISECONDS.sleep(100);
        }
        //SOME TESTS
        conn.stopConnection();
        System.out.println("Connection closed.");
    }
}
