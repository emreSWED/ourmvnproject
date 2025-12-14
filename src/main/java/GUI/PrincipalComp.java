package GUI;
import it.polito.appeal.traci.SumoTraciConnection;
import model.MyVehicle;
import util.ConnectionManager;

import java.awt.*;
import javax.swing.*;


public class PrincipalComp extends JFrame{

    ConnectionManager connManager = new ConnectionManager("myConfig.sumocfg");
    SumoTraciConnection conn = connManager.traciConnection;
    //Visuals of frame base
    public PrincipalComp() {
        JFrame frame = new JFrame("Sumo Trafic Simulation");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        JPanel titel_panel = new JPanel();
        titel_panel.setLayout(new BorderLayout());
        titel_panel.setPreferredSize(new Dimension(100, 100));

        JPanel buttons_panel = new JPanel();
        buttons_panel.setLayout(new GridLayout(1, 3));

        JPanel options_panel = new JPanel();
        options_panel.setLayout(new BorderLayout());

        JPanel slider_panel = new JPanel();
        slider_panel.setLayout(new BorderLayout());

        JPanel moreOpt_panel = new JPanel();
        moreOpt_panel.setLayout(new BoxLayout(moreOpt_panel,BoxLayout.Y_AXIS));

        //Labels for labl_panel show information about number of cars, avg speed, etc
        JPanel labl_panel = new JPanel();
        labl_panel.setLayout(new GridLayout(1, 2, 10, 10));

        JLabel textfield = new JLabel("select");
        textfield.setBackground(new Color(50, 60, 100));
        textfield.setForeground(new Color(200, 255, 0));
        textfield.setFont(new Font("Ink Free", Font.ITALIC, 60));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("SumoSimulation");
        textfield.setOpaque(true);
        JLabel speed_label = new JLabel("Speed");

        JSlider slider = new JSlider(0, 100, 60);
        slider.setMinorTickSpacing(0);
        slider.setMajorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(20));
        //experiment with Veh
        MyVehicle car1 = new MyVehicle("Car1",conn);

        //event listener of slider for speed. Its going to use method from class MyVehicle
        slider.addChangeListener(e->{
            if(!slider.getValueIsAdjusting()){
                double currentValue = slider.getValue();
                System.out.println("Slider Value" + currentValue);
                car1.setSpeed(currentValue);
            }

        });

        //buttons for options_panel
        JButton start_b = new JButton("Start"); //this Button should call main to execute the Simulation
        buttons_panel.add(start_b);
        JButton stop_b = new JButton("Stop");
        buttons_panel.add(stop_b);
        JButton addC_b = new JButton("+Add Car");



        //checkbox Options
        String[] carC_Options = {
                "Red","Green", "Blue", "Gray"
        };
        moreOpt_panel.setVisible(false);

        for(String colors : carC_Options){
            JCheckBox col_insert = new JCheckBox(colors);
            moreOpt_panel.add(col_insert);
        }
        //event for +AddCar button displays checkbox in more Options panel
        addC_b.addActionListener(e->{
            moreOpt_panel.setVisible(true);
            moreOpt_panel.revalidate(); //recalcula Layout
            moreOpt_panel.repaint(); // pinta todos de nuevo
        });
        buttons_panel.add(addC_b);

        //event for Stop Button
        //Speed soll auf  Null gesetzt werden, um die Simulation zu stoppen
        //Traffic Lights sollen gestopt werden
        //last Speed Value should be stored for next start


        //Panel Instance for Cars
        SimulAutosP simulation_Panel = new SimulAutosP();
        frame.add(simulation_Panel, BorderLayout.CENTER);

        //auto creation
        Cars car2 = new Cars(0, 150);
        Cars car3 = new Cars(30, 100);
        simulation_Panel.addCar(car2);
        simulation_Panel.addCar(car3);




        JLabel info_label1 = new JLabel("Number of cars: " + Cars.getCarsCount(), SwingConstants.CENTER);
        info_label1.setOpaque(true);
        info_label1.setBackground(Color.YELLOW);
        info_label1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labl_panel.add(info_label1);

        JLabel info_label2 = new JLabel("Average Speed: ", SwingConstants.CENTER);
        info_label2.setOpaque(true);
        info_label2.setBackground(Color.YELLOW);
        info_label2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labl_panel.add(info_label2);


        options_panel.add(moreOpt_panel, BorderLayout.EAST);
        options_panel.add(buttons_panel, BorderLayout.NORTH);
        options_panel.add(slider_panel, BorderLayout.SOUTH);
        slider_panel.add(speed_label, BorderLayout.NORTH);
        slider_panel.add(slider, BorderLayout.SOUTH);
        frame.add(options_panel, BorderLayout.EAST);
        titel_panel.add(textfield);

        frame.add(labl_panel, BorderLayout.SOUTH);
        frame.add(titel_panel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

}
