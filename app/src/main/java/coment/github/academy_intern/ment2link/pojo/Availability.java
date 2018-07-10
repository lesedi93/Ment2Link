package coment.github.academy_intern.ment2link.pojo;

/**
 * Created by team_leader on 2018/02/27.
 */

public class Availability {
    private String uid;
    private String title;
    private String dtstart;
    private String dtend;
    private Boolean all_day;
    private String event_end_timezone;
    private String event_timezone;
    private String calendar_id;

    public Availability() {
    }

    public Availability(String uid, String title, String dtstart, String dtend, Boolean all_day, String event_end_timezone, String event_timezone, String calendar_id) {
        this.uid = uid;
        this.title = title;
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.all_day = all_day;
        this.event_end_timezone = event_end_timezone;
        this.event_timezone = event_timezone;
        this.calendar_id = calendar_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDtstart() {
        return dtstart;
    }

    public void setDtstart(String dtstart) {
        this.dtstart = dtstart;
    }

    public String getDtend() {
        return dtend;
    }

    public void setDtend(String dtend) {
        this.dtend = dtend;
    }

    public Boolean getAll_day() {
        return all_day;
    }

    public void setAll_day(Boolean all_day) {
        this.all_day = all_day;
    }

    public String getEvent_end_timezone() {
        return event_end_timezone;
    }

    public void setEvent_end_timezone(String event_end_timezone) {
        this.event_end_timezone = event_end_timezone;
    }

    public String getEvent_timezone() {
        return event_timezone;
    }

    public void setEvent_timezone(String event_timezone) {
        this.event_timezone = event_timezone;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }
}
