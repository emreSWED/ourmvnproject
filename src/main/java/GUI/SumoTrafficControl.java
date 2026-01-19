package GUI;
import de.tudresden.sumo.objects.SumoColor;
import loader.LaneTrafficLight;
import loader.VehicleAdder;
import model.MyTrafficLight;
import model.MyVehicle;
import util.ConnectionManager;
import util.MySystem;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;


public class SumoTrafficControl extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    static JLabel infoCountVeh = new JLabel();
    private MapPanel mapPanel;

    private final Color COLOR_BG_MAIN   = new Color(52, 73, 94);

    private final Color COLOR_ACCENT    = new Color(26, 188, 156);

    private final Color COLOR_TEXT      = new Color(236, 240, 241);

    private final Color COLOR_DARK_ACC  = new Color(44, 62, 80);

    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 42);
    private final Font SUB_TITLE = new Font("Segoe UI", Font.BOLD, 27);
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.BOLD, 14);

    private ConnectionManager connectionManager;
    private MySystem mySystem = new MySystem(connectionManager.traciConnection);
    private List<MyTrafficLight> loadedTrafficLights = new ArrayList<>();
    private String currentTrafficLightID = mySystem.getTrafficLights().getFirst().getId();
    MyTrafficLight trafficLights = new MyTrafficLight(currentTrafficLightID, ConnectionManager.traciConnection);

    public void setTrafficLights(List<MyTrafficLight> lights) {
        this.loadedTrafficLights = lights;
    }

    public SumoTrafficControl() throws Exception {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sumo Trafic Simulation");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setBounds(150, 0, 1300, 900);
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BG_MAIN);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Sumo Traffic Simulation");
        lblNewLabel.setBounds(10, 11, 1674, 93);
        lblNewLabel.setFont(FONT_TITLE);
        lblNewLabel.setForeground(COLOR_TEXT);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //contentPane.add(lblNewLabel);

        mapPanel = new MapPanel();
        mapPanel.setBounds(320, 20, 1200, 740);
        contentPane.add(mapPanel);


        // start button
        JButton btnStart = new JButton("START");
        styleButton(btnStart);
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized (MySystem.stepLock) {
                    MySystem.stopped = false;
                    MySystem.stepLock.notifyAll();
                }
                System.out.println("Started simulation");
            }
        });
        btnStart.setBounds(10, 72, 133, 50);
        contentPane.add(btnStart);

        // stop button
        JButton btnStop = new JButton("STOP");
        styleButton(btnStop);
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MySystem.stopped = true;
                System.out.println("Stopped simulation");
            }
        });
        btnStop.setBounds(153, 72, 133, 50);
        contentPane.add(btnStop);

        // tps slider
        JLabel lblTickSpeed = new JLabel("Ticks/sec: " + MySystem.ticksPerSecond);
        lblTickSpeed.setBounds(10, 130, 200, 20);
        contentPane.add(lblTickSpeed);

        JSlider tickSlider = getJSliderTps(lblTickSpeed);

        contentPane.add(tickSlider);

        // color preview
        JPanel colorPreview = new JPanel();
        colorPreview.setBackground(Color.WHITE);
        colorPreview.setBounds(152, 222, 100, 50);
        colorPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        contentPane.add(colorPreview);

        // color picker
        JButton btnColor = new JButton("Change color");
        styleButton(btnColor);
        btnColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color chosen = JColorChooser.showDialog(
                        contentPane,
                        "Choose color",
                        Color.WHITE
                );

                if (chosen != null)
                    colorPreview.setBackground(chosen);
            }
        });
        btnColor.setBounds(10, 222, 133, 50);
        contentPane.add(btnColor);

        JButton btnStressTest = new JButton("Stress Test");
        styleButton(btnStressTest);
        btnStressTest.setBounds(10, 700, 276, 50);
        btnStressTest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    trafficLights.setState("GGGGGGGGG");
                    for(int i = 0; i<100; i++)
                    {
                        VehicleAdder.addRandomVehicle();
                    }

                }
                catch(Exception exception){
                    throw new RuntimeException(exception);
                }
            }
        });
        contentPane.add(btnStressTest);

        // add vehicle with color from color picker
        JButton btnAddVehicle = new JButton("Add Vehicle");
        styleButton(btnAddVehicle);
        btnAddVehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    VehicleAdder.addVehicle(colorPreview.getBackground());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnAddVehicle.setBounds(10, 282, 276, 50);
        contentPane.add(btnAddVehicle);

        // selected vehicles label
        JLabel lblSelectedVehicles = new JLabel("Edit selected vehicles");
        lblSelectedVehicles.setBounds(10, 350, 200, 20);
        contentPane.add(lblSelectedVehicles);

        // change color of every selected vehicle
        JButton btnChangeColor = new JButton("Change color");
        styleButton(btnChangeColor);
        btnChangeColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (MyVehicle myVehicle : MySystem.selectedVehicles) {
                    try { // if vehicle already despawned
                        myVehicle.setColor(colorPreview.getBackground());
                    } catch (Exception _) {}
                }
            }
        });
        btnChangeColor.setBounds(10, 372, 133, 50);
        contentPane.add(btnChangeColor);

        // change speed of every selected vehicle
        JButton btnChangeSpeed = new JButton("Change speed");
        styleButton(btnChangeSpeed);
        btnChangeSpeed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        });
        btnChangeSpeed.setBounds(152, 372, 133, 50);
        contentPane.add(btnChangeSpeed);

        // change speed label
        JLabel lblChangeSpeed = new JLabel("Speed: 0.0 m/s");
        lblChangeSpeed.setBounds(152, 330, 200, 20);
        lblChangeSpeed.setForeground(COLOR_TEXT);
        contentPane.add(lblChangeSpeed);

        // change speed slider
        JSlider speedSlider = getJSliderSpeed(lblChangeSpeed);
        contentPane.add(speedSlider);

        JButton btnNewButton_1 = new JButton("Red");
        styleButton(btnNewButton_1);
        btnNewButton_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setState("rrrrrrrrr");
            }
        });
        btnNewButton_1.setBounds(10, 492, 133, 50);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_1_1 = new JButton("Green (Priority)");
        styleButton(btnNewButton_1_1);
        btnNewButton_1_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setState("GGGGGGGGG");
            }
        });
        btnNewButton_1_1.setBounds(153, 572, 133, 50);
        contentPane.add(btnNewButton_1_1);

        JButton btnNewButton_1_1_1 = new JButton("Yellow");
        styleButton(btnNewButton_1_1_1);
        btnNewButton_1_1_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setState("yyyyyyyyy");
            }
        });
        btnNewButton_1_1_1.setBounds(153, 492, 133, 50);
        contentPane.add(btnNewButton_1_1_1);

        JButton btnNewButton_1_1_2 = new JButton("Green (non-Priority)");
        styleButton(btnNewButton_1_1_2);
        btnNewButton_1_1_2.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setState("ggggggggg");
            }
        });
        btnNewButton_1_1_2.setBounds(10, 572, 133, 50);
        contentPane.add(btnNewButton_1_1_2);

        JComboBox<String> comboBox = new JComboBox<>();
        List<MyTrafficLight> trafficLights = mySystem.getTrafficLights();
        String[] trafficLightIds = trafficLights.stream()
                .map(MyTrafficLight::getId)
                .toArray(String[]::new);
        comboBox.setModel(new DefaultComboBoxModel<>(
                trafficLightIds
        ));
        comboBox.setBounds(10, 442, 276, 39);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentTrafficLightID = comboBox.getSelectedItem().toString();

                if (loadedTrafficLights == null || loadedTrafficLights.isEmpty()) {
                    System.out.println("Warnung: Noch keine Ampeln geladen!");
                    return;
                }

                System.out.println("Ausgewählte ID: " + currentTrafficLightID);
            }
        });
        contentPane.add(comboBox);

        //newly Added Lable to Add information about total count of autos in simulation
        infoCountVeh.setFont(new Font("Tahoma", Font.TRUETYPE_FONT, 14));
        infoCountVeh.setHorizontalAlignment(SwingConstants.CENTER);
        infoCountVeh.setBounds(10, 650, 133, 50);
        contentPane.add(infoCountVeh);
    }

    // tps slider
    private static JSlider getJSliderTps(JLabel lblTickSpeed) {
        JSlider tickSlider = new JSlider(JSlider.HORIZONTAL, 1, 60, MySystem.ticksPerSecond);
        tickSlider.setBounds(10, 155, 280, 18);
        tickSlider.setPaintTicks(false);
        tickSlider.setPaintLabels(false);
        tickSlider.setFocusable(false);
        tickSlider.setOpaque(false);

        tickSlider.addChangeListener(e -> {
            int value = tickSlider.getValue();
            MySystem.ticksPerSecond = value;
            lblTickSpeed.setText("Ticks/sec: " + value);
        });

        return tickSlider;
    }

    private static JSlider getJSliderSpeed(JLabel lblChangeSpeed) {
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 0);
        speedSlider.setBounds(152, 350, 120, 18);
        speedSlider.setPaintTicks(false);
        speedSlider.setPaintLabels(false);
        speedSlider.setFocusable(false);
        speedSlider.setOpaque(false);

        speedSlider.addChangeListener(e -> {
            double speed = speedSlider.getValue() / 10.0;
            lblChangeSpeed.setText("Speed: " + speed + " m/s");

            for (MyVehicle myVehicle : MySystem.selectedVehicles) {
                myVehicle.setSpeed(speed);
            }
        });

        return speedSlider;
    }


    public void refreshMap(List<model.MyVehicle> vehicles, List<model.MyTrafficLight> lights) {
        if (mapPanel != null) {

            mapPanel.updateMap(vehicles, lights);
        }
    }

    public static String getStringVehiclesCount(int valueOfMyVehicle){ return String.valueOf(valueOfMyVehicle);}
    public static void setInfoCountVehText(String VehicleCountInSystem){infoCountVeh.setText("Total Vehicles: \n"+VehicleCountInSystem);}

    private void styleButton(JButton btn) {
        btn.setFont(FONT_NORMAL);
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(COLOR_TEXT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_DARK_ACC, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
