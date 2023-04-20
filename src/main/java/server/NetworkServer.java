package server;

import back.course.*;
import back.enums.*;
import back.persons.Admin;
import back.persons.Mohseni;
import back.persons.Student;
import back.persons.Teacher;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import dataBase.DataBaseController;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class NetworkServer {
    private static ServerSocket serverSocket;

    static LinkedList<Student> students = new LinkedList<>();
    static LinkedList<Teacher> teachers = new LinkedList<>();
    static LinkedList<Course> courses = new LinkedList<>();
    static Mohseni mohseni;
    static Admin admin;
    static DataBaseController dataBaseController = new DataBaseController();
    public static void addToListsForFirstTime(){
        LinkedList<Days> days3 = new LinkedList<>();
        days3.add(Days.Saturday);
        days3.add(Days.Thursday);
        Course course3 = new Course("1-1", 200 , "Math1" , College.CS, null, null, 4, Grade.BC, 1400, 11, 7, days3, 13, 15, new LinkedList<>());
        Course course15 = new Course("1-2", 100 , "Math1" , College.CS, null, null, 4, Grade.BC, 1400, 11, 7, days3, 13, 15, new LinkedList<>());

        LinkedList<String> reBp = new LinkedList();
        reBp.add(course3.getId());

        LinkedList<Days> days1 = new LinkedList<>();
        days1.add(Days.Monday);
        days1.add(Days.Saturday);
        Course course1 = new Course("2-1",40, "BP" , College.CS, null, reBp, 4, Grade.BC, 1400, 10, 26, days1, 10.5, 12.5, new LinkedList<>());


        LinkedList<Days> days2 = new LinkedList<>();
        days2.add(Days.Saturday);
        days2.add(Days.Sunday);
        Course course2 = new Course("3-1", 1, "BP" , College.CE, null, reBp, 3, Grade.BC, 1400, 10, 27, days2, 11.5, 1, new LinkedList<>());

        LinkedList<Days> days4 = new LinkedList<>();
        days4.add(Days.Thursday);
        days4.add(Days.Wednesday);
        Course course4 = new Course("4-1",-1, "Physic1" , College.Physics, null, reBp, 3, Grade.BC, 1400, 10, 31, days4, 15, 16.5, new LinkedList<>());

        LinkedList<String> preAp = new LinkedList();
        preAp.add(course1.getId());
        preAp.add(course3.getId());
        LinkedList<Days> days5 = new LinkedList<>();
        days5.add(Days.Thursday);
        days5.add(Days.Wednesday);
        Course course5 = new Course("5-1",0,"AP" , College.CS, preAp, null, 4, Grade.BC, 1401, 4, 10, days5, 17, 19, new LinkedList<>());


        LinkedList<Days> days6 = new LinkedList<>();
        days6.add(Days.Thursday);
        days6.add(Days.Friday);
        Course course6 = new Course("6-1",86,"AP" , College.CE, preAp, null, 3, Grade.BC, 1401, 4 , 5, days6, 10, 11.5, new LinkedList<>());

        LinkedList<String> preGossaste = new LinkedList();
        preGossaste.add(course3.getId());
        LinkedList<Days> days7 = new LinkedList<>();
        days7.add(Days.Monday);
        Course course7 = new Course("7-1",70, "Discrete structure" , College.CS, preGossaste, null, 3, Grade.BC, 1400, 10 , 27, days7, 11, 14, new LinkedList<>());


        LinkedList<Days> days8 = new LinkedList<>();
        days8.add(Days.Thursday);
        days8.add(Days.Wednesday);
        Course course8 = new Course("8-1",70, "Discrete structure" , College.CE, preGossaste, null, 3, Grade.BC, 1401, 4, 7, days8, 12, 13.5, new LinkedList<>());


        LinkedList<String> preMath2 = new LinkedList();
        preMath2.add(course3.getId());
        LinkedList<Days> days10 = new LinkedList<>();
        days10.add(Days.Tuesday);
        days10.add(Days.Friday);
        Course course10 = new Course("9-1",40, "Math2" , College.Math, preMath2, null, 4, Grade.BC, 1401, 4 , 15, days10, 7, 9, new LinkedList<>());


        LinkedList<String> preMath3 = new LinkedList();
        preMath2.add(course10.getId());
        LinkedList<Days> days13 = new LinkedList<>();
        days13.add(Days.Tuesday);
        days13.add(Days.Friday);
        Course course13 = new Course("10-1",45,  "Math3" , College.Math, preMath3, null, 4, Grade.BC, 1401, 4 , 15, days13, 7, 9, new LinkedList<>());

        LinkedList<String> prePhysics2 = new LinkedList();
        prePhysics2.add(course3.getId());
        LinkedList<String> rePhysics2 = new LinkedList();
        rePhysics2.add(course10.getId());
        LinkedList<Days> days9 = new LinkedList<>();
        days9.add(Days.Friday);
        days9.add(Days.Saturday);
        Course course9 = new Course("11-1",60, "Physics2" , College.Physics, prePhysics2, rePhysics2, 3, Grade.BC, 1401, 4, 10, days9, 12.5, 14, new LinkedList<>());

        LinkedList<Days> daysMoharef1 = new LinkedList<>();
        daysMoharef1.add(Days.Friday);
        Course moharef1 = new Course("19-1",60, "Moharef1" , College.Public,  new LinkedList<>(), new LinkedList<>(), 2, Grade.BC, 1401, 4, 10, daysMoharef1, 8, 10, new LinkedList<>());

        LinkedList<Days> daysMoharef2 = new LinkedList<>();
        daysMoharef2.add(Days.Saturday);
        Course moharef2 = new Course("20-1",60, "Moharef2" , College.Public,  new LinkedList<>(), new LinkedList<>(), 2, Grade.BC, 1401, 5, 10, daysMoharef1, 12, 14, new LinkedList<>());

        LinkedList<String> preLogical = new LinkedList();
        preLogical.add(course3.getId());
        preLogical.add(course2.getId());
        LinkedList<String> reLogical = new LinkedList();
        reLogical.add(course8.getId());
        reLogical.add(course6.getId());
        LinkedList<Days> days11 = new LinkedList<>();
        days11.add(Days.Thursday);
        days11.add(Days.Friday);
        Course course11 = new Course("12-1",50, "logical Design" , College.CE, preLogical, reLogical, 3, Grade.BC, 1401, 4, 1, days11, 8, 9.5, new LinkedList<>());

        LinkedList<Days> days12 = new LinkedList<>();
        days12.add(Days.Thursday);
        days12.add(Days.Friday);
        Course course12 = new Course("13-1",55, "logical Design" , College.EE, new LinkedList<>(), new LinkedList<>(), 4, Grade.BC, 1401, 4, 1, days12, 8, 9.5, new LinkedList<>());

        LinkedList<Days> days14 = new LinkedList<>();
        days14.add(Days.Saturday);
        days14.add(Days.Friday);
        Course course14 = new Course("14-1",60, "BP" , College.EE, new LinkedList<>(), new LinkedList<>(), 4, Grade.BC, 1401, 4, 5, days14, 8, 10, new LinkedList<>());


        LinkedList<String> courseTefagh = new LinkedList();
        courseTefagh.add(course5.getId());
        courseTefagh.add(course1.getId());
        Teacher tefagh = new Teacher(1, Color.YELLOW, null,  "Mojtaba" , "Tefagh" , "m@e." , "1".hashCode() + ""  ,"98902", "054461" ,College.CS, StatusTeacher.Simple, courseTefagh, TeacherDegree.AssistantProfessor, "100", 140);


        LinkedList<String> coursesKhazayee = new LinkedList();
        coursesKhazayee.add(course3.getId());
        coursesKhazayee.add(course15.getId());
        Teacher khazayee = new Teacher(2, Color.GRAY, null,"Shahram" , "Khazayee" , "s@kh." , "2".hashCode() + "", "090277777","98985",  College.CS, StatusTeacher.EA, coursesKhazayee ,TeacherDegree.AssociateProfessor, "200" , 143);


        LinkedList<String> Fanaicourses = new LinkedList();
        Fanaicourses.add(course7.getId());
        Teacher Fanai = new Teacher(3, Color.BLUE, null ,"HamidReza" , "Fanai" , "h@f." , "3".hashCode() + "" ,"9898", "34536" ,College.CS, StatusTeacher.Boss, Fanaicourses ,TeacherDegree.FullProfessor, "300", 200);



        LinkedList<String> courseFazli = new LinkedList();
        courseFazli.add(course6.getId());
        courseFazli.add(course2.getId());
        Teacher fazli = new Teacher(4, Color.GREEN, null,  "Amin" , "Fazli" , "AminFazli@gmail.com" , "4".hashCode() + "" ,"662", "3652" ,College.CE, StatusTeacher.Simple, courseFazli, TeacherDegree.AssistantProfessor, "400", 20);


        LinkedList<String> coursesAbam = new LinkedList();
        coursesAbam.add(course8.getId());
        Teacher abam = new Teacher(5, Color.GRAY, null,"Mohammad Ali" , "Abam" , "AliAbam@edu.com" , "5".hashCode() + "" ,"213", "563", College.CE, StatusTeacher.EA, coursesAbam ,TeacherDegree.AssistantProfessor, "500" , 143);


        LinkedList<String> Hessabicourses = new LinkedList();
        Hessabicourses.add(course11.getId());
        Teacher hessabi = new Teacher(6, Color.BLUE, null ,"Shahin" , "Hessabi" , "Shahin@edu.com" , "6".hashCode() + ""  ,"513", "6" ,College.CE, StatusTeacher.Boss, Hessabicourses ,TeacherDegree.AssociateProfessor, "600" + "", 200);


        Teacher newEducationalTeacher = new Teacher(7, Color.BLUE, null ,"new" , "New" , "n@n." , "7".hashCode() + "" ,"5433", "45655" ,College.Mechanic, StatusTeacher.Boss, new LinkedList<>() ,TeacherDegree.FullProfessor, "700", 200);


        teachers.add(tefagh);
        teachers.add(khazayee);
        teachers.add(Fanai);
        teachers.add(fazli);
        teachers.add(abam);
        teachers.add(hessabi);
        teachers.add(newEducationalTeacher);


        LinkedList<String> CourseThisTermAli = new LinkedList();
        CourseThisTermAli.add(course5.getId());


        LinkedList<String> CoursePastAli = new LinkedList();
        CoursePastAli.add(course3.getId());
        CoursePastAli.add(course4.getId());
        CoursePastAli.add(course1.getId());
        CoursePastAli.add(course2.getId());

        LinkedList<Double> CoursePastScoreAli = new LinkedList();
        CoursePastScoreAli.add(2.0);
        CoursePastScoreAli.add(2.0);
        CoursePastScoreAli.add(15.0);
        CoursePastScoreAli.add(12.0);

        Student ali = new Student(1, Color.GREENYELLOW, "400108785.jpg", "Ali" , "Banayeean" , "a.banayeean@gmail.com" , "a".hashCode() + "" ,"9027985542", "2283763614" ,College.CS ,"400108785", CourseThisTermAli, CoursePastAli ,CoursePastScoreAli, tefagh.getId(), Grade.BC, 1400, EducationStatus.Studying, true, 10);



        LinkedList<String> CourseThisTermReza = new LinkedList();
        CourseThisTermReza.add(course2.getId());
        CourseThisTermReza.add(course3.getId());
        CourseThisTermReza.add(course1.getId());
        CourseThisTermReza.add(course4.getId());
        CourseThisTermReza.add(course5.getId());

        LinkedList<String> CoursePastReza = new LinkedList();
        CoursePastReza.add(course6.getId());
        CoursePastReza.add(course7.getId());
        CoursePastReza.add(course8.getId());

        LinkedList<Double> CoursePastScoreReza = new LinkedList();
        CoursePastScoreReza.add(19.0);
        CoursePastScoreReza.add(14.0);
        CoursePastScoreReza.add(2.75);

        Student reza = new Student(2, Color.GRAY, null, "Reza" , "Reza" , "reza@deu.com" , "r".hashCode() + "" , "902", "228", College.CE, "99", CourseThisTermReza, CoursePastReza , CoursePastScoreReza, khazayee.getId() , Grade.BC, 1399, EducationStatus.Studying, false, -1);


        Student Phd = new Student(3, Color.LIGHTBLUE, null, "T" , "fasefase" , "amibfase@gmail.com" , "t".hashCode() + "" , "9462", "6223", College.CE, "4001515", new LinkedList<>(), null , null, fazli.getId() , Grade.PHD, 1398, EducationStatus.Studying, false, -1);



        LinkedList<String> CourseThisTermA = new LinkedList();
        CourseThisTermA.add(course3.getId());
        CourseThisTermA.add(course4.getId());

        LinkedList<String> CoursePast = new LinkedList();
        CoursePast.add(course5.getId());
        CoursePast.add(course6.getId());
        CoursePast.add(course7.getId());
        CoursePast.add(course8.getId());

        LinkedList<Double> CoursePastScore = new LinkedList();
        CoursePastScore.add(12.0);
        CoursePastScore.add(9.75);
        CoursePastScore.add(8.25);
        CoursePastScore.add(19.5);

        Student a = new Student(4, Color.LIGHTBLUE, null, "T" , "asfe" , "asfe@yahoo.com" , "m".hashCode() + "" , "2", "63", College.Physics, "40011", CourseThisTermA, CoursePast , CoursePastScore, fazli.getId(), Grade.BC, 1398, EducationStatus.Studying, true, 12);

        LinkedList<String> CourseThisTerm10 = new LinkedList();
        CourseThisTerm10.add(course5.getId());
        LinkedList<String> CoursePast10 = new LinkedList();
        CoursePast10.add(course4.getId());
        CoursePast10.add(course3.getId());
        LinkedList<Double> CoursePastScore10 = new LinkedList();
        CoursePastScore10.add(13.0);
        CoursePastScore10.add(10.0);
        Student student10 = new Student(5, Color.GREENYELLOW, null, "FirstName10", "Lastname10", "Email10", ("10").hashCode() + "", "1000 ", "10000", College.CS, "10", CourseThisTerm10, CoursePast10, CoursePastScore10,  khazayee.getId(), Grade.BC, 1397, EducationStatus.Studying, true, 14);
        student10.getExtrasTeachersInChat().add(khazayee.getId());

        student10.getExtraStudentsInChat().add(reza.getId());

        student10.getMessagesForStudent().getMessages().put("1400" ,  "you are added to class with id 1");
        student10.getMessagesForStudent().getMessages().put("1401" ,  "you are added to class with id 10");
        student10.getMessagesForStudent().getMessages().put("1402" ,  "you are added to class with id 100");
        student10.getMessagesForStudent().getMessages().put("1403" ,  "you are added to class with id 1000");
        student10.getMessagesForStudent().getMessages().put("1404" ,  "student with student number : 400108785 want to send message in chat room");


        students.add(student10);
        LinkedList<String> CourseThisTerm11 = new LinkedList();
        CourseThisTerm11.add(course1.getId());
        LinkedList<String> CoursePast11 = new LinkedList();
        CoursePast11.add(course4.getId());
        CoursePast11.add(course7.getId());
        LinkedList<Double> CoursePastScore11 = new LinkedList();
        CoursePastScore11.add(19.0);
        CoursePastScore11.add(10.0);
        Student student11 = new Student(6, Color.GREENYELLOW, null, "FirstName11", "Lastname11", "Email11", ("11").hashCode() + "", "1100 ", "11000", College.CS, "11", CourseThisTerm11, CoursePast11, CoursePastScore11,  Fanai.getId(), Grade.MS, 1397, EducationStatus.Studying, true, 14);


        students.add(student11);
        LinkedList<String> CourseThisTerm12 = new LinkedList();
        CourseThisTerm12.add(course1.getId());
        LinkedList<String> CoursePast12 = new LinkedList();
        CoursePast12.add(course1.getId());
        CoursePast12.add(course8.getId());
        LinkedList<Double> CoursePastScore12 = new LinkedList();
        CoursePastScore12.add(13.0);
        CoursePastScore12.add(10.0);
        Student student12 = new Student(7, Color.GREENYELLOW, null, "FirstName12", "Lastname12", "Email12", ("12").hashCode() + "", "1200 ", "12000", College.Mechanic, "12", CourseThisTerm12, CoursePast12, CoursePastScore12,  khazayee.getId(), Grade.MS, 1397, EducationStatus.Studying, false, -1);


        students.add(student12);
        LinkedList<String> CourseThisTerm13 = new LinkedList();
        CourseThisTerm13.add(course5.getId());
        LinkedList<String> CoursePast13 = new LinkedList();
        CoursePast13.add(course2.getId());
        CoursePast13.add(course9.getId());
        LinkedList<Double> CoursePastScore13 = new LinkedList();
        CoursePastScore13.add(13.0);
        CoursePastScore13.add(10.0);
        Student student13 = new Student(8, Color.GREENYELLOW, null, "FirstName13", "Lastname13", "Email13", ("13").hashCode() + "", "1300 ", "13000", College.Physics, "13", CourseThisTerm13, CoursePast13, CoursePastScore13,  Fanai.getId(), Grade.PHD, 1397, EducationStatus.Studying, false, -1);


        students.add(student13);
        LinkedList<String> CourseThisTerm14 = new LinkedList();
        CourseThisTerm14.add(course9.getId());
        LinkedList<String> CoursePast14 = new LinkedList();
        CoursePast14.add(course3.getId());
        CoursePast14.add(course9.getId());
        LinkedList<Double> CoursePastScore14 = new LinkedList();
        CoursePastScore14.add(15.0);
        CoursePastScore14.add(10.0);
        Student student14 = new Student(9, Color.GREENYELLOW, null, "FirstName14", "Lastname14", "Email14", ("14").hashCode() + "", "1400 ", "14000", College.CS, "14", CourseThisTerm14, CoursePast14, CoursePastScore14,  tefagh.getId(), Grade.PHD, 1396, EducationStatus.Studying, false, -1);


        students.add(student14);
        LinkedList<String> CourseThisTerm15 = new LinkedList();
        CourseThisTerm15.add(course1.getId());
        LinkedList<String> CoursePast15 = new LinkedList();
        CoursePast15.add(course1.getId());
        CoursePast15.add(course8.getId());
        LinkedList<Double> CoursePastScore15 = new LinkedList();
        CoursePastScore15.add(14.0);
        CoursePastScore15.add(10.0);
        Student student15 = new Student(10, Color.GREENYELLOW, null, "FirstName15", "Lastname15", "Email15", ("15").hashCode() + "", "1500 ", "15000", College.CS, "15", CourseThisTerm15, CoursePast15, CoursePastScore15,  tefagh.getId(), Grade.BC, 1397, EducationStatus.Studying, true, 14);


        students.add(student15);
        LinkedList<String> CourseThisTerm16 = new LinkedList();
        CourseThisTerm16.add(course2.getId());
        LinkedList<String> CoursePast16 = new LinkedList();
        CoursePast16.add(course2.getId());
        CoursePast16.add(course9.getId());
        LinkedList<Double> CoursePastScore16 = new LinkedList();
        CoursePastScore16.add(17.0);
        CoursePastScore16.add(10.0);
        Student student16 = new Student(11, Color.GREENYELLOW, null, "FirstName16", "Lastname16", "Email16", ("16").hashCode() + "", "1600 ", "16000", College.CS, "16", CourseThisTerm16, CoursePast16, CoursePastScore16,  khazayee.getId(), Grade.MS, 1397, EducationStatus.Studying, true, 14);


        students.add(student16);
        LinkedList<String> CourseThisTerm17 = new LinkedList();
        CourseThisTerm17.add(course5.getId());
        LinkedList<String> CoursePast17 = new LinkedList();
        CoursePast17.add(course2.getId());
        CoursePast17.add(course8.getId());
        LinkedList<Double> CoursePastScore17 = new LinkedList();
        CoursePastScore17.add(10.0);
        CoursePastScore17.add(10.0);
        Student student17 = new Student(12, Color.GREENYELLOW, null, "FirstName17", "Lastname17", "Email17", ("17").hashCode() + "", "1700 ", "17000", College.CS, "17", CourseThisTerm17, CoursePast17, CoursePastScore17,  khazayee.getId(), Grade.MS, 1397, EducationStatus.Studying, true, 14);


        students.add(student17);
        LinkedList<String> CourseThisTerm18 = new LinkedList();
        CourseThisTerm18.add(course2.getId());
        LinkedList<String> CoursePast18 = new LinkedList();
        CoursePast18.add(course1.getId());
        CoursePast18.add(course8.getId());
        LinkedList<Double> CoursePastScore18 = new LinkedList();
        CoursePastScore18.add(19.0);
        CoursePastScore18.add(10.0);
        Student student18 = new Student(13, Color.GREENYELLOW, null, "FirstName18", "Lastname18", "Email18", ("18").hashCode() + "", "1800 ", "18000", College.CS, "18", CourseThisTerm18, CoursePast18, CoursePastScore18,  khazayee.getId(), Grade.MS, 1397, EducationStatus.Studying, true, 14);


        students.add(student18);
        LinkedList<String> CourseThisTerm19 = new LinkedList();
        CourseThisTerm19.add(course7.getId());
        LinkedList<String> CoursePast19 = new LinkedList();
        CoursePast19.add(course4.getId());
        CoursePast19.add(course7.getId());
        LinkedList<Double> CoursePastScore19 = new LinkedList();
        CoursePastScore19.add(14.0);
        CoursePastScore19.add(10.0);
        Student student19 = new Student(14, Color.GREENYELLOW, null, "FirstName19", "Lastname19", "Email19", ("19").hashCode() + "", "1900 ", "19000", College.CS, "19", CourseThisTerm19, CoursePast19, CoursePastScore19,  khazayee.getId(), Grade.MS, 1397, EducationStatus.Studying, false, -1);


        students.add(student19);

        LinkedList<String> TAsForCourse1 = new LinkedList<>();
        TAsForCourse1.add(ali.getStudentNumber());
        TAsForCourse1.add(reza.getStudentNumber());
        TAsForCourse1.add(student10.getStudentNumber());
        course1.setTAsStudentNumber(TAsForCourse1);

        students.add(ali);
        students.add(reza);
        students.add(Phd);
        students.add(a);

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);
        courses.add(course5);
        courses.add(course6);
        courses.add(course7);
        courses.add(course8);
        courses.add(course9);
        courses.add(course10);
        courses.add(course11);
        courses.add(course12);
        courses.add(course13);
        courses.add(course14);
        courses.add(course15);
        courses.add(moharef1);
        courses.add(moharef2);

        mohseni = new Mohseni(1, Color.ORANGE, null, "Mohsen", "Mohseni", "a@.", "mohseni".hashCode() + "", "090279", "123456789", null, "mohsen");
        admin = new Admin(1, Color.CYAN, "1.png", "Admin", "Admin", "a@.", "1".hashCode() + "", "090279", "123456789", null, "1");

        student15.getCoursesIsTANow().add(course5.getId());

        for (Student student : students) {
            for (int j = 0; j < student.getCourses_thisTerm().size(); j++) {
                for (Course course : courses) {
                    if (course.getId().equals(student.getCourses_thisTerm().get(j))) {
                        course.getStudentsIdAreInClass().add(student.getId());
                    }
                }
            }
        }


        for (Teacher teacher : teachers) {
            for (int j = 0; j < teacher.getCourses().size(); j++) {
                for (Course course : courses) {
                    if (course.getId().equals(teacher.getCourses().get(j))) {
                        course.getTeachersId().add(teacher.getId());
                    }
                }
            }
        }


        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < students.get(i).getCoursesIsTANow().size(); j++) {
                for (int k = 0; k < courses.size(); k++) {
                    if(courses.get(k).getId().equals(students.get(i).getCoursesIsTANow().get(j)) ){
                        if(!courses.get(k).getTAsStudentNumber().contains(students.get(i).getStudentNumber())){
                            courses.get(k).getTAsStudentNumber().add(students.get(i).getStudentNumber());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < students.size(); i++) {
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + students.get(i).getStudentNumber());
            if(!file.exists()){
                file.mkdir();
            }
        }
        for (int i = 0; i < teachers.size(); i++) {
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teachers.get(i).getUsername());
            if(!file.exists()){
                file.mkdir();
            }
        }

        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + mohseni.getUsername());
        if(!file.exists()){
            file.mkdir();
        }

        file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + admin.getUsername());
        if(!file.exists()){
            file.mkdir();
        }

    }

    public static void saveToDataBase(){
        addToListsForFirstTime();
        dataBaseController.deleteAllRows();
        dataBaseController.addStudentsForFirstTime(students);
        dataBaseController.addTeachersForFirstTime(teachers);
        dataBaseController.addCoursesForFirstTime(courses);
        dataBaseController.addMohseniForFirstTime(mohseni);
        dataBaseController.addAdminForFirstTime(admin);
    }

    public static void main(String[] args) throws IOException {

        saveToDataBase();
        int port = Integer.parseInt(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.Port));
        serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(new NetworkThread(socket)).start();
        }
    }
}
