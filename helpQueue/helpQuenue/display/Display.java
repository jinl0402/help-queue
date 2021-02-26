package helpQuenue.display;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Display {

    final static int ServerPort = 3054;
    final static String Host = "localhost";
    private static boolean exit = false;
    Socket s = null;

    public static void main(String args[]) throws UnknownHostException, IOException {

        DisplayGui displayFrame = new DisplayGui();
        displayFrame.setVisible(true);
        showCurrentTime(displayFrame);

        Display display=new Display();

        InetAddress ip = InetAddress.getByName(Host);


        try {
            display.s = new Socket(ip, ServerPort);
        } catch (Exception e) {
            displayFrame.display.setText("Socket Connection Error. \r\nError Info:" + e + "\r\n");
            return;
        }

        DataInputStream dis = new DataInputStream(display.s.getInputStream());
        DataOutputStream dos = new DataOutputStream(display.s.getOutputStream());

        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exit) {
                    try {
                        String msg = dis.readUTF();
                        if(msg.startsWith("course"))
                        {
                            displayFrame.courseLabel.setText("Course: "+msg.split("#")[1]);
                            displayFrame.sectionLabel.setText("Section: "+msg.split("#")[2]);
                        }else {
                            displayFrame.display.setText(msg);
                        }
                    } catch (IOException e) {
                        exit = true;
                    }
                }
            }
        });

        readMessage.start();

        /**
         * When Window closed ,send message to server to logout
         */
        displayFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit=true;
                try {
                    sendMessageToServer(dos, "logout");
                } catch (Exception e1) {
                }
            }
        });
    }

    private static void sendMessageToServer(DataOutputStream dos, String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("Send message caught exception.\r\n" + ex);
        }
    }


    private static void showCurrentTime(DisplayGui displayFrame) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                displayFrame.timeLabel.setText("Current Time: " + df.format(new Date()));
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 3;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

}
