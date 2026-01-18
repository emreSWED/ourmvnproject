package loader;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MyLane;
import util.ConnectionManager;



public class LaneLoader {
    /**
     * keeps track of number of lanes
     */
    public static int laneCount;//keeps track of number of lanes
    /**
     * name of all laneIDs in the simulation as a list of strings
     */
    public static List<String> laneIDs; //list of all the lanes in the simulation
    /**
     * Lane Positions for later rendering
     */
    public static List<SumoGeometry> lanePositions; //List of a List of the singular "lines" the lanes is drawn out of.
    /**
     * List of our Lane wrapper
     */
    public static List<MyLane> myLanes;
    // public static ConnectionManager conn; instead of having conn in the Constructor, it could also be declared static here

    public static Map<String, MyLane> laneMap = new HashMap<>();

    /**
     *  when a loader.LaneLoader object is created, all the lanes are loaded into the Lists.
     *  creates a list of all the lanes in laneIDs
     *  loops through laneIDs List and grabs those coordinate List of the lines
     *  adds the laneID to our own MyLane class, constructor handles loading into Path2D for each lane to be rendered in Graphics2D engine
     */

    public LaneLoader(ConnectionManager conn) throws Exception {
        laneCount = (int) conn.dojobget(Lane.getIDCount()); //gets the number of lanes into laneCount
        laneIDs = (List<String>) conn.dojobget(Lane.getIDList());

        lanePositions       = new ArrayList<>();
        myLanes = new ArrayList<>();

        for(int i = 0; i < laneCount; i++){
            lanePositions.add(i, (SumoGeometry) conn.dojobget(Lane.getShape(laneIDs.get(i))));
        }


        for(int i = 0; i < laneIDs.size(); i++){
            myLanes.add(new MyLane(laneIDs.get(i), lanePositions.get(i).coords.size() ));
            MyLane newLane = new MyLane(laneIDs.get(i), lanePositions.get(i).coords.size());
            myLanes.add(newLane);

            laneMap.put(laneIDs.get(i), newLane);
        }

    }


    public static void printAllLaneIDs(){
        System.out.println(laneIDs);
    }

    public static MyLane getLaneById(String id) {
        return laneMap.get(id);
    }
}
