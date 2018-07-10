package coment.github.academy_intern.ment2link.pojo;

/**
 * Created by team_leader on 2018/03/13.
 */

public class Blocked {

    private String blocked_mentee_uid;
    private String blocking_mentor_uid;
    private String name;
    private String surname;
    private String imageUrl;


    public Blocked (String blocked_mentee_uid, String blocking_mentor_uid,String name,String surname,String imageUrl) {
        this.blocked_mentee_uid = blocked_mentee_uid;
        this.blocking_mentor_uid = blocking_mentor_uid;
        this.name = name;
        this.surname = surname;
        this.imageUrl = imageUrl;

    }

    public Blocked() {
    }

    public String getBlocked_mentee_uid() {
        return blocked_mentee_uid;
    }

    public void setBlocked_mentee_uid(String blocked_mentee_uid) {
        this.blocked_mentee_uid = blocked_mentee_uid;
    }

    public String getBlocking_mentor_uid() {
        return blocking_mentor_uid;
    }

    public void setBlocking_mentor_uid(String blocked_mentor_uid) {
        this.blocking_mentor_uid = blocked_mentor_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
