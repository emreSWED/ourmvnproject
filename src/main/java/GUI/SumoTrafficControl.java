package GUI;
import loader.VehicleAdder;
import model.MyTrafficLight;
import util.ConnectionManager;
import util.MySystem;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;import java.util.List;
import java.util.ArrayList;

public class SumoTrafficControl extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_Red;
    private JTextField textField_Green;
    private JTextField textField_Blue;
    private JTextField textField_Alpha;

    private MapPanel mapPanel;

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
        setBounds(100, 100, 1300, 900);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        // Big head title of the graphical user interface
        JLabel lblNewLabel = new JLabel("Sumo Traffic Simulation");
        lblNewLabel.setBounds(10, 11, 1674, 93);
        lblNewLabel.setFont(new Font("Ink Free", Font.ITALIC, 60));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel);

        mapPanel = new MapPanel();
        mapPanel.setBounds(320, 110, 950, 740);
        contentPane.add(mapPanel);
        // Sliders of the RGBA Color, change listener updates the value of the text field next to the sliders
        // --- Red color value slider ---
        JSlider slider_Red = new JSlider();
        slider_Red.setMinorTickSpacing(5);
        slider_Red.setMaximum(255);
        slider_Red.setBounds(57, 205, 190, 26);
        // Listener hinzufügen
        slider_Red.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // Wert holen und ins Textfeld schreiben
                textField_Red.setText(String.valueOf(slider_Red.getValue()));
            }
        });
        contentPane.add(slider_Red);

        // --- Green color value slider ---
        JSlider slider_Green = new JSlider();
        slider_Green.setMinorTickSpacing(5);
        slider_Green.setMaximum(255);
        slider_Green.setBounds(57, 230, 190, 26);
        // Listener hinzufügen
        slider_Green.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textField_Green.setText(String.valueOf(slider_Green.getValue()));
            }
        });
        contentPane.add(slider_Green);

        // --- Blue color value slider ---
        JSlider slider_Blue = new JSlider();
        slider_Blue.setMinorTickSpacing(5);
        slider_Blue.setMaximum(255);
        slider_Blue.setBounds(57, 257, 190, 26);
        // Listener hinzufügen
        slider_Blue.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textField_Blue.setText(String.valueOf(slider_Blue.getValue()));
            }
        });
        contentPane.add(slider_Blue);

        // --- Alpha value slider ---
        JSlider slider_Alpha = new JSlider();
        slider_Alpha.setMinorTickSpacing(5);
        slider_Alpha.setMaximum(255);
        slider_Alpha.setBounds(57, 283, 190, 26);
        // Listener hinzufügen
        slider_Alpha.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textField_Alpha.setText(String.valueOf(slider_Alpha.getValue()));
            }
        });
        contentPane.add(slider_Alpha);
        //Information Label describing the color modifying section
        JLabel lblNewLabel_1 = new JLabel("Color Changer");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(new Font("Ink Free", Font.ITALIC, 22));
        lblNewLabel_1.setBounds(10, 144, 276, 50);
        contentPane.add(lblNewLabel_1);
        // The Start button, allowing the user to start the simulation after stopping it
        JButton btnNewButton = new JButton("START");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton.setBounds(10, 72, 133, 50);
        contentPane.add(btnNewButton);
        // The Stop button, allowing the user to stop the simulation while it is running
        JButton btnStop = new JButton("STOP");
        btnStop.setBounds(153, 72, 133, 50);
        contentPane.add(btnStop);
        //Stress Test button, which adds more vehicles and sets the traffic lights to the phase 'G'
        JButton btnStressTest = new JButton("Stress Test");
        btnStressTest.setBounds(10, 800, 276, 50);
        contentPane.add(btnStressTest);
        //Injects another Vehicle into the simulation
        JButton btnAddVehicle = new JButton("Add Vehicle");
        btnAddVehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    VehicleAdder.addRandomVehicle();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnAddVehicle.setBounds(10, 320, 276, 50);
        contentPane.add(btnAddVehicle);
        // Button to set Traffic Light phase Red 'r'
        JButton btnNewButton_1 = new JButton("Red");
        btnNewButton_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setPhase("rrrrrrrrr");
            }
        });
        btnNewButton_1.setBounds(10, 492, 133, 50);
        contentPane.add(btnNewButton_1);
        // Button to set Traffic Light phase to Green with Priority 'G'
        JButton btnNewButton_1_1 = new JButton("Green (Priority)");
        btnNewButton_1_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setPhase("GGGGGGGGG");
            }
        });
        btnNewButton_1_1.setBounds(153, 572, 133, 50);
        contentPane.add(btnNewButton_1_1);
        // Button to set Traffic Light phase to Amber (yellow) 'y'
        JButton btnNewButton_1_1_1 = new JButton("Yellow");
        btnNewButton_1_1_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setPhase("yyyyyyyyy");
            }
        });
        btnNewButton_1_1_1.setBounds(153, 492, 133, 50);
        contentPane.add(btnNewButton_1_1_1);
        // Button to set Traffic Light phase to Green without Priority 'g'
        JButton btnNewButton_1_1_2 = new JButton("Green (non-Priority)");
        btnNewButton_1_1_2.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trafficLights.setPhase("ggggggggg");
            }
        });
        btnNewButton_1_1_2.setBounds(10, 572, 133, 50);
        contentPane.add(btnNewButton_1_1_2);
        // Visual Labels to mark down the correct labels for the color adjustment
        // Describes the Slider of the color which changes the red value
        JLabel lblNewLabel_2 = new JLabel("R");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2.setBounds(6, 209, 46, 14);
        contentPane.add(lblNewLabel_2);
        // Describes the Slider of the color which changes the green value
        JLabel lblNewLabel_2_1 = new JLabel("G");
        lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_1.setBounds(6, 234, 46, 14);
        contentPane.add(lblNewLabel_2_1);
        // Describes the Slider of the color which changes the blue value
        JLabel lblNewLabel_2_2 = new JLabel("B");
        lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_2.setBounds(6, 261, 46, 14);
        contentPane.add(lblNewLabel_2_2);
        // Describes the Slider of the color which changes the alpha value
        JLabel lblNewLabel_2_3 = new JLabel("A");
        lblNewLabel_2_3.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_3.setBounds(6, 287, 46, 14);
        contentPane.add(lblNewLabel_2_3);
        // Dropdown menu containing the traffic lights
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
        //Information-Textfield to show the color value when changing the slider
        // color value red
        textField_Red = new JTextField();
        textField_Red.setEditable(false);
        textField_Red.setBounds(257, 211, 29, 20);
        textField_Red.setText(String.valueOf(slider_Red.getValue())); // <--- NEU
        contentPane.add(textField_Red);
        textField_Red.setColumns(10);
        // color value green
        textField_Green = new JTextField();
        textField_Green.setEditable(false);
        textField_Green.setColumns(10);
        textField_Green.setBounds(257, 236, 29, 20);
        textField_Green.setText(String.valueOf(slider_Green.getValue())); // <--- NEU
        contentPane.add(textField_Green);
        // color value blue
        textField_Blue = new JTextField();
        textField_Blue.setEditable(false);
        textField_Blue.setColumns(10);
        textField_Blue.setBounds(257, 263, 29, 20);
        textField_Blue.setText(String.valueOf(slider_Blue.getValue())); // <--- NEU
        contentPane.add(textField_Blue);
        // color value alpha
        textField_Alpha = new JTextField();
        textField_Alpha.setEditable(false);
        textField_Alpha.setColumns(10);
        textField_Alpha.setBounds(257, 289, 29, 20);
        textField_Alpha.setText(String.valueOf(slider_Alpha.getValue())); // <--- NEU
        contentPane.add(textField_Alpha);

    }

    public void refreshMap(java.util.List<model.MyVehicle> vehicles) {
        if (mapPanel != null) {
            mapPanel.updateVehicles(vehicles);
        }
    }
}
