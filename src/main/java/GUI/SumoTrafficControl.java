package GUI;
import loader.VehicleAdder;

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
import javax.swing.event.ChangeEvent;

public class SumoTrafficControl extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_Red;
    private JTextField textField_Green;
    private JTextField textField_Blue;
    private JTextField textField_Alpha;

    /**
     * Launch the application.
     *//*
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SumoTrafficControl frame = new SumoTrafficControl();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    *//**
     * Create the frame.
     */
    public SumoTrafficControl() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1700, 900);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Sumo Traffic Simulation");
        lblNewLabel.setBounds(10, 11, 1674, 93);
        lblNewLabel.setFont(new Font("Ink Free", Font.ITALIC, 60));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel);

        // --- RED SLIDER ---
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

        // --- GREEN SLIDER ---
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

        // --- BLUE SLIDER ---
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

        // --- ALPHA SLIDER ---
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

        JLabel lblNewLabel_1 = new JLabel("Color Changer");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(new Font("Ink Free", Font.ITALIC, 22));
        lblNewLabel_1.setBounds(10, 144, 276, 50);
        contentPane.add(lblNewLabel_1);

        JButton btnNewButton = new JButton("START");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton.setBounds(10, 72, 133, 50);
        contentPane.add(btnNewButton);

        JButton btnStop = new JButton("STOP");
        btnStop.setBounds(153, 72, 133, 50);
        contentPane.add(btnStop);

        JButton btnStressTest = new JButton("Stress Test");
        btnStressTest.setBounds(10, 800, 276, 50);
        contentPane.add(btnStressTest);

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

        JButton btnNewButton_1 = new JButton("Red");
        btnNewButton_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton_1.setBounds(10, 492, 133, 50);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_1_1 = new JButton("Green (Priority)");
        btnNewButton_1_1.setBounds(153, 572, 133, 50);
        contentPane.add(btnNewButton_1_1);

        JButton btnNewButton_1_1_1 = new JButton("Amber");
        btnNewButton_1_1_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1_1_1.setBounds(153, 492, 133, 50);
        contentPane.add(btnNewButton_1_1_1);

        JButton btnNewButton_1_1_2 = new JButton("Green (non-Priority)");
        btnNewButton_1_1_2.setBounds(10, 572, 133, 50);
        contentPane.add(btnNewButton_1_1_2);

        JLabel lblNewLabel_2 = new JLabel("R");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2.setBounds(6, 209, 46, 14);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_2_1 = new JLabel("G");
        lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_1.setBounds(6, 234, 46, 14);
        contentPane.add(lblNewLabel_2_1);

        JLabel lblNewLabel_2_2 = new JLabel("B");
        lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_2.setBounds(6, 261, 46, 14);
        contentPane.add(lblNewLabel_2_2);

        JLabel lblNewLabel_2_3 = new JLabel("A");
        lblNewLabel_2_3.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2_3.setBounds(6, 287, 46, 14);
        contentPane.add(lblNewLabel_2_3);

        JComboBox comboBox = new JComboBox();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue = comboBox.getSelectedItem().toString();
            }
        });
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"Traffic Light 1", "Traffic Light 2", "Traffic Light 3", "Traffic Light 4", "Traffic Light 5", "Traffic Light 6", "Traffic Light 7", "Traffic Light 8"}));
        comboBox.setBounds(10, 442, 276, 39);
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

    }
}
