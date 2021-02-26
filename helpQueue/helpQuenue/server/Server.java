/*
 * Server - Java implementation of Server class for multi-threaded chat server 
 *
 *  from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 *  
 *  Assumes: ports used (here, port 1234) is not restricted or blocked by firewall
 */

package helpQuenue.server;

import helpQuenue.admin.Admin;
import helpQuenue.admin.AdminGui;
import helpQuenue.server.ClientHandler;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import helpQuenue.dao.HelpQueueDAO;
import helpQuenue.dao.HelpQueue;
import helpQuenue.dao.Event;


public class Server
{
	// Vector to store active clients
	public static Vector<ClientHandler> client = new Vector<>();
	public static Vector<ClientHandler> display = new Vector<>();
	public static Vector<ClientHandler> admin = new Vector<>();


	// counter for clients
	static int clientNum = 0;

	//current courseId, CourseNumber, section
	static String currentCourseID = "";
	static String currentCourseNum = "";
	static String currentSection = "";
	public static void main(String[] args) throws Exception {
		// server is listening on port 1234
		@SuppressWarnings("resource")
		ServerSocket clientSocket = new ServerSocket(3053); 	// socket for client to connect
		ServerSocket displaySocket = new ServerSocket(3054);	// socket for Display to connect
		ServerSocket adminSocket = new ServerSocket(3055);		// socket for admin to connect

		Socket s; 									// socket for client side

		Thread clientSocketAcceptThread= new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Socket cSocket = clientSocket.accept();
						DataInputStream dis = new DataInputStream(cSocket.getInputStream());
						DataOutputStream dos = new DataOutputStream(cSocket.getOutputStream());

						String clientName = "LAB-P155-" +String.format("%02d",client.size()+1);
						ClientHandler mtch = new ClientHandler(cSocket,clientName,"client",dis,dos);
                        System.out.println(clientName);

						// Create a new Thread with this client handler object.
						Thread t = new Thread(mtch);
						client.add(mtch);
						t.start();
						count();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		});

		clientSocketAcceptThread.start();

		Thread displaySocketAcceptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket dSocket = displaySocket.accept();
						DataInputStream dis = new DataInputStream(dSocket.getInputStream());
						DataOutputStream dos = new DataOutputStream(dSocket.getOutputStream());

						String displayName = "Display-" + String.format("%02d", display.size() + 1);
                        System.out.println(displayName);
						ClientHandler mtch = new ClientHandler(dSocket, displayName, "display", dis, dos);
						Thread t = new Thread(mtch);
						display.add(mtch);
						t.start();
						count();
						checkCourse();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		displaySocketAcceptThread.start();


		Thread adminSocketAcceptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						Socket aSocket = adminSocket.accept();
						DataInputStream dis = new DataInputStream(aSocket.getInputStream());
						DataOutputStream dos = new DataOutputStream(aSocket.getOutputStream());

						String adminName = "Admin- "+ String.format("%02d", admin.size() + 1);
                        System.out.println(adminName);
						ClientHandler mtch = new ClientHandler(aSocket,adminName,"admin",dis,dos);
						Thread t = new Thread(mtch);
						admin.add(mtch);
						t.start();
						count();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		adminSocketAcceptThread.start();

		/**
		 * Send Help Queue to display every 3 minutes.
		 */
		Thread displayUpdateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000);
						refreshDisplay();
					} catch (Exception e) {

					}
				}
			}
		});
		displayUpdateThread.start();

	}


//		// run infinite loop for getting client requests
//		while (true)
//		{
//			// NOTE: an accept call will wait (block) indefinitely waiting for a connection; if you want the enclosing loop to run regularly,
//			//		you need to put a timeout on your serversocket and use exception handling to determine if the accept call was successful
//			//		(any code following the accept call will execute after the accept() succeeds) or failed due to the timeout (throws a
//			//		SocketTimeoutException).  See the Java docs for the ServerSocket class or use online search on this topioc for details.
//			// Wait for and accept an incoming request
//			s = clientSocket.accept();
//			// System.out.println("New client request received : " + s);
//
//			// create input and output streams for this socket
//			DataInputStream dis = new DataInputStream(s.getInputStream());
//			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//
//			// Create a new handler object for handling this request.
//			String clientName = "client " + clientNum;
//			ClientHandler mtch = new ClientHandler(s, clientName, dis, dos);
//
//			// Create a new Thread with this client handler object.
//			Thread t = new Thread(mtch);
//
//			System.out.println("Adding client " + clientName  + " to active client list");
//
//			// add this client to active clients list
//			client.add(mtch);
//
//			// display client list
//			System.out.println("Current Clients:");
//			for (ClientHandler mc : Server.client) {
//				if (mc.isloggedin == true) {
//					System.out.println(mc.name);
//				}
//			}
//			System.out.println();
//
//			// start the thread.
//			t.start();
//
//			// increment i for new client name
//			clientNum++;
//
//		}	// end - while true loop
//	}	// end - method main


	// end - class Server
