package API;
import java.io.File;
import java.util.LinkedList;

import back.course.Course;
import back.course.EducationalMaterials;
import back.course.Exam;
import back.course.HomeWork;
import back.enums.*;
import back.persons.Admin;
import back.persons.Mohseni;
import back.persons.Student;
import back.persons.Teacher;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import dataBase.DataBaseController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class API {

    static DataBaseController dataBaseController = new DataBaseController();


    public static final Logger log = LogManager.getLogger(API.class);

    public synchronized static void changeColorForStudent(String studentNumber, String color){
        dataBaseController.changeColorForStudent(studentNumber, color);

        log.info("change color for student with studentNumber " + studentNumber);
    }

    public synchronized static void changeColorForTeacher(String username, String color){
        dataBaseController.changeColorForTeacher(username, color);

        log.info("change color for teacher with username " + username);

    }

    public synchronized static void changeColorForMohseni(String username, String color){
        dataBaseController.changeColorForMohseni(username, color);

        log.info("change color for teacher with username " + username);

    }

    public synchronized static void changeColorForAdmin(String username, String color){
        dataBaseController.changeColorForAdmin(username, color);

        log.info("change color for teacher with username " + username);

    }

    public synchronized static Student getStudentLogin(String username, String password){
        log.info("Find is Student login  Func: getStudentLogin");
        return dataBaseController.getStudentLogin(username, password);
    }

    public synchronized static Teacher getTeacherLogin(String username, String password){

        log.info("Find is Teacher login  Func: getTeacherLogin");

        return dataBaseController.getTeacherLogin(username, password);
    }

    public synchronized static Mohseni getMohseniLogin(String username, String password){

        log.info("Find is Teacher login  Func: getTeacherLogin");

        return dataBaseController.getMohseniLogin(username, password);
    }

    public synchronized static Admin getAdminLogin(String username, String password){

        log.info("Find is Admin login  Func: getTeacherLogin");

        return dataBaseController.getAdminLogin(username, password);
    }

    public synchronized static String getLastNameHelpTeacherFromId(int idTeacher) {
        return dataBaseController.getLastNameHelpTeacher(idTeacher);
//        String ForReturn = null;
//        for (int i = 0; i < teachers.size(); i++) {
//            if(idTeacher == teachers.get(i).getId()){
//                ForReturn = teachers.get(i).getLastname();
//                break;
//            }
//        }
//
//        return ForReturn;
    }

    public synchronized static Teacher getLastNameHelpTeacherFromLastName(String chooseTeacher){
        return dataBaseController.getLastNameHelpTeacher(chooseTeacher);
//        Teacher teacher = null;
//        for (Teacher teacher1 : teachers) {
//            if(teacher1.getLastname().equals(chooseTeacher)){
//                teacher = teacher1;
//                break;
//            }
//        }
//        return teacher;
    }

    public synchronized static LinkedList<Student> getAllStudents() {
        return dataBaseController.getAllStudents();
//        return students;
    }

    public synchronized static LinkedList<Teacher> getAllTeachers() {
        return dataBaseController.getAllTeachers();
//        return teachers;
    }

    public synchronized static LinkedList<Course> getAllCourses() {
        return dataBaseController.getAllCourses();
//        return courses;
    }

    public synchronized static LinkedList<Course> ShowTemperoryScoresForEA(int teacherId, String CourseName, String LastNameTeacher){
        LinkedList<Course> ForReturn = new LinkedList<>();
        Teacher teacher = dataBaseController.FindTeacherFromId(teacherId);
        CopyLinkedListCourse_TemperoryScoresForEA(ForReturn, teacher);
        ShowCoursesWithFilterForNameCourse_TemperoryScoresForEA(ForReturn, CourseName);
        ForReturn = ShowCoursesWithFilterForTeacherName_TemperoryScoresForEA(ForReturn, LastNameTeacher, teacher.getCollege());
        return ForReturn;
    }
    public synchronized static void CopyLinkedListCourse_TemperoryScoresForEA(LinkedList<Course> ForReturn, Teacher teacher){
        LinkedList<Course> courses = API.getAllCourses();
        for (Course course : courses) {
            if(teacher.getCollege() == course.getCollege()){
                ForReturn.add(course);
            }
        }
    }
    public synchronized static void ShowCoursesWithFilterForNameCourse_TemperoryScoresForEA (LinkedList<Course> ForReturn, String s){
        if(s == null){

        }
        else if(s.equals("")){

        }
        else if(s.equals("null")){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(!s.equals(ForReturn.get(i).getName())){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }
    public synchronized static LinkedList<Course> ShowCoursesWithFilterForTeacherName_TemperoryScoresForEA (LinkedList<Course> ForReturn, String s, College college){
        LinkedList<Course> newLinkedList = new LinkedList<>();
        if(s == null){
            return ForReturn;
        }
        else if(s.equals("")){
            return ForReturn;
        }

        else{
            LinkedList<Teacher> teachers = getAllTeachers();
            int t = 0;
            for (Teacher teacher : teachers) {
                if (teacher.getCollege() != college) {
                    continue;
                }
                if (s.length() > teacher.getLastname().length()) {
                    continue;
                }
                for (t = 0; t < s.length(); t++) {
                    if (Character.toLowerCase(s.charAt(t)) != Character.toLowerCase(teacher.getLastname().charAt(t))) {
                        break;
                    }
                }
                if (t == s.length()) {
                    for (int j = 0; j < teacher.getCourses().size(); j++) {
                        for (int k = 0; k < ForReturn.size(); k++) {
                            if (ForReturn.get(k).getId().equals(teacher.getCourses().get(j))) {
                                newLinkedList.add(ForReturn.get(k));
                            }
                        }
                    }
                }
            }
        }

        return newLinkedList;
    }

    public synchronized static LinkedList<Student> ShowStudentsForMohseni(String studentNumber, String collegeName, String grade) {
        LinkedList<Student> students = getAllStudents();
        ShowStudentsForMohseniF_FilterStudentNumber(studentNumber, students);
        ShowStudentsForMohseniF_FilterCollege(dataBaseController.StringToCollege(collegeName), students);
        ShowStudentsForMohseniF_FilterGrade(dataBaseController.StringToGrade(grade), students);

        return students;
    }
    public synchronized static void ShowStudentsForMohseniF_FilterStudentNumber(String studentNumber, LinkedList<Student> students){
        if(studentNumber == null){
            return ;
        }
        else if(studentNumber.equals("")){
            return ;
        }



        for (int i = 0; i < students.size(); i++) {
            if(students.get(i).getStudentNumber().length() < studentNumber.length()){
                students.remove(i);
                i = -1 ;
                continue;
            }
            int counter = 0;
            for (int j = 0; j < studentNumber.length(); j++) {
                if(students.get(i).getStudentNumber().charAt(j) == studentNumber.charAt(j)){
                    counter++;
                }
                else{
                    break;
                }
            }

            if(counter != studentNumber.length()){
                students.remove(i);
                i = -1 ;
            }

        }

    }
    public synchronized static void ShowStudentsForMohseniF_FilterCollege(College college, LinkedList<Student> students){
        if(college == null){
            return ;
        }
        else if(college.equals("--")){
            return ;
        }

        for (int i = 0; i < students.size(); i++) {
            if(students.get(i).getCollege() != college){
                students.remove(i);
            }
        }

    }
    public synchronized static void ShowStudentsForMohseniF_FilterGrade(Grade grade, LinkedList<Student> students){
        if(grade == null){
            return ;
        }
        else if(grade.equals("--")){
            return ;
        }

        for (int i = 0; i < students.size(); i++) {
            if(students.get(i).getGrade() != grade){
                students.remove(i);
            }
        }

    }

    public static LinkedList<Course> ShowCourses(String nameCollege, int Weight , String grade, String courseId){
        Grade g = dataBaseController.StringToGrade(grade);
        LinkedList<Course> ForReturn = new LinkedList<>();
        CopyLinkedListCourse(ForReturn);

        ShowCoursesWithFilterForNameCollege(ForReturn, nameCollege);
        ShowCoursesWithFilterForWeight(ForReturn, Weight);
        ShowCoursesWithFilterForGrade(ForReturn, g);
        ShowCoursesWithFilterForId_Course(ForReturn, courseId);
        return ForReturn;
    }
    public static void CopyLinkedListCourse(LinkedList<Course> ForReturn){
        LinkedList<Course> courses = API.getAllCourses();
        ForReturn.addAll(courses);
    }
    public static void ShowCoursesWithFilterForNameCollege (LinkedList<Course> ForReturn, String nameCollege){
        if(nameCollege == null){

        }
        else if(nameCollege.equals("")){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(!nameCollege.equals(ForReturn.get(i).getCollege() + "")){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }
    public static void ShowCoursesWithFilterForWeight (LinkedList<Course> ForReturn, int Weight){
        if(Weight <= 0){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(ForReturn.get(i).getWeight() != Weight){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }
    public static void ShowCoursesWithFilterForGrade (LinkedList<Course> ForReturn, Grade grade){
        if(grade == null){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(ForReturn.get(i).getGrade() != grade){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }
    public static void ShowCoursesWithFilterForId_Course (LinkedList<Course> ForReturn, String id){
        if(id == null){

        }
        else if(id.equals("")){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(!ForReturn.get(i).getId().equals(id)){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }

    }


    public static LinkedList<Teacher> ShowTeachers(String LastName, String nameCollege, String teacherDegree){
        TeacherDegree td = dataBaseController.StringToTeacherDegree(teacherDegree);
        LinkedList<Teacher> ForReturn = new LinkedList<>();
        CopyLinkedListTeacher(ForReturn);

        ShowTeachersWithFilterForLastName(ForReturn, LastName);
        ShowTeachersWithFilterForNameCollege(ForReturn, nameCollege);
        ShowTeachersWithFilterForTeacherDegree(ForReturn, td);
        return ForReturn;
    }
    public static void CopyLinkedListTeacher(LinkedList<Teacher> ForReturn){
        LinkedList<Teacher> teachers = API.getAllTeachers();
        ForReturn.addAll(teachers);
    }
    public static void ShowTeachersWithFilterForLastName (LinkedList<Teacher> ForReturn, String LastName){
        if(LastName == null){

        }
        else if(LastName.equals("")){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(ForReturn.get(i).getLastname().indexOf(LastName) == -1){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }
    public static void ShowTeachersWithFilterForNameCollege (LinkedList<Teacher> ForReturn, String nameCollege){
        if(nameCollege == null){

        }
        else if(nameCollege.equals("")){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(!nameCollege.equals(ForReturn.get(i).getCollege() + "")){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }
    public static void ShowTeachersWithFilterForTeacherDegree (LinkedList<Teacher> ForReturn, TeacherDegree teacherDegree){
        if(teacherDegree == null){

        }
        else{
            int size = ForReturn.size();
            for (int i = 0; i < size; i++) {
                if(ForReturn.get(i).getTeacherDegree() != teacherDegree){
                    ForReturn.remove(i);
                    i = -1;
                    size = ForReturn.size();
                }
            }
        }
    }

    public synchronized static String FindTeacherLastNameFromId(int id){
        return dataBaseController.FindTeacherLastNameFromId(id);
    }
    public synchronized static String FindTeacherFirstNameFromId(int id){
        return dataBaseController.FindTeacherFirstNameFromId(id);
    }
    public synchronized static String FindTeacherCollegeFromId(int id){
        return dataBaseController.FindTeacherCollegeFromId(id);
    }

    public synchronized static Course FindACourseFormACollege(String courseName, String collegeName){
        return dataBaseController.FindCourseFromName(courseName, collegeName);
    }

    public synchronized static LinkedList<Course> GiveCoursesFromAStudentForCW(int studentId){
        return dataBaseController.GiveCoursesFromAStudentForCW(studentId);
    }

    public synchronized static LinkedList<Course> GiveCoursesFromATeacher(int teacherId){
        return dataBaseController.GiveCoursesFromATeacher(teacherId);
    }

    public synchronized static String returnAllHelpTeachersFromACollege(String collegeName){
        College college = dataBaseController.StringToCollege(collegeName);
        return dataBaseController.returnAllHelpTeachersFromACollege(college + "");

    }

    public synchronized static boolean CanSignUpForDuplicateUsername(String username){
        return dataBaseController.CanSignUpForDuplicateUsername(username);

    }

    public synchronized static boolean validDuplicatePublicIdForCourse(int publicId, int groupId){
        return dataBaseController.validDuplicatePublicIdForCourse(publicId, groupId);

    }

    public synchronized static void addStudent(Student student){
        dataBaseController.addNewStudent(student);

        log.info("Create new student");

    }

    public synchronized static void addTeacher (Teacher teacher){
        dataBaseController.addNewTeacher(teacher);

        log.info("Create new teacher");

    }

    public synchronized static void addCourse (Course course){
        dataBaseController.addNewCourse(course);

        log.info("Create new course");

    }

    public synchronized static Teacher FindTeacher(int id){
        return dataBaseController.FindTeacherFromId(id);
    }

    public synchronized static boolean isValidTeacher(int id){
        if(dataBaseController.FindTeacherFromId(id) == null){
            return false;
        }
        return true;
    }

    public synchronized static Course FindCourse(String courseId){
        return dataBaseController.FindCourseFromId(courseId);
    }

    public synchronized static void AddNewCourseToTeacher(String courseId, String teacherName, String collegeName){
        dataBaseController.AddNewCourseToTeacher(courseId, teacherName, collegeName);
    }

    public synchronized static LinkedList<Teacher> StringToTeacher(String s){
        if(s == null){
            return getAllTeachers();
        }
        else if(s.equals("")){
            return getAllTeachers();
        }
        LinkedList<Teacher> t  = new LinkedList<>();
        int i = 0;

        LinkedList<Teacher> teachers = API.getAllTeachers();
        for (Teacher teacher1 : teachers) {
            if(s.length() > teacher1.getLastname().length()){
                continue;
            }
            for (i = 0; i < s.length(); i++) {
                if(Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(teacher1.getLastname().charAt(i))){
                    break;
                }
            }

            if(i == s.length()){
                t.add(teacher1);
            }
        }

        return t;
    }

    public synchronized static LinkedList<Student> SearchStudentByStudentNumberAndName(int teacherId, String Name, String studentNumber){
        Teacher teacher = dataBaseController.FindTeacherFromId(teacherId);
        LinkedList<Student> ForReturn = new LinkedList<>();
        CopyLinkedListStudent(ForReturn, teacher);
        StringToStudent(ForReturn, Name);
        StringToStudentForStudentNumber(ForReturn, studentNumber);

        return ForReturn;
    }
    public synchronized static void CopyLinkedListStudent(LinkedList<Student> ForReturn, Teacher teacher){
        LinkedList<Student> students = API.getAllStudents();
        for (Student student : students) {
            if(student.getCollege() == teacher.getCollege()){
                ForReturn.add(student);
            }
        }
    }
    public synchronized static void StringToStudent(LinkedList<Student> ForReturn, String Name){
        if(Name == null){
            return;
        }
        else if(Name.equals("")){
            return;
        }
        int i = 0;
        for (int j = 0; j < ForReturn.size(); j++) {
            if(Name.length() > ForReturn.get(j).getLastname().length()){
                ForReturn.remove(j);
                continue;
            }
            for (i = 0; i < Name.length(); i++) {
                if(Character.toLowerCase(Name.charAt(i)) != Character.toLowerCase(ForReturn.get(j).getLastname().charAt(i))){
                    break;
                }
            }

            if(i != Name.length()){
                ForReturn.remove(j);
                j = -1;
            }
        }
    }
    public synchronized static void StringToStudentForStudentNumber(LinkedList<Student> ForReturn, String studentNumber){
        if(studentNumber == null){
            return;
        }
        else if(studentNumber.equals("")){
            return;
        }
        int i = 0;
        for (int j = 0; j < ForReturn.size(); j++) {
            if(studentNumber.length() > ForReturn.get(j).getLastname().length()){
                ForReturn.remove(j);
                continue;
            }
            for (i = 0; i < studentNumber.length(); i++) {
                if(Character.toLowerCase(studentNumber.charAt(i)) != Character.toLowerCase(ForReturn.get(j).getStudentNumber().charAt(i))){
                    break;
                }
            }

            if(i != studentNumber.length()){
                ForReturn.remove(j);
                j = -1;
            }
        }
    }

    public synchronized static String getTeacherOfACourse(String courseId) {
        LinkedList<Integer> teachersId = dataBaseController.getTeachersIdForACourse(courseId);
        String forReturn = "";
        for (int i = 0; i < teachersId.size(); i++) {
            forReturn = forReturn + FindTeacherLastNameFromId(teachersId.get(i));
            break;
        }

        return forReturn;
    }

    public synchronized static LinkedList<Course> GiveCoursesOfATeacher(LinkedList<String> ids){
        LinkedList<Course> ForReturn = new LinkedList<>();
        LinkedList<Course> courses = API.getAllCourses();

        for (int i = 0; i < ids.size(); i++) {
            for (Course c : courses) {
                if(c.getId().equals(ids.get(i))){
                    ForReturn.add(c);
                    break;
                }
            }
        }

        return ForReturn;

    }

    public synchronized static LinkedList<Course> GiveCoursesOfAStudent(LinkedList<String> ids){
        LinkedList<Course> ForReturn = new LinkedList<>();
        LinkedList<Course> courses = API.getAllCourses();

        for (int i = 0; i < ids.size(); i++) {
            for (Course c : courses) {
                if(c.getId().equals(ids.get(i))){
                    ForReturn.add(c);
                    break;
                }
            }
        }

        return ForReturn;

    }

    public synchronized static LinkedList<Course> GiveCoursesOfACollege(String collegeName){
        College college = dataBaseController.StringToCollege(collegeName);
        LinkedList<Course> ForReturn = new LinkedList<>();
        LinkedList<Course> courses = API.getAllCourses();
        LinkedList<Student> students = API.getAllStudents();

        for (Course c : courses) {
            if(c.getCollege() != college){
                continue;
            }
            inner:
            for (int i = 0; i < students.size(); i++) {
                for (int j = 0; j < students.get(i).getCourses_thisTerm().size(); j++) {
                    if(students.get(i).getCourses_thisTerm().get(j).equals(c.getId())){
                        ForReturn.add(c);
                        break inner;
                    }
                }
            }
        }

        return ForReturn;

    }

    public synchronized static LinkedList<Student> GiveStudentsOfACoursesFromString(String courseId){

        LinkedList<Student> ForReturn = new LinkedList<>();
        LinkedList<Integer> studentsId = dataBaseController.getStudentsIdForACourse(courseId);

        for (int i = 0; i < studentsId.size(); i++) {
            ForReturn.add(FindStudentFromId(studentsId.get(i)));
        }

        return ForReturn;

    }

    public synchronized static Student FindStudentFromId(int id){
        return dataBaseController.FindStudentFromId(id);
    }

    public synchronized static int FindIndexCourseFormAStudentFromCourseName(int studentId, String courseId){
        Student student = dataBaseController.FindStudentFromId(studentId);

        for (int i = 0; i < student.getCourses_thisTerm().size(); i++) {
            if(student.getCourses_thisTerm().get(i).equals(courseId)){
                return i;
            }
        }

        return -1;

    }

    public synchronized static void RemoveCourseFromATeachersAndStudents(String courseName, Teacher teacher){
        LinkedList<Student> students = API.getAllStudents();
        Course course = null;
        for (int i = 0; i < teacher.getCourses().size(); i++) {
            course = FindCourse(teacher.getCourses().get(i));
            if(course.getName().equals(courseName)){
                break;
            }
        }
        if(course == null){
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < students.get(i).getCourses_thisTerm().size(); j++) {
                if(students.get(i).getCourses_thisTerm().get(j).equals(course.getId())){
                    students.get(i).getCourses_Past().add(course.getId());
                    students.get(i).getCourses_Past_Score().add(students.get(i).getCourses_thisTerm_Score().get(j));

                    students.get(i).getCourses_thisTerm().remove(j);
                    students.get(i).getCourses_thisTerm_Score().remove(j);
                    students.get(i).getAnswerTeacher().remove(j);
                    students.get(i).getProtestForCourseStudent().remove(j);

                    callDataBaseSaveForRemoveCourseFromStudents(students.get(i));

                    i = -1;
                    students = API.getAllStudents();
                    break;
                }
            }
        }

        dataBaseController.removeCoursesFromTAS(course.getId(), course.getTAsStudentNumber());
        dataBaseController.refactorCourse(course.getId());

    }
    private synchronized static void callDataBaseSaveForRemoveCourseFromStudents(Student student){
        String coursesThisTerm = dataBaseController.passStringFormGenericTypes(student.getCourses_thisTerm());

        String coursesThisTermScore = dataBaseController.passStringFormGenericTypes(student.getCourses_thisTerm_Score());
        String getAnswerTeacher = dataBaseController.passStringFormGenericTypes(student.getAnswerTeacher());
        String getProtestForCourseStudent = dataBaseController.passStringFormGenericTypes(student.getProtestForCourseStudent());

        String coursesPast = dataBaseController.passStringFormGenericTypes(student.getCourses_Past_Score());


        String coursesPastScore = dataBaseController.passStringFormGenericTypes(student.getCourses_Past_Score());

        dataBaseController.UpdateCoursesForAStudent(student.getId(), coursesThisTerm, coursesThisTermScore, coursesPast, coursesPastScore, getProtestForCourseStudent, getAnswerTeacher);

    }
    private synchronized static void callDataBaseSaveForRemoveCourseFromTeacher(Teacher teacher){
        String courses = dataBaseController.passStringFormGenericTypes(teacher.getCourses());


        dataBaseController.UpdateCoursesForATeacher(teacher.getId(), courses);

    }

    public synchronized static LinkedList<String> ShowableCoursesOfStudentTemporaryScores(int studentId){
        Student student = dataBaseController.FindStudentFromId(studentId);
        LinkedList<String> ForReturn = new LinkedList<>();

        for (int i = 0; i < student.getCourses_thisTerm().size(); i++) {
            LinkedList<Student> s = GiveStudentsOfACoursesFromId(student.getCourses_thisTerm().get(i));
            if(s.size() == 0){
                continue;
            }
            int counter = 0;
            for (Student st : s) {
                int index_score = FindIndexCourseFormAStudentFromId(st.getId(), student.getCourses_thisTerm().get(i));
                if(index_score == -1){
                    break;
                }
                if(st.getCourses_thisTerm_Score().get(index_score) == -1){
                    break;
                }

                counter++;
            }
            if(counter == s.size()){
                ForReturn.add(student.getCourses_thisTerm().get(i));
            }
        }

        return ForReturn;

    }
    public synchronized static LinkedList<Student> GiveStudentsOfACoursesFromId(String id){

        LinkedList<Student> ForReturn = new LinkedList<>();
        Course course = dataBaseController.FindCourseFromId(id);
        LinkedList<Student> students = getAllStudents();
        if(course == null){
            return ForReturn;
        }
        for (Student student : students) {
            for (int i = 0; i < student.getCourses_thisTerm().size(); i++) {
                if(student.getCourses_thisTerm().get(i).equals(course.getId())){
                    ForReturn.add(student);
                }
            }
        }

        return ForReturn;

    }

    public synchronized static int FindIndexCourseFormAStudentFromId(int studentId, String id){
        Student student = dataBaseController.FindStudentFromId(studentId);
        for (int i = 0; i < student.getCourses_thisTerm().size(); i++) {
            if(student.getCourses_thisTerm().get(i).equals(id)){
                return i;
            }
        }

        return -1;

    }

    public synchronized static double GetScore(String courseId, int studentId){
        Student student = dataBaseController.FindStudentFromId(studentId);
        for (int i = 0; i < student.getCourses_thisTerm().size(); i++) {
            if(student.getCourses_thisTerm().get(i).equals(courseId)){
                return student.getCourses_thisTerm_Score().get(i);
            }
        }

        return 0;
    }

    public synchronized static LinkedList<Student> FindStudentsWantsToReject(String collegeName){
        return dataBaseController.FindStudentsWantsToReject(collegeName);

    }

    public synchronized static LinkedList<Student> FindStudentsWantsToMinor(String collegeName){
        return dataBaseController.FindStudentsWantsToMinor(collegeName);
    }

    public synchronized static void RemoveStudent(int studentId){
        dataBaseController.RemoveStudent(studentId);

    }

    public synchronized static LinkedList<Course> ShowPastCoursesForAStudent(int studentId){
        Student student = dataBaseController.FindStudentFromId(studentId);
        LinkedList<Course> cs = new LinkedList<>();
        LinkedList<Course> courses = API.getAllCourses();
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                if(courses.get(i).getId().equals(courses.get(j).getId())){
                    courses.remove(i);
                    i = -1;
                    break;
                }
            }
        }

        for (int i = 0; i < student.getCourses_Past().size(); i++) {
            for (Course course : courses) {
                if (course.getId().equals(student.getCourses_Past().get(i))) {
                    cs.add(course);
                }
            }
        }

        return cs;
    }

    public synchronized static LinkedList<Student> ShowStudentsForATeacherEA(String collegeName){
        return dataBaseController.ShowStudentsForATeacherEA(collegeName);
    }

    public synchronized static String getStatusACourseFromAStudent(int studentId, String courseId){

        Student student = dataBaseController.FindStudentFromId(studentId);

        for (int i = 0; i < student.getCourses_Past().size(); i++) {
            if(courseId.equals(student.getCourses_Past().get(i))){
                if(student.getCourses_Past_Score().get(i) >= 10.0){
                    return "Pass";
                }
                else{
                    return "Fail";
                }
            }
        }

        return "------";
    }

    public synchronized static double getScoreACourseFromAStudent(int studentId, String courseId){
        Student student = dataBaseController.FindStudentFromId(studentId);
        for (int i = 0; i < student.getCourses_Past().size(); i++)
        {
            if(courseId.equals(student.getCourses_Past().get(i))){
                return student.getCourses_Past_Score().get(i);
            }
        }

        return -1.0 ;
    }

    public synchronized static String getPassesCourseFromAStudent(int studentId){
        Student student = dataBaseController.FindStudentFromId(studentId);

        String s = "------------";
        LinkedList<Course> courses = new LinkedList<>();
        for (int i = 0; i < courses.size(); i++) {
            for (int j = 0; j <  student.getCourses_Past().size(); j++) {
                if(courses.get(i).getId().equals(student.getCourses_Past().get(j))){
                    if(s.equals("------------")){
                        s = courses.get(i).getName() + " (" + courses.get(i).getCollege() + ")";

                    }
                    else{
                        s = s + " - " + courses.get(i).getName() + " (" + courses.get(i).getCollege() + ")";
                    }
                }
            }
        }

        return s;
    }

    public synchronized static LinkedList<Student> FindStudentsWantsToRecome(int teacherId){
        Teacher teacher = dataBaseController.FindTeacherFromId(teacherId);

        LinkedList<Student> ForReturn = new LinkedList<>();
        LinkedList<Student> students = API.getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < students.get(i).getIdTeacherForRequest().size(); j++) {
                if(teacher.getId() == students.get(i).getIdTeacherForRequest().get(j) & students.get(i).getRequestForRecommendation().get(j) == Request.Wait){
                    ForReturn.add(students.get(i));
                }
            }
        }

        return ForReturn;
    }

    public synchronized static int getIndexRequest(int studentId, int teacherId){
        Student student = dataBaseController.FindStudentFromId(studentId);

        for (int j = 0; j < student.getIdTeacherForRequest().size(); j++) {
            if(teacherId == student.getIdTeacherForRequest().get(j)){
                return j;
            }
        }

        return -1;
    }

    public synchronized static int getWeighOfCoursesPass(int studentId){
        Student student = dataBaseController.FindStudentFromId(studentId);

        int num = 0;
        for (int i = 0; i < student.getCourses_Past_Score().size(); i++) {
            if(student.getCourses_Past_Score().get(i) >= 10.0){
                num += FindCourse(student.getCourses_Past().get(i)).getWeight();
            }
        }

        return num;
    }

    public synchronized static LinkedList<String>  getNumPassForACourse(String courseId){
        Course course = dataBaseController.FindCourseFromId(courseId);
        LinkedList<String> ForReturn = new LinkedList<>();
        int numPass = 0;
        int numFail = 0;
        int numAll = 0;
        int averageScore = 0;
        LinkedList<Student> students = API.getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < students.get(i).getCourses_thisTerm().size(); j++) {
                if(students.get(i).getCourses_thisTerm().get(j).equals(course.getId())){
                    if(students.get(i).getCourses_thisTerm_Score().get(j) >= 10){
                        numPass++;
                        averageScore += students.get(i).getCourses_thisTerm_Score().get(j);
                    }
                    else if (students.get(i).getCourses_thisTerm_Score().get(j) >= 0 & students.get(i).getCourses_thisTerm_Score().get(j) < 10){
                        numFail++;
                    }
                    numAll++;
                }
            }
        }
        if (numPass == 0){

        }
        else{
            averageScore /= numPass;
        }
        ForReturn.add(numAll + "");
        ForReturn.add(numPass + "");
        ForReturn.add(numFail + "");
        ForReturn.add(averageScore + "");
        return ForReturn;
    }

    public synchronized static void  AddAndRemoveACourseFormTeachers(String id, int teacherId){
        dataBaseController.AddORRemoveACourseFromTeachers(id, teacherId);
    }

    public synchronized static void  RemoveACourseFormTeachers(String id){
        dataBaseController.RemoveACourseFromTeachers(id);
    }

    public synchronized static void changePasswordForAStudent(String username, String newPassword){
        dataBaseController.changePasswordForAStudent(username, newPassword);
    }
    public synchronized static void changePasswordAForTeacher(String username, String newPassword){
        dataBaseController.changePasswordAForTeacher(username, newPassword);
    }

    public synchronized static void changeLastLoginStudent(int id, int second, int minute, int hour, int day, int month, int year){
        dataBaseController.changeLastLoginStudent(id, second, minute, hour, day, month, year);
    }

    public synchronized static void changeLastLoginTeacher(int id, int second, int minute, int hour, int day, int month, int year){
        dataBaseController.changeLastLoginTeacher(id, second, minute, hour, day, month, year);
    }

    public synchronized static void changeEducationStatusOfAStudent(int id, String educationStatusString){
        EducationStatus educationStatus = dataBaseController.StringToEducationStatus(educationStatusString);
        dataBaseController.changeEducationStatusOfAStudent(id ,educationStatus);
    }

    public synchronized static void changeProtestForAStudent(int id, int index, String protest){
        dataBaseController.changeProtestForAStudent(id, index, protest);
    }

    public synchronized static void editEmailForAStudent(int id, String email){
        dataBaseController.editEmailForAStudent(id, email);
    }

    public synchronized static void editPhoneForAStudent(int id, String phoneNumber){
        dataBaseController.editPhoneForAStudent(id, phoneNumber);
    }

    public synchronized static void setImageAddressOfAStudent(int id, String image){
        dataBaseController.setImageAddressOfAStudent(id, image);
    }

    public synchronized static void addIdTeacherToStudentForRecome(int studentId, int teacherId){
        dataBaseController.addIdTeacherToStudentForRecome(studentId, teacherId);
    }

    public synchronized static void changeRecomeForStudentBecauseShowed(int studentId, int indexShouldRemove){
        dataBaseController.changeRecomeForStudentBecauseShowed(studentId, indexShouldRemove);
    }

    public synchronized static void changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted(int studentId, int indexShouldRemove, int idTeacherShouldAddToAccepted){
        dataBaseController.changeRecomeForStudentBecauseShowed(studentId, indexShouldRemove, idTeacherShouldAddToAccepted);

    }

    public synchronized static void setMinorCollegeForAStudent(int id, String collegeName){
        dataBaseController.setMinorCollegeForAStudent(id, collegeName);
    }

    public synchronized static void setDormRequestForAStudent(int id, String dorm){
        dataBaseController.setDormRequestForAStudent(id, dorm);
    }

    public synchronized static void changeMinorForStudentBecauseShowedAndRejected(int studentId){
        dataBaseController.changeMinorForStudentBecauseShowedAndRejected(studentId);
    }

    public synchronized static void submitRecomeForAStudent(int studentId, int index, String request){
        dataBaseController.acceptRecomeForAStudent(studentId, index, request);
    }

    public synchronized static void setRoomNumberForATeacher(int teacherId, int roomNumber){
        dataBaseController.setRoomNumberForATeacher(teacherId, roomNumber);
    }

    public synchronized static void removeATeacher(int teacherId){
        dataBaseController.removeATeacher(teacherId);
    }

    public synchronized static void removeACourse(String courseId){
        dataBaseController.removeACourse(courseId);
    }

    public synchronized static void changeStatusTeacher(int teacherId, String statusTeacher){
        dataBaseController.changeStatusTeacher(teacherId, statusTeacher);
    }

    public synchronized static void editEmailForATeacher(int id, String email){
        dataBaseController.editEmailForATeacher(id, email);
    }

    public synchronized static void editPhoneForATeacher(int id, String phoneNumber){
        dataBaseController.editPhoneForATeacher(id, phoneNumber);
    }

    public synchronized static void setImageAddressOfATeacher(int id, String image){
        dataBaseController.setImageAddressOfATeacher(id, image);
    }

    public synchronized static void setAnswerAndScoreForStudent(int id, int indexCourse,  double score, String answer){
        dataBaseController.setAnswerAndScoreForStudent(id, indexCourse, score, answer);
    }

    public synchronized static void changeMinorStatusFromEA(boolean isFirstMinorStatus, int studentId, String status){
        dataBaseController.changeMinorStatusFromEA(isFirstMinorStatus, studentId, status);
    }

    public synchronized static void setTimesLoginForAStudent(int studentId, int timesLogin){
        dataBaseController.setTimesLoginForAStudent(studentId, timesLogin);
    }

    public synchronized static void setTimesLoginForATeacher(int teacherId, int timesLogin){
        dataBaseController.setTimesLoginForATeacher(teacherId, timesLogin);
    }


    public synchronized static String getProtestForCourseStudent(int studentId, int index){
        return dataBaseController.getProtestForCourseStudent(studentId, index);
    }

    public synchronized static String getAnswerForCourseStudent(int studentId, int index){
        return dataBaseController.getAnswerForCourseStudent(studentId, index);
    }

    public synchronized static double getScoreForCourseStudent(int studentId, int index){
        return dataBaseController.getScoreForCourseStudent(studentId, index);
    }

    public synchronized static String getMinorStatus(int studentId, boolean isFirst){
        return dataBaseController.getMinorStatus(studentId, isFirst);
    }

    public synchronized static String getRequestForRecommendation(int studentId){
        return dataBaseController.getRequestForRecommendation(studentId);
    }

    public synchronized static int getIdTeacherForRequest(int studentId, int index){
        return dataBaseController.getIdTeacherForRequest(studentId, index);
    }

    public synchronized static String getDormStatus(int studentId){
        return dataBaseController.getDormStatus(studentId);
    }

    public synchronized static String getThesisDefenceFromAStudent(int studentId){
        return dataBaseController.getThesisDefenceFromAStudent(studentId);
    }

    public synchronized static boolean validStudentNumberForTA(String studentNumber){
        return dataBaseController.validStudentNumberForTA(studentNumber);
    }

    public synchronized static boolean validPublicIdForCourse(int publicId){
        return dataBaseController.validPublicIdForCourse(publicId);
    }

    public synchronized static String getFileNameOfFromUsername(String usernameTo){
        return dataBaseController.getFileNameOfFromName(usernameTo);
    }

    public synchronized static String getFileNameOfFromStudentNumber(String studentNumber){
        return dataBaseController.getFileNameOfFromStudentNumber(studentNumber);
    }



    public synchronized static void saveMessage(String usernameFrom, String userNameTo, String fileAddress, String typeFile){
        dataBaseController.saveMessage(usernameFrom, userNameTo, fileAddress, typeFile);
    }

    public synchronized static LinkedList<LinkedList<String>> getYearsToUniversityForEA(String college){
        return dataBaseController.getYearsToUniversityForEA(college);
    }

    public synchronized static void saveTimeForSelectionUnit(String college, int yearComeToUniversity, String time){
        dataBaseController.saveTimeForSelectionUnit(college, yearComeToUniversity, time);
    }

    public synchronized static void setExamDayForACourse(String courseId, int dayExam, int monthExam, int yearExam){
        dataBaseController.setExamDayForACourse(courseId, dayExam, monthExam, yearExam);
    }

    public synchronized static LinkedList<Course> getAllCoursesFromACollege(String college, String grade){
        return dataBaseController.getAllCoursesFromACollege(college, grade);
    }

    public synchronized static LinkedList<Course> getAllCoursesFromACollegeForSelectUnit(String college, String grade){
        return dataBaseController.getAllCoursesFromACollegeForSelectUnit(college, grade);
    }


    public synchronized static LinkedList<Course> getLikedCourses(int studentId){
        return dataBaseController.getLikedCourses(studentId);
    }

    public synchronized static LinkedList<Course> getSuggestedCourses(int studentId, String college, String grade){
        return dataBaseController.getSuggestedCourses(studentId, college, grade);
    }

    public synchronized static LinkedList<Boolean> getStatusOfACourseForUnitSelection(int studentId, String courseId){
        return dataBaseController.getStatusOfACourseForUnitSelection(studentId, courseId);
    }

    public synchronized static void addACourseToLikedCourses(int studentId, String courseId){
        dataBaseController.addACourseToLikedCourses(studentId, courseId);
    }

    public synchronized static void removeACourseFromLikedCourses(int studentId, String courseId){
        dataBaseController.removeACourseFromLikedCourses(studentId, courseId);
    }

    public synchronized static boolean addACourseFromCoursesWantToGetThisTerm(int studentId, String courseId){
        return dataBaseController.addACourseFromCoursesWantToGetThisTerm(studentId, courseId);
    }

    public synchronized static void changeGroupForSelectionUnit(String studentNumber, String college, int studentId, String courseId){
        dataBaseController.changeGroupForSelectionUnit(studentNumber, college, studentId, courseId);
    }

    public synchronized static void removeACourseFromCoursesWantToGetThisTerm(int studentId, String courseId){
        dataBaseController.removeACourseFromCoursesWantToGetThisTerm(studentId, courseId);
    }

    public synchronized static LinkedList<String> getAllStudentsFromOneCollegeAndFromOneYear(String college, int yearComeToUniversity, int studentId, int teacherId){
        return dataBaseController.getAllStudentsFromOneCollegeAndFromOneYear(college, yearComeToUniversity, studentId, teacherId);
    }

    public synchronized static LinkedList<LinkedList<String>> getAllMessagesFromAPerson(String username){
        return dataBaseController.getAllMessagesFromAStudent(username);
    }

    public synchronized static LinkedList<LinkedList<String>> getAllMessagesFromTwoPersons(String usernamePerson, String usernameFront){
        return dataBaseController.getAllMessagesFromAPerson(usernamePerson, usernameFront);
    }

    public synchronized static String getUserNameFromName(String nameFront, boolean isFrontStudent){
        return dataBaseController.getUserNameFromName(nameFront, isFrontStudent);
    }

    public synchronized static LinkedList<EducationalMaterials> getEducationalMaterialsFromACourse(String courseId){
        return dataBaseController.getEducationalMaterialsFromACourse(courseId);
    }

    public synchronized static LinkedList<Exam> getExamsFromACourse(String courseId){
        return dataBaseController.getExamsFromACourse(courseId);
    }

    public synchronized static LinkedList<HomeWork> getHomeWorksFromACourse(String courseId){
        return dataBaseController.getHomeWorksFromACourse(courseId);
    }

    public synchronized static void changeAnswerForHomeWorkOrExamForAStudent(String courseId, String exams, String homeWorks, boolean isExam){
        dataBaseController.changeAnswerForHomeWorkOrExamForAStudent(courseId, exams, homeWorks, isExam);
    }

    public synchronized static void removeAFileFromServerForCw(String address){
        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURLForServer) + address);
        if(file.exists()){
            file.delete();
        }

    }

    public synchronized static LinkedList<String> getMessagesForAStudent(int studentId){
        return dataBaseController.getMessagesForAStudent(studentId);
    }

    public synchronized static LinkedList<String> getMessagesForATeacher(int teacherId){
        return dataBaseController.getMessagesForATeacher(teacherId);
    }

    public synchronized static void removeAMessageFromAStudent(int studentId, String messageString, boolean isAcceptAndReject, boolean isAcceptedIfPreviousIsTrue){
        dataBaseController.removeAMessageFromAStudent(studentId, messageString, isAcceptAndReject, isAcceptedIfPreviousIsTrue);
    }

    public synchronized static void removeAMessageFromATeacherForMessages(int teacherId, String messageString, boolean isAccept){
        dataBaseController.removeAMessageFromATeacherForMessages(teacherId, messageString, isAccept);
    }

    public synchronized static boolean sendMessageToEAToGetACourse(String studentNumber, String college, String courseId){
        return dataBaseController.sendMessageToEAToGetACourse(studentNumber, college, courseId);
    }

    public synchronized static String getTimeSelectUnitForAStudent(int studentId){
        return dataBaseController.getTimeSelectUnitForAStudent(studentId);
    }

    public synchronized static LinkedList<String> FindATeacherOrStudentForChatRoom(String myUserName, String type, String username){
        return dataBaseController.FindATeacherOrStudentForChatRoom(myUserName, type, username);
    }

    public synchronized static boolean sendMessageToAPersonForRequestChatRoom(String type, String username, String myUsername){
        return dataBaseController.sendMessageToAPersonForRequestChatRoom(type, username, myUsername);
    }

    public synchronized static boolean addStudentToCourseInCW(String type, String username, String courseId){
        return dataBaseController.addStudentToCourseInCW(type, username, courseId);
    }

    public synchronized static boolean isAStudentTAOfACourse( int studentId, String courseId){
        return dataBaseController.isAStudentTAOfACourse(studentId, courseId);
    }

    public synchronized static void addNewFileInCwForExamOrHw(String courseId, boolean isExam, String s){
        dataBaseController.addNewFileInCwForExamOrHw(courseId, isExam, s);
    }

    public synchronized static String getNameOfAStudentFromId(int studentId){
        return dataBaseController.getNameOfAStudentFromId(studentId);
    }

    public synchronized static String getStudentsNumberOfAStudentFromId(int studentId){
        return dataBaseController.getStudentsNumberOfAStudentFromId(studentId);
    }

    public synchronized static void saveExamsOrHWsForCw(String courseId, String s, boolean isExam){
        dataBaseController.saveExamsOrHWsForCw(courseId, s, isExam);
    }

    public synchronized static void changeEducationalMaterialForACourse(String courseId, String s){
        dataBaseController.changeEducationalMaterialForACourse(courseId, s);
    }

    public static LinkedList<Teacher> getAllTeacherFormACollege(int teacherId, String college){
        return dataBaseController.getAllTeacherFormACollege(teacherId, college);
    }


    public static LinkedList<Course> getCoursesOfATeacher(int teacherId){
        return dataBaseController.getCoursesOfATeacher(teacherId);
    }

    public static LinkedList<Integer> getCoursesGroup (int publicId){
        return dataBaseController.getCoursesGroup(publicId);
    }

    public static LinkedList<String> getStudentsWithFilterForMohseni(String studentNumber){
        LinkedList<Student> students = API.getAllStudents();
        LinkedList<String> forReturn = new LinkedList<>();
        if(studentNumber == null){
            for (int i = 0; i < students.size(); i++) {
                forReturn.add(students.get(i).getFirstname() + " " + students.get(i).getLastname() + "... ... ..." + students.get(i).getStudentNumber());
            }

            return forReturn;
        }
        else if(studentNumber.equals("")){
            for (int i = 0; i < students.size(); i++) {
                forReturn.add(students.get(i).getFirstname() + " " + students.get(i).getLastname() + "... ... ..." + students.get(i).getStudentNumber());
            }

            return forReturn;
        }



        for (int i = 0; i < students.size(); i++) {
            if(students.get(i).getStudentNumber().length() < studentNumber.length()){
                continue;
            }
            int counter = 0;
            for (int j = 0; j < studentNumber.length(); j++) {
                if(students.get(i).getStudentNumber().charAt(j) == studentNumber.charAt(j)){
                    counter++;
                }
                else{
                    break;
                }
            }

            if(counter == studentNumber.length()){
                forReturn.add(students.get(i).getFirstname() + " " + students.get(i).getLastname() + "... ... ..." + students.get(i).getStudentNumber());
            }

        }

        return forReturn;
    }

    public static Student findStudentFromStudentNumber(String studentNumber){
        return dataBaseController.findStudentFromStudentNumber(studentNumber);
    }

    public static LinkedList<LinkedList<String>> getAllStudentForATeacherInChatRoom(int teacherId, String username){
        return dataBaseController.getAllStudentForATeacherInChatRoom(teacherId, username);
    }

    public static LinkedList<LinkedList<String>> getAllStudentForABossAndEAInChatRoom(int teacherId, String username, String college){
        return dataBaseController.getAllStudentForABossAndEAInChatRoom(teacherId, username, college);
    }

    public static boolean saveMessagesBecauseNotConnected(String username, LinkedList<String> toPersonsUsername, LinkedList<String> AddressOrText, LinkedList<String> Time, LinkedList<String> TypeFile){
        return dataBaseController.saveMessagesBecauseNotConnected(username, toPersonsUsername, AddressOrText, Time, TypeFile);
    }

    public static LinkedList<LinkedList<String>> getChatRoomsInfo(String username){
        return dataBaseController.getChatRoomsInfo(username);
    }

    public static void updateStudent(Student student){
        dataBaseController.updateStudent(student);
    }

    public static void updateTeacher(Teacher teacher){
        dataBaseController.updateTeacher(teacher);
    }

    public static void updateMohseni(Mohseni mohseni){
        dataBaseController.updateMohseni(mohseni);
    }

    public static void updateAdmin(Admin admin){
        dataBaseController.updateAdmin(admin);
    }

    public static void finishTimeSelection(String year, String college){
        dataBaseController.finishTimeSelection(year, college);
    }


}

