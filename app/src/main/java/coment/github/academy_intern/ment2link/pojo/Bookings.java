package coment.github.academy_intern.ment2link.pojo;

/**
 * Created by team_leader on 2018/03/05.
 */

public class Bookings {

    private String date;
    private String booking_mentee_uid;
    private String booking_mentor_uid;
    private String session_details;
    private String time;
    private String details;

    public Bookings() {
    }

    public Bookings(String date, String booking_mentee_uid, String booking_mentor_uid, String session_details, String time) {
        this.date = date;
        this.booking_mentee_uid = booking_mentee_uid;
        this.booking_mentor_uid = booking_mentor_uid;
        this.session_details = session_details;
        this.time = time;
    }

    public Bookings(String date, String booking_mentee_uid, String booking_mentor_uid, String session_details, String time, String details) {
        this.date = date;
        this.booking_mentee_uid = booking_mentee_uid;
        this.booking_mentor_uid = booking_mentor_uid;
        this.session_details = session_details;
        this.time = time;
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBooking_mentee_uid() {
        return booking_mentee_uid;
    }

    public void setBooking_mentee_uid(String booking_mentee_uid) {
        this.booking_mentee_uid = booking_mentee_uid;
    }

    public String getBooking_mentor_uid() {
        return booking_mentor_uid;
    }

    public void setBooking_mentor_uid(String booking_mentor_uid) {
        this.booking_mentor_uid = booking_mentor_uid;
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

    public void setTime(String time) {
        this.time = time;
    }
}
