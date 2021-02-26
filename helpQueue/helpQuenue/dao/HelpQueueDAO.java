package helpQuenue.dao;

import helpQuenue.server.Server;
import javafx.geometry.Orientation;

import java.lang.ref.Cleaner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelpQueueDAO {
    private static DataAccessObject dao = new DataAccessObject();//for all calls to the database
    private static ResultSet daoRset = null; // used for all calls to executeSQLQuery()
    private List<HelpQueue> helpQueueList = new ArrayList<>();
    private String workStationName = "";
    private Date requestTime = null;
    private Date cancelTime = null;
    private String status = "";
    private String waitTime =null;
    private String courseID;
    private String courseNum;
    private String courseSection;
    private Date courseStartTime;
    private Date courseEndTime;
    private String dayOfWeek;

    //get HelpQueue object, return as list
    public List<HelpQueue> getHelpQueue() throws SQLException {
        String sql = "SELECT * FROM HelpQueue ORDER BY requestTime" ;
        dao.connect();
        dao.setAutoCommit(true);
        try{
            daoRset = dao.executeSQLQuery(sql);
            while(daoRset.next()){
                workStationName = daoRset.getString("workStationName");
                requestTime = daoRset.getTime("requestTime");
                cancelTime = daoRset.getTime("cancelTime");
                status = daoRset.getString("status");
                waitTime = daoRset.getString("waitTime");

                String wait = waitTime.split(" ")[1];
                String[] time = wait.split(":");
                waitTime = String.format("%03d", Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1])) + ":"
                        + String.format("%02d", (int) Double.parseDouble(time[2]));

                HelpQueue helpQueue = new HelpQueue(workStationName,requestTime,cancelTime,status,waitTime);
                helpQueueList.add(helpQueue);
            }
            dao.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return helpQueueList;
    }

    //Takes in a workstation name, get the user information(workStationName, requestTime, cancelTime, and waitTime), return as a HelpQueue Object
    public HelpQueue getHelpQueueUser(String name) throws Exception{
        String sql = "SELECT * FROM HelpQueue WHERE workStationName = '" + name + "'";
        HelpQueue helpQueue = null;
        dao.connect();
//        dao.setAutoCommit(true);
        try{
            daoRset = dao.executeSQLQuery(sql);
            while(daoRset.next()){
                workStationName = daoRset.getString("workStationName");
                requestTime = daoRset.getTime("requestTime");
                cancelTime = daoRset.getTime("cancelTime");
                status = daoRset.getString("status");
                waitTime = daoRset.getString("waitTime");

                helpQueue = new HelpQueue(workStationName,requestTime,cancelTime,status,waitTime);

            }
            dao.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return helpQueue;
    }

//    //add a client in if user doesn't exist
//    public void addHelpQueueClient(HelpQueue helpQueue) throws SQLException {
//
//        //current Date and time
//        String currDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(helpQueue.getRequestTime());
//        String name = helpQueue.getWorkStationName();
//
//        String sql = "INSERT INTO HelpQueue (WORKSTATIONNAME, REQUESTTIME,STATUS) " + " VALUES ('"
//                + name + "', to_date('" + currDate + "','yyyy-mm-dd hh24:mi:ss')" + ",'Queuing')";
//        dao.connect();
//        dao.setAutoCommit(false);
//        dao.executeSQLNonQuery(sql);
//        dao.disconnect();
//    }

    public boolean addHelpQueueClient(HelpQueue helpQueue) throws Exception{
        workStationName = helpQueue.getWorkStationName();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(helpQueue.getRequestTime());
        String sql = "INSERT INTO HelpQueue (WorkStationName, requestTime,status) " + " VALUES ('"
                + workStationName + "', to_date('" + date + "','yyyy-mm-dd hh24:mi:ss')" + ",'Queuing')";

        dao.connect();
        dao.setAutoCommit(true);
        boolean result = dao.executeSQL(sql);
        dao.disconnect();
        return result;
    }


    // client request for help
    public void requestHelp(HelpQueue helpQueue) throws SQLException{
        //current Date and time
        String currDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(helpQueue.getRequestTime());
        String name = helpQueue.getWorkStationName();
        String sql = "UPDATE HelpQueue SET requestTime = to_date('" + currDate + "','yyyy-mm-dd hh24:mi:ss')"
                + " , status ='Queuing' WHERE workStationName='" + name + "'";
        dao.connect();
        dao.setAutoCommit(true);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();
    }


    //client cancel help
    public void cancelHelp(HelpQueue helpQueue) throws SQLException{
        //current Date and time
        String currDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(helpQueue.getRequestTime());
        String name = helpQueue.getWorkStationName();
        String sql = "UPDATE HelpQueue SET waitTime = to_date('" + currDate + "','yyyy-mm-dd hh24:mi:ss')"
                + " , status ='Not Queued' WHERE workStationName='" + name + "'";
        try{
            dao.connect();
            dao.setAutoCommit(true);
            dao.executeSQLNonQuery(sql);
            dao.disconnect();
        }catch (SQLException sq){
            System.out.println("see cancelHelp in HelpQueueDAO");
            sq.printStackTrace();
        }

    }

    //delete Course
    public void deleteCourse(String courseID) throws Exception {
        // TODO Auto-generated method stub
        String sql = "Delete From Course where courseID= '" + courseID + "'";
        dao.connect();
        dao.setAutoCommit(true);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();
    }


    // save course
    public void saveCourse(String courseStr) throws Exception {
        String[] arr = courseStr.split("&");

        String sql = "INSERT INTO COURSE (" + "	courseID," + " courseNum," + "	section," + "	startDate,"
                + "	endDate," + "	dayOfWeek," + "	startTime," + "	endTime" + ")" + "VALUES" + "	('"
                + UUID.randomUUID().toString() + "'," + "		'" + arr[0] + "'," + "		" + arr[1] + ","
                + "		TO_DATE ('" + arr[2] + "', 'yyyy-mm-dd')," + "		TO_DATE ('" + arr[3] + "', 'yyyy-mm-dd'),"
                + "		'" + arr[4] + "'," + "		TO_DATE ('" + arr[5] + "', 'hh24:mi')," + "		TO_DATE ('" + arr[6]
                + "', 'hh24:mi')" + "	)";

        dao.connect();
        dao.setAutoCommit(true);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();
    }

//    public String viewCourse() {
//        String sql = "select * from Course";
//        String msg = "";
//    }

    //update waitTime
    public void updateWaitTime() throws SQLException {
        String wait = "Sysdate - requestTime";
        String sql = "UPDATE HelpQueue SET waitTime = (" + wait + ")" +
                    "WHERE status = 'Queuing'";
        dao.connect();
        dao.setAutoCommit(true);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();
    }

    // clear all help requests
    public void clearClientRequest() throws SQLException{
        String sql = "UPDATE HelpQueue SET status = 'Not Queued'";
        dao.connect();
        dao.setAutoCommit(false);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();
    }

    public String getCurrentCourse() {
        String sql = "SELECT * FROM COURSE WHERE SYSDATE <= endDate "
                + "AND SYSDATE >= startDate  AND  dayOfWeek ='" + getDayOfWeek(new Date()) + "'";
        dao.connect();
        dao.setAutoCommit(true);
        try{
            daoRset = dao.executeSQLQuery(sql);

            while(daoRset.next()){
                courseID = daoRset.getString("courseID");
                courseNum = daoRset.getString("courseNum");
                courseSection  = daoRset.getString("section");
                courseStartTime = daoRset.getTime("startTime");
                courseEndTime = daoRset.getTime("endTime");

                Date currDate = new Date();
                String date =  new SimpleDateFormat("yyyy-MM-dd").format(currDate);
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                courseStartTime= fm.parse(date + " " + courseStartTime.toString());
                courseEndTime = fm.parse(date + " " + courseEndTime.toString());

                if (currDate.after(courseStartTime) && currDate.before(courseEndTime)) {
                    return courseID + "#" + courseNum + "#" + courseSection;
                }
            }

            dao.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //return the day of the week as String
    public static String getDayOfWeek(Date date){
        String[] weekDays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        Calendar calendar = Calendar.getInstance();
        //set the time to be current date time
        calendar.setTime(date);
        //get the day of the week and -1 for Sunday.
        int i = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(i < 0){
            i=0;
        }
        return weekDays[i];
    }

    //save help request event
    public void saveRequestEvent(Event event) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String eventTime = sdf.format(event.getEventTime());

        String sql = "INSERT INTO Event (" + "	eventID," + "	eventType," + "	workStationName,"
                + "	originator," + "	courseNum," + "	sectionNum," + "	eventTime  )"
                + "VALUES" + " ('" + event.getId() + "', '" + event.getEventType() + "'," + "	 '"
                + event.getWorkStationName() + "','" + event.getOriginator() + "'," + " '" + event.getCourseNum()+ "', "
                + event.getSectionNum() + "," + " TO_DATE ('" + eventTime + "',"
                + "	'yyyy-mm-dd hh24:mi:ss' ) )";
        dao.connect();
        dao.setAutoCommit(true);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();

    }

    // view courses
    public String viewCourse(){
        String sql = "SELECT * FROM Course";
        dao.connect();
        dao.setAutoCommit(true);
        String message = "";

        try{
            daoRset = dao.executeSQLQuery(sql);
            while (daoRset.next()){
                courseID = daoRset.getString("courseID");
                courseNum = daoRset.getString("courseNum");
                courseSection = daoRset.getString("section");
                Date startDate = daoRset.getDate("startDate");
                Date endDate = daoRset.getDate("endDate");
                dayOfWeek = daoRset.getString("dayOfWeek");
                Date startTime = daoRset.getTime("startTime");
                Date endTime = daoRset.getTime("endTime");

                message = message + String.format("%-38s", courseID) + String.format("%-10s", courseNum)
                        + String.format("%-9s", courseSection) + String.format("%-11s", startDate)
                        + String.format("%-11s", endDate) + String.format("%-12s", dayOfWeek)
                        + String.format("%-12s", new SimpleDateFormat("HH:mm").format(startTime))
                        + String.format("%-12s", new SimpleDateFormat("HH:mm").format(endTime)) + "\r\n";
            }

            if (!"".equals(message)) {
                message = String.format("%-38s", "ID") + String.format("%-10s", "courseNum")
                        + String.format("%-9s", "section") + String.format("%-11s", "startDate")
                        + String.format("%-11s", "endDate") + String.format("%-12s", "dayOfWeek")
                        + String.format("%-12s", "startTime") + String.format("%-12s", "endTime") + "\r\n" + message;
            }

            dao.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }



    public void saveCancelEvent(Event event, Date requestTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        String eventTime = sdf.format(event.getEventTime());

        long time1 = sd.parse(sd.format(event.getEventTime())).getTime();
        long time2 = requestTime.getTime();
        long test = Math.abs(time2 - time1);
        Date result = new Date();
        result.setTime(test);

        waitTime = sd.format(result);


        String sql = "INSERT INTO EVENT (" + "	eventID," + "	eventType," + "	workStationName,"
                + "	originator," + "	courseNum," + "	sectionNum," + "	eventTime,"
                + "	waitTime" + ")" + "VALUES" + " ('" + event.getId() + "', '" + event.getEventType() + "'," + "	 '"
                + event.getWorkStationName() + "','" + event.getOriginator() + "'," + " '" + event.getCourseNum() + "', "
                + event.getSectionNum() + "," + " TO_DATE ('" + eventTime + "',"
                + "	'yyyy-mm-dd hh24:mi:ss' ), INTERVAL '" + waitTime + "' HOUR TO SECOND )";
        Server.print(sql);
        dao.connect();
        dao.setAutoCommit(true);
        dao.executeSQLNonQuery(sql);
        dao.disconnect();
    }

}
