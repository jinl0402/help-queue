package helpQuenue.dao;

import java.util.Date;

/*
 * This class gets/sets client's workStationName, requestTime, cancelTime, status, and their waitTime
 */
public class HelpQueue {
    private String workStationName;
    private Date requestTime;
    private Date cancelTime;
    private String status;
    private String waitTime;

    public HelpQueue(){}


    public HelpQueue(String workStationName, Date requestTime, Date cancelTime,String status, String waitTime){
        this.workStationName = workStationName;
        this.requestTime = requestTime;
        this.cancelTime = cancelTime;
        this.status = status;
        this.waitTime = waitTime;
    }

    public String getWorkStationName() {
        return workStationName;
    }

    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
