package controller.TeacherController.EducationalAssistant;


import back.course.Course;
import back.enums.Days;
import back.enums.Grade;
import back.persons.Teacher;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

public class EducationalAssistant_ShowCourseList_AddNewCourse implements Initializable{
    public static final Logger log = LogManager.getLogger(EducationalAssistant_ShowCourseList_AddNewCourse.class);


    Teacher teacher;
    ClientNetwork clientNetwork;



    @FXML
    ColorPicker colorPicker;


    @FXML
    TextField NameTextField;

    @FXML
    TextField ExamTextField;

    @FXML
    TextField EndClassTextField;

    @FXML
    ChoiceBox GradeChoice;

    @FXML
    Button AddButton;

    @FXML
    TextField WeighTextField;

    @FXML
    Button BackButton;

    @FXML
    TextField BeginTextField;

    @FXML
    CheckBox SaturdayCheckBox;

    @FXML
    CheckBox SundayCheckBox;

    @FXML
    CheckBox MondayCheckBox;

    @FXML
    CheckBox TuesdayCheckBox;

    @FXML
    CheckBox WednesdayCheckBox;

    @FXML
    CheckBox ThursdayCheckBox;

    @FXML
    CheckBox FridayCheckBox;

    @FXML
    ChoiceBox TeacherChoiceBox;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    TextField textFieldForTAs;

    @FXML
    TextField publicIdTextField;

    @FXML
    ListView<String> listViewForTAs;

    @FXML
    Button addButtonForTAs;

    LinkedList<String> TAs = new LinkedList<>();

    @FXML
    TextField textFieldForPrerequisite;

    @FXML
    ListView<Integer> listViewForPrerequisite;

    @FXML
    Button addButtonForPrerequisite;

    LinkedList<Integer> Prerequisites = new LinkedList<>();


    @FXML
    TextField textFieldForRequisite;

    @FXML
    ListView<Integer> listViewForRequisite;

    @FXML
    Button addButtonForRequisite;

    @FXML
    TextField groupIdTextField;

    LinkedList<Integer> Requisites = new LinkedList<>();


    @FXML
    TextField capacity;

