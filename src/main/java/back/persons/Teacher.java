package back.persons;

import back.Messages.MessagesForTeacher;
import back.enums.College;
import back.enums.StatusTeacher;
import back.enums.TeacherDegree;
import com.google.gson.Gson;
import javafx.scene.paint.Color;

import java.util.*;


public class Teacher extends Person{

    private String username = "";
    private StatusTeacher statusTeacher;
    private LinkedList<String> courses;
    private TeacherDegree teacherDegree;
    private int roomNumber;
    private MessagesForTeacher messagesForTeacher = new MessagesForTeacher();

    private LinkedList<Integer> extraStudentsInChat = new LinkedList<>();
    private LinkedList<Integer> extraTeachersInChat = new LinkedList<>();

    public Teacher(int id, Color color, String image, String firstname, String lastname, String email, String password, String phoneNumber, String CodeMelli, College college, StatusTeacher statusTeacher, LinkedList<String> courses, TeacherDegree teacherDegree, String username, int roomNumber) {
        super(id, color, image, firstname, lastname, email, password, phoneNumber, CodeMelli, college);
        this.statusTeacher = statusTeacher;
        this.courses = courses;
        this.teacherDegree = teacherDegree;
        this.username = username;
        this.roomNumber = roomNumber;

    }


//    Getter and Setters:


    public StatusTeacher getStatusTeacher() {
        return statusTeacher;
    }

    public void setStatusTeacher(StatusTeacher statusTeacher) {
        this.statusTeacher = statusTeacher;
    }

    public LinkedList<String> getCourses() {
        return courses;
    }

    public void setCourses(LinkedList<String> courses) {
        this.courses = courses;
    }

    public TeacherDegree getTeacherDegree() {
        return teacherDegree;
    }

    public void setTeacherDegree(TeacherDegree teacherDegree) {
        this.teacherDegree = teacherDegree;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public MessagesForTeacher getMessagesForEA() {
        return messagesForTeacher;
    }

    public MessagesForTeacher getMessagesForTeacher() {
        return messagesForTeacher;
    }

    public void setMessagesForTeacher(MessagesForTeacher messagesForTeacher) {
        this.messagesForTeacher = messagesForTeacher;
    }

    public LinkedList<Integer> getExtraStudentsInChat() {
        return extraStudentsInChat;
    }

    public void setExtraStudentsInChat(LinkedList<Integer> extraStudentsInChat) {
        this.extraStudentsInChat = extraStudentsInChat;
    }

    public LinkedList<Integer> getExtraTeachersInChat() {
        return extraTeachersInChat;
    }

    public void setExtraTeachersInChat(LinkedList<Integer> extraTeachersInChat) {
        this.extraTeachersInChat = extraTeachersInChat;
    }


    //    Load - ToString - Save

    public String toString() {
        String result =  "#" + Integer.toHexString(color.hashCode()) + ":::" +
                this.image + ":::" +
                this.getFirstname() + ":::" + this.getLastname() + ":::" + this.getEmail() + ":::" + this.getPassword()+ ":::" + this.getPhoneNumber() + ":::" +
                this.getCodeMelli() + ":::" + this.getCollege() + ":::" + this.statusTeacher + ":::";
        if(courses == null){
            result = result + 0 + ":::";
        }
        else{
            result = result + courses.size() + ":::";
            for (int i = 0; i < courses.size(); i++) {
                result = result + courses.get(i) + ":::";
            }
        }


        result = result + teacherDegree + ":::" + username + ":::" + roomNumber + ":::" + getId() + ":::" + getTimesLogin() + ":::" + getLastYearLogin() + ":::" + getLastMonthLogin() + ":::" + getLastDayLogin() + ":::" + getLastHourLogin() + ":::" + getLastMinuteLogin() + ":::" + getLastSecondLogin() + ":::";;
        return result + "\n";
    }

    public String passSQLStringForSaveWithId(){
        String username = this.username;
        String statusTeacher = this.statusTeacher + "";

        String courses = "";
        if(this.courses == null){
            courses = "0:::";
        }
        else{
            courses = this.courses.size() + ":::";

            for (int j = 0; j < this.courses.size(); j++) {
                if(j == this.courses.size() - 1){
                    courses = courses + this.courses.get(j)+ "";
                    break;
                }
                courses = courses + this.courses.get(j) + ":::";
            }
        }

        String teacherDegree = this.teacherDegree + "";
        int roomNumber = this.roomNumber;


        String message = (new Gson()).toJson(messagesForTeacher);

        String studentsInC = passStringFormGenericTypes(extraStudentsInChat);
        String teachersInC = passStringFormGenericTypes(extraTeachersInChat);

        String str = passSQLStringForSavePersonWithId() + ", '" + username  + "', '" + statusTeacher + "', '" + courses +  "', '" + teacherDegree + "', " + roomNumber + ", '" + message +  "' , '" + studentsInC + "', '" +  teachersInC + "'";

        return str;
    }
    public String passSQLStringForSaveWithoutId(){
        String username = this.username;
        String statusTeacher = this.statusTeacher + "";

        String courses = "";
        if(this.courses == null){
            courses = "0:::";
        }
        else{
            courses = this.courses.size() + ":::";

            for (int j = 0; j < this.courses.size(); j++) {
                if(j == this.courses.size() - 1){
                    courses = courses + this.courses.get(j)+ "";
                    break;
                }
                courses = courses + this.courses.get(j) + ":::";
            }
        }

        String teacherDegree = this.teacherDegree + "";
        int roomNumber = this.roomNumber;

        String message = (new Gson()).toJson(messagesForTeacher);

        String studentsInC = passStringFormGenericTypes(extraStudentsInChat);
        String teachersInC = passStringFormGenericTypes(extraTeachersInChat);

        String str = passSQLStringForSavePersonWithoutId() + ", '" + username  + "', '" + statusTeacher + "', '" + courses + "', '" + teacherDegree + "', " + roomNumber + ", '" + message +  "' , '" + studentsInC + "', '" +  teachersInC + "'";

        return str;
    }

    public <T> String passStringFormGenericTypes(LinkedList<T >t){
        String groups = "";
        if(t == null){
            groups = "0:::";
        }
        else{
            groups = t.size() + ":::";

            for (int j = 0; j < t.size(); j++) {
                if(j == t.size() - 1){
                    groups = groups + t.get(j);
                    break;
                }
                groups = groups + t.get(j) + ":::";
            }
        }

        return groups;
    }


}


