package helpQuenue.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

public class Admin {
    final static int ServerPort = 3055;//connect to server port
    final static String Host = "localhost";
    private static boolean exit = false;
    private static AdminGui administratorFrame = new AdminGui();

    @SuppressWarnings("resource")
    public static void main(String args[]) throws UnknownHostException, IOException {
        administratorFrame.setVisible(true);

        try {
            InetAddress ip = InetAddress.getByName(Host); //get ip
            Socket s = new Socket(ip, ServerPort);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            /**
             * Read Message From server
             */
            Thread readMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!exit) {
                        try {
                            String msg = dis.readUTF();
                            administratorFrame.textArea.setText(msg);
                        } catch (IOException ex) {
                            exit = true;
                        }
                    }
                }
            });

            readMessage.start();

            /**
             * When cancel button click then delete input workstation queue
             */
            administratorFrame.cancelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String workStationName = administratorFrame.textField_WorkStationName.getText().trim();
                    if (workStationName == null || "".equals(workStationName.trim())) {
                        administratorFrame.textArea.setText("Cancel help error: Please input a workStationName");
                        return;
                    } else {
                        sendMessageToServer(dos, "cancel#" + workStationName);
                    }
                }
            });

            /**
             * When clear button click ,then clear all queue
             */
            administratorFrame.clearBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessageToServer(dos, "clear");
                }
            });
            /*
            * Add course button click add new course to server and database
            * */
            administratorFrame.addBtn.addActionListener(new ActionListener() {
                @Override
                /*
                * Course create method
                * */
                public void actionPerformed(ActionEvent e) {
                    String courseNo = administratorFrame.textField_CourseNo.getText().trim();
                    String section = administratorFrame.textField_Section.getText().trim();
                    String startDateStr = administratorFrame.textField_StartDate.getText().trim();
                    String endDateStr = administratorFrame.textField_EndDate.getText().trim();
                    String dayOfWeek = administratorFrame.comboBox_DayOfWeek.getSelectedItem().toString().trim();
                    String startTimeStr = administratorFrame.textField_StartTime.getText().trim();
                    String endTimeStr = administratorFrame.textField_EndTime.getText().trim();

                    if (courseNo == null || "".equals(courseNo)) {
                        administratorFrame.textArea.setText("CourseNum should not be empty.");
                        return;
                    }
                    if (section == null || "".equals(section)) {
                        administratorFrame.textArea.setText("Section should not be empty.");
                        return;
                    } else {
                        try {
                            Integer.parseInt(section);
                        } catch (Exception ex) {
                            administratorFrame.textArea.setText("Section should not be an Integer.");
                            return;
                        }
                    }

                    if (startDateStr == null || "".equals(startDateStr)) {
                        administratorFrame.textArea.setText("Start Date should not be empty.");
                        return;
                    } else {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.parse(startDateStr);
                        } catch (Exception ex) {
                            administratorFrame.textArea.setText("Start Date format is wrong.");
                            return;
                        }
                    }

                    if (endDateStr == null || "".equals(endDateStr)) {
                        administratorFrame.textArea.setText("End Date should not be empty.");
                        return;
                    } else {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.parse(endDateStr);
                        } catch (Exception ex) {
                            administratorFrame.textArea.setText("End Date format is wrong.");
                            return;
                        }
                    }

                    if (dayOfWeek == null || "".equals(dayOfWeek)) {
                        administratorFrame.textArea.setText("Day Of Week should not be empty.");
                        return;
                    }

                    if (startTimeStr == null || "".equals(startTimeStr)) {
                        administratorFrame.textArea.setText("Start Time should not be empty.");
                        return;
                    } else {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            sdf.parse(startTimeStr);
                        } catch (Exception ex) {
                            administratorFrame.textArea.setText("Start Time format is wrong.");
                            return;
                        }
                    }

                    if (endTimeStr == null || "".equals(endTimeStr)) {
                        administratorFrame.textArea.setText("End Time should not be empty.");
                        return;
                    } else {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            sdf.parse(endTimeStr);
                        } catch (Exception ex) {
                            administratorFrame.textArea.setText("End Time format is wrong.");
                            return;
                        }
                    }

                    String message=String.join("&", courseNo,section,startDateStr,endDateStr,dayOfWeek,startTimeStr,endTimeStr);
                    sendMessageToServer(dos, "course#"+message);
                }
            });
            administratorFrame.refreshBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessageToServer(dos, "view");
                }
            });

            administratorFrame.deleteBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String courseID = administratorFrame.textField_CourseID.getText().trim();

                    if (courseID == null || "".equals(courseID)) {
                        administratorFrame.textArea.setText("Course ID should not be empty.");
                        return;
                    }
                    sendMessageToServer(dos, "deletecourse#"+courseID);
                }
            });

            /**
             * When Window closed ,send message to server to logout
             */
            administratorFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    exit = true;
                    sendMessageToServer(dos, "logout");
                }
            });

        }catch (Exception ex) {
            administratorFrame.textArea.setText("Socket Connection Error. \r\nError Info:" + ex + "\r\n");
        }

    }
    private static void sendMessageToServer(DataOutputStream dos, String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("Send message caught exception.\r\n" + ex);
        }
    }

}



