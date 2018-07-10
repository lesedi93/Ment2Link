package coment.github.academy_intern.ment2link.pojo;

/**
 * Created by team_leader on 2018/03/01.
 */

public class UpcomingAppointment {

    private String mentee_uid;
    private String mentor_uid;
    private String session_details;
    private String time;

    public UpcomingAppointment() {

    }

    public UpcomingAppointment(String mentee_uid,String mentor_uid, String session_details, String time) {
        this.mentee_uid = mentee_uid;
        this.mentor_uid=mentor_uid;
        this.session_details = session_details;
        this.time = time;
    }

    public String getMentee_uid() {
        return mentee_uid;
    }

    public void setMentee_uid(String mentee_uid) {
        this.mentee_uid = mentee_uid;
    }

    public String getMentor_uid() {
        return mentor_uid;
    }

    public void setMentor_uid(String mentor_uid) {
        this.mentor_uid = mentor_uid;
    }

    public String getSession_details() {
        return session_details;
    }

    public void setSession_details(String session_details) {
        this.session_details = session_details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String start_time) {
        this.time = start_time;
    }
}