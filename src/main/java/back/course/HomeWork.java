package back.course;

import back.enums.HomeWorkOrExamType;

import java.util.HashMap;

public class HomeWork {
    private int id;
    private String name;
    private String addressOrTextQuestionInServer;
    private String addressOrTextAnswerInServer;
    private HomeWorkOrExamType typeAnswer;
    private boolean isAnswerUploaded = false;
    private String beginTime; //yyyy-mm-dd-hh-mm-ss;
    private String endTime; //yyyy-mm-dd-hh-mm-ss;
    private String description;
    private HashMap<Integer, Double> studentsSend_Score = new HashMap<>();
    private HashMap<Integer, String> studentsSend_AddressOrText = new HashMap<>();
    private HashMap<Integer, String> studentsSend_Time = new HashMap<>();
    private HomeWorkOrExamType homeWorkOrExamType;

    public HomeWork(int id, String name, String beginTime, String endTime, String addressOrTextQuestionInServer, HomeWorkOrExamType homeWorkOrExamType, String description) {
        this.id = id;
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.addressOrTextQuestionInServer = addressOrTextQuestionInServer;
        this.homeWorkOrExamType = homeWorkOrExamType;
        this.description = description;
    }

    public HomeWork(String name, String beginTime, String endTime, String addressOrTextQuestionInServer, HomeWorkOrExamType homeWorkOrExamType, String description) {
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.addressOrTextQuestionInServer = addressOrTextQuestionInServer;
        this.homeWorkOrExamType = homeWorkOrExamType;
        this.description = description;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public HashMap<Integer, Double> getStudentsSend_Score() {
        return studentsSend_Score;
    }

    public void setStudentsSend_Score(HashMap<Integer, Double> studentsSend_Score) {
        this.studentsSend_Score = studentsSend_Score;
    }

    public HashMap<Integer, String> getStudentsSend_AddressOrText() {
        return studentsSend_AddressOrText;
    }

    public void setStudentsSend_AddressOrText(HashMap<Integer, String> studentsSend_AddressOrText) {
        this.studentsSend_AddressOrText = studentsSend_AddressOrText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressOrTextQuestionInServer() {
        return addressOrTextQuestionInServer;
    }

    public void setAddressOrTextQuestionInServer(String addressOrTextQuestionInServer) {
        this.addressOrTextQuestionInServer = addressOrTextQuestionInServer;
    }

    public HomeWorkOrExamType getHomeWorkOrExamType() {
        return homeWorkOrExamType;
    }

    public void setHomeWorkOrExamType(HomeWorkOrExamType homeWorkOrExamType) {
        this.homeWorkOrExamType = homeWorkOrExamType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressOrTextAnswerInServer() {
        return addressOrTextAnswerInServer;
    }

    public void setAddressOrTextAnswerInServer(String addressOrTextAnswerInServer) {
        this.addressOrTextAnswerInServer = addressOrTextAnswerInServer;
    }

    public HashMap<Integer, String> getStudentsSend_Time() {
        return studentsSend_Time;
    }

    public void setStudentsSend_Time(HashMap<Integer, String> studentsSend_Time) {
        this.studentsSend_Time = studentsSend_Time;
    }

    public HomeWorkOrExamType getTypeAnswer() {
        return typeAnswer;
    }

    public void setTypeAnswer(HomeWorkOrExamType typeAnswer) {
        this.typeAnswer = typeAnswer;
    }

    public boolean isAnswerUploaded() {
        return isAnswerUploaded;
    }

    public void setAnswerUploaded(boolean answerUploaded) {
        isAnswerUploaded = answerUploaded;
    }

    @Override
    public String toString(){
        String de = "";
        for (int i = 0; i < description.length(); i++) {
            if(description.charAt(i) == '\n'){
                de = de + " ";
            }
            else{
                de = de + description.charAt(i);
            }
        }
        return "name: " + name + "  description: " + de + "  beginTime: " + beginTime + "  endTime: " + endTime;
    }
}
