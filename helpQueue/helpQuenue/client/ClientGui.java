package helpQuenue.client;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientGui extends JFrame {
    JTextArea textArea;
    JLabel lblNewLabel;
    JButton request;
    JButton cancel;
    JLabel lblStatus;

    private JScrollPane scrollPane;
//    LocalDateTime requestTime;


    public ClientGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("P115 Lab Help Queue - Client");
        setSize(750, 520);
        getContentPane().setLayout(null);


        lblNewLabel = new JLabel("P115 Lab - Help Queue Client");
        lblNewLabel.setBounds(161, 10, 398, 30);
        lblNewLabel.setForeground(Color.BLUE);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Consolas", Font.BOLD, 25));
        getContentPane().add(lblNewLabel);

        request = new JButton("Request");
        request.setBounds(100, 100, 200, 80);
        request.setBackground(Color.green);
        getContentPane().add(request);

        cancel = new JButton("Cancel");
        cancel.setBounds(100, 250, 200, 80);
        cancel.setBackground(Color.red);
        getContentPane().add(cancel);

        lblStatus = new JLabel("Status:");
        lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
        lblStatus.setForeground(Color.BLACK);
        lblStatus.setFont(new Font("Consolas", Font.PLAIN, 18));
        lblStatus.setBounds(10, 445, 203, 30);
        getContentPane().add(lblStatus);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        textArea.setBounds(350, 50, 357, 388);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(350, 50, 357, 388);
        getContentPane().add(scrollPane);


//        JTextField box = new JTextField();
//        box.setBounds(650, 150, 350, 450);



    }
}


