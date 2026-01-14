import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.SumoColor;

import loader.*;
import model.MyLane;
import model.MyTrafficLight;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConnectionManager;
import util.MySystem;


import model.MyVehicle;

public class Main {

    static GUI.SumoTrafficControl gui;
    private static final Logger LOG = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {

        LOG.info("initializing connection to SUMO");


        ConnectionManager conn = new ConnectionManager("SumoConfig/myconfig.sumocfg");
        conn.startConnection();
        RouteGenerator.conn = conn;
        MyLane.conn = conn;
        VehicleAdder.conn = conn;
        SimulationWindowBounds simulationWindowBounds = new SimulationWindowBounds(conn);

        MySystem mySystem = new MySystem(conn.traciConnection);
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
        /*EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SumoTrafficControl frame = new SumoTrafficControl();
                    frame.setTrafficLights(trafficLightsList);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        System.out.println("Location of lane :254384053_11_0: " + conn.dojobget(Lane.getShape(":254384053_11_0")));

        javax.swing.SwingUtilities.invokeAndWait(() -> {
            try {
                gui = new GUI.SumoTrafficControl();
                gui.setTrafficLights(trafficLightsList);
                gui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        /*javax.swing.SwingUtilities.invokeLater(() -> {
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
            if (!MySystem.stopped) conn.step();

            if (gui != null) {
                gui.refreshMap(mySystem.getVehicles());
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("step number " + step + ". Number of vehicles in simulation: " + mySystem.getVehicles().size());
            System.out.println("List of cars in simulation: " + mySystem.getVehicles());

            List<MyVehicle> vehicles = mySystem.getVehicles();
            for (MyVehicle v : vehicles) {
                if (step % 10 == 0) v.setColor(new SumoColor(255,0,0,255));
                if (step % 10 == 3) v.setColor(new SumoColor(0,255,0,255));
                if (step % 10 == 7) v.setColor(new SumoColor(0,9,255,255));
                System.out.println(v.getX() + ", " + v.getY() + ", " + v.getSpeed() + ", " + v.getId() + ", " + v.getColor());
            }

            TimeUnit.MILLISECONDS.sleep(100);
        }
        //SOME TESTS
        conn.stopConnection();
        System.out.println("Connection closed.");
    }
}
