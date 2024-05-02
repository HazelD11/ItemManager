package controller;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Dashboard;
import main.Index;
import data.Admin;

public class LoginController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button fxLogBtn;

    @FXML
    private PasswordField fxPass;

    @FXML
    private Index index;
    
    @FXML
    private TextField fxUser;
    
    // @FXML
    // public void setIndex(Index index) {
    //     this.index = index;
    // }

    @FXML
    public void setMainWindow(Stage mainWindow) {
        mainWindow.setTitle("Login");
    }

    public void login(){
        String username = fxUser.getText();
        String password = fxPass.getText();

         // Get the list of admins
        ObservableList<Admin> admins = Admin.acc();

        // Check if any admin's credentials match
        boolean loggedIn = false;
        for (Admin adminData : admins) {
            if (username.equals(adminData.getUsername()) && password.equals(adminData.getPassword())) {
                loggedIn = true;
                break;
            }
        }

        // Check if username and password match
        if (loggedIn) {
            System.out.println("berhasil");
            Dashboard dashboard = new Dashboard();
            try {
                dashboard.start(new Stage());
            } catch (Exception e) {
                // Handle exception
                System.err.println("Error opening dashboard: " + e.getMessage());
            }
            // Close the login window
            ((Stage) fxLogBtn.getScene().getWindow()).close();
            // index.closeIndexScene();

        } else {
            System.out.println("ID atau Password Salah!");
        }
    }

    @FXML
    void onBtnClick(ActionEvent event) throws IOException {
       login();
    }
}
