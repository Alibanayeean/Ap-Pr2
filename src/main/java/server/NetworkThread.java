package server;

import API.API;
import back.course.Course;
import back.course.HomeWork;
import back.persons.Student;
import back.persons.Teacher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.LinkedList;


public class NetworkThread implements Runnable {
    private Socket socket;
    private String authToken;
    DataInputStream inputStream = null;
    DataOutputStream outputStream = null;

    public NetworkThread(Socket socket) {
        this.socket = socket;
    }

    private String StudentToJson(Student student){
        Gson gson = new Gson();
        return gson.toJson(student);
    }
    private String ListStudentToJson(LinkedList<Student> student){
        Gson gson = new Gson();
        return gson.toJson(student);
    }
    private String TeacherToJson(Teacher teacher){
        Gson gson = new Gson();
        return gson.toJson(teacher);
    }
    private String ListTeacherToJson(LinkedList<Teacher> teacher){
        Gson gson = new Gson();
        return gson.toJson(teacher);
    }
    private String CourseToJson(Course course){
        Gson gson = new Gson();
        return gson.toJson(course);
    }
    private String ListCourseToJson(LinkedList<Course> course){
        Gson gson = new Gson();
        return gson.toJson(course);
    }

    @Override
    public void run() {


        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {

        }

        boolean notBroken = true;

        authToken = AuthTokenGenerator.nextToken();

        try {
            outputStream.writeUTF(authToken);
        } catch (IOException e) {
//            TODO
        }


        while (notBroken) {

            String input = "";

            try {
                input = inputStream.readUTF();

                if (input.equals(authToken)) {
                    outputStream.writeBoolean(true);
                }
                else {
                    outputStream.writeBoolean(false);
                    continue;
                }



                input = inputStream.readUTF();
                checkConditionAndDoAction(input, inputStream, outputStream);

            }
            catch (IOException e) {
                notBroken = false;
            }

        }
    }

