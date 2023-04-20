package dataBase;


import back.course.Course;
import back.course.EducationalMaterials;
import back.course.Exam;
import back.course.HomeWork;
import back.enums.*;
import back.Messages.MessagesForStudent;
import back.Messages.MessagesForTeacher;
import back.persons.Admin;
import back.persons.Mohseni;
import back.persons.Student;
import back.persons.Teacher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.paint.Color;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class DataBaseController {
    Connection connection;
    Statement statement;
    Statement statementForMessage;
    ResultSet resultSet;
    ResultSet resultSetForMessage;


    public DataBaseController(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "1234");
            statement = connection.createStatement();
            statementForMessage = connection.createStatement();
        }
        catch (SQLException throwables) {

        }

    }




    public void deleteAllRows(){
        try {
            statement.executeUpdate("DELETE FROM `edu-users`.`studentstable`");
            statement.executeUpdate("DELETE FROM `edu-users`.`teacherstable`");
            statement.executeUpdate("DELETE FROM `edu-users`.`coursestable`");
            statement.executeUpdate("DELETE FROM `edu-users`.`mohsenitable`");
            statement.executeUpdate("DELETE FROM `edu-users`.`adminstable`");

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void addStudentsForFirstTime(LinkedList<Student> students){

        for (int i = 0; i < students.size(); i++) {

            try {
                statement.executeUpdate("INSERT INTO `edu-users`.`studentstable` " +
                        "VALUES (" + students.get(i).passSQLStringForSaveWithId() + ")");
            } catch (SQLException throwables) {

            }
        }

    }

    public void addTeachersForFirstTime(LinkedList<Teacher> teachers){


        for (int i = 0; i < teachers.size(); i++) {

            try {
                statement.executeUpdate("INSERT INTO `edu-users`.`teacherstable` VALUES (" + teachers.get(i).passSQLStringForSaveWithId() + ")");
            } catch (SQLException throwables) {

            }
        }

    }

    public void addCoursesForFirstTime(LinkedList<Course> courses){

        for (int i = 0; i < courses.size(); i++) {

            try {

                statement.executeUpdate("INSERT INTO `edu-users`.`coursestable` VALUES (" + courses.get(i).passSQLStringForSaveWithId() + ")");

            }
            catch (SQLException throwables) {

            }
        }

    }

    public void addMohseniForFirstTime(Mohseni mohseni){
        try {
            statement.executeUpdate("INSERT INTO `edu-users`.`mohsenitable` VALUES (" + mohseni.passSQLStringForSaveWithId() + ")");
        }
        catch (SQLException throwables) {

        }

    }

    public void addAdminForFirstTime(Admin admin){
        try {
            statement.executeUpdate("INSERT INTO `edu-users`.`adminstable` VALUES (" + admin.passSQLStringForSaveWithId() + ")");
        }
        catch (SQLException throwables) {

        }

    }



    public void changeColorForStudent(String studentNumber, String color){

        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET color = '" + color + "' Where studentNumber = '" + studentNumber + "'");



        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void changeColorForTeacher(String userName, String color){
        try {
            String s = "#" + Integer.toHexString(color.hashCode());
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET color = '" + color + "' Where username = '" + userName + "'");



        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void changeColorForMohseni(String userName, String color){
        try {
            String s = "#" + Integer.toHexString(color.hashCode());
            statement.executeUpdate("UPDATE `edu-users`.`mohsenitable` SET color = '" + color + "' Where username = '" + userName + "'");
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void changeColorForAdmin(String userName, String color){
        try {
            String s = "#" + Integer.toHexString(color.hashCode());
            statement.executeUpdate("UPDATE `edu-users`.`adminstable` SET color = '" + color + "' Where username = '" + userName + "'");
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }




    public Student getStudentLogin(String username, String password){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where studentNumber = '" + username + "' AND password = '" + password + "'");

            while (resultSet.next()){
                return getAStudentFromResultSet(resultSet);
            }

            return null;


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
            return null;
        }

    }

    public Teacher getTeacherLogin(String username, String password){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable`  Where username = '" + username + "' AND password = '" + password + "'");

            while (resultSet.next()){
                return getATeacherFromResultSet(resultSet);
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 135");
        }

        return null;

    }

    public Mohseni getMohseniLogin(String username, String password){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`mohsenitable`  Where username = '" + username + "' AND password = '" + password + "'");

            while (resultSet.next()){
                Mohseni mohseni = new Mohseni(resultSet.getInt("id"), Color.web(resultSet.getString("color")), resultSet.getString("image"), resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("phoneNumber"), resultSet.getString("identityCode"),
                        StringToCollege(resultSet.getString("college")), resultSet.getString("username"));

                return mohseni;

            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 135");
        }

        return null;

    }

    public Admin getAdminLogin(String username, String password){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`adminstable`  Where username = '" + username + "' AND password = '" + password + "'");

            while (resultSet.next()){
                Admin admin = new Admin(resultSet.getInt("id"), Color.web(resultSet.getString("color")), resultSet.getString("image"), resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("phoneNumber"), resultSet.getString("identityCode"),
                        StringToCollege(resultSet.getString("college")), resultSet.getString("username"));

                return admin;

            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 135");
        }

        return null;

    }


    
    public LinkedList<Student> getAllStudents(){
        LinkedList<Student> allStudents = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`");

            while (resultSet.next()){
                allStudents.add(getAStudentFromResultSet(resultSet));
            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 194");
        }

        return allStudents;

    }

    public LinkedList<Teacher> getAllTeachers(){
        LinkedList<Teacher> allTeachers = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable`");

            while (resultSet.next()){
                allTeachers.add(getATeacherFromResultSet(resultSet));
            }



        }
        catch (SQLException throwables) {
            System.out.println("in Exception 227");
        }

        return allTeachers;
    }

    public LinkedList<Course> getAllCourses(){
        LinkedList<Course> allCourses = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`coursestable`");

            while (resultSet.next()){
                Course course = getACourseFromResultSet(resultSet);
                allCourses.add(course);
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 251");
        }

        return allCourses;
    }



    public String getLastNameHelpTeacher(int teacherId){
        try {
            resultSet = statement.executeQuery("SELECT lastName FROM `edu-users`.`teacherstable`  Where id = " + teacherId);
            while (resultSet.next()){
                return resultSet.getString("lastName");
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 150");
        }

        return "";
    }

    public Teacher getLastNameHelpTeacher(String lastName){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable`  Where lastName = '" + lastName + "'");
            while (resultSet.next()){
                return getATeacherFromResultSet(resultSet);
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 150");
        }

        return null;
    }

    public LinkedList<Course> GiveCoursesFromAStudentForCW(int studentId){
        LinkedList<Course> courses = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT coursesThisTerm FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            if (resultSet.next()){
                LinkedList<String> idsForCourses = getListStringFromString(resultSet.getString("coursesThisTerm"));
                for (int i = 0; i < idsForCourses.size(); i++) {

                    ResultSet rs = statementForMessage.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE id = '" + idsForCourses.get(i) + "'");
                    while (rs.next()){
                        courses.add(getACourseFromResultSet(rs));
                    }

                    rs.close();

                }
            }

            resultSet = statement.executeQuery("SELECT coursesIsTANow FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            if (resultSet.next()){
                LinkedList<String> coursesIsTANow = getListStringFromString(resultSet.getString("coursesIsTANow"));
                for (int i = 0; i < coursesIsTANow.size(); i++) {

                    ResultSet rs = statementForMessage.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE id = '" + coursesIsTANow.get(i) + "'");
                    while (rs.next()){
                        courses.add(getACourseFromResultSet(rs));
                    }

                    rs.close();

                }
            }

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return courses;

    }

    public LinkedList<Course> GiveCoursesFromATeacher(int teacherId){
        LinkedList<Course> courses = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT courses FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
            if (resultSet.next()){

                LinkedList<String> idsForCourses = getListStringFromString(resultSet.getString("courses"));
                for (int i = 0; i < idsForCourses.size(); i++) {
                    ResultSet rs = statement.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE id = '" + idsForCourses.get(i) + "'");
                    if(rs.next()){
                        courses.add(getACourseFromResultSet(rs));
                    }
                }

            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return courses;

    }

    public LinkedList<Integer> getTeachersIdForACourse(String courseId){
        LinkedList<Integer> teachersId = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT teachersId FROM `edu-users`.`coursestable` WHERE id = '" + courseId + "'");
            if (resultSet.next()){

                teachersId = getIntegersFromString(resultSet.getString("teachersId"));

            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return teachersId;

    }

    public LinkedList<Integer> getStudentsIdForACourse(String courseId){
        LinkedList<Integer> studentsId = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT studentsIdAreInClass FROM `edu-users`.`coursestable` WHERE id = '" + courseId + "'");
            if (resultSet.next()){

                studentsId = getIntegersFromString(resultSet.getString("studentsIdAreInClass"));

            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return studentsId;

    }

    public boolean CanSignUpForDuplicateUsername(String username){
        try {
            resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`teacherstable` WHERE username = '" + username + "'");
            if(resultSet.next()){
                return false;
            }

            resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`studentstable` WHERE studentNumber = '" + username + "'");
            if(resultSet.next()){
                return false;
            }

            resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`mohsenitable` WHERE username = '" + username + "'");
            if(resultSet.next()){
                return false;
            }

            resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`adminstable` WHERE username = '" + username + "'");
            if(resultSet.next()){
                return false;
            }

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return true;
    }

    public boolean validDuplicatePublicIdForCourse(int publicId, int groupId){
        try {
            String s = publicId + "-" + groupId;
            resultSet = statement.executeQuery("SELECT id, groupId, courseName, college FROM `edu-users`.`coursestable` WHERE id = '" + s + "'");
            if(resultSet.next()){
               return false;
            }

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return true;
    }

    public Course getACourseFromResultSet(ResultSet resultSet){
        Course course = null;
        try {
            StringToListDays(resultSet.getString("days"));
            course = new Course(resultSet.getString("id"), resultSet.getInt("courseCapacity")
                    ,resultSet.getString("courseName"), StringToCollege(resultSet.getString("college")),
                    getListStringFromString(resultSet.getString("prerequisites")), getListStringFromString(resultSet.getString("requisites")),
                    resultSet.getDouble("weigh"), StringToGrade(resultSet.getString("grade")), Integer.parseInt(resultSet.getString("yearExam")),
                    Integer.parseInt(resultSet.getString("monthExam")), Integer.parseInt(resultSet.getString("dayExam")), StringToListDays(resultSet.getString("days")), resultSet.getDouble("hourBeginClass"),
                    resultSet.getDouble("hourEndClass"), getListStringFromString(resultSet.getString("TAsStudentNumber")));
            course.setNumGetCoursesUntilNow(resultSet.getInt("numGetCoursesUntilNow"));

            Gson gson = new Gson();
            Type listType = new TypeToken<LinkedList<HomeWork>>(){}.getType();
            course.setHomeWorks(gson.fromJson(resultSet.getString("homeWorks"), listType));

            Type listType2 = new TypeToken<LinkedList<Exam>>(){}.getType();
            course.setExams(gson.fromJson(resultSet.getString("exams"), listType2));

            Type listType3 = new TypeToken<LinkedList<EducationalMaterials>>(){}.getType();
            course.setEducationalMaterials(gson.fromJson(resultSet.getString("educationalMaterials"), listType3));

            course.setStudentsIdAreInClass(getIntegersFromString(resultSet.getString("studentsIdAreInClass")));
            course.setTeachersId(getIntegersFromString(resultSet.getString("teachersId")));

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return course;
    }

    public Teacher getATeacherFromResultSet(ResultSet resultSet){
        Teacher teacher = null;
        try {
            teacher = new Teacher(resultSet.getInt("id"), Color.web(resultSet.getString("color")), resultSet.getString("image"), resultSet.getString("firstName"), resultSet.getString("lastName"),
                    resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("phoneNumber"), resultSet.getString("identityCode"),
                    StringToCollege(resultSet.getString("college")), StringToStatusTeacher(resultSet.getString("statusTeacher")),
                    getListStringFromString(resultSet.getString("courses")), StringToTeacherDegree(resultSet.getString("teacherDegree")), resultSet.getString("username"),
                    resultSet.getInt("roomNumber"));
            teacher.setLastSecondLogin(resultSet.getInt("lastSecondLogin"));
            teacher.setLastMinuteLogin(resultSet.getInt("lastMinuteLogin"));
            teacher.setLastHourLogin(resultSet.getInt("lastHourLogin"));
            teacher.setLastDayLogin(resultSet.getInt("lastDayLogin"));
            teacher.setLastMonthLogin(resultSet.getInt("lastMonthLogin"));
            teacher.setLastYearLogin(resultSet.getInt("lastYearLogin"));
            teacher.setTimesLogin(resultSet.getInt("timesLogin"));
            Gson gson = new Gson();
            MessagesForTeacher m = gson.fromJson(resultSet.getString("messagesForTeacher"), MessagesForTeacher.class);
            teacher.setMessagesForTeacher(gson.fromJson(resultSet.getString("messagesForTeacher"), MessagesForTeacher.class));
            teacher.setExtraStudentsInChat(getIntegersFromString(resultSet.getString("extraStudentsInChat")));
            teacher.setExtraTeachersInChat(getIntegersFromString(resultSet.getString("extraTeachersInChat")));
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return teacher;
    }

    public Student getAStudentFromResultSet(ResultSet resultSet){
        Student student = null;

        try {
            student = new Student(resultSet.getInt("id"), Color.web(resultSet.getString("color")), resultSet.getString("image"), resultSet.getString("firstName"), resultSet.getString("lastName"),
                    resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("phoneNumber"), resultSet.getString("identityCode"),
                    StringToCollege(resultSet.getString("college")), resultSet.getString("studentNumber"),
                    getListStringFromString(resultSet.getString("coursesThisTerm")), getListStringFromString(resultSet.getString("coursesPast")),
                    getCoursesScoreFromString(resultSet.getString("coursesPastScore")), resultSet.getInt("helpTeacher"), StringToGrade(resultSet.getString("grade")),
                    resultSet.getInt("yearComeToUniversity"), StringToEducationStatus((resultSet.getString("educationStatus"))), resultSet.getBoolean("registrationLicense"), resultSet.getInt("registrationTime"));
            student.setCourses_thisTerm_Score(getCoursesScoreFromString(resultSet.getString("coursesThisTermScore")));
            student.setLastSecondLogin(resultSet.getInt("lastSecondLogin"));
            student.setLastMinuteLogin(resultSet.getInt("lastMinuteLogin"));
            student.setLastHourLogin(resultSet.getInt("lastHourLogin"));
            student.setLastDayLogin(resultSet.getInt("lastDayLogin"));
            student.setLastMonthLogin(resultSet.getInt("lastMonthLogin"));
            student.setLastYearLogin(resultSet.getInt("lastYearLogin"));
            student.setTimesLogin(resultSet.getInt("timesLogin"));
            student.setFirstMinorStatus(StringToMinorStatus(resultSet.getString("firstMinorStatus")));
            student.setSecondMinorStatus(StringToMinorStatus(resultSet.getString("secondMinorStatus")));
            student.setRequestForRecommendation(StringToRequestForRecommendation(resultSet.getString("requestForRecome")));
            student.setAcceptedTeacherForRecome(getIdsTeachersAcceptedFromString(resultSet.getString("acceptedTeacherForRecome")));
            student.setMinorCollege(StringToCollege(resultSet.getString("minorCollege")));
            student.setIdTeacherForRequest(getIdsTeachersAcceptedFromString(resultSet.getString("idTeacherForRequest")));
            student.setProtestForCourseStudent(getListStringFromString(resultSet.getString("protestForCourseStudent")));
            student.setAnswerTeacher(getListStringFromString(resultSet.getString("answerTeacher")));
            student.setTimeThesisDefense(resultSet.getString("timeThesisDefence"));
            student.setDorm(StringToMinorRequest(resultSet.getString("dorm")));
            student.setLikedCourses(getListStringFromString(resultSet.getString("likedCourses")));
            student.setCoursesWantToGetThisTerm(getListStringFromString(resultSet.getString("coursesWantToGetThisTerm")));

            student.setMessagesForStudent((new Gson()).fromJson(resultSet.getString("messages"), MessagesForStudent.class));
            student.setExtraStudentsInChat(getIntegersFromString(resultSet.getString("extraStudentsInChat")));
            student.setExtrasTeachersInChat(getIntegersFromString(resultSet.getString("extraTeachersInChat")));
            student.setCoursesIsTANow(getListStringFromString(resultSet.getString("coursesIsTANow")));

        } catch (SQLException throwables) {
            System.out.println("exception");
        }

        return student;
    }

    public void addNewStudent(Student student){
        try {
            statement.executeUpdate("INSERT INTO `edu-users`.`studentstable` (firstName, lastName, color, image, email, password, phoneNumber, identityCode, college, lastSecondLogin, lastMinuteLogin, lastHourLogin, lastDayLogin, lastMonthLogin, lastYearLogin, timesLogin, studentNumber, coursesThisTerm, coursesThisTermScore, coursesPast, coursesPastScore, registrationLicense, registrationTime, grade, helpTeacher, yearComeToUniversity, educationStatus, firstMinorStatus, secondMinorStatus, requestForRecome, acceptedTeacherForRecome, minorCollege, idTeacherForRequest, protestForCourseStudent, answerTeacher, timeThesisDefence, dorm, timeForSelectUnit, likedCourses, coursesWantToGetThisTerm, extraStudentsInCaht, extraTeachersInCaht, messages, coursesIsTANow) " +
                    "VALUES (" + student.passSQLStringForSaveWithoutId() + ")");
        } catch (SQLException throwables) {

        }
    }

    public void addNewTeacher(Teacher teacher){
        try {
            statement.executeUpdate("INSERT INTO `edu-users`.`teacherstable` (firstName, lastName, color, image, email, password, phoneNumber, identityCode, college, lastSecondLogin, lastMinuteLogin, lastHourLogin, lastDayLogin, lastMonthLogin, lastYearLogin, timesLogin, username, statusTeacher, courses, teacherDegree, roomNumber, messagesForTeacher, extraStudentsInCaht, extraTeachersInCaht) " +
                    "VALUES (" + teacher.passSQLStringForSaveWithoutId() + ")");
        } catch (SQLException throwables) {

        }
    }

    public void addNewCourse(Course course){
        try {
            statement.executeUpdate("INSERT INTO `edu-users`.`coursestable` (publicId, courseName, college, prerequisites, requisites, weigh, grade, dayExam, monthExam, yearExam, days, hourBeginClass, hourEndClass, courseCapacity, TAsStudentNumber, numGetCoursesUntilNow, homeWorks, exams, educationalMaterials, studentsIdAreInClass, teachersId) " +
                    "VALUES (" + course.passSQLStringForSaveWithoutId() + ")");

            for (int i = 0; i < course.getTAsStudentNumber().size(); i++) {
                resultSet = statement.executeQuery("SELECT coursesIsTANow FROM `edu-users`.`studentstable` WHERE studentNumber = '" + course.getTAsStudentNumber().get(i) + "'");
                if(resultSet.next()){
                    LinkedList<String> coursesIsTANow = getListStringFromString(resultSet.getString("coursesIsTANow"));
                    coursesIsTANow.add(course.getId());
                    statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesIsTANow = '" + passStringFormGenericTypes(coursesIsTANow) + "'  WHERE studentNumber = '" + course.getTAsStudentNumber().get(i) + "'");
                }
            }

            for (int i = 0; i < course.getTeachersId().size(); i++) {
                resultSet = statement.executeQuery("SELECT courses FROM `edu-users`.`teacherstable` WHERE id = " + course.getTeachersId().get(i));
                if(resultSet.next()){
                    LinkedList<String> courses = getListStringFromString(resultSet.getString("courses"));
                    courses.add(course.getId());
                    statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET courses = '" + passStringFormGenericTypes(courses) + "'  WHERE id = " + course.getTeachersId().get(i));
                }
            }
        } catch (SQLException throwables) {

        }
    }

    public int lastIdOfTeachers(){
        int max = -1;
        try {
            ResultSet resultSet = resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`teacherstable`");
            while (resultSet.next()){
                max = Math.max(max, resultSet.getInt("id"));
            }

        } catch (SQLException throwables) {

        }

        return max;
    }

    public int lastIdOfStudents(){
        int max = -1;
        try {
            ResultSet resultSet = resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`studentstable`");
            while (resultSet.next()){
                max = Math.max(max, resultSet.getInt("id"));
            }

        } catch (SQLException throwables) {

        }

        return max;
    }


    public Teacher FindTeacherFromId(int id){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable` WHERE id = " + id);
            if (resultSet.next()){
                return getATeacherFromResultSet(resultSet);
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return null;
    }

    public String FindTeacherLastNameFromId(int id){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable` WHERE id = " + id);
            while (resultSet.next()){

                return getATeacherFromResultSet(resultSet).getLastname();
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";
    }
    public String FindTeacherFirstNameFromId(int id){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable` WHERE id = " + id);
            while (resultSet.next()){

                return getATeacherFromResultSet(resultSet).getFirstname();
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";
    }
    public String FindTeacherCollegeFromId(int id){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable` WHERE id = " + id);
            while (resultSet.next()){

                return getATeacherFromResultSet(resultSet).getCollege() + "";
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";
    }

    public Student FindStudentFromId(int id){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` WHERE id = " + id);
            while (resultSet.next()){

                return getAStudentFromResultSet(resultSet);
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return null;
    }

    public Course FindCourseFromId(String id){

        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE id = '" + id + "'");

            while (resultSet.next()){
                return getACourseFromResultSet(resultSet);
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return null;
    }

    public Course FindCourseFromName(String courseName, String college){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE courseName = '" + courseName + "' AND college = '" + college +  "'");
            while (resultSet.next()){
                return getACourseFromResultSet(resultSet);
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return null;
    }

    public void AddNewCourseToTeacher(String courseId, String teacherName, String collegeName){
        try {
            resultSet = statement.executeQuery("SELECT courses FROM `edu-users`.`teacherstable` WHERE lastName = '" + teacherName + "' And college = '" + collegeName + "'" );
            while (resultSet.next()){
                LinkedList<String> courses = getListStringFromString(resultSet.getString("courses"));
                if(courses.contains(courseId)){
                    return;
                }
                courses.add(courseId);

                statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET courses = '" + passStringFormGenericTypes(courses) + "'");

            }
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void UpdateCoursesForAStudent(int id, String coursesThisTerm, String coursesThisTermScore, String coursesPast, String coursesPastScore, String getProtestForCourseStudent, String getAnswerTeacher){

        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + coursesThisTerm + "', coursesThisTermScore = '" + coursesThisTermScore +"' , " +
                    " coursesPast = '"+ coursesPast +"', coursesPastScore = '" + coursesPastScore + "', protestForCourseStudent = '" + getProtestForCourseStudent + "', answerTeacher = '" + getAnswerTeacher + "' Where id = " + id);
        } catch (SQLException throwables) {

        }
    }

    public void refactorCourse(String courseId){

        try {
            Gson gson = new Gson();
            statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET educationalMaterials = '" + gson.toJson(new LinkedList<>()) + "', exams = '"+ gson.toJson(new LinkedList<>()) +"' , " +
                    " homeWorks = '"+ gson.toJson(new LinkedList<>())  + "', TAsStudentNumber = '" + passStringFormGenericTypes(new LinkedList<>()) + "', studentsIdAreInClass = '" + passStringFormGenericTypes(new LinkedList<>()) + "', numGetCoursesUntilNow = 0 Where id = '" + courseId + "'");
        } catch (SQLException throwables) {

        }
    }

    public void removeCoursesFromTAS(String courseId, LinkedList<String> studentsNumber){

        try {
            for (int i = 0; i < studentsNumber.size(); i++) {
                resultSet = statement.executeQuery("Select coursesIsTANow FROM `edu-users`.`studentstable` WHERE studentNumber = '" + studentsNumber.get(i) + "'" );
                if (resultSet.next()){
                    LinkedList<String> coursesIsTANow = getListStringFromString(resultSet.getString("coursesIsTANow"));
                    for (int j = 0; j < coursesIsTANow.size(); j++) {
                        if(coursesIsTANow.get(j).equals(courseId)){
                            coursesIsTANow.remove(j);
                            break;
                        }
                    }

                    statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesIsTANow = '" + passStringFormGenericTypes(coursesIsTANow) + "' Where studentNumber = '" + studentsNumber.get(i) + "'" );

                }
            }
        } catch (SQLException throwables) {

        }
    }

    public void UpdateCoursesForATeacher(int teacherId, String courses){

        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET courses = '" + courses + "' Where id = " + teacherId);
        } catch (SQLException throwables) {

        }
    }

    public LinkedList<Student> FindStudentsWantsToReject(String college){
        LinkedList<Student> students = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where college = '" + college + "' AND educationStatus = 'Wait'");

            while (resultSet.next()){
                students.add(getAStudentFromResultSet(resultSet));
            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
            return null;
        }

        return students;
    }

    public LinkedList<Student> FindStudentsWantsToMinor(String college){
        LinkedList<Student> students = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where (college = '" + college + "' AND firstMinorStatus = 'Wait') OR (college = '" + college + "' AND secondMinorStatus = 'Wait')");

            while (resultSet.next()){
                students.add(getAStudentFromResultSet(resultSet));
            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
            return null;
        }

        return students;
    }

    public void removeAStudentFromCourse(String courseId, int studentId){
        try {
            resultSet = statement.executeQuery("SELECT studentsIdAreInClass FROM `edu-users`.`coursestable` WHERE  id = '" + courseId + "'");
            while (resultSet.next()){
                LinkedList<Integer> linkedList = getIntegersFromString(resultSet.getString("studentsIdAreInClass"));
                for (int i = 0; i < linkedList.size(); i++) {
                    if(linkedList.get(i) == studentId){
                        linkedList.remove(i);
                        statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET studentsIdAreInClass = '" + passStringFormGenericTypes(linkedList) + "' Where id = '" + courseId + "'");
                        return;
                    }
                }
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public void removeAStudentFromCourseTA(String courseId, String studentNumber){
        try {
            resultSet = statement.executeQuery("SELECT TAsStudentNumber FROM `edu-users`.`coursestable` WHERE  id = '" + courseId + "'");
            while (resultSet.next()){
                LinkedList<String> linkedList = getListStringFromString(resultSet.getString("TAsStudentNumber"));
                for (int i = 0; i < linkedList.size(); i++) {
                    if(linkedList.get(i).equals(studentNumber)){
                        linkedList.remove(i);
                        statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET TAsStudentNumber = '" + passStringFormGenericTypes(linkedList) + "' Where id = '" + courseId + "'");
                        return;
                    }
                }
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public void AddORRemoveACourseFromTeachers(String courseId, int teacherId){
        try {
            int num = 0;
            resultSetForMessage = statementForMessage.executeQuery("SELECT courses FROM `edu-users`.`teacherstable` WHERE  id = " + teacherId);
            resultSet = statement.executeQuery("SELECT teachersId FROM `edu-users`.`coursestable` WHERE  id = '" + courseId + "'");
            while (resultSet.next() & resultSetForMessage.next()){
                LinkedList<Integer> linkedList = getIntegersFromString(resultSet.getString("teachersId"));
                LinkedList<String> coursesIdForTeacher = getListStringFromString(resultSetForMessage.getString("courses"));

                for (int i = 0; i < linkedList.size(); i++) {
                    if(linkedList.get(i) == teacherId){
                        linkedList.remove(i);
                        num++;
                    }
                }

                if(num == 0){
                    linkedList.add(teacherId);
                    coursesIdForTeacher.add(courseId);
                }
                else{
                    for (int i = 0; i < coursesIdForTeacher.size(); i++) {
                        if(coursesIdForTeacher.get(i).equals(courseId)){
                            coursesIdForTeacher.remove(i);
                        }
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET teachersId = '" + passStringFormGenericTypes(linkedList) + "' Where id = '" + courseId + "'");
                statementForMessage.executeUpdate("UPDATE `edu-users`.`teacherstable` SET courses = '" + passStringFormGenericTypes(coursesIdForTeacher) + "',   Where id = " + teacherId);
                break;
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public void RemoveACourseFromTeachers(String courseId){
        try {
            resultSet = statement.executeQuery("SELECT teachersId FROM `edu-users`.`coursestable` WHERE  id = '" + courseId + "'");
            if (resultSet.next()){
                LinkedList<Integer> linkedList = getIntegersFromString(resultSet.getString("teachersId"));
                for (int i = 0; i < linkedList.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT courses FROM `edu-users`.`teacherstable` WHERE  id = " + linkedList.get(i));
                    while (resultSetForMessage.next()){
                        LinkedList<String> courses = getListStringFromString(resultSetForMessage.getString("courses"));
                        for (int j = 0; j < courses.size(); j++) {
                            if(courses.get(j).equals(courseId)){
                                courses.remove(j);
                                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET courses = '" + passStringFormGenericTypes(courses) + "' Where id = " + linkedList.get(i));
                                break;
                            }
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public void RemoveStudent(int id){

        try {
            Student student = FindStudentFromId(id);
            for (int i = 0; i < student.getCourses_thisTerm().size(); i++) {
                removeAStudentFromCourse(student.getCourses_thisTerm().get(i), student.getId());
            }

            for (int i = 0; i < student.getCoursesIsTANow().size(); i++) {
                removeAStudentFromCourseTA(student.getCoursesIsTANow().get(i), student.getStudentNumber());
            }
            statement.executeUpdate("DELETE FROM `edu-users`.`studentstable` WHERE id = " + id);

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

    }

    public LinkedList<Student> ShowStudentsForATeacherEA(String college){

        LinkedList<Student> students = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where college = '" + college + "' OR minorCollege = '"+ college +"'");

            while (resultSet.next()){
                students.add(getAStudentFromResultSet(resultSet));
            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
            return null;
        }

        return students;

    }

    public void changePasswordForAStudent(String username, String newPassword){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET password = '" + newPassword + "' WHERE studentNumber = '" + username + "'");
        } catch (SQLException throwables) {

        }
    }
    public void changePasswordAForTeacher(String username, String newPassword){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET password = '" + newPassword + "' WHERE username = '" + username + "'");
        } catch (SQLException throwables) {

        }
    }

    public void changeTimesLoginStudent(String username){
        try {
            resultSet = statement.executeQuery("SELECT timesLogin FROM `edu-users`.`studentstable` WHERE studentNumber = '" + username + "'");
            int timesLogin = resultSet.getInt("timesLogin") + 1;
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET timesLogin = " + timesLogin + " WHERE studentNumber = '" + username + "'");

        } catch (SQLException throwables) {

        }
    }

    public void changeLastLoginStudent(int id, int second, int minute, int hour, int day, int month, int year){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET lastSecondLogin = " + second + ", lastMinuteLogin = " + minute +  ", lastHourLogin = " + hour + ", lastDayLogin = " + day + ", lastMonthLogin = " + month + ", lastYearLogin = " + year +" Where id = " + id);
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeTimesLoginTeacher(String username){
        try {
            resultSet = statement.executeQuery("SELECT timesLogin FROM `edu-users`.`teacherstable` WHERE username = '" + username + "'");
            int timesLogin = resultSet.getInt("timesLogin") + 1;
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET timesLogin = " + timesLogin + " WHERE username = '" + username + "'");

        } catch (SQLException throwables) {

        }
    }

    public void changeLastLoginTeacher(int id, int second, int minute, int hour, int day, int month, int year){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET lastSecondLogin = " + second + ", lastMinuteLogin = " + minute +  ", lastHourLogin = " + hour + ", lastDayLogin = " + day + ", lastMonthLogin = " + month + ", lastYearLogin = " + year +" Where id = " + id);
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeEducationStatusOfAStudent(int id, EducationStatus educationStatus){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET educationStatus = '" + educationStatus + "' Where id = " + id);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeProtestForAStudent(int id, int index, String protest){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` Where id = " + id);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                student.getProtestForCourseStudent().set(index , protest);

                String protestText = "";
                if(student.getProtestForCourseStudent() == null){
                    protestText = "0:::";
                }
                else{
                    protestText = student.getProtestForCourseStudent().size() + ":::";

                    for (int j = 0; j < student.getProtestForCourseStudent().size(); j++) {
                        if(j == student.getProtestForCourseStudent().size() - 1){
                            protestText = protestText + student.getProtestForCourseStudent().get(j);
                            break;
                        }
                        protestText = protestText + student.getProtestForCourseStudent().get(j) + ":::";
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET protestForCourseStudent = '" + protestText + "' Where id = " + id);
                break;
            }

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void editEmailForAStudent(int id, String email){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET email = '" + email + "' Where id = " + id);
        } catch (SQLException throwables) {

        }
    }

    public void editPhoneForAStudent(int id, String phoneNumber){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET phoneNumber = '" + phoneNumber + "' Where id = " + id);
        } catch (SQLException throwables) {

        }
    }

    public void setImageAddressOfAStudent(int id, String image){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET image = '" + image + "' Where id = " + id);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void editEmailForATeacher(int id, String email){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET email = '" + email + "' Where id = " + id);
        } catch (SQLException throwables) {

        }
    }

    public void editPhoneForATeacher(int id, String phoneNumber){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET phoneNumber = '" + phoneNumber + "' Where id = " + id);
        } catch (SQLException throwables) {

        }
    }

    public void setImageAddressOfATeacher(int id, String image){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET image = '" + image + "' Where id = " + id);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void addIdTeacherToStudentForRecome(int studentId, int teacherId){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                student.getIdTeacherForRequest().add(teacherId);
                student.getRequestForRecommendation().add(Request.Wait);

                String requestForRecome = "";
                if(student.getRequestForRecommendation() == null){
                    requestForRecome = "0:::";
                }
                else{
                    requestForRecome = student.getRequestForRecommendation().size() + ":::";

                    for (int j = 0; j < student.getRequestForRecommendation().size(); j++) {
                        if(j == student.getRequestForRecommendation().size() - 1){
                            requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j)+ "";
                            break;
                        }
                        requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j) + ":::";
                    }
                }

                String idTeacherForRequests = "";
                if(student.getIdTeacherForRequest() == null){
                    idTeacherForRequests = "0:::";
                }
                else{
                    idTeacherForRequests = student.getIdTeacherForRequest().size() + ":::";

                    for (int j = 0; j < student.getIdTeacherForRequest().size(); j++) {
                        if(j == student.getIdTeacherForRequest().size() - 1){
                            idTeacherForRequests = idTeacherForRequests + student.getIdTeacherForRequest().get(j);
                            break;
                        }
                        idTeacherForRequests = idTeacherForRequests + student.getIdTeacherForRequest().get(j) + ":::";
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET requestForRecome = '" + requestForRecome + "', idTeacherForRequest = '" + idTeacherForRequests + "'  Where id = " + studentId);

                break;
            }

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeRecomeForStudentBecauseShowed(int studentId, int indexShouldRemove){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);

                student.getRequestForRecommendation().remove(indexShouldRemove);
                student.getIdTeacherForRequest().remove(indexShouldRemove);

                String requestForRecome = "";
                if(student.getRequestForRecommendation() == null){
                    requestForRecome = "0:::";
                }
                else{
                    requestForRecome = student.getRequestForRecommendation().size() + ":::";

                    for (int j = 0; j < student.getRequestForRecommendation().size(); j++) {
                        if(j == student.getRequestForRecommendation().size() - 1){
                            requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j)+ "";
                            break;
                        }
                        requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j) + ":::";
                    }
                }

                String idTeacherForRequests = "";
                if(student.getIdTeacherForRequest() == null){
                    idTeacherForRequests = "0:::";
                }
                else{
                    idTeacherForRequests = student.getIdTeacherForRequest().size() + ":::";

                    for (int j = 0; j < student.getIdTeacherForRequest().size(); j++) {
                        if(j == student.getIdTeacherForRequest().size() - 1){
                            idTeacherForRequests = idTeacherForRequests + student.getIdTeacherForRequest().get(j);
                            break;
                        }
                        idTeacherForRequests = idTeacherForRequests + student.getIdTeacherForRequest().get(j) + ":::";
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET requestForRecome = '" + requestForRecome + "', idTeacherForRequest = '" + idTeacherForRequests + "'  Where id = " + studentId);

                break;
            }

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeRecomeForStudentBecauseShowed(int studentId, int indexShouldRemove, int idTeacherShouldAddToAccepted){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);

                student.getRequestForRecommendation().remove(indexShouldRemove);
                student.getIdTeacherForRequest().remove(indexShouldRemove);
                student.getAcceptedTeacherForRecome().add(idTeacherShouldAddToAccepted);

                String requestForRecome = "";
                if(student.getRequestForRecommendation() == null){
                    requestForRecome = "0:::";
                }
                else{
                    requestForRecome = student.getRequestForRecommendation().size() + ":::";

                    for (int j = 0; j < student.getRequestForRecommendation().size(); j++) {
                        if(j == student.getRequestForRecommendation().size() - 1){
                            requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j)+ "";
                            break;
                        }
                        requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j) + ":::";
                    }
                }

                String idTeacherForRequests = "";
                if(student.getIdTeacherForRequest() == null){
                    idTeacherForRequests = "0:::";
                }
                else{
                    idTeacherForRequests = student.getIdTeacherForRequest().size() + ":::";

                    for (int j = 0; j < student.getIdTeacherForRequest().size(); j++) {
                        if(j == student.getIdTeacherForRequest().size() - 1){
                            idTeacherForRequests = idTeacherForRequests + student.getIdTeacherForRequest().get(j);
                            break;
                        }
                        idTeacherForRequests = idTeacherForRequests + student.getIdTeacherForRequest().get(j) + ":::";
                    }
                }

                String acceptedTeacherForRecome = "";
                if(student.getAcceptedTeacherForRecome() == null){
                    acceptedTeacherForRecome = "0:::";
                }
                else{
                    acceptedTeacherForRecome = student.getAcceptedTeacherForRecome().size() + ":::";

                    for (int j = 0; j < student.getAcceptedTeacherForRecome().size(); j++) {
                        if(j == student.getAcceptedTeacherForRecome().size() - 1){
                            acceptedTeacherForRecome = acceptedTeacherForRecome + student.getAcceptedTeacherForRecome().get(j);
                            break;
                        }
                        acceptedTeacherForRecome = acceptedTeacherForRecome + student.getAcceptedTeacherForRecome().get(j) + ":::";
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET requestForRecome = '" + requestForRecome + "', idTeacherForRequest = '" + idTeacherForRequests + "', acceptedTeacherForRecome = '" + acceptedTeacherForRecome + "'  Where id = " + studentId);

                break;
            }

        }
        catch (SQLException throwables) {
        }
    }

    public void setMinorCollegeForAStudent(int id, String collegeName){
        try {

            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET firstMinorStatus = 'Wait',  secondMinorStatus = 'Wait', minorCollege = '" + collegeName + "' Where id = " + id);

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void setDormRequestForAStudent(int id, String dorm){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET dorm = '" + dorm + "' Where id = " + id);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeMinorForStudentBecauseShowedAndRejected(int studentId){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET firstMinorStatus = 'No',  secondMinorStatus = 'No', minorCollege = 'null' Where id = " + studentId);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void acceptRecomeForAStudent(int studentId, int index, String request){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);

                student.getRequestForRecommendation().set(index, StringToMinorRequest(request));


                String requestForRecome = "";
                if(student.getRequestForRecommendation() == null){
                    requestForRecome = "0:::";
                }
                else{
                    requestForRecome = student.getRequestForRecommendation().size() + ":::";

                    for (int j = 0; j < student.getRequestForRecommendation().size(); j++) {
                        if(j == student.getRequestForRecommendation().size() - 1){
                            requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j)+ "";
                            break;
                        }
                        requestForRecome = requestForRecome + student.getRequestForRecommendation().get(j) + ":::";
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET requestForRecome = '" + requestForRecome + "'  Where id = " + studentId);

                break;
            }

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void setRoomNumberForATeacher(int teacherId, int roomNumber){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET roomNumber = " + roomNumber + " Where id = " + teacherId);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void removeATeacher(int teacherId){
        try {
            statement.executeUpdate("DELETE FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }


    public void removeACourse(String courseId){
        try {
            resultSet = statement.executeQuery("SELECT studentsIdAreInClass, teachersId, TAsStudentNumber FROM `edu-users`.`corusestable` WHERE id = '" + courseId + "'");
            if (resultSet.next()){
                LinkedList<Integer> studentsIdAreInClass = getIntegersFromString(resultSet.getString("studentsIdAreInClass"));
                LinkedList<Integer> teachersId = getIntegersFromString(resultSet.getString("teachersId"));
                LinkedList<String> TAsStudentNumber = getListStringFromString(resultSet.getString("TAsStudentNumber"));

                for (int i = 0; i < studentsIdAreInClass.size(); i++) {
                    removeACourseFromCoursesWantToGetThisTerm(studentsIdAreInClass.get(i), courseId);
                }
                for (int i = 0; i < TAsStudentNumber.size(); i++) {
                    removeAStudentFromCourseTA(courseId, TAsStudentNumber.get(i));
                }
                for (int i = 0; i < teachersId.size(); i++) {
                    removeACourseFromTeacher(courseId, teachersId.get(i));
                }
            }
            statement.executeUpdate("DELETE FROM `edu-users`.`corusestable` WHERE id = '" + courseId + "'");
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void removeACourseFromTeacher(String courseId, int teacherId){
        try {
            resultSet = statement.executeQuery("SELECT courses FROM `edu-users`.`teacherstable` WHERE  id = " + teacherId);
            while (resultSet.next()){
                LinkedList<String> courses = getListStringFromString(resultSet.getString("courses"));
                for (int i = 0; i < courses.size(); i++) {
                    if(courses.get(i).equals(courseId)){
                        courses.remove(i);
                        statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET courses = '" + passStringFormGenericTypes(courses) + "' Where id = " + teacherId);
                        return;
                    }
                }
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeStatusTeacher(int teacherId, String statusTeacher){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET statusTeacher = " + statusTeacher + " Where id = " + teacherId);
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void setAnswerAndScoreForStudent(int id, int indexCourse, double score, String answer){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` Where id = " + id);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                student.getCourses_thisTerm_Score().set(indexCourse, score);
                student.getAnswerTeacher().set(indexCourse, answer);

                String answerTeacher = passStringFormGenericTypes(student.getAnswerTeacher());

                String coursesThisTermScore = passStringFormGenericTypes(student.getCourses_thisTerm_Score());

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTermScore = '" + coursesThisTermScore + "', answerTeacher = '" + answerTeacher + "' Where id = " + id);

                break;
            }

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public void changeMinorStatusFromEA(boolean isFirstMinorStatus, int studentId, String status){
        try {
            if(isFirstMinorStatus){
                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET firstMinorStatus = '" + status + "' Where id = " + studentId);
            }

            else{
                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET secondfirstMinorStatus = '" + status + "' Where id = " + studentId);
            }

        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }
    }

    public String returnAllHelpTeachersFromACollege(String college){
        try {

            ArrayList<String> arrayList = new ArrayList<>();
            resultSet = statement.executeQuery("SELECT lastName FROM `edu-users`.`teacherstable` Where college = '" + college + "'");
            while (resultSet.next()){
                arrayList.add(resultSet.getString("lastName"));
            }

            String forReturn = "";
            for (int i = 0; i < arrayList.size(); i++) {
                if(i == arrayList.size() - 1){
                    forReturn = forReturn + arrayList.get(i);
                    break;
                }
                forReturn = forReturn + arrayList.get(i) + ":::";
            }
            return forReturn;
        }
        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";
    }

    public void setTimesLoginForAStudent(int studentId, int timeLogin){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET timesLogin = " + timeLogin + " Where id = " + studentId);
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void setTimesLoginForATeacher(int teacherId, int timeLogin){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET timesLogin = " + timeLogin + " Where id = " + teacherId);
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public String getProtestForCourseStudent(int studentId, int index){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                return student.getProtestForCourseStudent().get(index);
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "";
    }

    public String getAnswerForCourseStudent(int studentId, int index){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                return student.getAnswerTeacher().get(index);
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "";
    }

    public double getScoreForCourseStudent(int studentId, int index){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                return student.getCourses_thisTerm_Score().get(index);
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return -1.0;
    }

    public String getRequestForRecommendation(int studentId){
        try {
            resultSet = statement.executeQuery("SELECT requestForRecome FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                return resultSet.getString("requestForRecome");
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "0:::";
    }

    public int getIdTeacherForRequest(int studentId, int index){
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                Student student = this.getAStudentFromResultSet(resultSet);
                return student.getIdTeacherForRequest().get(index);
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return -1;
    }

    public String getMinorStatus(int studentId, boolean isFirst){
        try {
            if(isFirst){
                resultSet = statement.executeQuery("SELECT firstMinorStatus FROM `edu-users`.`studentstable`  Where id = " + studentId);
                while (resultSet.next()){
                    return resultSet.getString("firstMinorStatus");
                }
            }
            else{
                resultSet = statement.executeQuery("SELECT secondMinorStatus FROM `edu-users`.`studentstable`  Where id = " + studentId);
                while (resultSet.next()){
                    return resultSet.getString("secondMinorStatus");
                }
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "";
    }

    public String getDormStatus(int studentId){
        try {
            resultSet = statement.executeQuery("SELECT dorm FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                return resultSet.getString("dorm");
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "";
    }

    public String getThesisDefenceFromAStudent(int studentId){
        try {
            resultSet = statement.executeQuery("SELECT timeThesisDefence FROM `edu-users`.`studentstable`  Where id = " + studentId);
            while (resultSet.next()){
                String s = resultSet.getString("timeThesisDefence");
                if(s == null){
                    return "";
                }
                return s;
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "";
    }

    public boolean validStudentNumberForTA(String studentNumber){
        try {
            resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`studentstable`  Where studentNumber = '" + studentNumber + "'");
            while (resultSet.next()){
                return true;
            }

            return false;

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return false;
    }

    public boolean validPublicIdForCourse(int publicId){
        try {
            resultSet = statement.executeQuery("SELECT id FROM `edu-users`.`coursestable`  Where publicId = " + publicId);
            while (resultSet.next()){
                return true;
            }

            return false;

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return false;
    }

    public String getFileNameOfFromName(String usernameTo){
        try {
            resultSet = statement.executeQuery("SELECT image FROM `edu-users`.`studentstable` WHERE studentNumber = '" + usernameTo + "'");
            if (resultSet.next()){
                String s = resultSet.getString("image");
                if(s == null){
                    s =  "userIcon256.png";
                }
                else if(s.equals("null")){
                    s = "userIcon256.png";
                }
                else if(s.equals("")){
                    s = "userIcon256.png";
                }

                return s;
            }

            resultSet = statement.executeQuery("SELECT image FROM `edu-users`.`teacherstable` WHERE username = '" + usernameTo + "'");
            if (resultSet.next()){
                String s = resultSet.getString("image");
                if(s == null){
                    s =  "userIcon256.png";
                }
                else if(s.equals("null")){
                    s = "userIcon256.png";
                }
                else if(s.equals("")){
                    s = "userIcon256.png";
                }

                return s;
            }

            resultSet = statement.executeQuery("SELECT image FROM `edu-users`.`mohsenitable` WHERE username = '" + usernameTo + "'");
            if (resultSet.next()){
                String s = resultSet.getString("image");
                if(s == null){
                    s =  "userIcon256.png";
                }
                else if(s.equals("null")){
                    s = "userIcon256.png";
                }
                else if(s.equals("")){
                    s = "userIcon256.png";
                }

                return s;
            }


            resultSet = statement.executeQuery("SELECT image FROM `edu-users`.`adminstable` WHERE username = '" + usernameTo + "'");
            if (resultSet.next()){
                String s = resultSet.getString("image");
                if(s == null){
                    s =  "userIcon256.png";
                }
                else if(s.equals("null")){
                    s = "userIcon256.png";
                }
                else if(s.equals("")){
                    s = "userIcon256.png";
                }

                return s;
            }


        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "userIcon256.png";
    }


    public String getFileNameOfFromStudentNumber(String studentNumber){
        try {
            resultSet = statement.executeQuery("SELECT image FROM `edu-users`.`studentstable` WHERE studentNumber = '" + studentNumber + "'");
            while (resultSet.next()){
                return resultSet.getString("image");
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return "userIcon256.png";
    }

    public void saveMessage(String usernameFrom, String usernameTo, String fileAddress, String typeFile){
        try {

            LocalDateTime localDateTime = LocalDateTime.now();
            String time = localDateTime.getYear() + "//" + localDateTime.getMonthValue()
                    + "//" + localDateTime.getDayOfMonth() + "//" + localDateTime.getHour()
                    + "//" + localDateTime.getMinute() + "//" + localDateTime.getSecond();
            statement.executeUpdate("INSERT INTO `edu-users`.`message` (fromUsername, toUsername, fileAddress, messageTime, typeFile) " +
                    " VALUES ( '" + usernameFrom + "', '" + usernameTo + "',  '" + fileAddress + "', '" + time + "', '" + typeFile + "')");
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }



    public LinkedList<LinkedList<String>> getYearsToUniversityForEA(String college){
        LinkedList<LinkedList<String>> strings = new LinkedList<>();
        LinkedList<String> years = new LinkedList<>();
        LinkedList<String> status = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT yearComeToUniversity, timeForSelectUnit FROM `edu-users`.`studentstable` WHERE college = '" +  college + "'");
            while (resultSet.next()){
                years.add(resultSet.getString("yearComeToUniversity"));
                String s = resultSet.getString("timeForSelectUnit");
                if(s == null){
                    s = "";
                }
                status.add(s);

            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
        strings.add(years);
        strings.add(status);
        return strings;
    }

    public void saveTimeForSelectionUnit(String college, int yearComeToUniversity, String time){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET timeForSelectUnit = '" + time + "' WHERE college = '" +  college + "' AND yearComeToUniversity = " + yearComeToUniversity);
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public void setExamDayForACourse(String courseId, int dayExam, int monthExam, int yearExam){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET dayExam = " + dayExam + ", monthExam = " + monthExam + ", yearExam = " + yearExam + "  WHERE id = '" +  courseId + "'");

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }
    }

    public LinkedList<Course> getAllCoursesFromACollege(String college, String grade){
        LinkedList<Course> c = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE college = '" +  college + "'");

            while (resultSet.next()){
                Course course = getACourseFromResultSet(resultSet);
                if(grade.equals("No")){
                    c.add(course);
                }
                else if(course.getGrade().equals(StringToGrade(grade))){
                    c.add(course);
                }
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return c;
    }

    public LinkedList<Course> getAllCoursesFromACollegeForSelectUnit(String college, String grade){
        LinkedList<Course> c = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`coursestable` WHERE college = '" +  college + "'");

            while (resultSet.next()){
                Course course = getACourseFromResultSet(resultSet);
                if(grade.equals("No")){
                    c.add(course);
                }
                else if(course.getGrade().equals(StringToGrade(grade))){
                    c.add(course);
                }
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        for (int i = 0; i < c.size(); i++) {
            for (int j = i + 1; j < c.size(); j++) {
                if(c.get(i).getId().equals(c.get(j).getId())){
                    c.remove(j);
                    i = -1;
                    break;
                }
            }
        }

        return c;
    }

    public LinkedList<Course> getLikedCourses(int studentId){
        LinkedList<Course> c = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` WHERE id = " +  studentId);

            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);
                for (int i = 0; i < student.getLikedCourses().size(); i++) {
                    c.add(FindCourseFromId(student.getLikedCourses().get(i)));
                }
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

        return c;
    }

    public LinkedList<Course> getSuggestedCourses(int studentId, String college, String grade){
        LinkedList<Course> c = new LinkedList<>();
        c = getAllCoursesFromACollege(college, grade);
        Student student;
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` WHERE id = " +  studentId);
            if(resultSet.next()){
                student = getAStudentFromResultSet(resultSet);
                LinkedList<Course> forReturn = new LinkedList<>();
                for (int i = 0; i < c.size(); i++) {
                    int counter = 0;
                    Second:
                    for (int j = 0; j < c.get(i).getPrerequisites().size(); j++) {
                        if(c.get(i).getNumGetCoursesUntilNow() >= c.get(i).getCourseCapacity()){
                            continue;
                        }
                        for (int k = 0; k < student.getCourses_thisTerm().size(); k++) {
                            if(c.get(i).getPrerequisites().get(j).equals(student.getCourses_thisTerm().get(k)) | c.get(i).getId().equals(student.getCourses_thisTerm().get(k))){
                                counter = 0;
                                break Second;
                            }
                        }
                        for (int k = 0; k < student.getCourses_Past().size(); k++) {
                            if(c.get(i).getPrerequisites().get(j).equals(student.getCourses_Past().get(k)) & student.getCourses_Past_Score().get(k) >= 10){
                                counter++;
                            }
                        }
                    }

                    if(counter == c.get(i).getPrerequisites().size() & counter != 0){
                        forReturn.add(c.get(i));
                    }
                }


                for (int i = 0; i < forReturn.size(); i++) {
                    for (int j = i + 1; j < forReturn.size(); j++) {
                        if(forReturn.get(i).getId().equals(forReturn.get(j).getId())){
                            forReturn.remove(j);
                            i = -1;
                            break;
                        }
                    }
                }

                return forReturn;
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }


        return new LinkedList<>();
    }

    public LinkedList<Boolean> getStatusOfACourseForUnitSelection(int studentId, String courseId){
        LinkedList<Boolean> booleans = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT coursesThisTermScore, likedCourses, coursesThisTerm, coursesPast, coursesPastScore, coursesWantToGetThisTerm FROM `edu-users`.`studentstable` WHERE id = " +  studentId);
            if(resultSet.next()){
                LinkedList<String> coursesPast = getListStringFromString(resultSet.getString("coursesPast"));
                LinkedList<String> likedCourses = getListStringFromString(resultSet.getString("likedCourses"));
                LinkedList<String> coursesThisTerm = getListStringFromString(resultSet.getString("coursesThisTerm"));
                LinkedList<Double> coursesPastScore = getCoursesScoreFromString(resultSet.getString("coursesPastScore"));
                LinkedList<Double> coursesThisTermScore = getCoursesScoreFromString(resultSet.getString("coursesThisTermScore"));
                LinkedList<String> coursesWantToGetThisTerm = getListStringFromString(resultSet.getString("coursesWantToGetThisTerm"));

                //For liked courses
                int i;
                for (i = 0; i < likedCourses.size(); i++) {
                    if(likedCourses.get(i).equals(courseId)){
                        booleans.add(true);
                        break;
                    }
                }
                if(i == likedCourses.size()){
                    booleans.add(false);
                }

                //For add or remove button;
                for (i = 0; i < coursesPast.size(); i++) {
                    if(coursesPast.get(i).equals(courseId) & coursesPastScore.get(i) >= 10){
                        booleans.add(null);
                        return booleans;
                    }
                }

                boolean b = false;
                for (i = 0; i < coursesThisTerm.size(); i++) {
                    if(coursesThisTerm.get(i).equals(courseId) & coursesThisTermScore.get(i) >= 10){
                        booleans.add(null);
                        return booleans;
                    }
                    else if(coursesThisTerm.get(i).equals(courseId) && coursesThisTermScore.get(i) < 10 && coursesThisTermScore.get(i) >= 0){
                        break;
                    }
                    else if(coursesThisTerm.get(i).equals(courseId) & coursesThisTermScore.get(i) == -1.0){
                        booleans.add(null);
                        return booleans;
                    }
                }

                for (int j = 0; j < coursesWantToGetThisTerm.size(); j++) {
                    if(coursesWantToGetThisTerm.get(j).equals(courseId)){
                        booleans.add(true);
                        return booleans;
                    }
                }


                booleans.add(b);
                return booleans;


            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }


        return booleans;
    }


    public void addACourseToLikedCourses(int studentId, String courseId){

        try {
            resultSet = statement.executeQuery("SELECT likedCourses FROM `edu-users`.`studentstable` WHERE id = " +  studentId);
            if(resultSet.next()){
                LinkedList<String> likedCourses = getListStringFromString(resultSet.getString("likedCourses"));
                likedCourses.add(courseId);
                String courses = passStringFormGenericTypes(likedCourses);

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET likedCourses = '" + courses + "' Where id = " + studentId);

            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

    }

    public void removeACourseFromLikedCourses(int studentId, String courseId){

        try {
            resultSet = statement.executeQuery("SELECT likedCourses FROM `edu-users`.`studentstable` WHERE id = " +  studentId);
            if(resultSet.next()){
                LinkedList<String> likedCourses = getListStringFromString(resultSet.getString("likedCourses"));

                for (int i = 0; i < likedCourses.size(); i++) {
                    if(likedCourses.get(i).equals(courseId)){
                        likedCourses.remove(i);
                        break;
                    }
                }


                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET likedCourses = '" + passStringFormGenericTypes(likedCourses) + "'  Where id = " + studentId);
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 105");
        }

    }

    public boolean addACourseFromCoursesWantToGetThisTerm(int studentId, String courseId){

        try {
            boolean can = false;
            ResultSet resultSet2 = statement.executeQuery("SELECT numGetCoursesUntilNow, courseCapacity FROM `edu-users`.`coursestable` WHERE id = '" +  courseId + "'");
            if(resultSet2.next()){
                if(resultSet2.getInt("numGetCoursesUntilNow") >= resultSet2.getInt("courseCapacity")){
                    return false;
                }
                else{
                    can = true;
                }
            }
            else{
                return false;
            }
            int numGetCourse = resultSet2.getInt("numGetCoursesUntilNow");
            resultSet = statement.executeQuery("SELECT coursesWantToGetThisTerm FROM `edu-users`.`studentstable` WHERE id = " +  studentId);
            if(resultSet.next()){
                LinkedList<String> coursesWantToGetThisTerm = getListStringFromString(resultSet.getString("coursesWantToGetThisTerm"));
                Course mainCourse = FindCourseFromId(courseId);
                for (int i = 0; i < coursesWantToGetThisTerm.size(); i++) {
                    Course course = FindCourseFromId(coursesWantToGetThisTerm.get(i));
                    if(mainCourse.getCollege() == College.Public){
                        if(course.getCollege() == College.Public){
                            return false;
                        }
                    }


                    if(course.getDayExam() == mainCourse.getDayExam() && course.getMonthExam() == mainCourse.getMonthExam() && course.getYearExam() == mainCourse.getYearExam()){
                        return false;
                    }
                    for (int j = 0; j < course.getDays().size(); j++) {
                        for (int k = 0; k < mainCourse.getDays().size(); k++) {
                            if(course.getDays().get(j) == mainCourse.getDays().get(k)){
                                if(course.getHourBeginClass() <= mainCourse.getHourBeginClass() & course.getHourEndClass() > mainCourse.getHourBeginClass()){
                                    return false;
                                }
                                else if(course.getHourBeginClass() < mainCourse.getHourEndClass() & course.getHourEndClass() >= mainCourse.getHourEndClass()){
                                    return false;
                                }
                            }
                        }
                    }
                }

                coursesWantToGetThisTerm.add(courseId);

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesWantToGetThisTerm = '" + passStringFormGenericTypes(coursesWantToGetThisTerm) + "' Where id = " + studentId);
                statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET numGetCoursesUntilNow = " + (numGetCourse + 1) + " Where id = '" + courseId + "'");
                resultSet2.close();
                return true;
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 1849");
        }

        return false;

    }



    public void changeGroupForSelectionUnit(String studentNumber, String college, int studentId, String courseId){

        try {
            ResultSet resultSet2 = statement.executeQuery("SELECT numGetCoursesUntilNow, courseCapacity, studentsIdAreInClass FROM `edu-users`.`coursestable` WHERE id = '" +  courseId + "'");
            if(resultSet2.next()){
                if(resultSet2.getInt("numGetCoursesUntilNow") >= resultSet2.getInt("courseCapacity")){
                    this.sendMessageToEAToGetACourse(studentNumber, college, courseId);
                    return;
                }
                else{

                }
            }
            else{
                return;
            }

            int numCourse = resultSet2.getInt("numGetCoursesUntilNow");

            resultSet = statement.executeQuery("SELECT coursesWantToGetThisTerm FROM `edu-users`.`studentstable` WHERE id = " +  studentId);
            if(resultSet.next()){

                LinkedList<String> coursesWantToGetThisTerm = getListStringFromString(resultSet.getString("coursesWantToGetThisTerm"));

                boolean shouldAdd = false;
                String cId = "";
                for (int i = 0; i < coursesWantToGetThisTerm.size(); i++) {
                    int group1 = Integer.parseInt(courseId.split("-")[0]);
                    int group2 = Integer.parseInt(coursesWantToGetThisTerm.get(i).split("-")[0]);
                    if(group2 == group1){
                        cId = coursesWantToGetThisTerm.get(i);
                        coursesWantToGetThisTerm.remove(i);
                        shouldAdd = true;
                        break;
                    }
                }

                coursesWantToGetThisTerm.add(courseId);

                if(shouldAdd){
                    statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET numGetCoursesUntilNow = (numGetCoursesUntilNow - 1) Where id = '" + cId + "'");
                    resultSetForMessage = statementForMessage.executeQuery("SELECT studentsIdAreInClass FROM `edu-users`.`coursestable` WHERE id = '" +  cId + "'");
                    if(resultSetForMessage.next()){
                        LinkedList<Integer> studentsId = getIntegersFromString(resultSetForMessage.getString("studentsIdAreInClass"));
                        for (int i = 0; i < studentsId.size(); i++) {
                            if(studentsId.get(i) == studentId){
                                studentsId.remove(i);
                                break;
                            }
                        }

                        statementForMessage.executeUpdate("UPDATE `edu-users`.`coursestable` SET studentsIdAreInClass = '" + passStringFormGenericTypes(studentsId) + "' Where id = '" + cId + "'");

                    }
                    statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET numGetCoursesUntilNow = (numGetCoursesUntilNow + 1) Where id = '" + courseId + "'");
                    resultSetForMessage = statementForMessage.executeQuery("SELECT studentsIdAreInClass FROM `edu-users`.`coursestable` WHERE id = '" +  courseId + "'");
                    if(resultSetForMessage.next()){
                        LinkedList<Integer> studentsId = getIntegersFromString(resultSetForMessage.getString("studentsIdAreInClass"));
                        studentsId.add(studentId);
                        statementForMessage.executeUpdate("UPDATE `edu-users`.`coursestable` SET studentsIdAreInClass = '" + passStringFormGenericTypes(studentsId) + "' Where id = '" + courseId + "'");
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesWantToGetThisTerm = '" + passStringFormGenericTypes(coursesWantToGetThisTerm) + "' Where id = " + studentId);

            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 1849");
        }


    }

    public void removeACourseFromCoursesWantToGetThisTerm(int studentId, String courseId){

        try {
            resultSet = statement.executeQuery("SELECT coursesWantToGetThisTerm FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            if(resultSet.next()){
                LinkedList<String> coursesWantToGetThisTerm = getListStringFromString(resultSet.getString("coursesWantToGetThisTerm"));

                for (int i = 0; i < coursesWantToGetThisTerm.size(); i++) {
                    if(coursesWantToGetThisTerm.get(i).equals(courseId)){
                        coursesWantToGetThisTerm.remove(i);
                        break;
                    }
                }

                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesWantToGetThisTerm = '" + passStringFormGenericTypes(coursesWantToGetThisTerm) + "'  Where id = " + studentId);

                ResultSet resultSet2 = statement.executeQuery("SELECT numGetCoursesUntilNow FROM `edu-users`.`coursestable` WHERE id = '" +  courseId + "'");
                if(resultSet2.next()){
                    statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET numGetCoursesUntilNow = " + (resultSet2.getInt("numGetCoursesUntilNow") - 1) + " Where id = '" + courseId + "'");
                }
            }
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 1897");
        }

    }

    public  LinkedList<String> getAllStudentsFromOneCollegeAndFromOneYear(String college, int yearComeToUniversity, int studentId, int teacherId){
        LinkedList<String> strings = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT firstName, lastName, studentNumber  FROM `edu-users`.`studentstable` WHERE (college = '" +  college + "' OR yearComeToUniversity = " + yearComeToUniversity + ") AND (NOT (id  = " + studentId + "))");
            while (resultSet.next()){
                strings.add(resultSet.getString("firstName") + " " + resultSet.getString("lastName") + "(" + resultSet.getString("studentNumber") + ")");
            }

            resultSet = statement.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
            while (resultSet.next()){
                strings.add(resultSet.getString("firstName") + " " + resultSet.getString("lastName") + "(" + resultSet.getString("username") + ")");
            }

            resultSet = statement.executeQuery("SELECT extraStudentsInChat, extraTeachersInChat FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            if (resultSet.next()){
                LinkedList<Integer> teachersInChat = getIntegersFromString(resultSet.getString("extraTeachersInChat"));
                LinkedList<Integer> studentsInChat = getIntegersFromString(resultSet.getString("extraStudentsInChat"));
                for (int i = 0; i < teachersInChat.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`teacherstable` WHERE id = " + teachersInChat.get(i));
                    if (resultSetForMessage.next()){
                        strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + resultSetForMessage.getString("username") + ")");
                    }
                }

                for (int i = 0; i < studentsInChat.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName, studentNumber FROM `edu-users`.`studentstable` WHERE id = " + studentsInChat.get(i));
                    if (resultSetForMessage.next()){
                        strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + resultSetForMessage.getString("studentNumber") + ")");
                    }
                }

            }


            List<String> distinctElements = strings.stream()
                    . distinct()
                    . collect(Collectors. toList());

            strings.clear();
            strings.addAll(distinctElements);
        }
        catch (SQLException throwables) {
            System.out.println("in Exception 1886");
        }

        return strings;
    }

    public  LinkedList<LinkedList<String>> getAllMessagesFromAStudent(String username){
        LinkedList<String> strings = new LinkedList<>();
        LinkedList<String> times = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`message` WHERE ( fromUsername = '" + username + "')");
            while (resultSet.next()){
                String toUsername = resultSet.getString("toUsername");
                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`teacherstable` WHERE username = '" + toUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable` WHERE studentNumber = '" + toUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }


                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`mohsenitable` WHERE username = '" + toUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`adminstable` WHERE username = '" + toUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

            }

            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`message` WHERE ( toUsername = '" + username + "')");

            while (resultSet.next()){
                String fromUsername = resultSet.getString("fromUsername");
                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`teacherstable` WHERE username = '" + fromUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable` WHERE studentNumber = '" + fromUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`mohsenitable` WHERE username = '" + fromUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`adminstable` WHERE username = '" + fromUsername + "'");
                while (resultSetForMessage.next()){
                    strings.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                    times.add(resultSet.getString("messageTime"));
                }

            }

            for (int i = 0; i < strings.size(); i++) {
                for (int j = i + 1; j < strings.size(); j++) {
                    if(times.get(i).equals(times.get(j))){
                        continue;
                    }
                    String s = WhichIsLatest(times.get(i), times.get(j));
                    if(strings.get(i).equals(strings.get(j))){
                        if(s.equals(times.get(i))){
                            strings.remove(j);
                            times.remove(j);
                        }
                        else{
                            strings.remove(i);
                            times.remove(i);
                        }

                        i = -1;
                        break;
                    }
                    else{
                        if(s.equals(times.get(i))){

                        }
                        else{
                            Collections.swap(strings, i, j);
                            Collections.swap(times, i, j);
                            i = -1;
                            break;
                        }
                    }
                }
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 1886");
        }

        LinkedList<LinkedList<String>> linkedLists = new LinkedList<>();
        linkedLists.add(strings);
        linkedLists.add(times);
        return linkedLists;
    }

    public LinkedList<LinkedList<String>> getAllMessagesFromAPerson(String usernamePerson, String usernameFront){

        LinkedList<LinkedList<String>> linkedLists = new LinkedList<>();

        try {

            LinkedList<String> typeFile = new LinkedList<>();
            LinkedList<String> address = new LinkedList<>();
            LinkedList<String> whoSent = new LinkedList<>(); // you | front
            LinkedList<String> messageTime = new LinkedList<>();


            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`message` WHERE (fromUsername = '" + usernamePerson + "'  AND toUsername = '" + usernameFront + "')");

            while (resultSet.next()){
                typeFile.add(resultSet.getString("typeFile"));
                address.add(resultSet.getString("fileAddress"));
                whoSent.add("you");
                messageTime.add(resultSet.getString("messageTime"));
            }


            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`message` WHERE (toUsername = '" + usernamePerson + "' AND fromUsername = '" + usernameFront + "')");

            while (resultSet.next()){
                typeFile.add(resultSet.getString("typeFile"));
                address.add(resultSet.getString("fileAddress"));
                whoSent.add("front");
                messageTime.add(resultSet.getString("messageTime"));
            }

            for (int i = 0; i < messageTime.size(); i++) {
                for (int j = i + 1; j < messageTime.size(); j++) {
                    if(messageTime.get(i).equals(messageTime.get(j))){
                        continue;
                    }
                    String s = WhichIsLatest(messageTime.get(i), messageTime.get(j));
                    if(s.equals(messageTime.get(i))){
                        Collections.swap(messageTime, i, j);
                        Collections.swap(whoSent, i, j);
                        Collections.swap(address, i, j);
                        Collections.swap(typeFile, i, j);
                    }
                }
            }

            linkedLists.add(typeFile);
            linkedLists.add(address);
            linkedLists.add(whoSent);
            linkedLists.add(messageTime);

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 2456");
        }

        return linkedLists;
    }
    
    public String getUserNameFromName(String nameFront, boolean isFrontStudent){


        try {
            String typePerson = "";
            if(isFrontStudent){
                resultSet = statement.executeQuery("SELECT firstName, lastName, studentNumber FROM `edu-users`.`studentstable`");

                while (resultSet.next()){
                    String s = resultSet.getString("firstName") + " " + resultSet.getString("lastName");

                    if(!s.equals(nameFront)){
                        continue;
                    }
                    return resultSet.getString("studentNumber");
                }
            }
            else{
                resultSet = statement.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`teacherstable`");

                while (resultSet.next()){
                    String s = resultSet.getString("firstName") + " " + resultSet.getString("lastName");

                    if(!s.equals(nameFront)){
                        continue;
                    }
                    return resultSet.getString("username");
                }

                resultSet = statement.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`mohsenitable`");

                while (resultSet.next()){
                    String s = resultSet.getString("firstName") + " " + resultSet.getString("lastName");

                    if(!s.equals(nameFront)){
                        continue;
                    }
                    return resultSet.getString("username");
                }

                resultSet = statement.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`adminstable`");

                while (resultSet.next()){
                    String s = resultSet.getString("firstName") + " " + resultSet.getString("lastName");

                    if(!s.equals(nameFront)){
                        continue;
                    }
                    return resultSet.getString("username");
                }
            }

        }
        catch (SQLException throwables) {
            System.out.println("in Exception 2092");
        }

        return "";
    }

    public String typePerson(String name){
        try {
            resultSet = statement.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable`");
            while (resultSet.next()){
                String s = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
                if(!s.equals(name)){
                    continue;
                }
                return "student";
            }


            resultSet = statement.executeQuery("SELECT firstName, lastName FROM `edu-users`.`teacherstable`");
            while (resultSet.next()){
                String s = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
                if(!s.equals(name)){
                    continue;
                }
                return "teacher";
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";

    }

    public LinkedList<EducationalMaterials> getEducationalMaterialsFromACourse(String courseId){
        LinkedList<EducationalMaterials> educationalMaterials = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT educationalMaterials FROM `edu-users`.`coursestable` WHERE id = '" + courseId + "'");
            if (resultSet.next()){
                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<EducationalMaterials>>(){}.getType();
                educationalMaterials = gson.fromJson(resultSet.getString("educationalMaterials"), listType);
                return educationalMaterials;
            }


        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return educationalMaterials;

    }

    public LinkedList<Exam> getExamsFromACourse(String courseId){
        LinkedList<Exam> exams = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT exams FROM `edu-users`.`coursestable` WHERE id = '" + courseId + "'");
            if (resultSet.next()){
                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<Exam>>(){}.getType();
                exams = gson.fromJson(resultSet.getString("exams"), listType);
                return exams;
            }


        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return exams;

    }

    public LinkedList<HomeWork> getHomeWorksFromACourse(String courseId){
        LinkedList<HomeWork> educationalMaterials = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT homeWorks FROM `edu-users`.`coursestable` WHERE id = '" + courseId + "'");
            if (resultSet.next()){
                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<HomeWork>>(){}.getType();
                educationalMaterials = gson.fromJson(resultSet.getString("homeWorks"), listType);
                return educationalMaterials;
            }


        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return educationalMaterials;

    }

    public void changeAnswerForHomeWorkOrExamForAStudent(String courseId, String exams, String homeWorks, boolean isExam){
        try {
            if(isExam){
                statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET exmas = '" + exams + "' Where id = '" + courseId + "'");
            }
            else{
                statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET homeWorks = '" + homeWorks + "' Where id = '" + courseId + "'");
            }

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }


    }

    public LinkedList<String> getMessagesForAStudent(int studentId){
        LinkedList<String> messagesString = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT messages FROM `edu-users`.`studentstable` WHERE id = " + studentId);

            while (resultSet.next()){
                MessagesForStudent messagesForStudent = (new Gson()).fromJson(resultSet.getString("messages"), MessagesForStudent.class);

                for (Map.Entry<String, String> entry : messagesForStudent.getMessages().entrySet()) {
                    String value = entry.getValue();
                    messagesString.add(value);
                }
                break;
            }

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return messagesString;
    }

    public LinkedList<String> getMessagesForATeacher(int teacherId){
        LinkedList<String> messages = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT messagesForTeacher FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);

            while (resultSet.next()){
                MessagesForTeacher messagesForTeacher = (new Gson()).fromJson(resultSet.getString("messagesForTeacher"),MessagesForTeacher.class);
                for (Map.Entry<String, String> entry : messagesForTeacher.getMessages().entrySet()) {
                    String value = entry.getValue();
                    messages.add(value);
                }
            }

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return messages;
    }

    public boolean sendMessageToEAToGetACourse(String studentNumber, String college, String courseId){
        try {
            resultSet = statement.executeQuery("SELECT messagesForTeacher FROM `edu-users`.`teacherstable` WHERE college = '" + college + "' AND statusTeacher = 'EA'");
            while (resultSet.next()){
                MessagesForTeacher messagesForTeacher = (new Gson()).fromJson(resultSet.getString("messagesForTeacher"),MessagesForTeacher.class);
                String text = "student with student number : " + studentNumber + " want to get course with id : " + courseId;
                if(messagesForTeacher.getMessages().containsValue(text)){
                    return false;
                }
                messagesForTeacher.addNewMessage(text, MessagesTypeForTeacher.getCourse);
                Gson gson = new Gson();
                statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET messagesForTeacher = '" + gson.toJson(messagesForTeacher) + "' WHERE college = '" + college + "' AND statusTeacher = 'EA'");

                return true;
            }

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return false;

    }

    public boolean sendMessageToAPersonForRequestChatRoom(String type, String username, String myUsername){
        try {
            if(type.toLowerCase().equals("student")){
                resultSet = statement.executeQuery("SELECT messages FROM `edu-users`.`studentstable` WHERE studentNumber = '" + username + "'");
                while (resultSet.next()){
                    Gson gson = new Gson();
                    MessagesForStudent messagesForStudent = gson.fromJson(resultSet.getString("messages"),MessagesForStudent.class);
                    String text = "student with student number : " + myUsername + " want to send message in chat room";
                    if(messagesForStudent.getMessages().containsValue(text)){
                        return false;
                    }
                    messagesForStudent.addNewMessage(text, MessageTypeForStudent.sendMessageInChatRoom);
                    statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET messages = '" + gson.toJson(messagesForStudent) + "' WHERE studentNumber = '" + username + "'");
                    return true;
                }
            }
            else if(type.toLowerCase().equals("teacher")){
                resultSet = statement.executeQuery("SELECT messagesForTeacher FROM `edu-users`.`teacherstable` WHERE username = '" + username + "'");
                while (resultSet.next()){
                    Gson gson = new Gson();
                    MessagesForTeacher messagesForTeacher = gson.fromJson(resultSet.getString("messagesForTeacher"),MessagesForTeacher.class);
                    String text = "student with student number : " + myUsername + " want to send message in chat room";
                    if(messagesForTeacher.getMessages().containsValue(text)){
                        return false;
                    }
                    messagesForTeacher.addNewMessage(text, MessagesTypeForTeacher.sendMessageInChatRoom);
                    statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET messagesForTeacher = '" + gson.toJson(messagesForTeacher) + "' WHERE username = '" + username + "'");
                    return true;
                }

            }
        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return false;

    }

    public boolean addStudentToCourseInCW(String type, String username, String courseId){
        try {
            if(type.toLowerCase().equals("student")){
                resultSet = statement.executeQuery("SELECT timeForSelectUnit, coursesThisTerm, coursesThisTermScore, coursesWantToGetThisTerm, answerTeacher, protestForCourseStudent ,messages FROM `edu-users`.`studentstable` WHERE studentNumber = '" + username + "'");

                if (resultSet.next()){
                    String timeForSelectUnit = resultSet.getString("timeForSelectUnit");
                    if(timeForSelectUnit == null){
                        LinkedList<String> coursesThisTerm  = getListStringFromString(resultSet.getString("coursesThisTerm"));
                        LinkedList<Double> coursesThisTermScore  = getCoursesScoreFromString(resultSet.getString("coursesThisTermScore"));
                        LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                        LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));
                        Gson gson = new Gson();
                        MessagesForStudent messages  = gson.fromJson(resultSet.getString("messages") , MessagesForStudent.class);

                        if(coursesThisTerm.contains(courseId)){
                            return false;
                        }
                        else{
                            messages.addNewMessage("You are added to course with id " + courseId + " as student" , MessageTypeForStudent.simpleMessage);
                            coursesThisTerm.add(courseId);
                            coursesThisTermScore.add(-1.0);
                            answerTeacher.add(" ");
                            protestForCourseStudent.add(" ");
                            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + passStringFormGenericTypes(coursesThisTerm) + "' , messages = '" + gson.toJson(messages) + "', coursesThisTermScore = '" + passStringFormGenericTypes(coursesThisTermScore) + "', protestForCourseStudent = '" + passStringFormGenericTypes(protestForCourseStudent) + "', answerTeacher = '" + passStringFormGenericTypes(answerTeacher) + "'  WHERE studentNumber = '" + username + "'");
                            return true;
                        }
                    }
                    else if(timeForSelectUnit.equals("")){
                        LinkedList<String> coursesThisTerm  = getListStringFromString(resultSet.getString("coursesThisTerm"));
                        LinkedList<Double> coursesThisTermScore  = getCoursesScoreFromString(resultSet.getString("coursesThisTermScore"));
                        LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                        LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));
                        Gson gson = new Gson();
                        MessagesForStudent messages  = gson.fromJson(resultSet.getString("messages") , MessagesForStudent.class);

                        if(coursesThisTerm.contains(courseId)){
                            return false;
                        }
                        else{
                            messages.addNewMessage("You are added to course with id " + courseId + " as student" , MessageTypeForStudent.simpleMessage);
                            coursesThisTerm.add(courseId);
                            coursesThisTermScore.add(-1.0);
                            answerTeacher.add(" ");
                            protestForCourseStudent.add(" ");
                            statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + passStringFormGenericTypes(coursesThisTerm) + "', messages = '" + gson.toJson(messages) + "', coursesThisTermScore = '" + passStringFormGenericTypes(coursesThisTermScore) + "', protestForCourseStudent = '" + passStringFormGenericTypes(protestForCourseStudent) + "', answerTeacher  = '" + passStringFormGenericTypes(answerTeacher) + "'  WHERE studentNumber = '" + username + "'");
                            return true;
                        }
                    }
                    else {
                        String[] strings1 = timeForSelectUnit.split("-");
                        LocalDateTime ldt = LocalDateTime.now();
                        Integer[] integers2 = new Integer[]{ldt.getYear(), ldt.getMonthValue() , ldt.getDayOfMonth()};
                        boolean isPast = true;

                        if(Integer.parseInt(strings1[0]) == integers2[0] && Integer.parseInt(strings1[1]) == integers2[1] && Integer.parseInt(strings1[2]) == integers2[2]){
                            isPast = false;
                        }

                        if (!isPast) {
                            LinkedList<String> coursesWantToGetThisTerm  = getListStringFromString(resultSet.getString("coursesWantToGetThisTerm"));
                            Gson gson = new Gson();
                            MessagesForStudent messages  = gson.fromJson(resultSet.getString("messages") , MessagesForStudent.class);

                            if(coursesWantToGetThisTerm.contains(courseId)){
                                return false;
                            }
                            else{
                                messages.addNewMessage("You are added to course with id " + courseId + " as student" , MessageTypeForStudent.simpleMessage);
                                coursesWantToGetThisTerm.add(courseId);
                                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesWantToGetThisTerm = '" + passStringFormGenericTypes(coursesWantToGetThisTerm) + "' , messages = '" + gson.toJson(messages) + "'   WHERE studentNumber = '" + username + "'");
                                return true;
                            }
                        }
                        else{
                            LinkedList<String> coursesThisTerm  = getListStringFromString(resultSet.getString("coursesThisTerm"));
                            LinkedList<Double> coursesThisTermScore  = getCoursesScoreFromString(resultSet.getString("coursesThisTermScore"));
                            LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                            LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));
                            Gson gson = new Gson();
                            MessagesForStudent messages  = gson.fromJson(resultSet.getString("messages") , MessagesForStudent.class);

                            if(coursesThisTerm.contains(courseId)){
                                return false;
                            }
                            else{
                                messages.addNewMessage("You are added to course with id " + courseId + " as student" , MessageTypeForStudent.simpleMessage);
                                coursesThisTerm.add(courseId);
                                coursesThisTermScore.add(-1.0);
                                answerTeacher.add(" ");
                                protestForCourseStudent.add(" ");
                                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + passStringFormGenericTypes(coursesThisTerm) + "' , messages = '" + gson.toJson(messages) + "', coursesThisTermScore = '" + passStringFormGenericTypes(coursesThisTermScore) + "', protestForCourseStudent = '" + passStringFormGenericTypes(protestForCourseStudent) + "', answerTeacher = '" + passStringFormGenericTypes(answerTeacher) + "'  WHERE studentNumber = '" + username + "'");
                                return true;
                            }
                        }
                    }
                }
            }
            else if(type.toLowerCase().equals("ta")){
                resultSet = statement.executeQuery("SELECT  coursesIsTANow, messages FROM `edu-users`.`studentstable` WHERE studentNumber = '" + username + "'");

                Gson gson = new Gson();

                if (resultSet.next()){
                    MessagesForStudent messages  = gson.fromJson(resultSet.getString("messages") , MessagesForStudent.class);
                    LinkedList<String> coursesIsTANow  = getListStringFromString(resultSet.getString("coursesIsTANow"));
                    if(coursesIsTANow.contains(courseId)){
                        return false;
                    }
                    else{
                        messages.addNewMessage("You are added to course with id " + courseId + " as TA" , MessageTypeForStudent.simpleMessage);
                        coursesIsTANow.add(courseId);
                        statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesIsTANow = '" + passStringFormGenericTypes(coursesIsTANow) + "' , messages = '" + gson.toJson(messages) + "' WHERE studentNumber = '" + username + "'");
                        resultSetForMessage = statementForMessage.executeQuery("SELECT TAsStudentNumber FROM `edu-users`.`coursestable` WHERE  id = '" + courseId + "'");
                        if(resultSetForMessage.next()){
                            LinkedList<String> TAsStudentNumber = getListStringFromString(resultSetForMessage.getString("TAsStudentNumber"));
                            TAsStudentNumber.add(username);
                            statementForMessage.executeUpdate("UPDATE `edu-users`.`coursestable` SET TAsStudentNumber = '" + passStringFormGenericTypes(TAsStudentNumber) + "' WHERE  id = '" + courseId + "'") ;
                        }

                        return true;
                    }
                }

            }

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return false;

    }

    public boolean isAStudentTAOfACourse(int studentId, String courseId){
        try {
            resultSet = statement.executeQuery("SELECT coursesIsTANow FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            if (resultSet.next()){
                LinkedList<String> coursesIsTANow  = getListStringFromString(resultSet.getString("coursesIsTANow"));
                if(coursesIsTANow.contains(courseId)){
                    return true;
                }
                else{
                    return false;
                }
            }

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return false;

    }

    public void removeAMessageFromAStudent(int studentId, String messageString, boolean isAcceptAndReject, boolean isAcceptedIfPreviousIsTrue){
        MessagesForStudent messagesForStudent = new MessagesForStudent();
        try {
            resultSet = statement.executeQuery("SELECT extraStudentsInChat , messages FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            Gson gson = new Gson();
            if (resultSet.next()){
                messagesForStudent = gson.fromJson(resultSet.getString("messages"), MessagesForStudent.class);
            }

            if(isAcceptAndReject){
                LinkedList<Integer> studentsInChat = getIntegersFromString(resultSet.getString("extraStudentsInChat"));
                String studentNumber = "";
                String p = messageString.substring(30);
                for (int i = 0; i < p.length(); i++) {
                    if(p.charAt(i) >= '0' & p.charAt(i) <= '9'){
                        studentNumber = studentNumber + p.charAt(i);
                    }
                    else{
                        break;
                    }
                }

                if(studentNumber.equals("")){
                    System.out.println("wrong 2379");
                    return;
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT id, extraStudentsInChat FROM `edu-users`.`studentstable` WHERE studentNumber = '" + studentNumber + "'");


                if(isAcceptedIfPreviousIsTrue){
                    if(resultSetForMessage.next()){
                        int id = resultSetForMessage.getInt("id");
                        LinkedList<Integer> studentsInChat2 = getIntegersFromString(resultSetForMessage.getString("extraStudentsInChat"));
                        studentsInChat.add(id);
                        statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable`  SET  extraStudentsInChat = '" + passStringFormGenericTypes(studentsInChat) + "' Where id = " + studentId);
                        studentsInChat2.add(studentId);
                        statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable`  SET  extraStudentsInChat = '" + passStringFormGenericTypes(studentsInChat2) + "' Where studentNumber = '" + studentNumber + "'");
                    }
                }
            }

            for (Map.Entry<String, String> entry : messagesForStudent.getMessages().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(value.equals(messageString)){
                    messagesForStudent.getMessages().remove(key);
                    messagesForStudent.getMessagesType().remove(key);
                    break;
                }
            }


            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET messages = '" + gson.toJson(messagesForStudent) + "' Where id = " + studentId);

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public void removeAMessageFromATeacherForMessages(int teacherId, String messageString, boolean isAccept){
        try {
            String time = "";
            resultSet = statement.executeQuery("SELECT messagesForTeacher, extraStudentsInChat FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
            Gson gson = new Gson();
            MessagesForTeacher messagesForTeacher = new MessagesForTeacher();
            if (resultSet.next()){
                messagesForTeacher = gson.fromJson(resultSet.getString("messagesForTeacher"), MessagesForTeacher.class);

                for (Map.Entry<String, String> entry : messagesForTeacher.getMessages().entrySet()) {
                    String value = entry.getValue();
                    if(value.equals(messageString)){
                        time = entry.getKey();
                        break;
                    }
                }

                if(time == null){
                    return;
                }
                else if(time.equals("")){
                    return;
                }
                else if(messagesForTeacher.getMessagesType().get(time) == MessagesTypeForTeacher.getCourse){
                    String s = messagesForTeacher.getMessages().get(time);
                    if(s.charAt(s.length() - 1) >= '0' &&  s.charAt(s.length() - 1) <= '9'){
                        String p = s.substring(30);
                        String studentNumber = "";
                        for (int i = 0; i < p.length(); i++) {
                            if(p.charAt(i) >= '0' & p.charAt(i) <= '9'){
                                studentNumber = studentNumber + p.charAt(i);
                            }
                            else{
                                break;
                            }
                        }

                        if(studentNumber.equals("")){
                            System.out.println("wrong 2410");
                            return;
                        }

                        p = s.substring(60 + studentNumber.length());
                        String courseId = "";
                        for (int i = 0; i < p.length(); i++) {
                            courseId = courseId + p.charAt(i);
                        }

                        if(courseId.equals("")){
                            System.out.println("wrong 2427");
                            return;
                        }


                        resultSetForMessage = statementForMessage.executeQuery("SELECT coursesWantToGetThisTerm, timeForSelectUnit, coursesThisTerm, coursesThisTermScore, answerTeacher, protestForCourseStudent From `edu-users`.`studentstable` Where studentNumber  = '" + studentNumber + "'");
                        while (resultSetForMessage.next()){
                            String timeForSelectUnit = resultSetForMessage.getString("timeForSelectUnit");
                            if(timeForSelectUnit == null){
                                if(isAccept){
                                    LinkedList<Double> coursesThisTermScore = getCoursesScoreFromString(resultSetForMessage.getString("coursesThisTermScore"));
                                    LinkedList<String> coursesThisTerm = getListStringFromString(resultSetForMessage.getString("coursesThisTerm"));
                                    LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                                    LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));

                                    for (int i = 0; i < coursesThisTerm.size(); i++) {
                                        if(coursesThisTerm.get(i).equals(courseId)){
                                            coursesThisTerm.remove(i);
                                            coursesThisTermScore.remove(i);
                                            answerTeacher.remove(i);
                                            protestForCourseStudent.remove(i);
                                        }
                                    }

                                    coursesThisTermScore.add(-1.0);
                                    coursesThisTerm.add(courseId);
                                    protestForCourseStudent.add(" ");
                                    answerTeacher.add(" ");

                                    statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + passStringFormGenericTypes(coursesThisTerm) + "', coursesThisTermScore = '" + passStringFormGenericTypes(coursesThisTermScore) + "', protestForCourseStudent = '" + passStringFormGenericTypes(protestForCourseStudent) + "', answerTeacher = '" + passStringFormGenericTypes(answerTeacher) +  "'  Where studentNumber = '" + studentNumber + "'");
                                }
                            }
                            else if(timeForSelectUnit.equals("")){
                                if(isAccept){
                                    LinkedList<Double> coursesThisTermScore = getCoursesScoreFromString(resultSetForMessage.getString("coursesThisTermScore"));
                                    LinkedList<String> coursesThisTerm = getListStringFromString(resultSetForMessage.getString("coursesThisTerm"));
                                    LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                                    LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));

                                    for (int i = 0; i < coursesThisTerm.size(); i++) {
                                        if(coursesThisTerm.get(i).equals(courseId)){
                                            coursesThisTerm.remove(i);
                                            coursesThisTermScore.remove(i);
                                            answerTeacher.remove(i);
                                            protestForCourseStudent.remove(i);
                                        }
                                    }

                                    coursesThisTermScore.add(-1.0);
                                    coursesThisTerm.add(courseId);
                                    protestForCourseStudent.add(" ");
                                    answerTeacher.add(" ");

                                    statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + passStringFormGenericTypes(coursesThisTerm) + "', coursesThisTermScore = '" + passStringFormGenericTypes(coursesThisTermScore) + "', protestForCourseStudent = '" + passStringFormGenericTypes(protestForCourseStudent) + "', answerTeacher = '" + passStringFormGenericTypes(answerTeacher) +  "'  Where studentNumber = '" + studentNumber + "'");
                                }
                            }
                            else {
                                String[] strings1 = timeForSelectUnit.split("-");
                                LocalDateTime ldt = LocalDateTime.now();
                                Integer[] integers2 = new Integer[]{ldt.getYear(), ldt.getMonthValue() , ldt.getDayOfMonth()};
                                boolean isPast = true;

                                if(Integer.parseInt(strings1[0]) == integers2[0] && Integer.parseInt(strings1[1]) == integers2[1] && Integer.parseInt(strings1[2]) == integers2[2]){
                                    isPast = false;
                                }

                                if (!isPast) {
                                    if(isAccept){
                                        LinkedList<String> coursesWantToGetThisTerm = getListStringFromString(resultSetForMessage.getString("coursesWantToGetThisTerm"));
                                        for (int i = 0; i < coursesWantToGetThisTerm.size(); i++) {
                                            if(coursesWantToGetThisTerm.get(i).equals(courseId)){
                                                coursesWantToGetThisTerm.remove(i);
                                                break;
                                            }
                                        }

                                        coursesWantToGetThisTerm.add(courseId);
                                        statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesWantToGetThisTerm = '" + passStringFormGenericTypes(coursesWantToGetThisTerm) + "' Where studentNumber = '" + studentNumber + "'");
                                    }
                                }
                                else{
                                    if(isAccept){
                                        LinkedList<Double> coursesThisTermScore = getCoursesScoreFromString(resultSetForMessage.getString("coursesThisTermScore"));
                                        LinkedList<String> coursesThisTerm = getListStringFromString(resultSetForMessage.getString("coursesThisTerm"));
                                        LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                                        LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));

                                        for (int i = 0; i < coursesThisTerm.size(); i++) {
                                            if(coursesThisTerm.get(i).equals(courseId)){
                                                coursesThisTerm.remove(i);
                                                coursesThisTermScore.remove(i);
                                                answerTeacher.remove(i);
                                                protestForCourseStudent.remove(i);
                                            }
                                        }

                                        coursesThisTermScore.add(-1.0);
                                        coursesThisTerm.add(courseId);
                                        protestForCourseStudent.add(" ");
                                        answerTeacher.add(" ");

                                        statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesThisTerm = '" + passStringFormGenericTypes(coursesThisTerm) + "', coursesThisTermScore = '" + passStringFormGenericTypes(coursesThisTermScore) + "', protestForCourseStudent = '" + passStringFormGenericTypes(protestForCourseStudent) + "', answerTeacher = '" + passStringFormGenericTypes(answerTeacher) +  "'  Where studentNumber = '" + studentNumber + "'");
                                    }
                                }
                            }

                            break;
                        }

                    }
                    else {
                        System.out.println("wrong 2476");
                        return;
                    }
                }
                else if(messagesForTeacher.getMessagesType().get(time) == MessagesTypeForTeacher.sendMessageInChatRoom){
                    String s = messagesForTeacher.getMessages().get(time);
                    if(s.charAt(s.length() - 1) == 'm'){
                        String p = s.substring(30);
                        String studentNumber = "";
                        for (int i = 0; i < p.length(); i++) {
                            if(p.charAt(i) >= '0' & p.charAt(i) <= '9'){
                                studentNumber = studentNumber + p.charAt(i);
                            }
                            else{
                                break;
                            }
                        }

                        if(studentNumber.equals("")){
                            System.out.println("wrong 2495");
                            return;
                        }

                        resultSetForMessage = statementForMessage.executeQuery("SELECT * From `edu-users`.`studentstable` Where studentNumber  = '" + studentNumber + "'");

                        if (resultSetForMessage.next()){
                            if(isAccept){
                                int id = resultSetForMessage.getInt("id");
                                LinkedList<Integer>  extraTeachersInChat = getIntegersFromString(resultSetForMessage.getString("extraTeachersInChat"));
                                extraTeachersInChat.add(teacherId);
                                statementForMessage.executeUpdate("UPDATE `edu-users`.`studentstable` SET extraTeachersInChat = '" + passStringFormGenericTypes(extraTeachersInChat) + "' Where studentNumber = '" + studentNumber + "'");
                                LinkedList<Integer> extraStudentsInChat = getIntegersFromString(resultSet.getString("extraStudentsInChat"));
                                extraStudentsInChat.add(id);
                                statementForMessage.executeUpdate("UPDATE `edu-users`.`teacherstable` SET extraStudentsInChat = '" + passStringFormGenericTypes(extraStudentsInChat) + "' Where id = " + teacherId);
                            }
                        }
                    }
                    else {
                        System.out.println("wrong 2516");
                        return;
                    }
                }

                messagesForTeacher.getMessages().remove(time);
                messagesForTeacher.getMessagesType().remove(time);

            }

            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET messagesForTeacher = '" + gson.toJson(messagesForTeacher) + "' Where id = " + teacherId);

        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public String getTimeSelectUnitForAStudent(int studentId){
        try {
            resultSet = statement.executeQuery("SELECT timeForSelectUnit FROM `edu-users`.`studentstable` WHERE id = " + studentId);
            while (resultSet.next()){
                return resultSet.getString("timeForSelectUnit");
            }


        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";

    }

    public LinkedList<String> FindATeacherOrStudentForChatRoom(String myUserName, String type, String username){
        LinkedList<String> forReturn = new LinkedList<>();
        try {
            if(type.toLowerCase().equals("teacher")){
                resultSet = statement.executeQuery("SELECT firstName, lastName FROM `edu-users`.`teacherstable` WHERE username = '" + username + "' AND NOT (username = '" + myUserName + "')");
                while (resultSet.next()){
                    forReturn.add(resultSet.getString("firstName") + " " + resultSet.getString("lastName") + "(" + username + ")");
                }
            }
            else if(type.toLowerCase().equals("student")){
                resultSet = statement.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable` WHERE studentNumber = '" + username + "' AND NOT (studentNumber = '" + myUserName + "')");
                while (resultSet.next()){
                    forReturn.add(resultSet.getString("firstName") + " " + resultSet.getString("lastName")  + "(" + username + ")");
                }
            }
//            can not send message to mr mohseni for first time;
        }

        catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return forReturn;

    }

    public void addNewFileInCwForExamOrHw(String courseId, boolean isExam, String s){
        if(isExam){
            try {
                resultSet = statement.executeQuery("SELECT exams FROM `edu-users`.`coursestable`  Where id = '" + courseId + "'");
                if(resultSet.next()){
                    Gson gson = new Gson();
                    Type listType = new TypeToken<LinkedList<Exam>>(){}.getType();
                    LinkedList<Exam> exams = gson.fromJson(resultSet.getString("exams"), listType);
                    exams.add(gson.fromJson(s, Exam.class));
                    s = gson.toJson(exams);
                    statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET exams = '" + s + "' Where id = '" + courseId + "'");

                }
            }
            catch (SQLException throwables) {
                            System.out.println("exception");

            }
        }
        else{
            try {
                resultSet = statement.executeQuery("SELECT homeWorks FROM `edu-users`.`coursestable`  Where id = '" + courseId + "'");
                if(resultSet.next()){
                    Gson gson = new Gson();
                    Type listType = new TypeToken<LinkedList<HomeWork>>(){}.getType();
                    LinkedList<HomeWork> homeWorks = gson.fromJson(resultSet.getString("homeWorks"), listType);
                    homeWorks.add(gson.fromJson(s, HomeWork.class));
                    s = gson.toJson(homeWorks);
                    statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET homeWorks = '" + s + "' Where id = '" + courseId + "'");
                }
            }
            catch (SQLException throwables) {
                            System.out.println("exception");

            }
        }
    }

    public String getNameOfAStudentFromId(int studentId){
        try {
            resultSet = statement.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable`  Where id = " + studentId);
            if(resultSet.next()){
                return resultSet.getString("firstName") + " " + resultSet.getString("lastName");
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";
    }

    public String getStudentsNumberOfAStudentFromId(int studentId){
        try {
            resultSet = statement.executeQuery("SELECT studentNumber FROM `edu-users`.`studentstable`  Where id = " + studentId);
            if(resultSet.next()){
                return resultSet.getString("studentNumber");
            }
        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";
    }

    public void saveExamsOrHWsForCw(String courseId, String s, boolean isExam){
        if(isExam){
            try {
                statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET exams = '" + s + "' Where id = '" + courseId+ "'");
            } catch (SQLException throwables) {

            }
        }
        else{
            try {
                statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET homeWorks = '" + s + "' Where id = '" + courseId + "'");
            } catch (SQLException throwables) {

            }
        }

    }

    public void changeEducationalMaterialForACourse(String courseId, String s){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`coursestable` SET educationalMaterials = '" + s + "' Where id = '" + courseId + "'");
        } catch (SQLException throwables) {

        }

    }

    public LinkedList<Teacher> getAllTeacherFormACollege(int teacherId, String college){
        LinkedList<Teacher> teachers = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`teacherstable` WHERE college = '" + college + "' AND NOT (id = " + teacherId + ")");
            while (resultSet.next()){
                teachers.add(getATeacherFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {

        }

        return teachers;

    }

    public LinkedList<Course> getCoursesOfATeacher(int teacherId){
        LinkedList<Course> courses = new LinkedList<>();
        try {
            resultSetForMessage = statementForMessage.executeQuery("SELECT courses  FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
            if (resultSetForMessage.next()){
                LinkedList<String> ids = getListStringFromString(resultSetForMessage.getString("courses"));
                for (int i = 0; i < ids.size(); i++) {
                    Course course = FindCourseFromId(ids.get(i));
                    courses.add(course);
                }

            }
        } catch (SQLException throwables) {

        }

        return courses;

    }

    public LinkedList<Integer> getCoursesGroup(int publicId){
        LinkedList<Integer> forReturn = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT id  FROM `edu-users`.`coursestable` WHERE publicId = " + publicId);
            while (resultSet.next()){
                String[] s = resultSet.getString("id").split("-");
                forReturn.add(Integer.parseInt(s[1]));
            }
        } catch (SQLException throwables) {

        }

        return forReturn;

    }

    public Student findStudentFromStudentNumber(String studentNumber){
        try {
            resultSet = statement.executeQuery("SELECT *  FROM `edu-users`.`studentstable` WHERE studentNumber = '" + studentNumber + "'");
            if (resultSet.next()){
                return getAStudentFromResultSet(resultSet);
            }
        } catch (SQLException throwables) {

        }

        return null;

    }

    public String findLastTimeSendMessage(String firstUsername, String secondUsername){
        LinkedList<String> times = new LinkedList<>();
        try {
            resultSetForMessage = statementForMessage.executeQuery("SELECT * FROM `edu-users`.`message` WHERE ( fromUsername = '" + firstUsername + "' AND toUsername = '" + secondUsername + "')");
            while (resultSetForMessage.next()){
                times.add(resultSetForMessage.getString("messageTime"));
            }

            resultSetForMessage = statementForMessage.executeQuery("SELECT * FROM `edu-users`.`message` WHERE ( toUsername = '" + firstUsername + "' AND fromUsername = '" + secondUsername + "')");
            while (resultSetForMessage.next()){
                times.add(resultSetForMessage.getString("messageTime"));
            }

            if(times.size() == 0){
                return "";
            }
            for (int i = 0; i < times.size(); i++) {
                for (int j = i + 1; j < times.size(); j++) {
                    if(times.get(i).equals(times.get(j))){
                        continue;
                    }
                    String s = WhichIsLatest(times.get(i), times.get(j));
                    if(s.equals(times.get(i))){
                        times.remove(j);
                        i = -1 ;
                        break;
                    }
                    else if(s.equals(times.get(j))){
                        times.remove(i);
                        i = -1 ;
                        break;
                    }
                }
            }

            return times.getLast();

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

        return "";

    }

    public LinkedList<LinkedList<String>> getAllStudentForATeacherInChatRoom(int teacherId, String username){
        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> times = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT studentNumber, firstName, lastName FROM `edu-users`.`studentstable` WHERE helpTeacher = " + teacherId);
            while (resultSet.next()){
                times.add(findLastTimeSendMessage(username, resultSet.getString("studentNumber")));
                names.add(resultSet.getString("firstName") + " " + resultSet.getString("lastName") + "(" + resultSet.getString("studentNumber") + ")");
            }

            resultSet = statement.executeQuery("SELECT extraStudentsInChat, extraTeachersInChat FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
            if (resultSet.next()){
                LinkedList<Integer> teachersInChat = getIntegersFromString(resultSet.getString("extraTeachersInChat"));
                LinkedList<Integer> studentsInChat = getIntegersFromString(resultSet.getString("extraStudentsInChat"));
                for (int i = 0; i < teachersInChat.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`teacherstable` WHERE id = " + teachersInChat.get(i));
                    if (resultSetForMessage.next()){
                        times.add(findLastTimeSendMessage(username, resultSet.getString("username")));
                        names.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + resultSetForMessage.getString("username") + ")");

                    }
                }

                for (int i = 0; i < studentsInChat.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName, studentNumber FROM `edu-users`.`studentstable` WHERE id = " + studentsInChat.get(i));
                    if (resultSetForMessage.next()){
                        times.add(findLastTimeSendMessage(username, resultSet.getString("studentNumber")));
                        names.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + resultSetForMessage.getString("studentNumber") + ")");
                    }
                }

            }

        }
        catch (SQLException throwables) {

        }

        LinkedList<LinkedList<String>> linkedLists = new LinkedList<>();
        linkedLists.add(names);
        linkedLists.add(times);


        for (int i = 0; i < times.size(); i++) {
            for (int j = i + 1; j < times.size(); j++) {
                if(times.get(i).equals("") | times.get(j).equals("")){
                    continue;
                }
                String s = WhichIsLatest(times.get(i), times.get(j));
                if(s.equals(times.get(j))){
                    Collections.swap(times, i, j);
                    Collections.swap(names, i, j);
                    i = -1;
                    break;
                }
            }
        }

        return linkedLists;

    }

    public LinkedList<LinkedList<String>> getAllStudentForABossAndEAInChatRoom(int teacherId, String username, String college){
        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> times = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT studentNumber, firstName, lastName FROM `edu-users`.`studentstable` WHERE college = '" + college + "'");
            while (resultSet.next()){
                times.add(findLastTimeSendMessage(username, resultSet.getString("studentNumber")));
                names.add(resultSet.getString("firstName") + " " + resultSet.getString("lastName") + "(" + resultSet.getString("studentNumber") + ")");
            }

            resultSet = statement.executeQuery("SELECT extraStudentsInChat, extraTeachersInChat FROM `edu-users`.`teacherstable` WHERE id = " + teacherId);
            if (resultSet.next()){
                LinkedList<Integer> teachersInChat = getIntegersFromString(resultSet.getString("extraTeachersInChat"));
                LinkedList<Integer> studentsInChat = getIntegersFromString(resultSet.getString("extraStudentsInChat"));
                for (int i = 0; i < teachersInChat.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName, username FROM `edu-users`.`teacherstable` WHERE id = " + teachersInChat.get(i));
                    if (resultSetForMessage.next()){
                        times.add(findLastTimeSendMessage(username, resultSet.getString("username")));
                        names.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + resultSetForMessage.getString("username") + ")");

                    }
                }

                for (int i = 0; i < studentsInChat.size(); i++) {
                    resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName, studentNumber FROM `edu-users`.`studentstable` WHERE id = " + studentsInChat.get(i));
                    if (resultSetForMessage.next()){
                        times.add(findLastTimeSendMessage(username, resultSet.getString("studentNumber")));
                        names.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + resultSetForMessage.getString("studentNumber") + ")");
                    }
                }

            }

        }
        catch (SQLException throwables) {

        }

        for (int i = 0; i < names.size(); i++) {
            for (int j = i + 1; j < names.size(); j++) {
                if(names.get(i).equals(names.get(j))){
                    if(times.get(i).equals(times.get(j))){
                        continue;
                    }
                    String s = WhichIsLatest(times.get(i), times.get(j));
                    if(s.equals(times.get(i))){
                        names.remove(j);
                        times.remove(j);
                    }
                    else{
                        names.remove(i);
                        times.remove(i);
                    }

                    i = -1;
                    break;
                }
                else{
                    if(times.get(i).equals(times.get(j))){
                        continue;
                    }
                    String s = WhichIsLatest(times.get(i), times.get(j));
                    if(s.equals(times.get(i))){

                    }
                    else{
                        Collections.swap(names, i, j);
                        Collections.swap(times, i, j);
                        i = -1;
                        break;
                    }
                }
            }
        }





        LinkedList<LinkedList<String>> linkedLists = new LinkedList<>();
        linkedLists.add(names);
        linkedLists.add(times);

        return linkedLists;

    }

    public boolean saveMessagesBecauseNotConnected(String username, LinkedList<String> toPersonsUsername, LinkedList<String> AddressOrText, LinkedList<String> Time, LinkedList<String> TypeFile){
        try {
            for (int i = 0; i < toPersonsUsername.size(); i++) {
                statement.executeUpdate("INSERT INTO `edu-users`.`message` (fromUsername, toUsername, fileAddress, messageTime, typeFile) " +
                        "VALUES ('" + username + "', '" + toPersonsUsername.get(i) + "', '" + AddressOrText.get(i) +"', '" + Time.get(i) +"', '" + TypeFile.get(i) + "')");
            }

            return true;

        }
        catch (SQLException throwables) {

        }

        return false;

    }


    public LinkedList<LinkedList<String>> getChatRoomsInfo(String username){
        LinkedList<LinkedList<String>> linkedLists = new LinkedList<>();
        LinkedList<String> whoSend = new LinkedList<>();
        LinkedList<String> frontName = new LinkedList<>();
        LinkedList<String> fileAddress = new LinkedList<>();
        LinkedList<String> messageTime = new LinkedList<>();
        LinkedList<String> typeFile = new LinkedList<>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`message` WHERE ( fromUsername = '" + username + "')");
            while (resultSet.next()){
                whoSend.add("you");
                fileAddress.add(resultSet.getString("fileAddress"));
                messageTime.add(resultSet.getString("messageTime"));
                typeFile.add(resultSet.getString("typeFile"));

                String toUsername = resultSet.getString("toUsername");
                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`teacherstable` WHERE username = '" + toUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable` WHERE studentNumber = '" + toUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                }


                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`mohsenitable` WHERE username = '" + toUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`adminstable` WHERE username = '" + toUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + toUsername + ")");
                }

            }

            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`message` WHERE ( toUsername = '" + username + "')");

            while (resultSet.next()){
                whoSend.add("front");
                fileAddress.add(resultSet.getString("fileAddress"));
                messageTime.add(resultSet.getString("messageTime"));
                typeFile.add(resultSet.getString("typeFile"));

                String fromUsername = resultSet.getString("fromUsername");
                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`teacherstable` WHERE username = '" + fromUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`studentstable` WHERE studentNumber = '" + fromUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`mohsenitable` WHERE username = '" + fromUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                }

                resultSetForMessage = statementForMessage.executeQuery("SELECT firstName, lastName FROM `edu-users`.`adminstable` WHERE username = '" + fromUsername + "'");
                if (resultSetForMessage.next()){
                    frontName.add(resultSetForMessage.getString("firstName") + " " + resultSetForMessage.getString("lastName") + "(" + fromUsername + ")");
                }

            }
        }
        catch (SQLException throwables) {

        }

        for (int i = 0; i < whoSend.size(); i++) {
            for (int j = i + 1; j < whoSend.size(); j++) {
                if(messageTime.get(i).equals(messageTime.get(j))){
                    continue;
                }
                String s = WhichIsLatest(messageTime.get(i), messageTime.get(j));
                if(s.equals(messageTime.get(j))){
                    Collections.swap(whoSend, i, j);
                    Collections.swap(frontName, i, j);
                    Collections.swap(fileAddress, i, j);
                    Collections.swap(messageTime, i, j);
                    Collections.swap(typeFile, i, j);
                    i = -1;
                    break;
                }
            }
        }


        linkedLists.add(whoSend);
        linkedLists.add(frontName);
        linkedLists.add(fileAddress);
        linkedLists.add(messageTime);
        linkedLists.add(typeFile);
        return linkedLists;
    }

    public void updateStudent(Student student){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET (firstName, lastName, color, image, email, password, phoneNumber, identityCode, college, lastSecondLogin, lastMinuteLogin, lastHourLogin, lastDayLogin, lastMonthLogin, lastYearLogin, timesLogin, studentNumber, coursesThisTerm, coursesThisTermScore, coursesPast, coursesPastScore, registrationLicense, registrationTime, grade, helpTeacher, yearComeToUniversity, educationStatus, firstMinorStatus, secondMinorStatus, requestForRecome, acceptedTeacherForRecome, minorCollege, idTeacherForRequest, protestForCourseStudent, answerTeacher, timeThesisDefence, dorm, timeForSelectUnit, likedCourses, coursesWantToGetThisTerm, extraStudentsInCaht, extraTeachersInCaht, messages, coursesIsTANow) " +
                    "VALUES (" + student.passSQLStringForSaveWithoutId() + ")  WHERE id = " + student.getId());



        } catch (SQLException throwables) {
            System.out.println("exception");
        }

    }
    
    public void updateTeacher(Teacher teacher){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`teacherstable` SET (firstName, lastName, color, image, email, password, phoneNumber, identityCode, college, lastSecondLogin, lastMinuteLogin, lastHourLogin, lastDayLogin, lastMonthLogin, lastYearLogin, timesLogin, username, statusTeacher, courses, teacherDegree, roomNumber, messagesForTeacher, extraStudentsInCaht, extraTeachersInCaht) " +
                    "VALUES (" + teacher.passSQLStringForSaveWithoutId() + ") WHERE id = " + teacher.getId());

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }
    
    public void updateMohseni(Mohseni mohseni){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`mohsenitable` SET (firstName, lastName, color, image, email, password, phoneNumber, identityCode, college, lastSecondLogin, lastMinuteLogin, lastHourLogin, lastDayLogin, lastMonthLogin, lastYearLogin, timesLogin, username) " +
                    "VALUES (" + mohseni.passSQLStringForSaveWithoutId() + ") WHERE id = " + mohseni.getId());

        } catch (SQLException throwables) {
            System.out.println("exception");
        }

    }
    
    public void updateAdmin(Admin admin){
        try {
            statement.executeUpdate("UPDATE `edu-users`.`adminstable` SET (firstName, lastName, color, image, email, password, phoneNumber, identityCode, college, lastSecondLogin, lastMinuteLogin, lastHourLogin, lastDayLogin, lastMonthLogin, lastYearLogin, timesLogin, username) " +
                    "VALUES (" + admin.passSQLStringForSaveWithoutId() + ") WHERE id = " + admin.getId());

        } catch (SQLException throwables) {
                        System.out.println("exception");

        }

    }

    public void finishTimeSelection(String year, String college){
        try {
            LinkedList<Student> students = new LinkedList<>();
            resultSet = statement.executeQuery("SELECT * FROM `edu-users`.`studentstable` WHERE college = '" + college + "' AND yearComeToUniversity = " + Integer.parseInt(year));

            while (resultSet.next()){
                Student student = getAStudentFromResultSet(resultSet);

                String studentNumber = resultSet.getString("studentNumber");
                LinkedList<String> coursesWantToGetThisTerm  = getListStringFromString(resultSet.getString("coursesWantToGetThisTerm"));
                LinkedList<String> coursesPast  = getListStringFromString(resultSet.getString("coursesPast"));
                LinkedList<Double> coursesPastScore  = getCoursesScoreFromString(resultSet.getString("coursesPastScore"));
                LinkedList<String> coursesThisTerm  = getListStringFromString(resultSet.getString("coursesThisTerm"));
                LinkedList<Double> coursesThisTermScore  = getCoursesScoreFromString(resultSet.getString("coursesThisTermScore"));
                LinkedList<String> answerTeacher  = getListStringFromString(resultSet.getString("answerTeacher"));
                LinkedList<String> protestForCourseStudent  = getListStringFromString(resultSet.getString("protestForCourseStudent"));

                int counter = 0;
                for (int i = 0; i < coursesWantToGetThisTerm.size(); i++) {
                    Course course = FindCourseFromId(coursesWantToGetThisTerm.get(i));
                    if(course.getCollege() == College.Public){
                        if(counter == 1){
                            coursesWantToGetThisTerm.remove(i);
                            i = -1;
                            continue;
                        }
                        else if(counter == 0){
                            counter++;
                        }
                    }
                    int count = 0;
                    for (int j = 0; j < course.getPrerequisites().size(); j++) {
                        for (int k = 0; k < coursesPast.size(); k++) {
                            if(course.getPrerequisites().get(j).equals(coursesPast.get(k)) && coursesPastScore.get(k) >= 10.0){
                                count++;
                                break;
                            }
                        }
                        for (int k = 0; k < coursesThisTerm.size(); k++) {
                            if(course.getPrerequisites().get(j).equals(coursesThisTerm.get(k))){
                                count++;
                                break;
                            }
                        }
                    }

                    if(count != course.getPrerequisites().size()){
                        coursesWantToGetThisTerm.remove(i);
                        i = -1;
                        continue;
                    }

                    count = 0;
                    for (int j = 0; j < course.getRequisites().size(); j++) {
                        for (int k = 0; k < coursesPast.size(); k++) {
                            if(course.getRequisites().get(j).equals(coursesPast.get(k)) && coursesPastScore.get(k) >= 10.0){
                                count++;
                                break;
                            }
                        }
                        for (int k = 0; k < coursesThisTerm.size(); k++) {
                            if(course.getRequisites().get(j).equals(coursesThisTerm.get(k))){
                                count++;
                                break;
                            }
                        }
                    }

                    if(count != course.getRequisites().size()){
                        coursesWantToGetThisTerm.remove(i);
                        i = -1;
                        continue;
                    }

                }


                for (int i = 0; i < coursesWantToGetThisTerm.size(); i++) {
                    coursesThisTerm.add(coursesWantToGetThisTerm.get(i));
                    coursesThisTermScore.add(-1.0);
                    answerTeacher.add(" ");
                    protestForCourseStudent.add(" ");
                }
                coursesWantToGetThisTerm.clear();

                student.setCourses_thisTerm(coursesThisTerm);
                student.setCourses_thisTerm_Score(coursesThisTermScore);
                student.setAnswerTeacher(answerTeacher);
                student.setProtestForCourseStudent(protestForCourseStudent);
                student.setCoursesWantToGetThisTerm(coursesWantToGetThisTerm);
                students.add(student);
            }

            for (int i = 0; i < students.size(); i++) {
                statement.executeUpdate("UPDATE `edu-users`.`studentstable` SET coursesWantToGetThisTerm = '" + passStringFormGenericTypes(students.get(i).getCoursesWantToGetThisTerm()) + "', coursesThisTerm = '" + passStringFormGenericTypes(students.get(i).getCourses_thisTerm()) + "', coursesThisTermScore = '" + passStringFormGenericTypes(students.get(i).getCourses_thisTerm_Score()) + "', answerTeacher = '" + passStringFormGenericTypes(students.get(i).getAnswerTeacher()) + "', protestForCourseStudent = '" + passStringFormGenericTypes(students.get(i).getProtestForCourseStudent()) + "', timeForSelectUnit = ''    WHERE id = " + students.get(i).getId());
                System.out.println("succfull");
            }
        } catch (SQLException throwables) {
            System.out.println("fessafasfeasfeasfe");
        }



    }



//  EXTRAS METHODS

    public LinkedList<Integer> getIntegersFromString(String s){
        String[] strings = s.split(":::");
        LinkedList<Integer> courses = new LinkedList<>();
        int size = Integer.parseInt(strings[0]);
        int counter = 1;
        for (int i = 0; i < size; i++) {
            courses.add(Integer.parseInt(strings[counter]));
            counter++;
        }

        return courses;
    }

    public LinkedList<String> getListStringFromString(String s){
        String[] strings = s.split(":::");
        LinkedList<String> strings1 = new LinkedList<>();
        int size = Integer.parseInt(strings[0]);
        int counter = 1;
        for (int i = 0; i < size; i++) {
            strings1.add(strings[counter]);
            counter++;
        }

        return strings1;
    }

    public LinkedList<Integer> getIdsTeachersAcceptedFromString(String s){
        String[] strings = s.split(":::");
        LinkedList<Integer> ids = new LinkedList<>();
        int size = Integer.parseInt(strings[0]);
        int counter = 1;
        for (int i = 0; i < size; i++) {
            ids.add(Integer.parseInt(strings[counter]));
            counter++;
        }

        return ids;
    }

    public LinkedList<Double> getCoursesScoreFromString(String s){
        String[] strings = s.split(":::");
        LinkedList<Double> coursesScore = new LinkedList<>();
        int size = Integer.parseInt(strings[0]);
        int counter = 1;
        for (int i = 0; i < size; i++) {
            coursesScore.add(Double.parseDouble(strings[counter]));
            counter++;
        }

        return coursesScore;
    }

    public LinkedList<Request> StringToRequestForRecommendation(String s) {
        String[] strings = s.split(":::");
        LinkedList<Request> requests = new LinkedList<>();
        int counter = 1;
        int size = Integer.parseInt(strings[0]);
        for (int i = 0; i < size ; i++) {
            requests.add(StringToMinorRequest(strings[counter]));
            counter++;
        }
        return requests;
    }

    public LinkedList<Days> StringToListDays(String s){
        String[] strings = s.split(":::");
        LinkedList<Days> days = new LinkedList<>();
        int counter = 1;
        int size = Integer.parseInt(strings[0]);
        for (int i = 0; i < size ; i++) {
            StringToDays(strings[counter]);
            days.add(StringToDays(strings[counter]));
            counter++;
        }

        return days;
    }

    public Days StringToDays(String s){
        if(s.toLowerCase().equals("Monday".toLowerCase())){
            return Days.Monday;
        }
        else if(s.toLowerCase().equals("Tuesday".toLowerCase())){
            return Days.Tuesday;
        }
        else if(s.toLowerCase().equals("Wednesday".toLowerCase())){
            return Days.Wednesday;
        }
        else if(s.toLowerCase().equals("Thursday".toLowerCase())){
            return Days.Thursday;
        }
        else if(s.toLowerCase().equals("Friday".toLowerCase())){
            return Days.Friday;
        }
        else if(s.toLowerCase().equals("Saturday".toLowerCase())){
            return Days.Saturday;
        }
        else if(s.toLowerCase().equals("Sunday".toLowerCase())){
            return Days.Sunday;
        }
        else{
            return null;
        }
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

        if(s.toLowerCase().equals("BC".toLowerCase())){
            return Grade.BC;
        }
        else if(s.toLowerCase().equals("MS".toLowerCase())){
            return Grade.MS;
        }
        else if(s.toLowerCase().equals("PHD".toLowerCase())){
            return Grade.PHD;
        }
        else{
            return null;
        }
    }

    public EducationStatus StringToEducationStatus(String s){
        if(s.toLowerCase().equals("Studying".toLowerCase())){
            return EducationStatus.Studying;
        }
        else if(s.toLowerCase().equals("Graduated".toLowerCase())){
            return EducationStatus.Graduated;
        }
        else if(s.toLowerCase().equals("Refuse".toLowerCase())){
            return EducationStatus.Refuse;
        }
        else if(s.toLowerCase().equals("Wait".toLowerCase())){
            return EducationStatus.Wait;

        }
        else{
            return null;
        }
    }

    public TeacherDegree StringToTeacherDegree(String s){

        if(s.toLowerCase().equals("AssistantProfessor".toLowerCase())){
            return TeacherDegree.AssistantProfessor;
        }
        else if(s.toLowerCase().equals("AssociateProfessor".toLowerCase())){
            return TeacherDegree.AssociateProfessor;
        }
        else if(s.toLowerCase().equals("FullProfessor".toLowerCase())){
            return TeacherDegree.FullProfessor;
        }
        else{
            return null;
        }
    }

    public StatusTeacher StringToStatusTeacher(String s){

        if(s.toLowerCase().equals("EA".toLowerCase())){
            return StatusTeacher.EA;
        }
        else if(s.toLowerCase().equals("Simple".toLowerCase())){
            return StatusTeacher.Simple;
        }
        else if(s.toLowerCase().equals("Boss".toLowerCase())){
            return StatusTeacher.Boss;
        }
        else{
            return null;
        }
    }

    public MinorStatus StringToMinorStatus(String s){

        if(s.toLowerCase().equals("NO".toLowerCase())){
            return MinorStatus.No;
        }
        else if(s.toLowerCase().equals("Wait".toLowerCase())){
            return MinorStatus.Wait;
        }
        else if(s.toLowerCase().equals("Accept".toLowerCase())){
            return MinorStatus.Accept;
        }
        else if(s.toLowerCase().equals("Reject".toLowerCase())){
            return MinorStatus.Reject;
        }
        else{
            return null;
        }
    }

    public Request StringToMinorRequest(String s){

        if(s.toLowerCase().equals("Wait".toLowerCase())){
            return Request.Wait;
        }
        else if(s.toLowerCase().equals("Accept".toLowerCase())){
            return Request.Accept;
        }
        else if(s.toLowerCase().equals("Reject".toLowerCase())){
            return Request.Reject;
        }
        else{
            return null;
        }
    }

    public String WhichIsLatest(String first, String second){
        String[] firstsParts = first.split("//");
        String[] secondsParts = second.split("//");

        for (int i = 0; i < firstsParts.length; i++) {
            if(Integer.parseInt(firstsParts[i]) > Integer.parseInt(secondsParts[i])){
                return first;
            }
            else if(Integer.parseInt(secondsParts[i]) > Integer.parseInt(firstsParts[i])){
                return second;
            }
        }


        return first;
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
