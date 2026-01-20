package model;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

import de.tudresden.sumo.objects.SumoLinkList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConnectionManager;

import java.util.Arrays;
import java.util.List;

public class MyTrafficLight {
    private static final Logger LOG = LogManager.getLogger(ConnectionManager.class.getName());


    private String id;
    private SumoTraciConnection conn;

    public MyTrafficLight(String id, SumoTraciConnection conn) {
        this.id = id;
        this.conn = conn;
    }

    public String getId(){
        return id;
    }

    public List<String> getControlledJunctions() throws Exception {
        return (List<String>) this.conn.do_job_get(Trafficlight.getControlledJunctions(this.id));
    }

   public SumoStringList getControlledLanes() throws Exception {
        return (SumoStringList) this.conn.do_job_get(Trafficlight.getControlledLanes(this.id));
   }

    public SumoLinkList getControlledLinks() throws Exception {
        return (SumoLinkList) this.conn.do_job_get(Trafficlight.getControlledLinks(this.id));
    }


    public String getState() throws Exception {
        return (String) this.conn.do_job_get(Trafficlight.getRedYellowGreenState(this.id));
    }



    public void setState(String state){
        try {
            conn.do_job_set(Trafficlight.setRedYellowGreenState(this.id, state));
        } catch (Exception e) {
            System.out.println("Could not set state");
            throw new RuntimeException(e);
        }
    }


    public void setStateToGreen(){
        try {
            String tlState = (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(this.id));
            int tlStatelenght =  tlState.length();
            StringBuilder stateToSet = new StringBuilder();
            char[] stateArray = new char[tlStatelenght];
            Arrays.fill(stateArray, 'g');
            stateToSet.append(stateArray);

            conn.do_job_set(Trafficlight.setRedYellowGreenState(this.id, stateToSet.toString()));
        } catch (Exception e) {
            System.out.println("Could not set phase");
            throw new RuntimeException(e);
        }
    }

    public void setStateToRed(){
        try {
            String tlState = (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(this.id));
            int tlStatelenght =  tlState.length();
            StringBuilder stateToSet = new StringBuilder();
            char[] stateArray = new char[tlStatelenght];
            Arrays.fill(stateArray, 'r');
            stateToSet.append(stateArray);

            conn.do_job_set(Trafficlight.setRedYellowGreenState(this.id, stateToSet.toString()));
        } catch (Exception e) {
            System.out.println("Could not set phase");
            throw new RuntimeException(e);
        }
    }

    public void setStateToYellow(){
        try {
            String tlState = (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(this.id));
            int tlStatelenght =  tlState.length();
            StringBuilder stateToSet = new StringBuilder();
            char[] stateArray = new char[tlStatelenght];
            Arrays.fill(stateArray, 'y');
            stateToSet.append(stateArray);

            conn.do_job_set(Trafficlight.setRedYellowGreenState(this.id, stateToSet.toString()));
        } catch (Exception e) {
            System.out.println("Could not set phase");
            throw new RuntimeException(e);
        }
    }

    public void setStateToFullGreen(){
        try {
            String tlState = (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(this.id));
            int tlStatelenght =  tlState.length();
            StringBuilder stateToSet = new StringBuilder();
            char[] stateArray = new char[tlStatelenght];
            Arrays.fill(stateArray, 'G');
            stateToSet.append(stateArray);

            conn.do_job_set(Trafficlight.setRedYellowGreenState(this.id, stateToSet.toString()));
        } catch (Exception e) {
            System.out.println("Could not set phase");
            throw new RuntimeException(e);
        }
    }


}