    public void checkConditionAndDoAction(String input, DataInputStream inputStream, DataOutputStream outputStream) {

        if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.CHANGE_COLOR_OF_STUDENT))){
            try {
                API.changeColorForStudent(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
//                TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.CHANGE_COLOR_OF_TEACHER))){
            try {
                API.changeColorForTeacher(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
//                TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeColorForMohseni))){
            try {
                API.changeColorForMohseni(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
//                TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeColorForAdmin))){
            try {
                API.changeColorForAdmin(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
//                TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStudentLogin))){
            try {
                Student student = API.getStudentLogin(inputStream.readUTF(), inputStream.readUTF());
                outputStream.writeUTF(StudentToJson(student));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getTeacherLogin))){
            try {
                outputStream.writeUTF(TeacherToJson(API.getTeacherLogin(inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMohseniLogin))){
            try {
                outputStream.writeUTF((new Gson()).toJson(API.getMohseniLogin(inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAdminLogin))){
            try {
                outputStream.writeUTF((new Gson()).toJson(API.getAdminLogin(inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getLastNameHelpTeacherFromId))){
            try {
                outputStream.writeUTF(API.getLastNameHelpTeacherFromId(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getLastNameHelpTeacherFromLastName))){
            try {
                outputStream.writeUTF(TeacherToJson(API.getLastNameHelpTeacherFromLastName(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudents))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.getAllStudents()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllTeachers))){
            try {
                outputStream.writeUTF(ListTeacherToJson(API.getAllTeachers()));

            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllCourses))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.getAllCourses()));

            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowTemperoryScoresForEA))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.ShowTemperoryScoresForEA(inputStream.readInt(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowStudentsForMohseni))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.ShowStudentsForMohseni(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowCourses))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.ShowCourses(inputStream.readUTF(), inputStream.readInt(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowTeachers))){
            try {
                outputStream.writeUTF(ListTeacherToJson(API.ShowTeachers(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacherLastNameFromId))){
            try {
                outputStream.writeUTF(API.FindTeacherLastNameFromId(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacherFirstNameFromId))){
            try {
                outputStream.writeUTF(API.FindTeacherFirstNameFromId(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacherCollegeFromId))){
            try {
                outputStream.writeUTF(API.FindTeacherCollegeFromId(inputStream.readInt()));

            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindACourseFormACollege))){
            try {
                outputStream.writeUTF(CourseToJson(API.FindACourseFormACollege(inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesFromAStudent))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.GiveCoursesFromAStudentForCW(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesFromATeacher))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.GiveCoursesFromATeacher(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.returnAllHelpTeachersFromACollege))){
            try {
                outputStream.writeUTF(API.returnAllHelpTeachersFromACollege(inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.CanSignUpForDuplicateUsername))){
            try {
                outputStream.writeBoolean(API.CanSignUpForDuplicateUsername(inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.validDuplicatePublicIdForCourse))){
            try {
                outputStream.writeBoolean(API.validDuplicatePublicIdForCourse(inputStream.readInt(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addStudent))){
            try {
                Gson gson = new Gson();
                API.addStudent(gson.fromJson(inputStream.readUTF(), Student.class));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addTeacher))){
            try {
                Gson gson = new Gson();
                API.addTeacher(gson.fromJson(inputStream.readUTF(), Teacher.class));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addCourse))){
            try {
                Gson gson = new Gson();
                API.addCourse(gson.fromJson(inputStream.readUTF(), Course.class));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindTeacher))){
            try {
                outputStream.writeUTF(TeacherToJson(API.FindTeacher(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.isValidTeacher))){
            try {
                outputStream.writeBoolean(API.isValidTeacher(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindCourse))){
            try {
                outputStream.writeUTF(CourseToJson(API.FindCourse(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.AddNewCourseToTeacher))){
            try {
                API.AddNewCourseToTeacher(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.StringToTeacher))){
            try {
                outputStream.writeUTF(ListTeacherToJson(API.StringToTeacher(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.SearchStudentByStudentNumberAndName))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.SearchStudentByStudentNumberAndName(inputStream.readInt(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getTeacherOfACourse))){
            try {
                String s = API.getTeacherOfACourse(inputStream.readUTF());
                outputStream.writeUTF(s);
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesOfATeacher))){
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                LinkedList<String> ids = gson.fromJson(inputStream.readUTF(), listType);
                outputStream.writeUTF(ListCourseToJson(API.GiveCoursesOfATeacher(ids)));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesOfAStudent))){
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                LinkedList<String> ids = gson.fromJson(inputStream.readUTF(), listType);
                outputStream.writeUTF(ListCourseToJson(API.GiveCoursesOfAStudent(ids)));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveCoursesOfACollege))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.GiveCoursesOfACollege(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveStudentsOfACoursesFromId))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.GiveStudentsOfACoursesFromId(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GiveStudentsOfACoursesFromString))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.GiveStudentsOfACoursesFromString(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentFromId))){
            try {
                outputStream.writeUTF(StudentToJson(API.FindStudentFromId(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindIndexCourseFormAStudentFromCourseName))){
            try {
                outputStream.writeInt(API.FindIndexCourseFormAStudentFromCourseName(inputStream.readInt(), inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.RemoveCourseFromATeachersAndStudents))){
            try {
                Gson gson = new Gson();
                String courseName = inputStream.readUTF();
                Teacher teacher = gson.fromJson(inputStream.readUTF(), Teacher.class);
                API.RemoveCourseFromATeachersAndStudents(courseName, teacher);
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowableCoursesOfStudentTemporaryScores))){
            try {
                LinkedList<String> integers = API.ShowableCoursesOfStudentTemporaryScores(inputStream.readInt());
                Gson gson = new Gson();
                outputStream.writeUTF(gson.toJson(integers));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindIndexCourseFormAStudentFromId))){
            try {
                outputStream.writeInt(API.FindIndexCourseFormAStudentFromId(inputStream.readInt(), inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.GetScore))){
            try {
                outputStream.writeDouble(API.GetScore(inputStream.readUTF(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentsWantsToReject))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.FindStudentsWantsToReject(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentsWantsToMinor))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.FindStudentsWantsToMinor(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.RemoveStudent))){
            try {
                API.RemoveStudent(inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowPastCoursesForAStudent))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.ShowPastCoursesForAStudent(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.ShowStudentsForATeacherEA))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.ShowStudentsForATeacherEA(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStatusACourseFromAStudent))){
            try {
                outputStream.writeUTF(API.getStatusACourseFromAStudent(inputStream.readInt(), inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getScoreACourseFromAStudent))){
            try {
                outputStream.writeDouble(API.getScoreACourseFromAStudent(inputStream.readInt(), inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getPassesCourseFromAStudent))){
            try {
                outputStream.writeUTF(API.getPassesCourseFromAStudent(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindStudentsWantsToRecome))){
            try {
                outputStream.writeUTF(ListStudentToJson(API.FindStudentsWantsToRecome(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getIndexRequest))){
            try {
                outputStream.writeInt(API.getIndexRequest(inputStream.readInt(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getWeighOfCoursesPass))){
            try {
                outputStream.writeInt(API.getWeighOfCoursesPass(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getNumPassForACourse))){
            try {
                LinkedList<String> strings = API.getNumPassForACourse(inputStream.readUTF());
                Gson gson = new Gson();
                outputStream.writeUTF(gson.toJson(strings));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.AddAndRemoveACourseFormTeachers))){
            try {
                API.AddAndRemoveACourseFormTeachers(inputStream.readUTF(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.RemoveACourseFormTeachers))){
            try {
                API.RemoveACourseFormTeachers(inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changePasswordForAStudent))){
            try {
                API.changePasswordForAStudent(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changePasswordAForTeacher))){
            try {
                API.changePasswordAForTeacher(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeLastLoginStudent))){
            try {
                API.changeLastLoginStudent(inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeLastLoginTeacher))){
            try {
                API.changeLastLoginTeacher(inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeEducationStatusOfAStudent))){
            try {
                API.changeEducationStatusOfAStudent(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeProtestForAStudent))){
            try {
                API.changeProtestForAStudent(inputStream.readInt(), inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editEmailForAStudent))){
            try {
                API.editEmailForAStudent(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editPhoneForAStudent))){
            try {
                API.editPhoneForAStudent(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setImageAddressOfAStudent))){
            try {
                API.setImageAddressOfAStudent(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addIdTeacherToStudentForRecome))){
            try {
                API.addIdTeacherToStudentForRecome(inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeRecomeForStudentBecauseShowed))){
            try {
                API.changeRecomeForStudentBecauseShowed(inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted))){
            try {
                API.changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted(inputStream.readInt(), inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setMinorCollegeForAStudent))){
            try {
                API.setMinorCollegeForAStudent(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setDormRequestForAStudent))){
            try {
                API.setDormRequestForAStudent(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeMinorForStudentBecauseShowedAndRejected))){
            try {
                API.changeMinorForStudentBecauseShowedAndRejected(inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.submitRecomeForAStudent))){
            try {
                API.submitRecomeForAStudent(inputStream.readInt(), inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setRoomNumberForATeacher))){
            try {
                API.setRoomNumberForATeacher(inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeATeacher))){
            try {
                API.removeATeacher(inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeStatusTeacher))){
            try {
                API.changeStatusTeacher(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editEmailForATeacher))){
            try {
                API.editEmailForATeacher(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.editPhoneForATeacher))){
            try {
                API.editPhoneForATeacher(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setImageAddressOfATeacher))){
            try {
                API.setImageAddressOfATeacher(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setAnswerAndScoreForStudent))){
            try {
                API.setAnswerAndScoreForStudent(inputStream.readInt(), inputStream.readInt(), inputStream.readDouble(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeMinorStatusFromEA))){
            try {
                API.changeMinorStatusFromEA(inputStream.readBoolean(), inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setTimesLoginForAStudent))){
            try {
                API.setTimesLoginForAStudent(inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setTimesLoginForATeacher))){
            try {
                API.setTimesLoginForATeacher(inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getProtestForCourseStudent))){
            try {
                outputStream.writeUTF(API.getProtestForCourseStudent(inputStream.readInt(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAnswerForCourseStudent))){
            try {
                outputStream.writeUTF(API.getAnswerForCourseStudent(inputStream.readInt(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getScoreForCourseStudent))){
            try {
                outputStream.writeDouble(API.getScoreForCourseStudent(inputStream.readInt(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMinorStatus))){
            try {
                outputStream.writeUTF(API.getMinorStatus(inputStream.readInt(), inputStream.readBoolean()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getRequestForRecommendation))){
            try {
                outputStream.writeUTF(API.getRequestForRecommendation(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getIdTeacherForRequest))){
            try {
                outputStream.writeInt(API.getIdTeacherForRequest(inputStream.readInt(), inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getDormStatus))){
            try {
                outputStream.writeUTF(API.getDormStatus(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getThesisDefenceFromAStudent))){
            try {
                outputStream.writeUTF(API.getThesisDefenceFromAStudent(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.validStudentNumberForTA))){
            try {
                outputStream.writeBoolean(API.validStudentNumberForTA(inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.validPublicIdForCourse))){
            try {
                outputStream.writeBoolean(API.validPublicIdForCourse(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getFileNameOfFromUsername))){
            try {
                outputStream.writeUTF(API.getFileNameOfFromUsername(inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getFileNameOfFromStudentNumber))){
            try {
                outputStream.writeUTF(API.getFileNameOfFromStudentNumber(inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveMessage))){
            try {
                API.saveMessage(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getYearsToUniversityForEA))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getYearsToUniversityForEA(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveTimeForSelectionUnit))){
            try {
                API.saveTimeForSelectionUnit(inputStream.readUTF(), inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.setExamDayForACourse))){
            try {
                API.setExamDayForACourse(inputStream.readUTF(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllCoursesFromACollege))){
            try {
                LinkedList<Course> linkedList = API.getAllCoursesFromACollege(inputStream.readUTF(), inputStream.readUTF());
                outputStream.writeUTF(ListCourseToJson(linkedList));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllCoursesFromACollegeForSelectUnit))){
            try {
                LinkedList<Course> linkedList = API.getAllCoursesFromACollegeForSelectUnit(inputStream.readUTF(), inputStream.readUTF());
                outputStream.writeUTF(ListCourseToJson(linkedList));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getLikedCourses))){
            try {
                LinkedList<Course> linkedList = API.getLikedCourses(inputStream.readInt());
                outputStream.writeUTF(ListCourseToJson(linkedList));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getSuggestedCourses))){
            try {
                LinkedList<Course> linkedList = API.getSuggestedCourses(inputStream.readInt(), inputStream.readUTF(), inputStream.readUTF());
                outputStream.writeUTF(ListCourseToJson(linkedList));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStatusOfACourseForUnitSelection))){
            try {
                LinkedList<Boolean> linkedList = API.getStatusOfACourseForUnitSelection(inputStream.readInt(), inputStream.readUTF());
                outputStream.writeUTF((new Gson()).toJson(linkedList));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addACourseToLikedCourses))){
            try {
                API.addACourseToLikedCourses(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourseFromLikedCourses))){
            try {
                API.removeACourseFromLikedCourses(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourseFromLikedCourses))){
            try {
                API.removeACourseFromLikedCourses(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addACourseFromCoursesWantToGetThisTerm))){
            try {
                outputStream.writeBoolean(API.addACourseFromCoursesWantToGetThisTerm(inputStream.readInt(), inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeGroupForSelectionUnit))){
            try {
                API.changeGroupForSelectionUnit(inputStream.readUTF(), inputStream.readUTF(), inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourseFromCoursesWantToGetThisTerm))){
            try {
                API.removeACourseFromCoursesWantToGetThisTerm(inputStream.readInt(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudentsFromOneCollegeAndFromOneYear))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getAllStudentsFromOneCollegeAndFromOneYear(inputStream.readUTF(), inputStream.readInt(), inputStream.readInt(), inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllMessagesFromAPerson))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getAllMessagesFromAPerson(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllMessagesFromTwoPersons))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getAllMessagesFromTwoPersons(inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getUserNameFromName))){
            try {
                outputStream.writeUTF((API.getUserNameFromName(inputStream.readUTF(), inputStream.readBoolean())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getEducationalMaterialsFromACourse))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getEducationalMaterialsFromACourse(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getExamsFromACourse))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getExamsFromACourse(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getHomeWorksFromACourse))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getHomeWorksFromACourse(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeAnswerForHomeWorkOrExamForAStudent))){
            try {
                API.changeAnswerForHomeWorkOrExamForAStudent(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF(), inputStream.readBoolean());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeAFileFromServerForCw))){
            try {
                API.removeAFileFromServerForCw(inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMessagesForAStudent))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getMessagesForAStudent(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getMessagesForATeacher))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getMessagesForATeacher(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeAMessageFromAStudent))){
            try {
                API.removeAMessageFromAStudent(inputStream.readInt(), inputStream.readUTF(), inputStream.readBoolean(), inputStream.readBoolean());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeAMessageFromATeacherForMessages))){
            try {
                API.removeAMessageFromATeacherForMessages(inputStream.readInt(), inputStream.readUTF(), inputStream.readBoolean());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendMessageToEAToGetACourse))){
            try {
                outputStream.writeBoolean(API.sendMessageToEAToGetACourse(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getTimeSelectUnitForAStudent))){
            try {
                outputStream.writeUTF(API.getTimeSelectUnitForAStudent(inputStream.readInt()));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.FindATeacherOrStudentForChatRoom))){
            try {
                outputStream.writeUTF((new Gson()).toJson(API.FindATeacherOrStudentForChatRoom(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendMessageToAPersonForRequestChatRoom))){
            try {
                outputStream.writeBoolean((API.sendMessageToAPersonForRequestChatRoom(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addStudentToCourseInCW))){
            try {
                outputStream.writeBoolean((API.addStudentToCourseInCW(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.isAStudentTAOfACourse))){
            try {
                outputStream.writeBoolean((API.isAStudentTAOfACourse(inputStream.readInt(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.addNewFileInCwForExamOrHw))){
            try {
                String courseId = inputStream.readUTF();
                boolean isExam = inputStream.readBoolean();
                String address = inputStream.readUTF();
                boolean doesHvaFile = inputStream.readBoolean();
                if(doesHvaFile){
                    receiveFile(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForServer) + address);
                }


                API.addNewFileInCwForExamOrHw(courseId, isExam, inputStream.readUTF());
            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {

            }

        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getNameOfAStudentFromId))){
            try {
                outputStream.writeUTF(API.getNameOfAStudentFromId(inputStream.readInt()));
            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {

            }

        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStudentsNumberOfAStudentFromId))){
            try {
                outputStream.writeUTF(API.getStudentsNumberOfAStudentFromId(inputStream.readInt()));
            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {

            }

        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveExamsOrHWsForCw))){
            try {
                API.saveExamsOrHWsForCw(inputStream.readUTF(), inputStream.readUTF(), inputStream.readBoolean());
            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {

            }

        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.changeEducationalMaterialForACourse))){
            try {
                API.changeEducationalMaterialForACourse(inputStream.readUTF(), inputStream.readUTF());
            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {

            }

        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllTeacherFormACollege))){
            try {
                outputStream.writeUTF(ListTeacherToJson(API.getAllTeacherFormACollege(inputStream.readInt(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getCoursesOfATeacher))){
            try {
                outputStream.writeUTF(ListCourseToJson(API.getCoursesOfATeacher(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getCoursesGroup))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getCoursesGroup(inputStream.readInt())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getStudentsWithFilterForMohseni))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getStudentsWithFilterForMohseni(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.findStudentFromStudentNumber))){
            try {
                outputStream.writeUTF(StudentToJson(API.findStudentFromStudentNumber(inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudentForATeacherInChatRoom))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getAllStudentForATeacherInChatRoom(inputStream.readInt(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getAllStudentForABossAndEAInChatRoom))){
            try {
                outputStream.writeUTF(new Gson().toJson(API.getAllStudentForABossAndEAInChatRoom(inputStream.readInt(), inputStream.readUTF(), inputStream.readUTF())));
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.finishTimeSelection))){
            try {
               API.finishTimeSelection(inputStream.readUTF(), inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.removeACourse))){
            try {
               API.removeACourse(inputStream.readUTF());
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.saveMessagesBecauseNotConnected))){
            try {
                int size = inputStream.readInt();
                if(size == 0){
                    return;
                }
                else{
                    Type listType = new TypeToken<LinkedList<String>>(){}.getType();
                    String username = inputStream.readUTF();
                    Gson gson = new Gson();
                    LinkedList<String> toPersonsUsername = gson.fromJson(inputStream.readUTF(), listType);
                    LinkedList<String> AddressOrText = gson.fromJson(inputStream.readUTF(), listType);
                    LinkedList<String> Time = gson.fromJson(inputStream.readUTF(), listType);
                    LinkedList<String> TypeFile = gson.fromJson(inputStream.readUTF(), listType);

                    outputStream.writeBoolean(API.saveMessagesBecauseNotConnected(username, toPersonsUsername, AddressOrText, Time, TypeFile));
                }
            } catch (IOException e) {
                //TODO
            }
        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.getChatRoomsInfo))){
            try {
                Gson gson = new Gson();
                String username = inputStream.readUTF();
                if(username.equals("")){
                    return;
                }

                String s = gson.toJson(API.getChatRoomsInfo(username));

                outputStream.writeUTF(s);
            } catch (IOException e) {
                //TODO
            }
        }




        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendFileToServer))){
            try {
                String address = inputStream.readUTF();
                receiveFile(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForServer) + address);
            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {
                
            }


            System.out.println("finish");

        }
        else if(input.equals(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.sendFileFromServer))){
            try {
                String address = inputStream.readUTF();
                sendFile(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForServer) + address);

            }
            catch (IOException e) {
                System.out.println("in catch");
            }
            catch (Exception e) {

            }

            System.out.println("finish sendFileFromServer in server");

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
