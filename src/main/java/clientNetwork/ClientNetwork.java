package clientNetwork;

import back.course.Course;
import back.course.EducationalMaterials;
import back.course.Exam;
import back.course.HomeWork;
import back.enums.Request;
import back.persons.Admin;
import back.persons.Mohseni;
import back.persons.Student;
import back.persons.Teacher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class ClientNetwork {
    private Socket socket;

    DataInputStream inputStream = null;
    DataOutputStream outputStream = null;
    String authToken;
    public boolean isConnected;
    public final Object lock = 0;

    public LinkedList<String> whoSend = new LinkedList<>();
    public LinkedList<String> frontName = new LinkedList<>();
    public LinkedList<String> fileAddress = new LinkedList<>();
    public LinkedList<String> messageTime = new LinkedList<>();
    public LinkedList<String> typeFile = new LinkedList<>();


    String username = "";

    public ClientNetwork() {
        isConnected = false;
        try {

            String ip = ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.IP);
            int port = Integer.parseInt(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.Port));

            socket = new Socket(ip, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            authToken = inputStream.readUTF();
            isConnected = true;
        }
        catch (IOException e) {
            try {
            synchronized (lock){
                
            }
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            isConnected = false;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    getChatRoomsInfo();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();

    }

    public Student getStudentFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Student.class);
    }
    public Teacher getTeacherFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Teacher.class);
    }
    public Course getCourseFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Course.class);
    }
    public LinkedList<Student> getListStudentFromJson(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<LinkedList<Student>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    public LinkedList<Teacher> getListTeacherFromJson(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<LinkedList<Teacher>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    public LinkedList<Course> getListCourseFromJson(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<LinkedList<Course>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public boolean validAuthToken(){
        try {
            outputStream.writeUTF(authToken);
            if (!inputStream.readBoolean()) {
                return false;
            }

            return true;
        }
        catch (IOException e) {
            isConnected = false;
        }
        catch (NullPointerException e){
            isConnected = false;
        }

        return false;

    }

    public void changeColorForStudent(String studentNumber, String color) {

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.CHANGE_COLOR_OF_STUDENT));
                outputStream.writeUTF(studentNumber);
                outputStream.writeUTF(color);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public void changeColorForTeacher(String username, String color) {

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.CHANGE_COLOR_OF_TEACHER));
                outputStream.writeUTF(username);
                outputStream.writeUTF(color);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public void changeColorForMohseni(String username, String color){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeColorForMohseni));
                outputStream.writeUTF(username);
                outputStream.writeUTF(color);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public void changeColorForAdmin(String username, String color){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeColorForAdmin));
                outputStream.writeUTF(username);
                outputStream.writeUTF(color);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  Student getStudentLogin(String username, String password){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return null;
                }
                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStudentLogin));
                outputStream.writeUTF(username);
                outputStream.writeUTF(password);

                Student student = getStudentFromJson(inputStream.readUTF());;
                if(student != null){
                    this.username = username;
                }
                return student;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;

    }

    public  Teacher getTeacherLogin(String username, String password){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getTeacherLogin));
                outputStream.writeUTF(username);
                outputStream.writeUTF(password);

                Teacher teacher = getTeacherFromJson(inputStream.readUTF());
                if(teacher != null){
                    this.username = username;
                }
                return teacher;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;

    }

    public Mohseni getMohseniLogin(String username, String password){

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMohseniLogin));
                outputStream.writeUTF(username);
                outputStream.writeUTF(password);

                Mohseni mohseni = (new Gson()).fromJson(inputStream.readUTF(), Mohseni.class);
                if(mohseni != null){
                    this.username = username;
                }
                return mohseni;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public Admin getAdminLogin(String username, String password){

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAdminLogin));
                outputStream.writeUTF(username);
                outputStream.writeUTF(password);

                Admin admin = (new Gson()).fromJson(inputStream.readUTF(), Admin.class);
                if(admin != null){
                    this.username = username;
                }
                return admin;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public  String getLastNameHelpTeacherFromId(int idTeacher) {
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getLastNameHelpTeacherFromId));
                outputStream.writeInt(idTeacher);

                return inputStream.readUTF();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  Teacher getLastNameHelpTeacherFromLastName(String chooseTeacher){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getLastNameHelpTeacherFromLastName));
                outputStream.writeUTF(chooseTeacher);

                return getTeacherFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public  LinkedList<Student> getAllStudents() {
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudents));

                return getListStudentFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Teacher> getAllTeachers() {
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllTeachers));

                return getListTeacherFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Course> getAllCourses() {
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllCourses));

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Course> ShowTemperoryScoresForEA(int teacherId, String CourseName ,  String LastNameTeacher){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowTemperoryScoresForEA));
                outputStream.writeInt(teacherId);
                outputStream.writeUTF(CourseName);
                outputStream.writeUTF(LastNameTeacher);
                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public LinkedList<Student> ShowStudentsForMohseni(String studentNumber, String collegeName, String grade){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowStudentsForMohseni));
                outputStream.writeUTF(studentNumber);
                outputStream.writeUTF(collegeName);
                outputStream.writeUTF(grade);
                return getListStudentFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Course> ShowCourses(String nameCollege, int Weight , String grade, String id_Course){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowCourses));
                outputStream.writeUTF(nameCollege);
                outputStream.writeInt(Weight);
                outputStream.writeUTF(grade);
                outputStream.writeUTF(id_Course);

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Teacher> ShowTeachers(String LastName, String nameCollege, String teacherDegree){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowTeachers));
                outputStream.writeUTF(LastName);
                outputStream.writeUTF(nameCollege);
                outputStream.writeUTF(teacherDegree);

                return getListTeacherFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  String FindTeacherLastNameFromId(int id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacherLastNameFromId));
                outputStream.writeInt(id);

                return inputStream.readUTF();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }
    public  String FindTeacherFirstNameFromId(int id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacherFirstNameFromId));
                outputStream.writeInt(id);

                return inputStream.readUTF();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }
    public  String FindTeacherCollegeFromId(int id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacherCollegeFromId));
                outputStream.writeInt(id);

                return inputStream.readUTF();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  Course FindACourseFormACollege(String courseName, String collegeName){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindACourseFormACollege));
                outputStream.writeUTF(courseName);
                outputStream.writeUTF(collegeName);

                return getCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public  LinkedList<Course> GiveCoursesFromAStudent(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesFromAStudent));
                outputStream.writeInt(studentId);

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Course> GiveCoursesFromATeacher(int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesFromATeacher));
                outputStream.writeInt(teacherId);

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  String returnAllHelpTeachersFromACollege(String collegeName){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.returnAllHelpTeachersFromACollege));
                outputStream.writeUTF(collegeName);

                return inputStream.readUTF();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  boolean CanSignUpForDuplicateUsername(String username){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.CanSignUpForDuplicateUsername));
                outputStream.writeUTF(username);

                return inputStream.readBoolean();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public boolean validDuplicatePublicIdForCourse(int publicId, int groupId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.validDuplicatePublicIdForCourse));
                outputStream.writeInt(publicId);
                outputStream.writeInt(groupId);

                return inputStream.readBoolean();
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public void addStudent(Student student){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return ;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addStudent));
                Gson gson = new Gson();
                outputStream.writeUTF(gson.toJson(student));

                return ;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return ;

    }

    public  void addTeacher (Teacher teacher){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return ;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addTeacher));
                Gson gson = new Gson();
                outputStream.writeUTF(gson.toJson(teacher));

                return ;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return ;

    }

    public  void addCourse (Course course){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return ;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addCourse));
                Gson gson = new Gson();
                outputStream.writeUTF(gson.toJson(course));

                return ;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return ;

    }

    public  Teacher FindTeacher(int id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacher));
                outputStream.writeInt(id);

                return getTeacherFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public  boolean isValidTeacher(int id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.isValidTeacher));
                outputStream.writeInt(id);

                return inputStream.readBoolean();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public  Course FindCourse(String id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindCourse));
                outputStream.writeUTF(id);

                return getCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public  void AddNewCourseToTeacher(String courseId, String teacherName, String collegeName){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.AddNewCourseToTeacher));
                outputStream.writeUTF(courseId);
                outputStream.writeUTF(teacherName);
                outputStream.writeUTF(collegeName);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return;
    }

    public  LinkedList<Teacher> StringToTeacher(String s){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.StringToTeacher));
                outputStream.writeUTF(s);

                return getListTeacherFromJson(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Student> SearchStudentByStudentNumberAndName(int teacherId, String Name, String studentNumber){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.SearchStudentByStudentNumberAndName));
                outputStream.writeInt(teacherId);
                outputStream.writeUTF(Name);
                outputStream.writeUTF(studentNumber);

                return getListStudentFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  String getTeacherOfACourse(String courseId) {
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getTeacherOfACourse));
                outputStream.writeUTF(courseId);

                return inputStream.readUTF();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  LinkedList<Course> GiveCoursesOfATeacher(LinkedList<String> ids){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesOfATeacher));

                Gson gson = new Gson();
                String id =  gson.toJson(ids);
                outputStream.writeUTF(id);

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  LinkedList<Course> GiveCoursesOfAStudent(LinkedList<String> ids){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesOfAStudent));

                Gson gson = new Gson();
                String id =  gson.toJson(ids);
                outputStream.writeUTF(id);

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  LinkedList<Course> GiveCoursesOfACollege(String college){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesOfACollege));
                outputStream.writeUTF(college);

                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  LinkedList<Student> GiveStudentsOfACoursesFromString(String courseId){

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveStudentsOfACoursesFromString));
                outputStream.writeUTF(courseId);

                return getListStudentFromJson(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  LinkedList<Student> GiveStudentsOfACoursesFromId(String id){

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveStudentsOfACoursesFromId));
                outputStream.writeUTF(id);

                return getListStudentFromJson(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  Student FindStudentFromId(int id){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentFromId));
                outputStream.writeInt(id);

                return getStudentFromJson(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public  int FindIndexCourseFormAStudentFromCourseName(int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return -1;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindIndexCourseFormAStudentFromCourseName));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

                return inputStream.readInt();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return -1;
    }

    public  void RemoveCourseFromATeachersAndStudents(String courseName, Teacher teacher){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return ;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.RemoveCourseFromATeachersAndStudents));
                outputStream.writeUTF(courseName);
                outputStream.writeUTF(new Gson().toJson(teacher));
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return ;
    }

    public  LinkedList<String> ShowableCoursesOfStudentTemporaryScores(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowableCoursesOfStudentTemporaryScores));
                outputStream.writeInt(studentId);

                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                return gson.fromJson(inputStream.readUTF(), listType);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public int FindIndexCourseFormAStudentFromId(int studentId, String id){

        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return -1;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindIndexCourseFormAStudentFromId));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(id);

                return inputStream.readInt();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return -1;

    }

    public double GetScore(String courseId, int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return 0.0;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GetScore));
                outputStream.writeUTF(courseId);
                outputStream.writeInt(studentId);

                return inputStream.readDouble();
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return 0.0;
    }

    public  LinkedList<Student> FindStudentsWantsToReject(String collegeName){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentsWantsToReject));
                outputStream.writeUTF(collegeName);

                return getListStudentFromJson(inputStream.readUTF());

            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Student> FindStudentsWantsToMinor(String collegeName){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentsWantsToMinor));
                outputStream.writeUTF(collegeName);

                return getListStudentFromJson(inputStream.readUTF());

            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public  void RemoveStudent(int studentId){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.RemoveStudent));
                outputStream.writeInt(studentId);


            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  LinkedList<Course> ShowPastCoursesForAStudent(int studentId){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowPastCoursesForAStudent));
                outputStream.writeInt(studentId);

                return getListCourseFromJson(inputStream.readUTF());

            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Student> ShowStudentsForATeacherEA(String collegeName){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowStudentsForATeacherEA));
                outputStream.writeUTF(collegeName);

                return getListStudentFromJson(inputStream.readUTF());

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  String getStatusACourseFromAStudent(int studentId, String courseId){

        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStatusACourseFromAStudent));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

                return inputStream.readUTF();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  double getScoreACourseFromAStudent(int studentId, String courseId){

        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return 0.0;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getScoreACourseFromAStudent));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

                return inputStream.readDouble();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return 0.0;

    }

    public  String getPassesCourseFromAStudent(int studentId){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getPassesCourseFromAStudent));
                outputStream.writeInt(studentId);

                return inputStream.readUTF();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  LinkedList<Student> FindStudentsWantsToRecome(int teacherId){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentsWantsToRecome));
                outputStream.writeInt(teacherId);

                return getListStudentFromJson(inputStream.readUTF());

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  int getIndexRequest(int studentId, int teacherId){

        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return -1;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getIndexRequest));
                outputStream.writeInt(studentId);
                outputStream.writeInt(teacherId);

                return inputStream.readInt();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return -1;
    }

    public  int getWeighOfCoursesPass(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return -1;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getWeighOfCoursesPass));
                outputStream.writeInt(studentId);

                return inputStream.readInt();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return -1;
    }

    public  LinkedList<String>  getNumPassForACourse(String courseId){
        Gson gson = new Gson();
        Type listType = new TypeToken<LinkedList<String>>(){}.getType();


        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getNumPassForACourse));
                outputStream.writeUTF(courseId);

                return gson.fromJson(inputStream.readUTF(), listType);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  void  AddAndRemoveACourseFormTeachers(String id, int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.AddAndRemoveACourseFormTeachers));
                outputStream.writeUTF(id);
                outputStream.writeInt(teacherId);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void  RemoveACourseFormTeachers(String id){

        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.RemoveACourseFormTeachers));
                outputStream.writeUTF(id);

                return;

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  void changePasswordForAStudent(String username, String newPassword){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changePasswordForAStudent));
                outputStream.writeUTF(username);
                outputStream.writeUTF(newPassword);

                return;

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  void changePasswordAForTeacher(String username, String newPassword){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changePasswordAForTeacher));
                outputStream.writeUTF(username);
                outputStream.writeUTF(newPassword);

                return;

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeLastLoginStudent(int id, int second, int minute, int hour, int day, int month, int year){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }
                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeLastLoginStudent));
                outputStream.writeInt(id);
                outputStream.writeInt(second);
                outputStream.writeInt(minute);
                outputStream.writeInt(hour);
                outputStream.writeInt(day);
                outputStream.writeInt(month);
                outputStream.writeInt(year);


                username = "";

            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeLastLoginTeacher(int id, int second, int minute, int hour, int day, int month, int year){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeLastLoginTeacher));
                outputStream.writeInt(id);
                outputStream.writeInt(second);
                outputStream.writeInt(minute);
                outputStream.writeInt(hour);
                outputStream.writeInt(day);
                outputStream.writeInt(month);
                outputStream.writeInt(year);

                username = "";

                return;

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeEducationStatusOfAStudent(int id, String educationStatus){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeEducationStatusOfAStudent));
                outputStream.writeInt(id);
                outputStream.writeUTF(educationStatus);

                return;

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeProtestForAStudent(int id, int index, String protest){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeProtestForAStudent));
                outputStream.writeInt(id);
                outputStream.writeInt(index);
                outputStream.writeUTF(protest);

                return;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void editEmailForAStudent(int id, String email){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editEmailForAStudent));
                outputStream.writeInt(id);
                outputStream.writeUTF(email);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void editPhoneForAStudent(int id, String phoneNumber){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editPhoneForAStudent));
                outputStream.writeInt(id);
                outputStream.writeUTF(phoneNumber);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setImageAddressOfAStudent(int id, String image){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setImageAddressOfAStudent));
                outputStream.writeInt(id);
                outputStream.writeUTF(image);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void addIdTeacherToStudentForRecome(int studentId, int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addIdTeacherToStudentForRecome));
                outputStream.writeInt(studentId);
                outputStream.writeInt(teacherId);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeRecomeForStudentBecauseShowed(int studentId, int indexShouldRemove){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeRecomeForStudentBecauseShowed));
                outputStream.writeInt(studentId);
                outputStream.writeInt(indexShouldRemove);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted(int studentId, int indexShouldRemove, int idTeacherShouldAddToAccepted){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted));
                outputStream.writeInt(studentId);
                outputStream.writeInt(indexShouldRemove);
                outputStream.writeInt(idTeacherShouldAddToAccepted);

                return;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  void setMinorCollegeForAStudent(int id, String collegeName){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setMinorCollegeForAStudent));
                outputStream.writeInt(id);
                outputStream.writeUTF(collegeName);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setDormRequestForAStudent(int id, String dorm){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setDormRequestForAStudent));
                outputStream.writeInt(id);
                outputStream.writeUTF(dorm);

                return;

            }


        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeMinorForStudentBecauseShowedAndRejected(int studentId){
        try {
            synchronized (lock){

                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeMinorForStudentBecauseShowedAndRejected));
                outputStream.writeInt(studentId);

                return;
            }


        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void submitRecomeForAStudent(int studentId, int index, String request){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.submitRecomeForAStudent));
                outputStream.writeInt(studentId);
                outputStream.writeInt(index);
                outputStream.writeUTF(request);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setRoomNumberForATeacher(int teacherId, int roomNumber){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setRoomNumberForATeacher));
                outputStream.writeInt(teacherId);
                outputStream.writeInt(roomNumber);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void removeATeacher(int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeATeacher));
                outputStream.writeInt(teacherId);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeStatusTeacher(int teacherId, String statusTeacher){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeStatusTeacher));
                outputStream.writeInt(teacherId);
                outputStream.writeUTF(statusTeacher);

                return;
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void editEmailForATeacher(int id, String email){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editEmailForATeacher));
                outputStream.writeInt(id);
                outputStream.writeUTF(email);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void editPhoneForATeacher(int id, String phoneNumber){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editPhoneForATeacher));
                outputStream.writeInt(id);
                outputStream.writeUTF(phoneNumber);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setImageAddressOfATeacher(int id, String image){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setImageAddressOfATeacher));
                outputStream.writeInt(id);
                outputStream.writeUTF(image);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setAnswerAndScoreForStudent(int id, int indexCourse,  double score, String answer){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setAnswerAndScoreForStudent));
                outputStream.writeInt(id);
                outputStream.writeInt(indexCourse);
                outputStream.writeDouble(score);
                outputStream.writeUTF(answer);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void changeMinorStatusFromEA(boolean isFirstMinorStatus, int studentId, String status){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeMinorStatusFromEA));
                outputStream.writeBoolean(isFirstMinorStatus);
                outputStream.writeInt(studentId);
                outputStream.writeUTF(status);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setTimesLoginForAStudent(int studentId, int timesLogin){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setTimesLoginForAStudent));
                outputStream.writeInt(studentId);
                outputStream.writeInt(timesLogin);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void setTimesLoginForATeacher(int teacherId, int timesLogin){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setTimesLoginForATeacher));
                outputStream.writeInt(teacherId);
                outputStream.writeInt(timesLogin);

                return;
            }



        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public String getProtestForCourseStudent(int studentId, int index){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getProtestForCourseStudent));
                outputStream.writeInt(studentId);
                outputStream.writeInt(index);

                return inputStream.readUTF();
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public String getAnswerForCourseStudent(int studentId, int index){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAnswerForCourseStudent));
                outputStream.writeInt(studentId);
                outputStream.writeInt(index);

                return inputStream.readUTF();
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public double getScoreForCourseStudent(int studentId, int index){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return -1.0;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getScoreForCourseStudent));
                outputStream.writeInt(studentId);
                outputStream.writeInt(index);

                return inputStream.readDouble();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return -1.0;
    }

    public String getMinorStatus(int studentId, boolean isFirst){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMinorStatus));
                outputStream.writeInt(studentId);
                outputStream.writeBoolean(isFirst);

                return inputStream.readUTF();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public LinkedList<Request> getRequestForRecommendation(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getRequestForRecommendation));
                outputStream.writeInt(studentId);

                return StringToRequestForRecommendation(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public int getIdTeacherForRequest(int studentId, int index){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return -1;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getIdTeacherForRequest));
                outputStream.writeInt(studentId);
                outputStream.writeInt(index);

                return inputStream.readInt();
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return -1;
    }

    public String getDormStatus(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getDormStatus));
                outputStream.writeInt(studentId);

                return inputStream.readUTF();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public String getThesisDefenceFromAStudent(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getThesisDefenceFromAStudent));
                outputStream.writeInt(studentId);

                return inputStream.readUTF();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public boolean validStudentNumberForTA(String studentNumber){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.validStudentNumberForTA));
                outputStream.writeUTF(studentNumber);

                return inputStream.readBoolean();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public boolean validPublicIdForCourse(int publicId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.validPublicIdForCourse));
                outputStream.writeInt(publicId);

                return inputStream.readBoolean();
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public String getFileNameOfFromUsername(String usernameTo){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "userIcon256.png";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getFileNameOfFromUsername));
                outputStream.writeUTF(usernameTo);

                return inputStream.readUTF();
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return "userIcon256.png";
    }

    public String getFileNameOfFromStudentNumber(String usernameTo){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "userIcon256.png";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getFileNameOfFromStudentNumber));
                outputStream.writeUTF(usernameTo);

                return inputStream.readUTF();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "userIcon256.png";
    }

    public void saveMessage(String usernameFrom, String userNameTo, String fileAddress, String typeFile){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    saveToFile(usernameFrom, userNameTo, fileAddress, typeFile);
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveMessage));

                outputStream.writeUTF(usernameFrom);
                outputStream.writeUTF(userNameTo);
                outputStream.writeUTF(fileAddress);
                outputStream.writeUTF(typeFile);
            }
        }
        catch (IOException e) {
            isConnected = false;
            saveToFile(usernameFrom, userNameTo, fileAddress, typeFile);
        }

    }

    public LinkedList<LinkedList<String>> getYearsToUniversityForEA(String college){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getYearsToUniversityForEA));

                outputStream.writeUTF(college);
                Type listType = new TypeToken<LinkedList<LinkedList<String>>>(){}.getType();
                LinkedList<LinkedList<String>> linkedLists = (new Gson()).fromJson(inputStream.readUTF(), listType);
                return linkedLists;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public void saveTimeForSelectionUnit(String college, int yearComeToUniversity, String time){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return ;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveTimeForSelectionUnit));

                outputStream.writeUTF(college);
                outputStream.writeInt(yearComeToUniversity);
                outputStream.writeUTF(time);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public void setExamDayForACourse(String courseId, int dayExam, int monthExam, int yearExam){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return ;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setExamDayForACourse));
                outputStream.writeUTF(courseId);
                outputStream.writeInt(dayExam);
                outputStream.writeInt(monthExam);
                outputStream.writeInt(yearExam);
            }


        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public LinkedList<Course> getAllCoursesFromACollege(String college, String grade){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllCoursesFromACollege));
                outputStream.writeUTF(college);
                outputStream.writeUTF(grade);

                return getListCourseFromJson(inputStream.readUTF());
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public LinkedList<Course> getAllCoursesFromACollegeForSelectUnit(String college, String grade){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllCoursesFromACollegeForSelectUnit));
                outputStream.writeUTF(college);
                outputStream.writeUTF(grade);

                return getListCourseFromJson(inputStream.readUTF());
            }



        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();

    }

    public LinkedList<Course> getLikedCourses(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getLikedCourses));
                outputStream.writeInt(studentId);

                return getListCourseFromJson(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Course> getSuggestedCourses(int studentId, String college, String grade){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getSuggestedCourses));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(college);
                outputStream.writeUTF(grade);

                return getListCourseFromJson(inputStream.readUTF());
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Boolean> getStatusOfACourseForUnitSelection(int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStatusOfACourseForUnitSelection));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);
                Type listType = new TypeToken<LinkedList<Boolean>>(){}.getType();
                Gson gson = new Gson();

                return gson.fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  void addACourseToLikedCourses(int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addACourseToLikedCourses));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  void removeACourseFromLikedCourses(int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourseFromLikedCourses));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  boolean addACourseFromCoursesWantToGetThisTerm(int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addACourseFromCoursesWantToGetThisTerm));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

                return inputStream.readBoolean();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public  void changeGroupForSelectionUnit(String studentNumber, String college, int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeGroupForSelectionUnit));
                outputStream.writeUTF(studentNumber);
                outputStream.writeUTF(college);
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  void removeACourseFromCoursesWantToGetThisTerm(int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourseFromCoursesWantToGetThisTerm));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  LinkedList<String> getAllStudentsFromOneCollegeAndFromOneYear(String college, int yearComeToUniversity, int studentId, int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudentsFromOneCollegeAndFromOneYear));
                outputStream.writeUTF(college);
                outputStream.writeInt(yearComeToUniversity);
                outputStream.writeInt(studentId);
                outputStream.writeInt(teacherId);

                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<LinkedList<String>> getAllMessagesFromAPerson(String username){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllMessagesFromAPerson));
                outputStream.writeUTF(username);

                Type listType = new TypeToken<LinkedList<LinkedList<String>>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<LinkedList<String>> getAllMessagesFromTwoPersons(String usernamePerson, String usernameFront){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllMessagesFromTwoPersons));
                outputStream.writeUTF(usernamePerson);
                outputStream.writeUTF(usernameFront);

                Type listType = new TypeToken<LinkedList<LinkedList<String>>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  String getUserNameFromName(String nameFront, boolean isFrontStudent){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getUserNameFromName));
                outputStream.writeUTF(nameFront);
                outputStream.writeBoolean(isFrontStudent);

                return inputStream.readUTF();
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  LinkedList<EducationalMaterials> getEducationalMaterialsFromACourse(String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getEducationalMaterialsFromACourse));
                outputStream.writeUTF(courseId);

                Type listType = new TypeToken<LinkedList<EducationalMaterials>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<Exam> getExamsFromACourse(String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getExamsFromACourse));
                outputStream.writeUTF(courseId);

                Type listType = new TypeToken<LinkedList<Exam>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<HomeWork> getHomeWorksFromACourse(String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getHomeWorksFromACourse));
                outputStream.writeUTF(courseId);

                Type listType = new TypeToken<LinkedList<HomeWork>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  void changeAnswerForHomeWorkOrExamForAStudent(String courseId, String exams, String homeWorks, boolean isExam){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeAnswerForHomeWorkOrExamForAStudent));
                outputStream.writeUTF(courseId);
                outputStream.writeUTF(exams);
                outputStream.writeUTF(homeWorks);
                outputStream.writeBoolean(isExam);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public void removeAFileFromServerForCw(String address){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeAFileFromServerForCw));
                outputStream.writeUTF(address);
            }

        }
        catch (IOException e) {
            isConnected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("finish sendFileFromServer in client");



    }

    public  LinkedList<String> getMessagesForAStudent(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMessagesForAStudent));
                outputStream.writeInt(studentId);

                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  LinkedList<String> getMessagesForATeacher(int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMessagesForATeacher));
                outputStream.writeInt(teacherId);

                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  void removeAMessageFromAStudent(int studentId, String messageString, boolean isAcceptAndReject, boolean isAcceptedIfPreviousIsTrue){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeAMessageFromAStudent));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(messageString);
                outputStream.writeBoolean(isAcceptAndReject);
                outputStream.writeBoolean(isAcceptedIfPreviousIsTrue);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  void removeAMessageFromATeacherForMessages(int teacherId, String messageString, boolean isAccept){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeAMessageFromATeacherForMessages));
                outputStream.writeInt(teacherId);
                outputStream.writeUTF(messageString);
                outputStream.writeBoolean(isAccept);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public  boolean sendMessageToEAToGetACourse(String studentNumber, String college, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendMessageToEAToGetACourse));
                outputStream.writeUTF(studentNumber);
                outputStream.writeUTF(college);
                outputStream.writeUTF(courseId);

                return inputStream.readBoolean();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public  String getTimeSelectUnitForAStudent(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getTimeSelectUnitForAStudent));
                outputStream.writeInt(studentId);

                return inputStream.readUTF();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  LinkedList<String> FindATeacherOrStudentForChatRoom(String myUserName, String type, String username){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindATeacherOrStudentForChatRoom));
                outputStream.writeUTF(myUserName);
                outputStream.writeUTF(type);
                outputStream.writeUTF(username);

                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                return (new Gson()).fromJson(inputStream.readUTF(), listType);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public  boolean sendMessageToAPersonForRequestChatRoom(String type, String username, String myUsername){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendMessageToAPersonForRequestChatRoom));
                outputStream.writeUTF(type);
                outputStream.writeUTF(username);
                outputStream.writeUTF(myUsername);

                return inputStream.readBoolean();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public  boolean addStudentToCourseInCW(String type, String username, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addStudentToCourseInCW));
                outputStream.writeUTF(type);
                outputStream.writeUTF(username);
                outputStream.writeUTF(courseId);

                return inputStream.readBoolean();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public  boolean isAStudentTAOfACourse( int studentId, String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return false;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.isAStudentTAOfACourse));
                outputStream.writeInt(studentId);
                outputStream.writeUTF(courseId);

                return inputStream.readBoolean();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return false;
    }

    public void addNewFileInCwForExamOrHw(String courseId, boolean isExam, String folder, boolean doesHaveFile, String address, String s){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addNewFileInCwForExamOrHw));
                outputStream.writeUTF(courseId);
                outputStream.writeBoolean(isExam);
                outputStream.writeUTF(address);
                outputStream.writeBoolean(doesHaveFile);
                if(doesHaveFile) {
                    sendFile(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForUser) + folder + "\\\\" + address);
                }

                outputStream.writeUTF(s);
            }



        }
        catch (IOException e) {
            isConnected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("finish sendFileFromServer in client");



    }

    public  String getNameOfAStudentFromId(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getNameOfAStudentFromId));
                outputStream.writeInt(studentId);

                return inputStream.readUTF();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  String getStudentsNumberOfAStudentFromId(int studentId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return "";
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStudentsNumberOfAStudentFromId));
                outputStream.writeInt(studentId);

                return inputStream.readUTF();

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

        return "";
    }

    public  void saveExamsOrHWsForCw(String courseId, String s, boolean isExam){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveExamsOrHWsForCw));
                outputStream.writeUTF(courseId);
                outputStream.writeUTF(s);
                outputStream.writeBoolean(isExam);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public  void changeEducationalMaterialForACourse(String courseId, String s){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeEducationalMaterialForACourse));
                outputStream.writeUTF(courseId);
                outputStream.writeUTF(s);

            }

        }
        catch (IOException e) {
            isConnected = false;
        }

    }

    public LinkedList<Teacher> getAllTeacherFormACollege(int teacherId, String college){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllTeacherFormACollege));
                outputStream.writeInt(teacherId);
                outputStream.writeUTF(college);

                return getListTeacherFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }


        return new LinkedList<>();

    }

    public LinkedList<Course> getCoursesOfATeacher(int teacherId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getCoursesOfATeacher));
                outputStream.writeInt(teacherId);
                return getListCourseFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }


        return new LinkedList<>();
    }

    public LinkedList<Integer> getCoursesGroup (int publicId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getCoursesGroup));
                outputStream.writeInt(publicId);

                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<Integer>>(){}.getType();
                return gson.fromJson(inputStream.readUTF(), listType);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public LinkedList<String> getStudentsWithFilterForMohseni(String studentNumber){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStudentsWithFilterForMohseni));
                outputStream.writeUTF(studentNumber);

                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                return gson.fromJson(inputStream.readUTF(), listType);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public Student findStudentFromStudentNumber(String studentNumber){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return null;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.findStudentFromStudentNumber));
                outputStream.writeUTF(studentNumber);

                return getStudentFromJson(inputStream.readUTF());
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return null;
    }

    public LinkedList<LinkedList<String>> getAllStudentForATeacherInChatRoom(int teacherId, String username){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudentForATeacherInChatRoom));

                outputStream.writeInt(teacherId);
                outputStream.writeUTF(username);
                Type listType = new TypeToken<LinkedList<LinkedList<String>>>(){}.getType();
                LinkedList<LinkedList<String>> linkedLists = (new Gson()).fromJson(inputStream.readUTF(), listType);
                return linkedLists;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public LinkedList<LinkedList<String>> getAllStudentForABossAndEAInChatRoom(int teacherId, String username, String college){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return new LinkedList<>();
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudentForABossAndEAInChatRoom));

                outputStream.writeInt(teacherId);
                outputStream.writeUTF(username);
                outputStream.writeUTF(college);
                Type listType = new TypeToken<LinkedList<LinkedList<String>>>(){}.getType();
                LinkedList<LinkedList<String>> linkedLists = (new Gson()).fromJson(inputStream.readUTF(), listType);
                return linkedLists;
            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return new LinkedList<>();
    }

    public void saveMessagesBecauseNotConnected(String username){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                LinkedList<String> toPersonsUsername = new LinkedList<>();
                LinkedList<String> AddressOrText = new LinkedList<>();
                LinkedList<String> Time = new LinkedList<>();
                LinkedList<String> TypeFile = new LinkedList<>();


                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveMessagesBecauseNotConnected));

                loadFromAFile(username, toPersonsUsername, AddressOrText, Time, TypeFile);

                outputStream.writeInt(toPersonsUsername.size());

                if(toPersonsUsername.size() == 0){
                    return;
                }


                outputStream.writeUTF(username);
                Gson gson = new Gson();
                outputStream.writeUTF(gson.toJson(toPersonsUsername));
                outputStream.writeUTF(gson.toJson(AddressOrText));
                outputStream.writeUTF(gson.toJson(Time));
                outputStream.writeUTF(gson.toJson(TypeFile));

                boolean b = inputStream.readBoolean();
                if(b){
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + username + "\\" + username + ".txt");
                    PrintWriter writer = new PrintWriter(file);
                    writer.print("");
                    writer.close();
                }


                for (int i = 0; i < TypeFile.size(); i++) {
                    if(TypeFile.get(i).toLowerCase().equals("file")){
                        sendFileToServer(username, AddressOrText.get(i));
                    }
                }
            }



        }
        catch (IOException e) {
            isConnected = false;
        }


        return;

    }

    public void getChatRoomsInfo(){
        try {
            synchronized (lock) {

                if (!validAuthToken()) {
                    return ;
                }
                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getChatRoomsInfo));
                outputStream.writeUTF(username);
                if(username.equals("")){
                    return;
                }

                String json = inputStream.readUTF();

                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<LinkedList<String>>>(){}.getType();
                LinkedList<LinkedList<String>> linkedLists = gson.fromJson(json, listType);

                if(linkedLists.size() != 5){
                    return;
                }


                whoSend = linkedLists.get(0);
                frontName = linkedLists.get(1);
                fileAddress = linkedLists.get(2);
                messageTime = linkedLists.get(3);
                typeFile = linkedLists.get(4);


            }
        }
        catch (IOException e) {
            isConnected = false;
        }

        return;
    }

    public void finishTimeSelection(String year, String college){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.finishTimeSelection));
                outputStream.writeUTF(year);
                outputStream.writeUTF(college);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public void removeACourse(String courseId){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourse));
                outputStream.writeUTF(courseId);
            }
        }
        catch (IOException e) {
            isConnected = false;
        }
    }



    public void attemptToMakeNewConnection(){
        try {
            synchronized (lock){
                String ip = ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.IP);
                int port = Integer.parseInt(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.Port));
                socket = new Socket(ip, port);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                authToken = inputStream.readUTF();
                isConnected = true;

                saveMessagesBecauseNotConnected(username);
            }

        }
        catch (IOException e) {
            isConnected = false;
        }
    }

    public void testConnection(){

        try {
            synchronized (lock){
                validAuthToken();
                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.testConnection));
            }
        } catch (IOException e) {
            isConnected = false;
        }
        catch (NullPointerException e){
            isConnected = false;
        }
    }

    public void sendFileToServer(String folder, String address){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }

                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendFileToServer));
                outputStream.writeUTF(address);

                sendFile(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForUser) + folder + "\\\\" + address);
            }
        }
        catch (IOException e) {
            System.out.println("in catch");
            isConnected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("finish in client");

    }

    public void sendFileFromServer(String address, String username){
        try {
            synchronized (lock){
                if(!validAuthToken()){
                    return;
                }
                outputStream.writeUTF(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendFileFromServer));
                outputStream.writeUTF(address);

                String s = "";
                s =  username;


                receiveFile(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForUser) + s + "\\\\" + address);
            }
        }
        catch (IOException e) {
            isConnected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("finish sendFileFromServer in client");



    }


