package model;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

import java.awt.*;

public class MyVehicle {
    private String id;
    private SumoTraciConnection conn;

    public MyVehicle(String id, SumoTraciConnection conn){
        this.id = id;
        this.conn = conn;
    }

    public String getId() {
       return this.id;
    }

    public double getX() {
        try {
            SumoPosition2D pos = (SumoPosition2D) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(this.id));
            return pos.x;
        } catch(Exception e){
            return -1.0;
        }
    }

    public double getY(){
        try {
            SumoPosition2D pos = (SumoPosition2D) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(this.id));
            return pos.y;
        } catch(Exception e){
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

    public double getSpeed() {
        try {
            return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getSpeed(this.id));
        } catch(Exception e){
            return 0.0;
        }
    }

    public Color getColor() {
        try {
            de.tudresden.sumo.objects.SumoColor c =
                    (de.tudresden.sumo.objects.SumoColor)
                            conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getColor(this.id));

            return new Color(
                    c.r & 0xFF,
                    c.g & 0xFF,
                    c.b & 0xFF,
                    c.a & 0xFF
            );
        } catch (Exception e) {
            return Color.BLACK;
        }
    }



    public void setSpeed(double speedMetersPerSecond) {
        try {
            conn.do_job_set(de.tudresden.sumo.cmd.Vehicle.setSpeed(this.id, speedMetersPerSecond));
            System.out.println("Fahrzeug " + id + " auf " + speedMetersPerSecond + " m/s gesetzt.");
        } catch(Exception e){
            System.out.println("Fehler speed " + id);

        }
    }

    public void setColor(SumoColor color) {
        try {
            conn.do_job_set(de.tudresden.sumo.cmd.Vehicle.setColor(this.id, new SumoColor(color.r, color.g, color.b, color.a)));
            System.out.println("Color set to " + color.r + ", " + color.g + ", " + color.b + ", " + color.a);
        } catch (Exception e) {
            System.out.println("Fehler color " + id + " " + e.getMessage());
        }
    }
}
