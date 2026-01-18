package util;

import model.MyVehicle;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
/**
 * The {@code TrafficDataExporter} class is responsible for exporting simulation data
 * to a CSV file. It records vehicle states such as position, speed, and angle
 * for each simulation step.
 */
public class TrafficDataExporter {
    private PrintWriter writer;
    // Permanent folder name
    private static final String FOLDER_PATH = "simulation_logs";
    private static final int MAX_FILES = 5;
    /**
     * Initializes a new exporter and creates the destination file.
     * If the file already exists, it will be overwritten.
     */
    public TrafficDataExporter() {
        try {
            // Check if folder exists, if not, create it once
            File folder = new File(FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
                System.out.println("Created permanent log folder: " + FOLDER_PATH);
            }

            // Clean up if there are already too many files from previous runs
            cleanupOldFiles();

            // Create the new file for this specific simulation run
            String fileName = generateTimestampedFileName();
            writer = new PrintWriter(new FileWriter(new File(folder, fileName), false));

            // Standard CSV Header
            writer.println("Step;VehicleID;X_Pos;Y_Pos;Speed_ms;Angle");
        } catch (IOException e) {
            System.err.println("Critical Error: Could not initialize CSV export: " + e.getMessage());
        }
    }
    /**
     * Generates a unique filename based on the current date and time.
     * The format used is {@code yyyy-MM-dd_HH-mm-ss} to ensure file system compatibility
     * and chronological ordering.
     * @return A string containing the formatted filename with a .csv extension.
     */
    private String generateTimestampedFileName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return "sim_run_" + dtf.format(LocalDateTime.now()) + ".csv";
    }
    /**
     * Manages the number of log files in the export folder.
     * It identifies all CSV files, sorts them by their last modification date,
     * and deletes the oldest entries until the total count is below the {@code MAX_FILES} limit.
     * This ensures the storage does not fill up with obsolete simulation data.
     */
    private void cleanupOldFiles() {
        File folder = new File(FOLDER_PATH);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length >= MAX_FILES) {
            // Sorting by last modified to find the oldest files
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));

            // Logic: Delete files until we have room for the new one (MAX_FILES - 1)
            int numToDelete = files.length - (MAX_FILES - 1);
            for (int i = 0; i < numToDelete; i++) {
                if (files[i].delete()) {
                    System.out.println("Auto-Cleanup: Deleted oldest log: " + files[i].getName());
                }
            }
        }
    }
    /**
     * Writes the current data of all provided vehicles to the CSV file.
     * Each vehicle is logged in a new line with the current simulation step.
     * @param step The current time step of the SUMO simulation.
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