//    EXTRAS:

    public static void loadFromAFile(String username, LinkedList<String> toPersonsUsername, LinkedList<String> AddressOrText, LinkedList<String> Time, LinkedList<String> TypeFile)  {
        if(username.equals("")){
            return;
        }
        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + username + "\\" + username + ".txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return;
        }
        while(scanner.hasNext()){
            String str = scanner.nextLine();
            String[] s = str.split(":::");
            toPersonsUsername.add(s[0]);
            AddressOrText.add(s[1]);
            Time.add(s[2]);
            TypeFile.add(s[3]);
        }
        scanner.close();
    }

    public static void saveToFile(String usernameFrom, String userNameTo, String fileAddress, String typeFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(userNameTo + ":::");
        String s = "";
        for (int i = 0; i < fileAddress.length(); i++) {
            if(fileAddress.charAt(i) == '\n'){
                continue;
            }
            s = s + fileAddress.charAt(i);
        }
        fileAddress = s;
        sb.append(fileAddress + ":::");
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.getYear() + "//" + localDateTime.getMonthValue()
                + "//" + localDateTime.getDayOfMonth() + "//" + localDateTime.getHour()
                + "//" + localDateTime.getMinute() + "//" + localDateTime.getSecond();
        sb.append(time + ":::");
        sb.append(typeFile);
        try {
            SaveToFileString(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + usernameFrom + "\\" + usernameFrom + ".txt", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SaveToFileString(String path ,String value) throws IOException {
        File file = new File(path);
        Scanner scanner2 = new Scanner(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        PrintStream printStream = new PrintStream(fileOutputStream);
        printStream.println(value);
        scanner2.close();
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

    private void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        outputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,bytes);
            outputStream.flush();
        }
        fileInputStream.close();
    }

    private void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = inputStream.readLong();     // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = inputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }






}
