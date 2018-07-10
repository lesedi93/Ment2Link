package coment.github.academy_intern.ment2link.pojo;

/**
 * Created by team_leader on 2018/03/02.
 */

public class Request {

    private String date;
    private String mentee_uid;
    private String mentor_uid;
    private String reason;
    private String time;

    public Request() {
    }

    public Request(String date, String mentee_uid, String mentor_uid, String reason, String time) {
        this.date = date;
        this.mentee_uid = mentee_uid;
        this.mentor_uid = mentor_uid;
        this.reason = reason;
        this.time = time;
    }

    @Override
    public String toString() {
        return date + "          " + reason + "          " + time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
