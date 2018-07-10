package coment.github.academy_intern.ment2link.pojo;

/**
 * Created by team_leader on 2018/02/26.
 */

public class MentorProfile {

    private String uid;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String bio;
    private String location;
    private String field_of_study;
    private String imageUrl;

    public MentorProfile() {
    }


    public MentorProfile(String uid, String imageUrl,String name, String surname, String email) {
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.surname = surname;
        this.email = email;

    }

    public MentorProfile(String uid, String imageUrl, String name, String surname, String email, String bio, String location, String field_of_study) {
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.field_of_study = field_of_study;
    }

    public MentorProfile(String uid, String imageUrl, String name, String surname, String role, String e_mail, String bio, String location, String field_of_study) {
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.field_of_study = field_of_study;
    }

    public String getRole() {
        return role;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getField_of_study() {
        return field_of_study;
    }

    public void setField_of_study(String field_of_study) {
        this.field_of_study = field_of_study;
    }

    @Override
    public String toString() {
        return "MentorProfile{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", e_mail='" + email + '\'' +
                ", role='" + role + '\'' +
                ", bio='" + bio + '\'' +
                ", location='" + location + '\'' +
                ", field_of_study='" + field_of_study + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
