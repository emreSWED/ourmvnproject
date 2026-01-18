package GUI;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import loader.VehicleAdder;
import model.MyTrafficLight;
import util.ConnectionManager;
import util.MySystem;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.ArrayList;


public class SumoTrafficControl extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_Red;
    private JTextField textField_Green;
    private JTextField textField_Blue;
    private JTextField textField_Alpha;

    static JLabel infoCountVeh = new JLabel();
    static JSlider slider_Red = new JSlider();
    static JSlider slider_Green = new JSlider();
    static JSlider slider_Blue = new JSlider();
    static JSlider slider_Alpha = new JSlider();



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

        // --- RED SLIDER ---

        JSlider slider_Red = new JSlider();
        slider_Red.setBackground(COLOR_BG_MAIN);
        slider_Red.setForeground(COLOR_TEXT);
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

        // --- GREEN SLIDER ---

        JSlider slider_Green = new JSlider();
        slider_Green.setBackground(COLOR_BG_MAIN);
        slider_Green.setForeground(COLOR_TEXT);
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

        // --- BLUE SLIDER ---

        JSlider slider_Blue = new JSlider();
        slider_Blue.setBackground(COLOR_BG_MAIN);
        slider_Blue.setForeground(COLOR_TEXT);
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

        // --- ALPHA SLIDER ---

        JSlider slider_Alpha = new JSlider();
        slider_Alpha.setBackground(COLOR_BG_MAIN);
        slider_Alpha.setForeground(COLOR_TEXT);
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

        JLabel lblNewLabel_1 = new JLabel("Color Changer");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(SUB_TITLE);
        lblNewLabel_1.setForeground(COLOR_TEXT);
        lblNewLabel_1.setBounds(10, 150, 290, 40);
        contentPane.add(lblNewLabel_1);

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

        JButton btnStressTest = new JButton("Stress Test");
        styleButton(btnStressTest);
        btnStressTest.setBounds(10, 800, 276, 50);
        btnStressTest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try{
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

        JButton btnAddVehicle = new JButton("Add Vehicle");
        styleButton(btnAddVehicle);
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

        JLabel lblNewLabel_2 = new JLabel("R");
        lblNewLabel_2.setForeground(COLOR_TEXT);
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2.setBounds(6, 209, 46, 14);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_2_1 = new JLabel("G");
        lblNewLabel_2_1.setForeground(COLOR_TEXT);
        lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_1.setBounds(6, 234, 46, 14);
        contentPane.add(lblNewLabel_2_1);

        JLabel lblNewLabel_2_2 = new JLabel("B");
        lblNewLabel_2_2.setForeground(COLOR_TEXT);
        lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_2.setBounds(6, 261, 46, 14);
        contentPane.add(lblNewLabel_2_2);

        JLabel lblNewLabel_2_3 = new JLabel("A");
        lblNewLabel_2_3.setForeground(COLOR_TEXT);
        lblNewLabel_2_3.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_3.setBounds(6, 287, 46, 14);
        contentPane.add(lblNewLabel_2_3);

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

        textField_Red = new JTextField();
        textField_Red.setEditable(false);
        textField_Red.setBounds(257, 211, 29, 20);
        textField_Red.setText(String.valueOf(slider_Red.getValue())); // <--- NEU
        contentPane.add(textField_Red);
        textField_Red.setColumns(10);

        textField_Green = new JTextField();
        textField_Green.setEditable(false);
        textField_Green.setColumns(10);
        textField_Green.setBounds(257, 236, 29, 20);
        textField_Green.setText(String.valueOf(slider_Green.getValue())); // <--- NEU
        contentPane.add(textField_Green);

        textField_Blue = new JTextField();
        textField_Blue.setEditable(false);
        textField_Blue.setColumns(10);
        textField_Blue.setBounds(257, 263, 29, 20);
        textField_Blue.setText(String.valueOf(slider_Blue.getValue())); // <--- NEU
        contentPane.add(textField_Blue);

        textField_Alpha = new JTextField();
        textField_Alpha.setEditable(false);
        textField_Alpha.setColumns(10);
        textField_Alpha.setBounds(257, 289, 29, 20);
        textField_Alpha.setText(String.valueOf(slider_Alpha.getValue())); // <--- NEU
        contentPane.add(textField_Alpha);

        //newly Added Lable to Add information about total count of autos in simulation
        infoCountVeh.setFont(new Font("Tahoma", Font.TRUETYPE_FONT, 14));
        infoCountVeh.setHorizontalAlignment(SwingConstants.CENTER);
        infoCountVeh.setBounds(10, 650, 133, 50);
        contentPane.add(infoCountVeh);


    }

    public void refreshMap(List<model.MyVehicle> vehicles, List<model.MyTrafficLight> lights) {
        if (mapPanel != null) {

            mapPanel.updateMap(vehicles, lights);
        }
    }
    public static int getRed(){ return slider_Red.getValue();}
    public static int getGreen(){ return slider_Green.getValue();}
    public static int getBlue(){ return slider_Blue.getValue();}
    public static int getAlpha(){ return slider_Alpha.getValue();}

    public  static Color getCarColor(){
        return new Color(
                getRed(),
                getGreen(),
                getBlue(),
                getAlpha()
        );
    }

    public static SumoColor getCarSColor(){
        return new SumoColor(
                getBlue(),
                getGreen(),
                getAlpha(),
                getRed()


        );
    }


    public static String getStringVehiclesCount(int valueOfMyVehicle){ return String.valueOf(valueOfMyVehicle);}
    public static void setInfoCountVehText(String VehicleCountInSystem){infoCountVeh.setText("Total Vehicles: \n"+VehicleCountInSystem);}


    // --- HILFSMETHODE FÜR DAS DESIGN ---
    private void styleButton(JButton btn) {
        btn.setFont(FONT_NORMAL);
        btn.setBackground(COLOR_ACCENT);    // Türkis
        btn.setForeground(COLOR_TEXT);      // Weiße Schrift
        btn.setFocusPainted(false);         // Entfernt den Klick-Rahmen
        btn.setBorder(BorderFactory.createLineBorder(COLOR_DARK_ACC, 1)); // Dünner Rahmen
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand-Mauszeiger
    }
}
