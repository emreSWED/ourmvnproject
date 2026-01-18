package util;

import model.MyVehicle;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
/**
 * The {@code TrafficDataExporter} class is responsible for exporting simulation data
 * to a CSV file. It records vehicle states such as position, speed, and angle
 * for each simulation step.
 */
public class TrafficDataExporter {
    private PrintWriter writer;
    /**
     * Initializes a new exporter and creates the destination file.
     * If the file already exists, it will be overwritten.
     * * @param path The relative or absolute path to the CSV file.
     */
    public TrafficDataExporter(String path) {
        try {
            // 'false' ensures that the file is overwritten every time the simulation restarts
            writer = new PrintWriter(new FileWriter(path, false));
            // Header line for Excel and other statistical tools
            writer.println("Step;VehicleID;X_Pos;Y_Pos;Speed_ms;Angle");
        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen der CSV-Datei: " + e.getMessage());
        }
    }
    /**
     * Writes the current data of all provided vehicles to the CSV file.
     * Each vehicle is logged in a new line with the current simulation step.
     * * @param step     The current time step of the SUMO simulation.
     * @param vehicles A list of {@link MyVehicle} objects currently active in the simulation.
     */
    public void logCurrentStep(int step, List<MyVehicle> vehicles) {
        if (writer == null || vehicles == null) return;

        for (MyVehicle v : vehicles) {
            // Using semicolon as a separator for compatibility with regional Excel versions
            writer.printf("%d;%s;%.2f;%.2f;%.2f;%.2f%n",
                    step, v.getId(), v.getX(), v.getY(), v.getSpeed(), v.getAngle());
        }
        // flush() ensures that data is written to the disk even if the program crashes
        writer.flush();
    }
    /**
     * Closes the data stream and ensures the file is saved to the disk.
     * This method should be called when the simulation stops.
     */
    public void closeExport() {
        if (writer != null) {
            writer.close();
            System.out.println("CSV-export successfully ended.");
        }
    }
}