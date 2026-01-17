package model;

import GUI.SumoTrafficControl;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyVehicle {
    private static final Logger LOG = LogManager.getLogger(MyVehicle.class.getName());
    private String id;
    private SumoTraciConnection conn;
    SumoColor color ;

    public MyVehicle(String id, SumoTraciConnection conn, SumoColor color){
        this.id = id;
        this.conn = conn;
        //newly added trying to save color values for each Vehicle Object(still doesn't work)
         this.color =  new SumoColor(color.r, color.g, color.b, color.a);
    }

    public String getId() {
        return this.id;
    }

    public double getX() {
        try{
            SumoPosition2D pos = (SumoPosition2D) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(this.id));
            return pos.x;
        }catch(Exception e){
            return -1.0;
        }
    }

    public double getY(){
        try{
            SumoPosition2D pos = (SumoPosition2D) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(this.id));
            return pos.y;
        }catch(Exception e){
            return -1.0;
        }
    }
    public double getAngle() {
        try {

            return (double) conn.do_job_get(Vehicle.getAngle(this.id));
        } catch (Exception e) {
            return 0;
        }

    }

    public double getSpeed(){
        try{
            return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getSpeed(this.id));
        }catch(Exception e){
            return 0.0;
        }
    }

    public void setSpeed(double speedMetersPerSecond){
        try{
            conn.do_job_set(de.tudresden.sumo.cmd.Vehicle.setSpeed(this.id, speedMetersPerSecond));
            System.out.println("Fahrzeug " + id + " auf " + speedMetersPerSecond + " m/s gesetzt.");
        }catch(Exception e){
            System.out.println("Fehler speed " + id);

        }
    }

    public void setColor(String id, SumoColor color) {

        try {
            conn.do_job_set(de.tudresden.sumo.cmd.Vehicle.setColor(this.id, color));//new SumoColor(color.a, color.g, color.r, color.b)
            System.out.println("Color set to R " +color.r + ", G" + color.g + ", B" + color.b + ", A" + color.a);
        } catch (Exception e) {
           // System.out.println("Fehler color " + id + " " + e.getMessage());
            LOG.error("Fehler color..."+ id,e);
        }
    }
}