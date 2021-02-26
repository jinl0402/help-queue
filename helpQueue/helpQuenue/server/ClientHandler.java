/*
 * ClientHandler - Java implementation of ClientHandler class under Server class for multi-threaded chat server 
 *
 * from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 */
package helpQuenue.server;



import helpQuenue.dao.Event;
import helpQuenue.dao.HelpQueue;
import helpQuenue.dao.HelpQueueDAO;


import java.io.*;
import java.util.*;
import java.net.*;


//ClientHandler class
public class ClientHandler implements Runnable {
	// data
	//Scanner scn = new Scanner(System.in); 		// ???
	//private String name; 							// client name
	String name;                                    // client name
	String type;
	final DataInputStream dis;                        // input stream for this client
	final DataOutputStream dos;                    // output stream for this client
	Socket s;                                        // socket for this client
	boolean isloggedin;                            // flag, whether client is currently connected
	boolean exit = false;

	// constructor
	public ClientHandler(Socket s, String name, String type, DataInputStream dis, DataOutputStream dos) throws IOException {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.type = type;
		this.s = s;
		this.isloggedin = true;

		if (type.equals("client")) {
			dos.writeUTF("Status: Not Queued#Server Message: Welcome to P115 Lab , " + name);
		}

	}    // end - constructor

