package loader;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoLink;
import de.tudresden.sumo.objects.SumoLinkList;
import de.tudresden.sumo.objects.SumoStringList;
import model.MyTrafficLight;
import util.MySystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Signature;
import java.util.ArrayList;
import java.util.List;



public class TrafficLightSplitter {
    public static final Logger LOG = LogManager.getLogger(TrafficLightSplitter.class.getName().toString());
    /**
     * A List of our MyTrafficLight, which is our Wrapper Class for SUMOs Traffic Light Clusters
     */
    public static List<MyTrafficLight> trafficLightClusters = new ArrayList<>();
    public static List<SingleTrafficLight> singleTrafficLights = new ArrayList<>();
    /**
     * Function loads the Traffic Light Clusters from SUMO into trafficLightClusters as our own Wrapper Class
     *
     * @param mySystem instance of mySystem is created in main(), needed as parameter because of the connection to SUMO.
     */
    public static void loadTrafficLights(MySystem mySystem) {
        try {
            trafficLightClusters = mySystem.getTrafficLights();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Splits the loaded Traffic Light Clusters into single Traffic Lights of the class SingleTrafficLight.
     * SingleTrafficLight now holds a single traffic Light going from a lane only into one direction (e.g. car wants to go straight).
     */
    public static void splitTrafficLight() {
        //This loop iterates through the List of all traffic lights
        int clusterIndex = 0;
        int singleTLindex = 0;
        for(MyTrafficLight trafficLight : trafficLightClusters) {


            try {
                SumoLinkList linkList = trafficLight.getControlledLinks();
                System.out.println(trafficLight.getState().toString());

                    for(SumoLink link : linkList) {
                        StringBuilder id = new StringBuilder();

                        id.append("TL");
                        id.append(clusterIndex);
                        id.append("_");
                        id.append(singleTLindex);

                        SingleTrafficLight singleTrafficLight = new SingleTrafficLight(trafficLight, singleTLindex, id.toString());
                        singleTrafficLights.add(singleTrafficLight);
                        singleTLindex++;
                    }
                    clusterIndex++;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}