    Stage stage;

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        BackButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButtonForTAs.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButtonForPrerequisite.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButtonForRequisite.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);
        log.info("change background");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String[] choices_Grade = {"BC", "MS", "PHD"};
        GradeChoice.getItems().addAll(choices_Grade);

    }

    public void setTeacher(Teacher teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = teacher;

        Color color = teacher.color;
        AddButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        BackButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButtonForTAs.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButtonForPrerequisite.setBackground(new Background(new BackgroundFill(color, null, null)));
        addButtonForRequisite.setBackground(new Background(new BackgroundFill(color, null, null)));

        colorPicker.setValue(color);

        ObservableList<String> list = TeacherChoiceBox.getItems();

        for (Teacher t : clientNetwork.getAllTeachers()) {
            if(t.getCollege() == teacher.getCollege()){
                list.add(t.getLastname());
            }
        }

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);
    }

    public void AddNewCourseFunction(ActionEvent e) {


        String stringChooseTeacher =(String) TeacherChoiceBox.getValue();

        if(stringChooseTeacher == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of Teachers!!");
            log.error("Choose one of Teachers");

            alert.showAndWait();
            return;
        }
        else if(stringChooseTeacher.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of Teachers!!");
            alert.showAndWait();
            log.error("Choose one of Teachers");

            return;
        }


        String stringChooseGrade = chooseGrade();

        if(stringChooseGrade == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of Grades!!");
            alert.showAndWait();
            log.error("Choose one of grades");

            return;
        }
        else if(stringChooseGrade.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of Grades!!");
            alert.showAndWait();
            log.error("Choose one of grades");

            return;
        }


        if(NameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Name field is empty");
            alert.showAndWait();
            log.error("Name field is empty");

            return;
        }
        if(!validInputForAddCourse(NameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Name should not have :::");
            alert.showAndWait();
            log.error("Name should not have :::");

            return;
        }




        if(WeighTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Units field is empty");
            alert.showAndWait();
            log.error("Units field is empty");

            return;
        }
        if(!validInputForAddCourse(WeighTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Units should not have :::");
            alert.showAndWait();
            log.error("Units should not have :::");

            return;
        }
        if(WeighTextField.getText().equals("0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Units can not be zero!!!");
            alert.showAndWait();
            log.error("Units can not be zero!!!");

            return;
        }
        for (int i = 0; i < WeighTextField.getText().length(); i++) {
            if(!(WeighTextField.getText().charAt(i) >= '0' & WeighTextField.getText().charAt(i) <= '9')){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Units should be number!!!");
                alert.showAndWait();
                log.error("Units should be number!!!");

                return;
            }
        }

        if(groupIdTextField.getText() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Group field is empty");
            alert.showAndWait();
            log.error("Group field is empty");

            return;
        }
        else if(groupIdTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Group field is empty");
            alert.showAndWait();
            log.error("Group field is empty");

            return;
        }
        for (int i = 0; i < groupIdTextField.getText().length(); i++) {
            if(!(groupIdTextField.getText().charAt(i) >= '0' & groupIdTextField.getText().charAt(i) <= '9')){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Group id should be number!!!");
                alert.showAndWait();
                log.error("Group id should be number!!!");

                return;
            }
        }

        if(!isAStringNumber(publicIdTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Public id should be number!!!");
            alert.showAndWait();
            log.error("Public id should be number!!!");

            return;
        }
        if(!clientNetwork.validDuplicatePublicIdForCourse(Integer.parseInt(publicIdTextField.getText()), Integer.parseInt(groupIdTextField.getText()))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Duplicate public group!!!");
            alert.showAndWait();
            log.error("Duplicate public group!!!");

            return;
        }

        if(ExamTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Identity code field is empty");
            alert.showAndWait();
            log.error("Identity code field is empty");

            return;
        }
        if(!validInputForAddCourse(ExamTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Identity code should not have :::");
            alert.showAndWait();
            log.error("Identity code should not have :::");

            return;
        }

        if(ExamTextField.getText().length() != 10){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Incorrect exam format");
            alert.showAndWait();
            log.error("Incorrect exam format");

            return;
        }
        for (int j = 0; j < ExamTextField.getText().length(); j++) {

            if(j == 0 || j == 1 || j == 2 || j == 3 | j == 5 || j == 6 || j == 8 || j == 9){
                if (!(ExamTextField.getText().charAt(j) >= '0' && ExamTextField.getText().charAt(j) <= '9')) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Exam incorrect");
                    alert.showAndWait();
                    log.error("Exam incorrect");


                    return;
                }
            }
            else if(j == 4 || j == 7){
                if (!(ExamTextField.getText().charAt(j) == '-')) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect");
                    alert.setHeaderText("Exam incorrect");
                    alert.showAndWait();
                    log.error("Exam incorrect");

                    return;
                }
            }
        }


        if(BeginTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Begin field is empty");
            alert.showAndWait();
            log.error("Begin field is empty");

            return;
        }
        for (int i = 0; i < BeginTextField.getText().length(); i++) {
            if(!(BeginTextField.getText().charAt(i) >= '0' & BeginTextField.getText().charAt(i) <= '9')){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("Begin field should be number");
                alert.showAndWait();
                log.error("Begin field should be number");

                return;
            }
        }

        if(EndClassTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("End field is empty");
            alert.showAndWait();
            log.error("End field is empty");

            return;
        }
        for (int i = 0; i < EndClassTextField.getText().length(); i++) {
            if(!(EndClassTextField.getText().charAt(i) >= '0' & BeginTextField.getText().charAt(i) <= '9')){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect");
                alert.setHeaderText("End field should be number");
                alert.showAndWait();
                log.error("End field should be number");

                return;
            }
        }

        double endClass = Double.parseDouble(EndClassTextField.getText());
        double beginClass = Double.parseDouble(BeginTextField.getText());
        if(beginClass >= endClass){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Begin field should less than End field");
            alert.showAndWait();
            log.error("Begin field should less than End field");

            return;
        }

        LinkedList<Days> days = new LinkedList<>();
        if(FridayCheckBox.isSelected()){
            days.add(Days.Friday);
        }
        if(SaturdayCheckBox.isSelected()){
            days.add(Days.Saturday);
        }
        if(SundayCheckBox.isSelected()){
            days.add(Days.Sunday);
        }
        if(MondayCheckBox.isSelected()){
            days.add(Days.Monday);
        }
        if(TuesdayCheckBox.isSelected()){
            days.add(Days.Tuesday);
        }
        if(WednesdayCheckBox.isSelected()){
            days.add(Days.Wednesday);
        }
        if(ThursdayCheckBox.isSelected()){
            days.add(Days.Thursday);
        }
        if(days.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("Choose one of days");
            alert.showAndWait();
            log.error("Choose one of days");

            return;
        }



        String s = ExamTextField.getText();
        int yearExam = Integer.parseInt(s.substring(0, 4));
        s = ExamTextField.getText();
        int monthExam =  Integer.parseInt(s.substring(5, 7));
        s = ExamTextField.getText();
        int dayExam = Integer.parseInt(s.substring(8, 10));
        int capacityForAdd = -1;
        if(capacity.getText() == null){
            capacityForAdd = -1;
        }
        else if(capacity.getText().equals("")){
            capacityForAdd = -1;
        }
        else if(!isAStringNumber(capacity.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("capacity should be natural number");
            alert.showAndWait();
            log.error("capacity should be natural number");

            return;
        }
        else{
            capacityForAdd = Integer.parseInt(capacity.getText());
        }
        Course course = new Course(publicIdTextField.getText() + "-" + groupIdTextField.getText(),  capacityForAdd,NameTextField.getText(), teacher.getCollege(), null, null, Double.parseDouble(WeighTextField.getText()), StringToGrade(stringChooseGrade), yearExam ,monthExam, dayExam , days, beginClass, endClass, TAs);
        clientNetwork.addCourse(course);
        clientNetwork.AddNewCourseToTeacher(course.getId(), stringChooseTeacher, teacher.getCollege() + "");

        

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added successfully");
        log.info("Added successfully");

        alert.setHeaderText("Course added successfully");
        alert.showAndWait();



        try{
            stage = ((Stage)(((Scene)AddButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowCourseList.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowCourseList EducationalAssistant_ShowCourseList = loader.getController();
            EducationalAssistant_ShowCourseList.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    public String  chooseGrade (){
        String stringChooseGrade = (String) GradeChoice.getValue();
        if(stringChooseGrade == null){
            return "";
        }
        return stringChooseGrade;
    }

    public boolean validInputForAddCourse(String s){
        for (int i = 0; i < s.length() - 3; i++) {
            if(s.charAt(i) == ':' & s.charAt(i + 1) == ':' & s.charAt(i + 2) == ':'){
                return false;
            }
        }
        return true;
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

    public void addButtonForTAsFunction(ActionEvent event) {
        if(textFieldForTAs.getText() == null){
            return;
        }
        else if(textFieldForTAs.getText().equals("")){
            return;
        }
        else if(clientNetwork.validStudentNumberForTA(textFieldForTAs.getText())){
            TAs.add(textFieldForTAs.getText());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("invalid student number!!!");
            log.error("invalid student number");
            alert.showAndWait();
        }

        PrintForListViewTAs();
    }

    public void PrintForListViewTAs(){
        listViewForTAs.getItems().clear();
        for (int i = 0; i < TAs.size(); i++) {
            listViewForTAs.getItems().add(TAs.get(i));
        }
    }

    public void addButtonForPrerequisiteFunction(ActionEvent event) {
        if(!isAStringNumber(textFieldForPrerequisite.getText())){
            return;
        }
        if(textFieldForPrerequisite.getText() == null){
            return;
        }
        else if(textFieldForPrerequisite.getText().equals("")){
            return;
        }
        else if(clientNetwork.validPublicIdForCourse(Integer.parseInt(textFieldForPrerequisite.getText()))){
            Prerequisites.add(Integer.parseInt(textFieldForPrerequisite.getText()));
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("invalid course id");
            log.error("invalid course id");
            alert.showAndWait();
        }

        PrintForListViewPrerequisite();
    }

    public void PrintForListViewPrerequisite(){
        listViewForPrerequisite.getItems().clear();
        for (int i = 0; i < Prerequisites.size(); i++) {
            listViewForPrerequisite.getItems().add(Prerequisites.get(i));
        }
    }

    public void addButtonForRequisiteFunction(ActionEvent event) {
        if(!isAStringNumber(textFieldForRequisite.getText())){
            return;
        }
        else if(textFieldForRequisite.getText() == null){
            return;
        }
        else if(textFieldForRequisite.getText().equals("")){
            return;
        }
        else if(clientNetwork.validPublicIdForCourse(Integer.parseInt(textFieldForRequisite.getText()))){
            Requisites.add(Integer.parseInt(textFieldForRequisite.getText()));
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setHeaderText("invalid course id!!!");
            log.error("invalid course id");
            alert.showAndWait();
        }

        PrintForListViewRequisite();
    }

    public void PrintForListViewRequisite(){
        listViewForRequisite.getItems().clear();
        for (int i = 0; i < Requisites.size(); i++) {
            listViewForRequisite.getItems().add(Requisites.get(i));
        }
    }

    public boolean isAStringNumber(String s){
        if(s == null){
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if(!(s.charAt(i) >= '0' && s.charAt(i) <= '9')){
                return false;
            }
        }
        if(s.length() == 0){
            return false;
        }

        return true;
    }



    public void BackButton(ActionEvent event) {
        timelineForConnected.stop();

        try{
            stage = ((Stage)(((Scene)AddButton.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowCourseList.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowCourseList EducationalAssistant_ShowCourseList = loader.getController();
            EducationalAssistant_ShowCourseList.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }



}

