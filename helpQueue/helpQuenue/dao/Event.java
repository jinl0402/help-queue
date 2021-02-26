package helpQuenue.dao;

import java.util.Date;

public class Event {
    private String id;
    private String eventType;
    private String workStationName;
    private String originator;
    private String courseNum;
    private String sectionNum;
    private Date eventTime;

    public Event(){}


    public Event(String id, String eventType, String workStationName, String originator, String courseNum, String sectionNum, Date eventTime) {
        this.id = id;
        this.eventType = eventType;
        this.workStationName = workStationName;
        this.originator = originator;
        this.courseNum = courseNum;
        this.sectionNum = sectionNum;
        this.eventTime = eventTime;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getWorkStationName() {
        return workStationName;
    }

    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}

