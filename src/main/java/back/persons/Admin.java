package back.persons;

import back.enums.College;
import javafx.scene.paint.Color;

public class Admin extends Person{
    String username;
    public Admin(int id, Color color, String image, String firstname, String lastname, String email, String password, String phoneNumber, String CodeMelli, College college, String username) {
        super(id, color, image, firstname, lastname, email, password, phoneNumber, CodeMelli, college);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String passSQLStringForSaveWithId(){
        return passSQLStringForSavePersonWithId() + ", '" + username + "'";
    }

    public String passSQLStringForSaveWithoutId(){
        return passSQLStringForSavePersonWithoutId() + ", '" + username + "'";
    }

}

