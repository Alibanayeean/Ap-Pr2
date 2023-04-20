package back.persons;

import back.enums.*;
import com.google.gson.Gson;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Person {
    protected int id;
    public Color color;
    public String image;
    private String Firstname;
    private String Lastname;
    private String email;
    private String password;
    private String phoneNumber;
    private String CodeMelli;
    private College college;

    private int lastSecondLogin;
    private int lastMinuteLogin;
    private int lastHourLogin;
    private int lastDayLogin;
    private int lastMonthLogin;
    private int lastYearLogin;
    private int TimesLogin;

    public Person(int id, Color color, String image, String firstname, String lastname, String email, String password, String phoneNumber, String CodeMelli, College college) {
        this.color = color;
        this.image = image;
        this.id = id;
        Firstname = firstname;
        Lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.CodeMelli = CodeMelli;
        this.college = college;
        TimesLogin = 0;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCodeMelli() {
        return CodeMelli;
    }

    public void setCodeMelli(String codeMelli) {
        CodeMelli = codeMelli;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public int getLastHourLogin() {
        return lastHourLogin;
    }

    public void setLastHourLogin(int lastHourLogin) {
        this.lastHourLogin = lastHourLogin;
    }

    public int getLastSecondLogin() {
        return lastSecondLogin;
    }

    public void setLastSecondLogin(int lastSecondLogin) {
        this.lastSecondLogin = lastSecondLogin;
    }

    public int getLastMinuteLogin() {
        return lastMinuteLogin;
    }

    public void setLastMinuteLogin(int lastMinuteLogin) {
        this.lastMinuteLogin = lastMinuteLogin;
    }

    public int getLastDayLogin() {
        return lastDayLogin;
    }

    public void setLastDayLogin(int lastDayLogin) {
        this.lastDayLogin = lastDayLogin;
    }

    public int getLastMonthLogin() {
        return lastMonthLogin;
    }

    public void setLastMonthLogin(int lastMonthLogin) {
        this.lastMonthLogin = lastMonthLogin;
    }

    public int getLastYearLogin() {
        return lastYearLogin;
    }

    public void setLastYearLogin(int lastYearLogin) {
        this.lastYearLogin = lastYearLogin;
    }

    public int getTimesLogin() {
        return TimesLogin;
    }

    public void setTimesLogin(int timesLogin) {
        TimesLogin = timesLogin;
    }


    public College StringToCollege(String s){

        if(s.toLowerCase().equals("EE".toLowerCase())){
            return College.EE;
        }
        else if(s.toLowerCase().equals("CE".toLowerCase())){
            return College.CE;
        }
        else if(s.toLowerCase().equals("CS".toLowerCase())){
            return College.CS;
        }
        else if(s.toLowerCase().equals("Math".toLowerCase())){
            return College.Math;
        }
        else if(s.toLowerCase().equals("Physics".toLowerCase())){
            return College.Physics;
        }
        else if(s.toLowerCase().equals("Ch".toLowerCase())){
            return College.Ch;
        }
        else if(s.toLowerCase().equals("Mechanic".toLowerCase())){
            return College.Mechanic;
        }
        else if(s.toLowerCase().equals("Public".toLowerCase())){
            return College.Public;
        }
        else{
            return null;
        }
    }

    public Grade StringToGrade(String s){

        if(s.equals("BC")){
            return Grade.BC;
        }
        else if(s.equals("MS")){
            return Grade.MS;
        }
        else if(s.equals("PHD")){
            return Grade.PHD;
        }
        else{
            return null;
        }
    }

    public EducationStatus StringToEducationStatus(String s){
        if(s.equals("Studying")){
            return EducationStatus.Studying;
        }
        else if(s.equals("Graduated")){
            return EducationStatus.Graduated;
        }
        else if(s.equals("Refuse")){
            return EducationStatus.Refuse;
        }
        else if(s.equals("Wait")){
            return EducationStatus.Wait;

        }
        else{
            return null;
        }
    }

    public TeacherDegree StringToTeacherDegree(String s){

        if(s.equals("AssistantProfessor")){
            return TeacherDegree.AssistantProfessor;
        }
        else if(s.equals("AssociateProfessor")){
            return TeacherDegree.AssociateProfessor;
        }
        else if(s.equals("FullProfessor")){
            return TeacherDegree.FullProfessor;
        }
        else{
            return null;
        }
    }

    public StatusTeacher StringToStatusTeacher(String s){

        if(s.equals("EA")){
            return StatusTeacher.EA;
        }
        else if(s.equals("Simple")){
            return StatusTeacher.Simple;
        }
        else if(s.equals("Boss")){
            return StatusTeacher.Boss;
        }
        else{
            return null;
        }
    }

    public MinorStatus StringToMinorStatus(String s){

        if(s.equals("NO")){
            return MinorStatus.No;
        }
        else if(s.equals("Wait")){
            return MinorStatus.Wait;
        }
        else if(s.equals("Accept")){
            return MinorStatus.Accept;
        }
        else if(s.equals("Reject")){
            return MinorStatus.Reject;
        }
        else{
            return null;
        }
    }

    public Request StringToMinorRequest(String s){

        if(s.equals("Wait")){
            return Request.Wait;
        }
        else if(s.equals("Accept")){
            return Request.Accept;
        }
        else if(s.equals("Reject")){
            return Request.Reject;
        }
        else{
            return null;
        }
    }

    public String passSQLStringForSavePersonWithId(){
        String firstName = this.getFirstname();
        String lastName = this.getLastname();
        String color =   "#" + Integer.toHexString(this.color.hashCode());
        String image = this.image;
        String email = this.getEmail();
        String password = this.getPassword();
        String phoneNumber = this.getPhoneNumber();
        String identityCode = this.getCodeMelli();
        String college = this.getCollege() + "";
        int lastSecondLogin = this.getLastSecondLogin();
        int lastMinuteLogin = this.getLastMinuteLogin();
        int lastHourLogin = this.getLastHourLogin();
        int lastDayLogin = this.getLastDayLogin();
        int lastMonthLogin = this.getLastMonthLogin();
        int lastYearLogin = this.getLastYearLogin();
        int timesLogin = this.getTimesLogin();

        String str = id + ", '" + firstName + "', '" + lastName + "', '" + color + "', '" + image + "', '" + email + "', '" + password + "', '" + phoneNumber + "', '" + identityCode + "', '"
                + college + "', " + lastSecondLogin + ", " + lastMinuteLogin + ", " + lastHourLogin + ", " + lastDayLogin + ", " + lastMonthLogin + ", " + lastYearLogin + ", " +
                timesLogin;
        return str;
    }

    public String passSQLStringForSavePersonWithoutId(){
        String firstName = this.getFirstname();
        String lastName = this.getLastname();
        String color =   "#" + Integer.toHexString(this.color.hashCode());
        String image = this.image;
        String email = this.getEmail();
        String password = this.getPassword();
        String phoneNumber = this.getPhoneNumber();
        String identityCode = this.getCodeMelli();
        String college = this.getCollege() + "";
        int lastSecondLogin = this.getLastSecondLogin();
        int lastMinuteLogin = this.getLastMinuteLogin();
        int lastHourLogin = this.getLastHourLogin();
        int lastDayLogin = this.getLastDayLogin();
        int lastMonthLogin = this.getLastMonthLogin();
        int lastYearLogin = this.getLastYearLogin();
        int timesLogin = this.getTimesLogin();

        String str = "'" + firstName + "', '" + lastName + "', '" + color + "', '" + image + "', '" + email + "', '" + password + "', '" + phoneNumber + "', '" + identityCode + "', '"
                + college + "', " + lastSecondLogin + ", " + lastMinuteLogin + ", " + lastHourLogin + ", " + lastDayLogin + ", " + lastMonthLogin + ", " + lastYearLogin + ", " +
                timesLogin;
        return str;
    }


}