//
	public static void count() {
		System.out.println("Clients: " + client.size());
		System.out.println("Displays: " + display.size());
		System.out.println("Admin: " + admin.size());
	}

	//refresh Display
	public static void refreshDisplay() throws Exception {
		if (display.size() == 0) {
			return;
		}

		checkCourse();

		String displayContent = "";
		HelpQueueDAO dao = new HelpQueueDAO();
		dao.updateWaitTime();

		List<HelpQueue> queue = dao.getHelpQueue();
		if (queue == null || queue.size() == 0) {
			displayContent = "No current help requests\r\n";
		} else {
			displayContent = String.format("%-12s", "PositionNum") + String.format("%-13s", "WorkStationName")
					+ String.format("%-13s", "RequestTime") + String.format("%-10s", "WaitTime") + "\n";
			for (int i = 0; i < queue.size(); i++) {
				String date = new SimpleDateFormat("HH:mm:ss").format(queue.get(i).getRequestTime());
				displayContent = displayContent + String.format("%-12s", (i + 1))
						+ String.format("%-13s", queue.get(i).getWorkStationName()) + String.format("%-13s", date)
						+ String.format("%-10s", queue.get(i).getWaitTime()) + "\n";
			}
		}

		for (ClientHandler helper : display) {
			helper.dos.writeUTF(displayContent);
			helper.dos.writeUTF("course#"+currentCourseNum+"#"+currentSection);
		}
	}

		public static void initQueue() throws Exception {
			HelpQueueDAO dao = new HelpQueueDAO();
			List<HelpQueue> queue= dao.getHelpQueue();
			dao.clearClientRequest();

			for(HelpQueue item : queue)
			{
				Event log=new Event ();
				log.setId(UUID.randomUUID().toString());
				log.setEventType("cancel");
				log.setWorkStationName(item.getWorkStationName());
				log.setOriginator("system");
				log.setCourseNum(currentCourseNum);
				log.setSectionNum(currentSection);
				log.setEventTime(new Date());
				dao.saveCancelEvent(log,item.getRequestTime());
			}

		}

		public static void checkCourse() throws Exception {
			HelpQueueDAO dao = new HelpQueueDAO();
			String currentCourse = dao.getCurrentCourse();
			if (currentCourse!=null&&!"".equals(currentCourse)) {

				if (!currentCourseID.equals(currentCourse.split("#")[0])) {
					currentCourseID = currentCourse.split("#")[0];
					currentCourseNum = currentCourse.split("#")[1];
					currentSection = currentCourse.split("#")[2];
					initQueue();
				}
			}

		}

		public static void print(String s){
			System.out.println(s);
		}


	}



//	public static void initQueue() throws Exception {
//		HelpQueueDAO dao = new HelpQueueDAO();
//		List<HelpQueue> queue= dao.getHelpQueue();
//		dao.clearClientRequest();
//
//		for(HelpQueue item : queue)
//		{
//			Event log=new Event();
//			log.setId(UUID.randomUUID().toString());
//			log.setEventType("cancel");
//			log.setWorkStationName(item.getWorkStationName());
//			log.setOriginator("system");
//			log.setCourseNum(currentCourseNum);
//			log.setSectionNum(currentSection);
//			log.setEventTime(new Date());
//			dao.saveCancelEvent(log,item.getRequestTime());
//		}
//
//	}
//	public static void checkCourse() throws Exception {
//		HelpQueueDAO dao = new HelpQueueDAO();
//		String currentCourse = dao.getCurrentCourse();
//		if (currentCourse!=null&&!"".equals(currentCourse)) {
//
//			if (!currentCourseID.equals(currentCourse.split("#")[0])) {
//				currentCourseID = currentCourse.split("#")[0];
//				currentCourseNum = currentCourse.split("#")[1];
//				currentSection = currentCourse.split("#")[2];
//				initQueue();
//			}
//		}
//
//	}
//	}
//}


