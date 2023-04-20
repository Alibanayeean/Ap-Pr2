package back.course;

import back.enums.College;
import back.enums.Days;
import back.enums.Grade;
import com.google.gson.Gson;

import java.util.LinkedList;

public class Course {

    private String id;

    private String name;
    private College college;
    private LinkedList<String> prerequisites;
    private LinkedList<String> requisites;
    private double weight;
    private Grade grade;
    private int yearExam;
    private int monthExam;
    private int dayExam;
    private LinkedList<Days> days;
    private double HourBeginClass;
    private double HourEndClass;
    private int publicId;//should remove
    private int courseCapacity;
    private int numGetCoursesUntilNow = 0;
    LinkedList<String> TAsStudentNumber = new LinkedList<>();
    LinkedList<Integer> studentsIdAreInClass = new LinkedList<>();
    LinkedList<Integer> teachersId = new LinkedList<>();


    private LinkedList<HomeWork> homeWorks = new LinkedList<>();
    private LinkedList<Exam> exams = new LinkedList<>();
    private LinkedList<EducationalMaterials> educationalMaterials = new LinkedList<>();

    public Course(String id , int courseCapacity, String name, College college, LinkedList<String> prerequisites, LinkedList<String> requisites,
                  double weight, Grade grade, int yearExam, int monthExam, int dayExam, LinkedList<Days> days, double HourBeginClass, double HourEndClass, LinkedList<String> TAsStudentNumber) {
        this.name = name;
        this.college = college;
        this.prerequisites = prerequisites;
        this.requisites = requisites;
        this.id = id;
        this.weight = weight;
        this.grade = grade;
        this.dayExam = dayExam;
        this.monthExam = monthExam;
        this.yearExam = yearExam;
        this.days = days;
        this.HourBeginClass = HourBeginClass;
        this.HourEndClass = HourEndClass;
        this.courseCapacity = courseCapacity;
        this.TAsStudentNumber = TAsStudentNumber;

        String[] s = id.split("-");
        publicId = Integer.parseInt(s[0]);
    }



//    Getter and Setters:


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public LinkedList<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(LinkedList<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public LinkedList<String> getRequisites() {
        return requisites;
    }

    public void setRequisites(LinkedList<String> requisites) {
        this.requisites = requisites;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public int getYearExam() {
        return yearExam;
    }

    public void setYearExam(int yearExam) {
        this.yearExam = yearExam;
    }

    public int getMonthExam() {
        return monthExam;
    }

    public void setMonthExam(int monthExam) {
        this.monthExam = monthExam;
    }

    public int getDayExam() {
        return dayExam;
    }

    public void setDayExam(int dayExam) {
        this.dayExam = dayExam;
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

    public Days StringToDays(String s){
        if(s.equals("Monday")){
            return Days.Monday;
        }
        else if(s.equals("Tuesday")){
            return Days.Tuesday;
        }
        else if(s.equals("Wednesday")){
            return Days.Wednesday;
        }
        else if(s.equals("Thursday")){
            return Days.Thursday;
        }
        else if(s.equals("Friday")){
            return Days.Friday;
        }
        else if(s.equals("Saturday")){
            return Days.Saturday;
        }
        else if(s.equals("Sunday")){
            return Days.Sunday;
        }
        else{
            return null;
        }
    }

    public LinkedList<Days> getDays() {
        return days;
    }

    public void setDays(LinkedList<Days> days) {
        this.days = days;
    }

    public double getHourBeginClass() {
        return HourBeginClass;
    }

    public void setHourBeginClass(double hourBeginClass) {
        HourBeginClass = hourBeginClass;
    }

    public double getHourEndClass() {
        return HourEndClass;
    }

    public void setHourEndClass(double hourEndClass) {
        HourEndClass = hourEndClass;
    }

    public int getPublicId() {
        return publicId;
    }

    public void setPublicId(int publicId) {
        this.publicId = publicId;
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(int courseCapacity) {
        this.courseCapacity = courseCapacity;
    }

    public LinkedList<String> getTAsStudentNumber() {
        return TAsStudentNumber;
    }

    public void setTAsStudentNumber(LinkedList<String> TAsStudentNumber) {
        this.TAsStudentNumber = TAsStudentNumber;
    }

    public LinkedList<Integer> getStudentsIdAreInClass() {
        return studentsIdAreInClass;
    }

    public void setStudentsIdAreInClass(LinkedList<Integer> studentsIdAreInClass) {
        this.studentsIdAreInClass = studentsIdAreInClass;
    }

    public LinkedList<Integer> getTeachersId() {
        return teachersId;
    }

    public void setTeachersId(LinkedList<Integer> teachersId) {
        this.teachersId = teachersId;
    }

    public int getNumGetCoursesUntilNow() {
        return numGetCoursesUntilNow;
    }

    public void setNumGetCoursesUntilNow(int numGetCoursesUntilNow) {
        this.numGetCoursesUntilNow = numGetCoursesUntilNow;
    }

    public LinkedList<HomeWork> getHomeWorks() {
        return homeWorks;
    }

    public void setHomeWorks(LinkedList<HomeWork> homeWorks) {
        this.homeWorks = homeWorks;
    }

    public LinkedList<Exam> getExams() {
        return exams;
    }

    public void setExams(LinkedList<Exam> exams) {
        this.exams = exams;
    }

    public LinkedList<EducationalMaterials> getEducationalMaterials() {
        return educationalMaterials;
    }

    public void setEducationalMaterials(LinkedList<EducationalMaterials> educationalMaterials) {
        this.educationalMaterials = educationalMaterials;
    }

    public String toString() {
        String result =  name  + ":::" + college + ":::";
        if(prerequisites == null){
            result = result + 0 + ":::";
            prerequisites = new LinkedList<>();
        }
        else{
            result = result + prerequisites.size() + ":::";
        }

        for (int i = 0; i < prerequisites.size(); i++) {
            result = result + prerequisites.get(i) + ":::";
        }

        if(requisites == null){
            result = result + 0 + ":::";
            requisites = new LinkedList<>();
        }
        else{
            result = result + requisites.size() + ":::";
        }

        for (int i = 0; i < requisites.size(); i++) {
            result = result + requisites.get(i) + ":::";
        }

        if(days == null){
            result = result + 0 + ":::";
            days = new LinkedList<>();
        }
        else{
            result = result + days.size() + ":::";
        }

        for (int i = 0; i < days.size(); i++) {
            result = result + days.get(i) + ":::";
        }

        result = result + weight + ":::" + grade + ":::" + yearExam + ":::" + monthExam + ":::" + dayExam + ":::" + HourBeginClass + ":::" +  HourEndClass + ":::" + id + ":::" ;
        return result + "\n";
    }


    public String passSQLStringForSaveWithId(){


        String id = this.id;
        String name = this.name;

        String college = this.college + "";

        String prerequisites = "";
        if(this.prerequisites == null){
            prerequisites = "0:::";
        }
        else{
            prerequisites = this.prerequisites.size() + ":::";

            for (int j = 0; j < this.prerequisites.size(); j++) {
                if(j == this.prerequisites.size() - 1){
                    prerequisites = prerequisites + this.prerequisites.get(j);
                    break;
                }
                prerequisites = prerequisites + this.prerequisites.get(j) + ":::";
            }
        }

        String requisites = "";
        if(this.requisites == null){
            requisites = "0:::";
        }
        else{
            requisites = this.requisites.size() + ":::";

            for (int j = 0; j < this.requisites.size(); j++) {
                if(j == this.requisites.size() - 1){
                    requisites = requisites + this.requisites.get(j);
                    break;
                }
                requisites = requisites + this.requisites.get(j) + ":::";
            }
        }

        double weight = this.weight;
        String grade = this.grade + "";
        int dayExam = this.dayExam;
        int monthExam = this.monthExam;
        int yearExam = this.yearExam;

        String days = "";
        if(this.days == null){
            days = "0:::";
        }
        else{
            days = this.days.size() + ":::";

            for (int j = 0; j < this.days.size(); j++) {
                if(j == this.days.size() - 1){
                    days = days + this.days.get(j)+ "";
                    break;
                }
                days = days + this.days.get(j) + ":::";
            }
        }

        String TAsStudentNumber = "";
        if(this.TAsStudentNumber == null){
            TAsStudentNumber = "0:::";
        }
        else{
            TAsStudentNumber = this.TAsStudentNumber.size() + ":::";

            for (int j = 0; j < this.TAsStudentNumber.size(); j++) {
                if(j == this.TAsStudentNumber.size() - 1){
                    TAsStudentNumber = TAsStudentNumber + this.TAsStudentNumber.get(j);
                    break;
                }
                TAsStudentNumber = TAsStudentNumber + this.TAsStudentNumber.get(j) + ":::";
            }
        }

        double HourBeginClass = this.HourBeginClass;
        double HourEndClass = this.HourEndClass;

        Gson gson = new Gson();
        String jsonHomeWork = gson.toJson(homeWorks);
        String jsonExams = gson.toJson(exams);
        String jsonEducationalMaterials = gson.toJson(educationalMaterials);
        String studentsInClass = passStringFormGenericTypes(getStudentsIdAreInClass());
        String teachersInClass = passStringFormGenericTypes(getTeachersId());

        String str = "'" + id + "', " + publicId + ", '" + name  + "', '" + college + "', '" + prerequisites + "' , '" + requisites + "', " + weight + ", '" + grade + "', " +
                dayExam + "," + monthExam + ", " + yearExam + ", '" + days + "' , " + HourBeginClass + ", " +  HourEndClass + ", " + courseCapacity + ", '" + TAsStudentNumber + "' ,"  + numGetCoursesUntilNow + ", '" + jsonHomeWork + "', '" + jsonExams + "', '" + jsonEducationalMaterials + "', '" + studentsInClass + "', '" + teachersInClass + "'";


        return str;

    }
    public String passSQLStringForSaveWithoutId(){

        String name = this.name;

        String college = this.college + "";

        String prerequisites = "";
        if(this.prerequisites == null){
            prerequisites = "0:::";
        }
        else{
            prerequisites = this.prerequisites.size() + ":::";

            for (int j = 0; j < this.prerequisites.size(); j++) {
                if(j == this.prerequisites.size() - 1){
                    prerequisites = prerequisites + this.prerequisites.get(j);
                    break;
                }
                prerequisites = prerequisites + this.prerequisites.get(j) + ":::";
            }
        }

        String requisites = "";
        if(this.requisites == null){
            requisites = "0:::";
        }
        else{
            requisites = this.requisites.size() + ":::";

            for (int j = 0; j < this.requisites.size(); j++) {
                if(j == this.requisites.size() - 1){
                    requisites = requisites + this.requisites.get(j);
                    break;
                }
                requisites = requisites + this.requisites.get(j) + ":::";
            }
        }

        double weight = this.weight;
        String grade = this.grade + "";
        int dayExam = this.dayExam;
        int monthExam = this.monthExam;
        int yearExam = this.yearExam;

        String days = "";
        if(this.days == null){
            days = "0:::";
        }
        else{
            days = this.days.size() + ":::";

            for (int j = 0; j < this.days.size(); j++) {
                if(j == this.days.size() - 1){
                    days = days + this.days.get(j)+ "";
                    break;
                }
                days = days + this.days.get(j) + ":::";
            }
        }

        String TAsStudentNumber = "";
        if(this.TAsStudentNumber == null){
            TAsStudentNumber = "0:::";
        }
        else{
            TAsStudentNumber = this.TAsStudentNumber.size() + ":::";

            for (int j = 0; j < this.TAsStudentNumber.size(); j++) {
                if(j == this.TAsStudentNumber.size() - 1){
                    TAsStudentNumber = TAsStudentNumber + this.TAsStudentNumber.get(j);
                    break;
                }
                TAsStudentNumber = TAsStudentNumber + this.TAsStudentNumber.get(j) + ":::";
            }
        }

        double HourBeginClass = this.HourBeginClass;
        double HourEndClass = this.HourEndClass;

        Gson gson = new Gson();
        String jsonHomeWork = gson.toJson(homeWorks);
        String jsonExams = gson.toJson(exams);
        String jsonEducationalMaterials = gson.toJson(educationalMaterials);
        String studentsInClass = passStringFormGenericTypes(getStudentsIdAreInClass());
        String teachersInClass = passStringFormGenericTypes(getTeachersId());

        String str = publicId + ", '" + name  + "', '" + college + "', '" + prerequisites + "' , '" + requisites + "', " + weight + ", '" + grade + "', " +
                dayExam + "," + monthExam + ", " + yearExam + ", '" + days + "' , " + HourBeginClass + ", " +  HourEndClass + ", " + courseCapacity + ", '" + TAsStudentNumber + "' ,"  + numGetCoursesUntilNow + ", '" + jsonHomeWork + "', '" + jsonExams + "', '" + jsonEducationalMaterials + "', '" + studentsInClass + "', '" + teachersInClass + "'";


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