	// run method - called when thread starts
    @Override
    public void run() {
        String received;
        while (!exit) {
            try {
                // receive the string
                received = dis.readUTF();

                // TODO: need to fully handle thread disconnecting; remove from server list
                if (received.equals("logout")) {
                    this.s.close();
                    if (type.equals("client")) {
                        Server.client.remove(this);
                        Server.print("Client: " + name + " logout");
                    } else if (type.equals("display")) {
                        Server.display.remove(this);
                        Server.print("Display: " + name + " logout");
                    } else {
                        Server.admin.remove(this);
                        Server.print("Administrator: " + name + " logout");
                    }
                    Server.count();
                    break;
                }

                if (type.equals("client") && received.equals("help")) {
                    Server.print("Client: " + name + " ask for Help\r\n");

                    HelpQueueDAO dao = new HelpQueueDAO();
                    HelpQueue item = dao.getHelpQueueUser(name);
                    if (item == null) {
                        item = new HelpQueue();
                        item.setWorkStationName(name);
                        item.setRequestTime(new Date());
                        item.setStatus("Queuing");

                        dao.addHelpQueueClient(item);
                        dos.writeUTF("Status: Queuing#Server Message: You are added to the queue.");
                    } else {
                        if (item.getStatus().equals("Queuing")) {
                            dos.writeUTF("Status: Queuing#Server Message: You are already in the help queue, please wait.");
                        } else {
                            item.setRequestTime(new Date());
                            item.setStatus("Queuing");
                            dao.requestHelp(item);
                            dos.writeUTF("Status: Queuing#Server Message: You are added to the queue.");
                        }
                    }

                    //Save log
                    Event e =new Event();
                    e.setId(UUID.randomUUID().toString());
                    e.setEventType("help");
//                    System.out.println(e.getEventType());
                    e.setWorkStationName(item.getWorkStationName());
//                    System.out.println(e.getWorkStationName());
                    e.setOriginator("client");
//                    System.out.println(e.getOriginator());
                    e.setCourseNum(dao.getCurrentCourse().split("#")[1]);
//                    System.out.println(e.getCourseNum());
                    e.setSectionNum(dao.getCurrentCourse().split("#")[2]);
//                    System.out.println(e.getSectionNum());
                    e.setEventTime(new Date());
//                    System.out.println(e.getEventTime());
                    dao.saveRequestEvent(e);

                    // refresh display
                    Server.refreshDisplay();
                }

                if (type.equals("client") && received.equals("cancel")) {
                    Server.print("Client: " + name + " cancel help request\r\n");

                    HelpQueueDAO dao = new HelpQueueDAO();
                    HelpQueue item = dao.getHelpQueueUser(name);
                    if (item == null) {
                        dos.writeUTF("Status: Not Queued#Server Message: You are NOT in the help queue.");
                    } else {
                        if (item.getStatus().equals("Queuing")) {
                            item.setCancelTime(new Date());
                            item.setStatus("Not Queued");
                            dao.cancelHelp(item);

                            dos.writeUTF( "Status: Not Queued#Server Message: You are removed from the help queue. If you need help , please request again.");
                        } else {
                            dos.writeUTF("Status: Not Queued#Server Message: You are NOT in the help queue.");
                        }
                    }

                    //Save log
                    Event e =new Event();
                    e.setId(UUID.randomUUID().toString());
                    e.setEventType("cancel");
                    e.setWorkStationName(item.getWorkStationName());
                    e.setOriginator("client");
                    e.setCourseNum(dao.getCurrentCourse().split("#")[1]);
                    e.setSectionNum(dao.getCurrentCourse().split("#")[2]);
                    e.setEventTime(new Date());
                    dao.saveCancelEvent(e, item.getRequestTime());

                    // refresh display
                    Server.refreshDisplay();
                }

                if (type.equals("admin") && received.startsWith("cancel")) {
                    String workStationName = received.split("#")[1];
                    Server.print("Admin: " + name + " cancel help request. WorkStationName - "
                            + workStationName + " \r\n");

                    HelpQueueDAO dao = new HelpQueueDAO();
                    HelpQueue item = dao.getHelpQueueUser(workStationName);
                    if (item == null) {
                        dos.writeUTF("Server Message: " + workStationName + " is NOT in the help queue.");
                    } else {
                        if (item.getStatus().equals("Queuing")) {
                            item.setCancelTime(new Date());
                            item.setStatus("Not Queued");
                            dao.cancelHelp(item);

                            //Save log
                            Event e =new Event();
                            e.setId(UUID.randomUUID().toString());
                            e.setEventType("cancel");
                            e.setWorkStationName(item.getWorkStationName());
                            e.setOriginator("admin");
                            e.setCourseNum(dao.getCurrentCourse().split("#")[1]);
                            e.setSectionNum(dao.getCurrentCourse().split("#")[2]);
                            e.setEventTime(new Date());
                            dao.saveCancelEvent(e, item.getRequestTime());

                            dos.writeUTF("Server Message: " + workStationName + " is removed from the help queue.");
                        } else {
                            dos.writeUTF("Server Message: " + workStationName + " is NOT in the help queue.");
                        }
                    }

                    for (ClientHandler ch: Server.client) {
                        if (ch.name.equals(workStationName)) {
                            ch.dos.writeUTF( "Status: Not Queued#Server Message: You are removed from the help queue by Administrator.");
                        }
                    }

                    // refresh display
                    Server.refreshDisplay();
                }

                if (type.equals("admin") && received.startsWith("clear")) {
                    HelpQueueDAO dao = new HelpQueueDAO();

                    List<HelpQueue> queue = dao.getHelpQueue();
                    dao.clearClientRequest();

                    for (HelpQueue item : queue) {

                        //Save log
                        Event e =new Event();
                        e.setId(UUID.randomUUID().toString());
                        e.setEventType("cancel");
                        e.setWorkStationName(item.getWorkStationName());
                        e.setOriginator("admin");
                        e.setCourseNum(dao.getCurrentCourse().split("#")[1]);
                        e.setSectionNum(dao.getCurrentCourse().split("#")[2]);
                        e.setEventTime(new Date());
                        dao.saveCancelEvent(e, item.getRequestTime());

                        for (ClientHandler ch : Server.client) {
                            if (ch.name.equals(item.getWorkStationName())) {
                                ch.dos.writeUTF( "Status: Not Queued#Server Message: You are removed from the help queue by Administrator.");
                            }
                        }
                    }

                    dos.writeUTF("Server Message: The help queue is cleared.");

                    // refresh display
                    Server.refreshDisplay();
                }

                if (type.equals("admin") && received.startsWith("course")) {
                    System.out.println(received);
                    String courseStr = received.split("#")[1];
                    System.out.println(courseStr);
                    HelpQueueDAO dao = new HelpQueueDAO();
                    dao.saveCourse(courseStr);

                    dos.writeUTF("Server Message: Course saved.");
                }

                if (type.equals("admin") && received.startsWith("view")) {
                    HelpQueueDAO dao = new HelpQueueDAO();
                    String msg=dao.viewCourse();
                    dos.writeUTF(msg);
                }

                if (type.equals("admin") && received.startsWith("deletecourse")) {
                    HelpQueueDAO dao = new HelpQueueDAO();
                    String courseID = received.split("#")[1];
                    dao.deleteCourse(courseID);

                    String msg=dao.viewCourse();
                    dos.writeUTF("Server Message: Course delete success.\r\n Current Course:\r\n"+msg);
                }


//
//				// break the string into message and recipient part
//				StringTokenizer st = new StringTokenizer(received, "#");
//				String MsgToSend = st.nextToken();
//				String recipient = st.nextToken();
//
//				// search for the recipient in the connected devices list from global Server class.
//				// ar is the vector storing client of active users
//				// TODO: this search could be slow if large number of clients - could use HashMap
//				for (ClientHandler mc : Server.client) {
//					// if the recipient is found, write on its output stream
//					if (mc.name.equals(recipient) && mc.isloggedin == true) {
//						mc.dos.writeUTF(this.name + " : " + MsgToSend);
//						break;
//					}
//				}


			} catch (Exception e) {
				try {
					this.dis.close();
					this.dos.close();
					s.close();
					exit = true;
				} catch (IOException ex) {

				}
			}
		}
	}
}
//		// closing resources
//		try {
//			this.dis.close();
//			this.dos.close();
//		} catch(IOException e) {
//			System.out.println("ClientHandler, closing resources section");
//			e.printStackTrace();
//		}
//	} 	// end - method run
//
//}	// end - class ClientHandler
