package loader;

import de.tudresden.sumo.cmd.Junction;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.List;


//loads the junctions
public class JunctionLoader {

    public static List<String> JunctionIdList;
    public static ArrayList<Object> JunctionPositionList;
    public static int numberOfJunctions = 2; //these can all be static since no instance of loader.JunctionLoader is ever initiated
    //private double x;
    //private double y;

    public JunctionLoader(SumoTraciConnection connection) throws Exception {
        JunctionPositionList = new ArrayList<Object>();
        JunctionIdList = (List<String>) connection.do_job_get(Junction.getIDList());
        numberOfJunctions = (int) connection.do_job_get(Junction.getIDCount());
        for(int i = 0; i < numberOfJunctions; i++){

            JunctionPositionList.add(i, connection.do_job_get(Junction.getPosition(JunctionIdList.get(i))));
            //Gets the Position of each Junction into the JunctionPositionList
        }
    }
}
