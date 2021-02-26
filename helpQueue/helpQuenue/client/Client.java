package helpQuenue.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

public class Client {
	// data
	final static int ServerPort = 3053;//client port
	final static String Host = "localhost";
	private static boolean exit = false;
	private static ClientGui clientFrame = new ClientGui();

	@SuppressWarnings("resource")
	public static void main(String args[]) throws UnknownHostException, IOException {

		clientFrame.setVisible(true);

		try {
			InetAddress ip = InetAddress.getByName(Host);//get ip
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
							clientFrame.lblStatus.setText(msg.split("#")[0] + "\r\n");
							appendText(msg.split("#")[1] + "\r\n");
						} catch (IOException ex) {
							exit = true;
						}
					}
				}
			});

			readMessage.start();

			/**
			 * When btnAskforHelp clicked,send message to server to ask for help
			 */
			clientFrame.request.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					appendText("You: Make a help request\r\n");
//					try {
//						dos.writeUTF("help");
//					} catch (IOException ex) {
//						System.out.println("help message error");
//						ex.printStackTrace();
//					}
					sendMessageToServer(dos, "help");
				}
			});

			/**
			 * When btnCancel clicked,send message to server to cancel help
			 */
			clientFrame.cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					appendText("You: Cancel help request\r\n");
					sendMessageToServer(dos, "cancel");

//					try {
//						dos.writeUTF("cancel");
//					} catch (IOException ex) {
//						System.out.println("cancel message error");
//						ex.printStackTrace();
//					}
				}
			});

			/**
			 * When Window closed ,send message to server to logout
			 */
			clientFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					exit=true;
					sendMessageToServer(dos, "logout");


				}
			});

		} catch (Exception ex) {
			appendText("Socket Connection Error. \r\nError Info:" + ex + "\r\n");
		}

	}

	private static void sendMessageToServer(DataOutputStream dos, String message) {
		try {
			dos.writeUTF(message);
		} catch (IOException ex) {
			System.out.println("Send message caught exception.\r\n" + ex);
		}
	}

	public static void appendText(String text) {
		clientFrame.textArea.append(text);
		clientFrame.textArea.setCaretPosition(clientFrame.textArea.getText().length());
	}

}
