package helpQuenue.display;

import javax.swing.*;
import java.awt.*;



public class DisplayGui extends JFrame {
    private JScrollPane scrollPane;
    JLabel timeLabel = null;
    JLabel courseLabel = null;
    JLabel sectionLabel = null;
    JTextArea display = null;
    JLabel displayLabel = null;

    public DisplayGui() {							// title for pane

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("P115 Lab Help Queue Display");
        setSize(800, 580);
        getContentPane().setLayout(null);

        displayLabel = new JLabel("Phillips 115 Lab - Help Queue Display");
        displayLabel.setBounds(85, 10, 556, 30);
        displayLabel.setForeground(Color.BLUE);
        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setFont(new Font("Consolas", Font.BOLD, 25));
        getContentPane().add(displayLabel);


        //current time label
        timeLabel = new JLabel("Current Time: ");
        timeLabel.setFont(new Font("Arial", Font.BOLD,14));
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setBounds(30, 70, 300, 25);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        getContentPane().add(timeLabel);

        // course label
        courseLabel = new JLabel("Course: ");
        courseLabel.setFont(new Font("Arial", Font.BOLD,14));
        courseLabel.setForeground(Color.BLACK);
        courseLabel.setBounds(30, 90, 110, 25);
        courseLabel.setHorizontalAlignment(SwingConstants.LEFT);
        getContentPane().add(courseLabel);

        //Section label
        sectionLabel = new JLabel("Section: ");
        sectionLabel.setFont(new Font("Arial", Font.BOLD,14));
        sectionLabel.setForeground(Color.BLACK);
        sectionLabel.setBounds(30, 110, 110, 25);
        sectionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        getContentPane().add(sectionLabel);


        // display textArea
        display= new JTextArea();
        display.setEditable(false);
        display.setLineWrap(true);
        display.setFont(new Font("Consolas", Font.PLAIN, 21));
        display.setBounds(10, 100, 714, 371);

        scrollPane = new JScrollPane(display);
        scrollPane.setBounds(10, 182, 714, 289);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        getContentPane().add(scrollPane);


        // --- construct the highest level content-pane




    }

}
