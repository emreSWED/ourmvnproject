package GUI;

import loader.LaneLoader;
import model.MyLane;
import loader.YCoordinateFlipper;
import model.MyVehicle;
import util.MySystem;
import model.MyTrafficLight;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class draws the map for the simulation.
 * It shows the roads, cars, and traffic lights.
 * It also handles mouse clicks to select cars.
 */
public class MapPanel extends JPanel {
    private static final Logger LOG = LogManager.getLogger(MapPanel.class.getName());

    private List<MyVehicle> currentVehicles = new ArrayList<>(); //save list of Vehicles we want to draw
    private final Map<MyVehicle, Shape> carHitboxes = new HashMap<>();
    private final CarRenderer carRenderer = new CarRenderer();
    private final TrafficLightRenderer trafficLightRenderer = new TrafficLightRenderer();

    private List<MyTrafficLight> currentTrafficLights = new ArrayList<>();


    //Camera system: for coordinate transformation from huge values in small window values
    private boolean boundsCalculated = false;
    private double scaleFactor = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    //definition of constants for color, for code readability
    private final Color COLOR_BACKGROUND = new Color(150, 177, 137);
    private final Color COLOR_ROAD_BORDER = Color.WHITE;
    private final Color COLOR_ASPHALT = new Color(28, 28, 28);
    private final Color COLOR_DASH = Color.WHITE;

    private final double LANE_WIDTH = 3.5;

