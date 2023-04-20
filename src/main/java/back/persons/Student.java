package back.persons;

import API.API;
import back.Messages.MessagesForStudent;
import back.course.Course;
import back.enums.*;
import com.google.gson.Gson;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Student extends Person{

    private String studentNumber;
    private LinkedList<String> courses_thisTerm ;
    private LinkedList<Double> courses_thisTerm_Score ;

    private LinkedList<String> courses_Past;
    private LinkedList<Double> courses_Past_Score;


    private boolean registrationLicense;
    private int registrationTime;

    private Grade grade;
    private int helpTeacher;
    private int YearComeToUniversity;
    private EducationStatus educationStatus;
    private MinorStatus firstMinorStatus = MinorStatus.No;
    private MinorStatus secondMinorStatus = MinorStatus.No;

    private LinkedList<Request> RequestForRecommendation = new LinkedList<>();
    private LinkedList<Integer> AcceptedTeacherForRecome = new LinkedList<>();
    private College MinorCollege = null;

    private  LinkedList<Integer> IdTeacherForRequest = new LinkedList<>();

    private  LinkedList<String> ProtestForCourseStudent = new LinkedList<>();
    private  LinkedList<String> AnswerTeacher = new LinkedList<>();

    private String timeThesisDefense = "";
    private Request Dorm;

    private String timeForSelectUnit = "";

    private LinkedList<String> likedCourses = new LinkedList<>();

    private LinkedList<String> coursesWantToGetThisTerm = new LinkedList<>();;

    private LinkedList<Integer> extraStudentsInChat = new LinkedList<>();
    private LinkedList<Integer> extrasTeachersInChat = new LinkedList<>();

    private MessagesForStudent messagesForStudent = new MessagesForStudent();

    private LinkedList<String> coursesIsTANow = new LinkedList<>();



    public Student(int id, Color color, String image, String firstname, String lastname, String email,
                   String password, String phoneNumber, String CodeMelli, College college,
                   String studentNumber, LinkedList<String> courses_thisTerm, LinkedList<String> courses_Past,
                   LinkedList<Double> courses_Past_Score, int helpTeacher, Grade grade,
                   int YearComeToUniversity, EducationStatus educationStatus, boolean registrationLicense, int registrationTime) {
        super(id, color, image, firstname, lastname, email, password, phoneNumber, CodeMelli, college);
        this.studentNumber = studentNumber;
        this.courses_thisTerm = courses_thisTerm;
        this.grade = grade;
        this.helpTeacher = helpTeacher;
        this.YearComeToUniversity = YearComeToUniversity;
        this.educationStatus = educationStatus;
        this.registrationLicense = registrationLicense;
        this.registrationTime = registrationTime;
        this.courses_Past = courses_Past;
        this.courses_Past_Score = courses_Past_Score;

        courses_thisTerm_Score = new LinkedList<>();
        if(courses_thisTerm  == null){
            courses_thisTerm_Score = null;
            courses_thisTerm = new LinkedList<>();
        }
        else if(courses_thisTerm.size() == 0){
            courses_thisTerm_Score = new LinkedList<>();
            ProtestForCourseStudent = new LinkedList<>();
            AnswerTeacher = new LinkedList<>();
        }
        else{
            for (int i = 0; i < courses_thisTerm.size(); i++) {
                courses_thisTerm_Score.add(-1.0);
                ProtestForCourseStudent.add(" ");
                AnswerTeacher.add(" ");
            }
        }

        this.Dorm = null;
        this.setTimeThesisDefense(null);

        likedCourses = new LinkedList<>();
    }


//    Getter and Setters:

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public LinkedList<String> getCourses_thisTerm() {
        return courses_thisTerm;
    }

    public void setCourses_thisTerm(LinkedList<String> courses_thisTerm) {
        this.courses_thisTerm = courses_thisTerm;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public int getHelpTeacher() {
        return helpTeacher;
    }

    public void setHelpTeacher(int helpTeacher) {
        this.helpTeacher = helpTeacher;
    }

    public int getYearComeToUniversity() {
        return YearComeToUniversity;
    }

    public void setYearComeToUniversity(int yearComeToUniversity) {
        YearComeToUniversity = yearComeToUniversity;
    }

    public EducationStatus getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(EducationStatus educationStatus) {
        this.educationStatus = educationStatus;
    }

    public College getMinorCollege() {
        return MinorCollege;
    }

    public void setMinorCollege(College minorCollege) {
        MinorCollege = minorCollege;
    }

    public MinorStatus getFirstMinorStatus() {
        return firstMinorStatus;
    }

    public void setFirstMinorStatus(MinorStatus firstMinorStatus) {
        this.firstMinorStatus = firstMinorStatus;
    }

    public MinorStatus getSecondMinorStatus() {
        return secondMinorStatus;
    }

    public void setSecondMinorStatus(MinorStatus secondMinorStatus) {
        this.secondMinorStatus = secondMinorStatus;
    }

    public LinkedList<Request> getRequestForRecommendation() {
        return RequestForRecommendation;
    }

    public void setRequestForRecommendation(LinkedList<Request> requestForRecommendation) {
        RequestForRecommendation = requestForRecommendation;
    }

    public LinkedList<Integer> getIdTeacherForRequest() {
        return IdTeacherForRequest;
    }

    public void setIdTeacherForRequest(LinkedList<Integer> idTeacherForRequest) {
        IdTeacherForRequest = idTeacherForRequest;
    }

    public LinkedList<String> getProtestForCourseStudent() {
        return ProtestForCourseStudent;
    }

    public void setProtestForCourseStudent(LinkedList<String> protestForCourseStudent) {
        ProtestForCourseStudent = protestForCourseStudent;
    }

    public LinkedList<String> getAnswerTeacher() {
        return AnswerTeacher;
    }

    public void setAnswerTeacher(LinkedList<String> answerTeacher) {
        AnswerTeacher = answerTeacher;
    }

    public LinkedList<Double> getCourses_thisTerm_Score() {
        return courses_thisTerm_Score;
    }

    public void setCourses_thisTerm_Score(LinkedList<Double> courses_thisTerm_Score) {
        this.courses_thisTerm_Score = courses_thisTerm_Score;
    }

    public boolean isRegistrationLicense() {
        return registrationLicense;
    }

    public void setRegistrationLicense(boolean registrationLicense) {
        this.registrationLicense = registrationLicense;
    }

    public int getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(int registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getTimeThesisDefense() {
        return timeThesisDefense;
    }

    public void setTimeThesisDefense(String timeThesisDefense) {
        this.timeThesisDefense = timeThesisDefense;
    }

    public Request getDorm() {
        return Dorm;
    }

    public void setDorm(Request dorm) {
        Dorm = dorm;
    }

    public LinkedList<String> getCourses_Past() {
        return courses_Past;
    }

    public void setCourses_Past(LinkedList<String> courses_Past) {
        this.courses_Past = courses_Past;
    }

    public LinkedList<Double> getCourses_Past_Score() {
        return courses_Past_Score;
    }

    public void setCourses_Past_Score(LinkedList<Double> courses_Past_Score) {
        this.courses_Past_Score = courses_Past_Score;
    }

    public LinkedList<Integer> getAcceptedTeacherForRecome() {
        return AcceptedTeacherForRecome;
    }

    public void setAcceptedTeacherForRecome(LinkedList<Integer> acceptedTeacherForRecome) {
        AcceptedTeacherForRecome = acceptedTeacherForRecome;
    }

    public String getAverage_score(){
        if(this.courses_Past_Score == null){
            return "------------";
        }
        else if(this.courses_Past_Score.size() == 0){
            return "------------";
        }
        double m = 0;
        double weigh = 0;
        LinkedList<Course> courses = API.getAllCourses();
        for (int i = 0; i < courses_Past_Score.size(); i++) {
            for (int j = 0; j < courses.size(); j++) {
                if(courses.get(j).getId().equals(courses_Past.get(i))){
                    m = m + courses.get(j).getWeight() * courses_Past_Score.get(i);
                    weigh = weigh + courses.get(j).getWeight();
                }
            }
        }

        return m / weigh + "";
    }

    public int getNumberOfCoursesPass(){
        if(this.courses_Past_Score == null){
            return 0;
        }
        else if(this.courses_Past_Score.size() == 0){
            return 0;
        }

        int m = 0;
        double weigh = 0;
        LinkedList<Course> courses = API.getAllCourses();
        for (int i = 0; i < courses_Past_Score.size(); i++) {
            if(courses_Past_Score.get(i) >= 10.0){
                m++;
            }
        }

        return m;
    }

    public String getTimeForSelectUnit() {
        return timeForSelectUnit;
    }

    public void setTimeForSelectUnit(String timeForSelectUnit) {
        this.timeForSelectUnit = timeForSelectUnit;
    }

    public LinkedList<Integer> getExtraStudentsInChat() {
        return extraStudentsInChat;
    }

    public void setExtraStudentsInChat(LinkedList<Integer> extraStudentsInChat) {
        this.extraStudentsInChat = extraStudentsInChat;
    }

    public LinkedList<Integer> getExtrasTeachersInChat() {
        return extrasTeachersInChat;
    }

    public void setExtrasTeachersInChat(LinkedList<Integer> extrasTeachersInChat) {
        this.extrasTeachersInChat = extrasTeachersInChat;
    }

    public LinkedList<String> getLikedCourses() {
        return likedCourses;
    }

    public void setLikedCourses(LinkedList<String> likedCourses) {
        this.likedCourses = likedCourses;
    }


    public LinkedList<String> getCoursesWantToGetThisTerm() {
        return coursesWantToGetThisTerm;
    }

    public void setCoursesWantToGetThisTerm(LinkedList<String> coursesWantToGetThisTerm) {
        this.coursesWantToGetThisTerm = coursesWantToGetThisTerm;
    }

    public MessagesForStudent getMessagesForStudent() {
        return messagesForStudent;
    }

    public void setMessagesForStudent(MessagesForStudent messagesForStudent) {
        this.messagesForStudent = messagesForStudent;
    }

    public LinkedList<String> getCoursesIsTANow() {
        return coursesIsTANow;
    }

    public void setCoursesIsTANow(LinkedList<String> coursesIsTANow) {
        this.coursesIsTANow = coursesIsTANow;
    }

    public String toString() {

        String result =  "#" + Integer.toHexString(color.hashCode()) + ":::" +
                this.image + ":::" +
                this.getFirstname() + ":::" + this.getLastname() + ":::" + this.getEmail() + ":::" + this.getPassword() + ":::" + this.getPhoneNumber() + ":::" +
                this.getCodeMelli() + ":::" + this.getCollege() + ":::" + this.studentNumber + ":::";
        if(courses_thisTerm == null){
            result = result + 0 + ":::";
        }

        else{
            result = result + courses_thisTerm.size() + ":::";
            for (int i = 0; i < courses_thisTerm.size(); i++) {
                result = result + courses_thisTerm.get(i) + ":::";
            }
        }

        if(courses_thisTerm_Score == null){
            result = result + 0 + ":::";
        }

        else{
            result = result + courses_thisTerm_Score.size() + ":::";
            for (int i = 0; i < courses_thisTerm_Score.size(); i++) {
                result = result + courses_thisTerm_Score.get(i) + ":::";
            }
        }

        if(ProtestForCourseStudent == null){
            result = result + 0 + ":::";
        }

        else{
            result = result + ProtestForCourseStudent.size() + ":::";
            for (int i = 0; i < ProtestForCourseStudent.size(); i++) {
                result = result + ProtestForCourseStudent.get(i) + ":::";
            }
        }

        if(AnswerTeacher == null){
            result = result + 0 + ":::";
        }

        else{
            result = result + AnswerTeacher.size() + ":::";
            for (int i = 0; i < AnswerTeacher.size(); i++) {
                result = result + AnswerTeacher.get(i) + ":::";
            }
        }

        if(courses_Past == null){
            result = result + 0 + ":::";
        }

        else{
            result = result + courses_Past.size() + ":::";
            for (int i = 0; i < courses_Past.size(); i++) {
                result = result + courses_Past.get(i) + ":::";
            }
        }

        if(courses_Past_Score == null){
            result = result + 0 + ":::";
        }

        else{
            result = result + courses_Past_Score.size() + ":::";
            for (int i = 0; i < courses_Past_Score.size(); i++) {
                result = result + courses_Past_Score.get(i) + ":::";
            }
        }

        if(RequestForRecommendation == null){
            result = result + 0 + ":::";
        }
        else{
            result = result + RequestForRecommendation.size() + ":::";
            for (int i = 0; i < RequestForRecommendation.size(); i++) {
                result = result + RequestForRecommendation.get(i) + ":::";
            }
        }

        if(IdTeacherForRequest == null){
            result = result + 0 + ":::";
        }
        else{
            result = result + IdTeacherForRequest.size() + ":::";
            for (int i = 0; i < IdTeacherForRequest.size(); i++) {
                result = result + IdTeacherForRequest.get(i) + ":::";
            }
        }

        if(AcceptedTeacherForRecome == null){
            result = result + 0 + ":::";
        }
        else{
            result = result + AcceptedTeacherForRecome.size() + ":::";
            for (int i = 0; i < AcceptedTeacherForRecome.size(); i++) {
                result = result + AcceptedTeacherForRecome.get(i) + ":::";
            }
        }


        result = result + helpTeacher + ":::" + grade + ":::" + YearComeToUniversity + ":::" + educationStatus + ":::" + id + ":::" + firstMinorStatus + ":::" + secondMinorStatus +":::" + MinorCollege + ":::" + registrationLicense + ":::" + registrationTime + ":::" + timeThesisDefense + ":::" + Dorm + ":::" + getTimesLogin() + ":::" + getLastYearLogin() + ":::" + getLastMonthLogin() + ":::" + getLastDayLogin() + ":::" + getLastHourLogin() + ":::" + getLastMinuteLogin() + ":::" + getLastSecondLogin() + ":::";
        return result + "\n";
    }

    public String passSQLStringForSaveWithId(){
        String studentNumber = this.getStudentNumber();

        String coursesThisTerm = "";
        if(this.getCourses_thisTerm() == null){
            coursesThisTerm = "0:::";
        }
        else{
            coursesThisTerm = this.getCourses_thisTerm().size() + ":::";

            for (int j = 0; j < this.getCourses_thisTerm().size(); j++) {
                if(j == this.getCourses_thisTerm().size() - 1){
                    coursesThisTerm = coursesThisTerm + this.getCourses_thisTerm().get(j);
                    break;
                }
                coursesThisTerm = coursesThisTerm + this.getCourses_thisTerm().get(j) + ":::";
            }
        }

        String coursesThisTermScore = "";
        if(this.getCourses_thisTerm_Score() == null){
            coursesThisTermScore = "0:::";
        }
        else{
            coursesThisTermScore = this.getCourses_thisTerm_Score().size() + ":::";

            for (int j = 0; j < this.getCourses_thisTerm_Score().size(); j++) {
                if(j == this.getCourses_thisTerm_Score().size() - 1){
                    coursesThisTermScore = coursesThisTermScore + this.getCourses_thisTerm_Score().get(j);
                    break;
                }
                coursesThisTermScore = coursesThisTermScore + this.getCourses_thisTerm_Score().get(j) + ":::";
            }
        }



        String coursesPast = "";
        if(this.getCourses_Past_Score() == null){
            coursesPast = "0:::";
        }
        else{
            coursesPast = this.getCourses_Past_Score().size() + ":::";

            for (int j = 0; j < this.getCourses_Past().size(); j++) {
                if(j == this.getCourses_Past().size() - 1){
                    coursesPast = coursesPast + this.getCourses_Past().get(j);
                    break;
                }
                coursesPast = coursesPast + this.getCourses_Past().get(j) + ":::";
            }
        }


        String coursesPastScore = "";
        if(this.getCourses_Past_Score() == null){
            coursesPastScore = "0:::";
        }
        else{
            coursesPastScore = this.getCourses_Past_Score().size() + ":::";

            for (int j = 0; j < this.getCourses_Past_Score().size(); j++) {
                if(j == this.getCourses_Past_Score().size() - 1){
                    coursesPastScore = coursesPastScore + this.getCourses_Past_Score().get(j);
                    break;
                }
                coursesPastScore = coursesPastScore + this.getCourses_Past_Score().get(j) + ":::";
            }
        }

        boolean registrationLicense = this.isRegistrationLicense();
        int timeRegistrationLicense = this.getRegistrationTime();
        String grade = this.getGrade()+ "";
        int helpTeacher = this.getHelpTeacher();
        int yearComeToUniversity = this.getYearComeToUniversity();
        String educationStatus = this.getEducationStatus()+ "";
        String firstMinorStatus = this.getFirstMinorStatus()+ "";
        String secondMinorStatus = this.getSecondMinorStatus()+ "";


        String requestForRecome = "";
        if(this.getRequestForRecommendation() == null){
            requestForRecome = "0:::";
        }
        else{
            requestForRecome = this.getRequestForRecommendation().size() + ":::";

            for (int j = 0; j < this.getRequestForRecommendation().size(); j++) {
                if(j == this.getRequestForRecommendation().size() - 1){
                    requestForRecome = requestForRecome + this.getRequestForRecommendation().get(j)+ "";
                    break;
                }
                requestForRecome = requestForRecome + this.getRequestForRecommendation().get(j) + ":::";
            }
        }



        String acceptedTeacherForRecome = "";
        if(this.getAcceptedTeacherForRecome() == null){
            acceptedTeacherForRecome = "0:::";
        }
        else{
            acceptedTeacherForRecome = this.getAcceptedTeacherForRecome().size() + ":::";

            for (int j = 0; j < this.getAcceptedTeacherForRecome().size(); j++) {
                if(j == this.getAcceptedTeacherForRecome().size() - 1){
                    acceptedTeacherForRecome = acceptedTeacherForRecome + this.getAcceptedTeacherForRecome().get(j);
                    break;
                }
                acceptedTeacherForRecome = acceptedTeacherForRecome + this.getAcceptedTeacherForRecome().get(j) + ":::";
            }
        }


        String minorCollege = this.getMinorCollege()+ "";
        if(minorCollege == null){
            minorCollege = "";
        }

        String idTeacherForRequests = "";
        if(this.getIdTeacherForRequest() == null){
            idTeacherForRequests = "0:::";
        }
        else{
            idTeacherForRequests = this.getIdTeacherForRequest().size() + ":::";

            for (int j = 0; j < this.getIdTeacherForRequest().size(); j++) {
                if(j == this.getIdTeacherForRequest().size() - 1){
                    idTeacherForRequests = idTeacherForRequests + this.getIdTeacherForRequest().get(j);
                    break;
                }
                idTeacherForRequests = idTeacherForRequests + this.getIdTeacherForRequest().get(j) + ":::";
            }
        }



        String protest = "";
        if(this.getProtestForCourseStudent() == null){
            protest = "0:::";
        }
        else{
            protest = this.getProtestForCourseStudent().size() + ":::";

            for (int j = 0; j < this.getProtestForCourseStudent().size(); j++) {
                if(j == this.getProtestForCourseStudent().size() - 1){
                    protest = protest + this.getProtestForCourseStudent().get(j);
                    break;
                }
                protest = protest + this.getProtestForCourseStudent().get(j) + ":::";
            }
        }



        String answerTeacher = "";
        if(this.getAnswerTeacher() == null){
            answerTeacher = "0:::";
        }
        else{
            answerTeacher = this.getAnswerTeacher().size() + ":::";

            for (int j = 0; j < this.getAnswerTeacher().size(); j++) {
                if(j == this.getAnswerTeacher().size() - 1){
                    answerTeacher = answerTeacher + this.getAnswerTeacher().get(j);
                    break;
                }
                answerTeacher = answerTeacher + this.getAnswerTeacher().get(j) + ":::";
            }
        }

        String coursesLiked = "";
        if(this.getLikedCourses() == null){
            coursesLiked = "0:::";
        }
        else{
            coursesLiked = this.getLikedCourses().size() + ":::";

            for (int j = 0; j < this.getLikedCourses().size(); j++) {
                if(j == this.getLikedCourses().size() - 1){
                    coursesLiked = coursesLiked + this.getLikedCourses().get(j);
                    break;
                }
                coursesLiked = coursesLiked + this.getLikedCourses().get(j) + ":::";
            }
        }


        String timeThesisDefence = this.getTimeThesisDefense();
        String Dorm = this.getTimeThesisDefense();

        String wantToGet = passStringFormGenericTypes(coursesWantToGetThisTerm);

        String studentsInC = passStringFormGenericTypes(extraStudentsInChat);
        String teachersInC = passStringFormGenericTypes(extrasTeachersInChat);
        String messages = (new Gson()).toJson(messagesForStudent);
        String tas = passStringFormGenericTypes(coursesIsTANow);

        String str = passSQLStringForSavePersonWithId() + ", '" + studentNumber  + "', '" + coursesThisTerm + "', '" + coursesThisTermScore +  "', '" + coursesPast + "', '" + coursesPastScore + "', " + registrationLicense + ", " +
                timeRegistrationLicense + ", '" + grade + "' , " + helpTeacher + " , " +  yearComeToUniversity + " , '" + educationStatus + "', '" + firstMinorStatus + "' , '" + secondMinorStatus + "', '" +
                requestForRecome + "', '" + acceptedTeacherForRecome + "', '" + minorCollege + "', '" + idTeacherForRequests + "', '" + protest + "' , '" + answerTeacher + "', '" + timeThesisDefence + "', '" + Dorm + "', '" + timeForSelectUnit + "', '" + coursesLiked + "', '" + wantToGet + "', '" +  studentsInC + "', '" + teachersInC + "', '" + messages + "', '" + tas + "'";

        return str;
    }
    public String passSQLStringForSaveWithoutId(){
        String studentNumber = this.getStudentNumber();

        String coursesThisTerm = "";
        if(this.getCourses_thisTerm() == null){
            coursesThisTerm = "0:::";
        }
        else{
            coursesThisTerm = this.getCourses_thisTerm().size() + ":::";

            for (int j = 0; j < this.getCourses_thisTerm().size(); j++) {
                if(j == this.getCourses_thisTerm().size() - 1){
                    coursesThisTerm = coursesThisTerm + this.getCourses_thisTerm().get(j);
                    break;
                }
                coursesThisTerm = coursesThisTerm + this.getCourses_thisTerm().get(j) + ":::";
            }
        }

        String coursesThisTermScore = "";
        if(this.getCourses_thisTerm_Score() == null){
            coursesThisTermScore = "0:::";
        }
        else{
            coursesThisTermScore = this.getCourses_thisTerm_Score().size() + ":::";

            for (int j = 0; j < this.getCourses_thisTerm_Score().size(); j++) {
                if(j == this.getCourses_thisTerm_Score().size() - 1){
                    coursesThisTermScore = coursesThisTermScore + this.getCourses_thisTerm_Score().get(j);
                    break;
                }
                coursesThisTermScore = coursesThisTermScore + this.getCourses_thisTerm_Score().get(j) + ":::";
            }
        }



        String coursesPast = "";
        if(this.getCourses_Past_Score() == null){
            coursesPast = "0:::";
        }
        else{
            coursesPast = this.getCourses_Past_Score().size() + ":::";

            for (int j = 0; j < this.getCourses_Past().size(); j++) {
                if(j == this.getCourses_Past().size() - 1){
                    coursesPast = coursesPast + this.getCourses_Past().get(j);
                    break;
                }
                coursesPast = coursesPast + this.getCourses_Past().get(j) + ":::";
            }
        }


        String coursesPastScore = "";
        if(this.getCourses_Past_Score() == null){
            coursesPastScore = "0:::";
        }
        else{
            coursesPastScore = this.getCourses_Past_Score().size() + ":::";

            for (int j = 0; j < this.getCourses_Past_Score().size(); j++) {
                if(j == this.getCourses_Past_Score().size() - 1){
                    coursesPastScore = coursesPastScore + this.getCourses_Past_Score().get(j);
                    break;
                }
                coursesPastScore = coursesPastScore + this.getCourses_Past_Score().get(j) + ":::";
            }
        }

        boolean registrationLicense = this.isRegistrationLicense();
        int timeRegistrationLicense = this.getRegistrationTime();
        String grade = this.getGrade()+ "";
        int helpTeacher = this.getHelpTeacher();
        int yearComeToUniversity = this.getYearComeToUniversity();
        String educationStatus = this.getEducationStatus()+ "";
        String firstMinorStatus = this.getFirstMinorStatus()+ "";
        String secondMinorStatus = this.getSecondMinorStatus()+ "";


        String requestForRecome = "";
        if(this.getRequestForRecommendation() == null){
            requestForRecome = "0:::";
        }
        else{
            requestForRecome = this.getRequestForRecommendation().size() + ":::";

            for (int j = 0; j < this.getRequestForRecommendation().size(); j++) {
                if(j == this.getRequestForRecommendation().size() - 1){
                    requestForRecome = requestForRecome + this.getRequestForRecommendation().get(j)+ "";
                    break;
                }
                requestForRecome = requestForRecome + this.getRequestForRecommendation().get(j) + ":::";
            }
        }



        String acceptedTeacherForRecome = "";
        if(this.getAcceptedTeacherForRecome() == null){
            acceptedTeacherForRecome = "0:::";
        }
        else{
            acceptedTeacherForRecome = this.getAcceptedTeacherForRecome().size() + ":::";

            for (int j = 0; j < this.getAcceptedTeacherForRecome().size(); j++) {
                if(j == this.getAcceptedTeacherForRecome().size() - 1){
                    acceptedTeacherForRecome = acceptedTeacherForRecome + this.getAcceptedTeacherForRecome().get(j);
                    break;
                }
                acceptedTeacherForRecome = acceptedTeacherForRecome + this.getAcceptedTeacherForRecome().get(j) + ":::";
            }
        }


        String minorCollege = this.getMinorCollege()+ "";
        if(minorCollege == null){
            minorCollege = "";
        }

        String idTeacherForRequests = "";
        if(this.getIdTeacherForRequest() == null){
            idTeacherForRequests = "0:::";
        }
        else{
            idTeacherForRequests = this.getIdTeacherForRequest().size() + ":::";

            for (int j = 0; j < this.getIdTeacherForRequest().size(); j++) {
                if(j == this.getIdTeacherForRequest().size() - 1){
                    idTeacherForRequests = idTeacherForRequests + this.getIdTeacherForRequest().get(j);
                    break;
                }
                idTeacherForRequests = idTeacherForRequests + this.getIdTeacherForRequest().get(j) + ":::";
            }
        }



        String protest = "";
        if(this.getProtestForCourseStudent() == null){
            protest = "0:::";
        }
        else{
            protest = this.getProtestForCourseStudent().size() + ":::";

            for (int j = 0; j < this.getProtestForCourseStudent().size(); j++) {
                if(j == this.getProtestForCourseStudent().size() - 1){
                    protest = protest + this.getProtestForCourseStudent().get(j);
                    break;
                }
                protest = protest + this.getProtestForCourseStudent().get(j) + ":::";
            }
        }



        String answerTeacher = "";
        if(this.getAnswerTeacher() == null){
            answerTeacher = "0:::";
        }
        else{
            answerTeacher = this.getAnswerTeacher().size() + ":::";

            for (int j = 0; j < this.getAnswerTeacher().size(); j++) {
                if(j == this.getAnswerTeacher().size() - 1){
                    answerTeacher = answerTeacher + this.getAnswerTeacher().get(j);
                    break;
                }
                answerTeacher = answerTeacher + this.getAnswerTeacher().get(j) + ":::";
            }
        }

        String coursesLiked = "";
        if(this.getLikedCourses() == null){
            coursesLiked = "0:::";
        }
        else{
            coursesLiked = this.getLikedCourses().size() + ":::";

            for (int j = 0; j < this.getLikedCourses().size(); j++) {
                if(j == this.getLikedCourses().size() - 1){
                    coursesLiked = coursesLiked + this.getLikedCourses().get(j);
                    break;
                }
                coursesLiked = coursesLiked + this.getLikedCourses().get(j) + ":::";
            }
        }


        String timeThesisDefence = this.getTimeThesisDefense();
        String Dorm = this.getTimeThesisDefense();

        String wantToGet = passStringFormGenericTypes(coursesWantToGetThisTerm);

        String studentsInC = passStringFormGenericTypes(extraStudentsInChat);
        String teachersInC = passStringFormGenericTypes(extrasTeachersInChat);
        String messages = (new Gson()).toJson(messagesForStudent);
        String tas = passStringFormGenericTypes(coursesIsTANow);


        String str = passSQLStringForSavePersonWithoutId() + ", '" + studentNumber  + "', '" + coursesThisTerm + "', '" + coursesThisTermScore + "', '" + coursesPast + "', '" + coursesPastScore + "', " + registrationLicense + ", " +
                timeRegistrationLicense + ", '" + grade + "' , " + helpTeacher + " , " +  yearComeToUniversity + " , '" + educationStatus + "', '" + firstMinorStatus + "' , '" + secondMinorStatus + "', '" +
                requestForRecome + "', '" + acceptedTeacherForRecome + "', '" + minorCollege + "', '" + idTeacherForRequests + "', '" + protest + "' , '" + answerTeacher + "', '" + timeThesisDefence + "', '" + Dorm + "', '" + timeForSelectUnit + "', '" + coursesLiked + "', '" + wantToGet + "', '" + studentsInC + "', '" + teachersInC + "', '" + messages + "', '" + tas + "'";



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


