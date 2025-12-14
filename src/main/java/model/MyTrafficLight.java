package model;
import de.tudresden.sumo.cmd.Trafficlight;
import it.polito.appeal.traci.SumoTraciConnection;

import de.tudresden.sumo.objects.SumoLinkList;

import java.util.List;

public class MyTrafficLight {
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

    public List<String> getControlledLanes() throws Exception {
        return ((List<String>) this.conn.do_job_get(Trafficlight.getControlledLanes(this.id))).stream().distinct().toList();
    }

    public SumoLinkList getControlledLinks() throws Exception {
        return (SumoLinkList) this.conn.do_job_get(Trafficlight.getControlledLinks(this.id));
    }

    public String getState() throws Exception {
        return (String) this.conn.do_job_get(Trafficlight.getRedYellowGreenState(this.id));
    }

    public void setPhase(String state){
        try {
            conn.do_job_set(Trafficlight.setRedYellowGreenState(this.id, state));
        } catch (Exception e) {
            System.out.println("Could not set phase");
        }
    }
}