    /**
     * Creates the MapPanel.
     * It sets the background color and checks for mouse clicks.
     * If you click a car, it gets selected.
     */
    //constructor
    public MapPanel() {
        this.setBackground(COLOR_BACKGROUND);   //color of meadow

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    AffineTransform cam = new AffineTransform();
                    cam.translate(offsetX, offsetY);
                    cam.scale(scaleFactor, scaleFactor);

                    Point2D worldPoint = cam.createInverse().transform(e.getPoint(), null);

                    boolean shift = e.isShiftDown();

                    for (Map.Entry<MyVehicle, Shape> entry : carHitboxes.entrySet()) {
                        if (entry.getValue().contains(worldPoint)) {
                            MyVehicle car = entry.getKey();
                            System.out.println("Clicked on car: " + car.getId());

                            if (!shift) {
                                MySystem.selectedVehicles.clear();
                                MySystem.selectedVehicles.add(car);
                            } else {
                                if (!MySystem.selectedVehicles.remove(car)) {
                                    MySystem.selectedVehicles.add(car);
                                }
                            }

                            System.out.println("All selected cars: " + MySystem.selectedVehicles);
                            repaint();
                            return;
                        }
                    }

                    if (!shift) {
                        MySystem.selectedVehicles.clear();
                        System.out.println("Deselected all cars");
                        repaint();
                    } else
                        System.out.println("Nothing happened");

                } catch (Exception ex) {
                    //ex.printStackTrace();
                    LOG.error("Error processing mouse click in MapPanel ",ex);// if scaleFactor == 0
                }
            }
        });

        InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "selectAll");

        am.put("selectAll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MySystem.selectedVehicles.clear();
                MySystem.selectedVehicles.addAll(currentVehicles);
                System.out.println("Selected all car: " + MySystem.selectedVehicles);
                repaint();
            }
        });
    }

    /**
     * Gets new data from the main program.
     * It saves the list of cars and lights, then redraws the picture.
     *
     * @param vehicles The list of cars to show.
     * @param lights   The list of traffic lights to show.
     */

    //interface to the outside, Main calls up this method, when new cars
    public void updateMap(List<MyVehicle> vehicles, List<MyTrafficLight> lights) {
        this.currentVehicles = vehicles;
        this.currentTrafficLights = lights; // Daten speichern
        repaint();
    }

    /**
     * This is the main drawing method.
     * It draws the background, roads, markings, lights, and cars.
     *
     * @param g The graphics object used for drawing.
     */

    //where the drawing happens
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if (LaneLoader.myLanes == null || LaneLoader.myLanes.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Waiting for simulation...", 50, 50);
            return;
        }

        if (!boundsCalculated || getWidth() > 0) {
            calculateZoom();
        }

        AffineTransform old = g2d.getTransform();
        g2d.translate(offsetX, offsetY);
        g2d.scale(scaleFactor, scaleFactor);

        drawRoadLayer(g2d, COLOR_ROAD_BORDER, LANE_WIDTH + 0.4);

        drawRoadLayer(g2d, COLOR_ASPHALT, LANE_WIDTH);

        drawRoadMarkings(g2d);

        drawTrafficLights(g2d);
        drawCars(g2d);
        g2d.setTransform(old);
    }

    /**
     * Calculates the zoom and position of the map.
     * It makes sure the whole map fits inside the window.
     */

    private void calculateZoom() {
        //initialize min/max with extrem values
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        boolean found = false;

        //Search for extrem coordinate (iterate through all lines for bounding Box of city network)
        for (MyLane lane : LaneLoader.myLanes) {
            if (lane.lanePath != null) {
                Rectangle2D b = lane.lanePath.getBounds2D();
                if (b.getWidth() > 0) {
                    if (b.getMinX() < minX) minX = b.getMinX();
                    if (b.getMinY() < minY) minY = b.getMinY();
                    if (b.getMaxX() > maxX) maxX = b.getMaxX();
                    if (b.getMaxY() > maxY) maxY = b.getMaxY();
                    found = true;
                }
            }
        }

        if (!found) return; // if no map data found, Stop

        // Calculate the dimensions of the map
        double mapW = maxX - minX;
        double mapH = maxY - minY;
        //plus margin for window
        double buffer = Math.max(mapW, mapH) * 0.1;

        // Calculate scaling factors for width and height
        double scaleX = getWidth() / (mapW + buffer);
        double scaleY = getHeight() / (mapH + buffer);

        // Use the smaller scale factor to fit the map in the window
        this.scaleFactor = Math.min(scaleX, scaleY);

        // Calculate offsets to center the map in the window
        this.offsetX = (getWidth() - (mapW * scaleFactor)) / 2 - (minX * scaleFactor);
        this.offsetY = (getHeight() - (mapH * scaleFactor)) / 2 - (minY * scaleFactor);

        //mark as calculated
        this.boundsCalculated = true;
    }

    /**
     * Draws the main shape of the roads.
     * It can draw the white border or the black asphalt.
     *
     * @param g2d   The 2D graphics object.
     * @param c     The color of the road part.
     * @param width The width of the line to draw.
     */

    //draw road
    private void drawRoadLayer(Graphics2D g2d, Color c, double width) {
        g2d.setColor(c);

        g2d.setStroke(new BasicStroke((float)width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (MyLane lane : LaneLoader.myLanes) {
            if (lane.lanePath != null) g2d.draw(lane.lanePath);
        }
    }

    /**
     * Draws the white markings on the road.
     * It draws the dashed line in the middle and stop lines at intersections.
     *
     * @param g2d The 2D graphics object.
     */

    private void drawRoadMarkings(Graphics2D g2d) {
        // --- KONFIGURATION ---
        float[] dashPattern = {4.0f, 12.0f}; // Muster: 4m Strich, 12m Lücke
        Stroke dashedStroke = new BasicStroke(0.15f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
        Stroke stopLineStroke = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER); // Etwas dickerer Stoppstrich

        double centerOffset = LANE_WIDTH / 2.0; // Verschiebung zur Mitte
        double stopLineGap = 1.0; // Abstand zwischen Stopplinie und Ende der gestrichelten Linie

        for (MyLane lane : LaneLoader.myLanes) {
            // 1. Interne Spuren (in der Kreuzung) ignorieren
            // In SUMO beginnen Spuren innerhalb einer Kreuzung meist mit ":"
            if (lane.laneID.startsWith(":")) continue;

            if (lane.xpositions == null || lane.xpositions.length < 2) continue;

            // --- BERECHNUNGEN AM ENDE DER SPUR (Für Richtung und Stopplinie) ---
            int n = lane.xpositions.length;
            double xEnd = lane.xpositions[n-1];
            double yEnd = lane.ypositions[n-1];
            double xPrev = lane.xpositions[n-2];
            double yPrev = lane.ypositions[n-2];

            double dx = xEnd - xPrev;
            double dy = yEnd - yPrev;
            double lenEnd = Math.sqrt(dx*dx + dy*dy);

            if (lenEnd < 0.01) continue;

            // Normalisierter Richtungsvektor am Ende
            double uX = dx / lenEnd;
            double uY = dy / lenEnd;

            // Senkrechter Vektor (nach Rechts) für die Stopplinie
            double perpX = -uY;
            double perpY = uX;

            double stopLeftX = xEnd + perpX * (LANE_WIDTH / 2.0);
            double stopLeftY = yEnd + perpY * (LANE_WIDTH / 2.0);
            double stopRightX = xEnd - perpX * (LANE_WIDTH / 2.0);
            double stopRightY = yEnd - perpY * (LANE_WIDTH / 2.0);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(stopLineStroke);
            g2d.draw(new Line2D.Double(stopLeftX, stopLeftY, stopRightX, stopRightY));


            double totalLength = 0;
            for (int i = 0; i < n - 1; i++) {
                double ddx = lane.xpositions[i+1] - lane.xpositions[i];
                double ddy = lane.ypositions[i+1] - lane.ypositions[i];
                totalLength += Math.sqrt(ddx*ddx + ddy*ddy);
            }

            double cutOffLength = 2.0 + stopLineGap;

            if (totalLength <= cutOffLength) continue;

            Path2D dashedPath = new Path2D.Double();
            double currentDist = 0;
            boolean firstPointSet = false;

            for (int i = 0; i < n - 1; i++) {
                double x1 = lane.xpositions[i];
                double y1 = lane.ypositions[i];
                double x2 = lane.xpositions[i+1];
                double y2 = lane.ypositions[i+1];

                double segDx = x2 - x1;
                double segDy = y2 - y1;
                double segLen = Math.sqrt(segDx*segDx + segDy*segDy);

                if (segLen == 0) continue;

                double nx = segDy / segLen;
                double ny = -segDx / segLen;

                double ox1 = x1 + nx * centerOffset;
                double oy1 = y1 + ny * centerOffset;
                double ox2 = x2 + nx * centerOffset;
                double oy2 = y2 + ny * centerOffset;

                if (currentDist + segLen > totalLength - cutOffLength) {
                    double remaining = (totalLength - cutOffLength) - currentDist;
                    if (remaining > 0) {
                        double factor = remaining / segLen;
                        double cutX = ox1 + (ox2 - ox1) * factor;
                        double cutY = oy1 + (oy2 - oy1) * factor;

                        if (!firstPointSet) { dashedPath.moveTo(ox1, oy1); firstPointSet = true; }
                        dashedPath.lineTo(cutX, cutY);
                    }
                    break;
                } else {
                    if (!firstPointSet) { dashedPath.moveTo(ox1, oy1); firstPointSet = true; }
                    dashedPath.lineTo(ox2, oy2);
                }
                currentDist += segLen;
            }

            g2d.setColor(COLOR_DASH);
            g2d.setStroke(dashedStroke);
            g2d.draw(dashedPath);
        }
    }

    /**
     * Draws all the cars on the map.
     * It sets the right position, rotation, and color for each car.
     *
     * @param g2d The 2D graphics object.
     */

    private void drawCars(Graphics2D g2d) {
        carHitboxes.clear();

        for (MyVehicle car : currentVehicles) {
            Shape worldShape = carRenderer.draw(g2d, car);
            carHitboxes.put(car, worldShape);
        }
    }

    /**
     * Draws the traffic lights.
     * It puts them next to the road and shows red, yellow, or green lights.
     *
     * @param g2d The 2D graphics object.
     */
    private void drawTrafficLights(Graphics2D g2d) {
        if (currentTrafficLights == null) return;

        for (MyTrafficLight tl : currentTrafficLights) {
            trafficLightRenderer.draw(g2d, tl);
        }
    }
}
