package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Dashboard;
import data.Admin;
import data.AdminDAO;

public class LoginController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button fxLogBtn;

    @FXML
    private PasswordField fxPass;

    @FXML
    private TextField fxUser;

    private ObservableList<Admin> admins; // Tambahkan atribut untuk menyimpan data admin

    public void initialize() {
        // Ambil data admin dari database saat controller diinisialisasi
        admins = AdminDAO.getAdminsFromDatabase();
    }

    public void login() {
        String Username = fxUser.getText();
        String Password = fxPass.getText();

        // Check if any admin's credentials match
        boolean loggedIn = false;
        for (Admin adminData : admins) {
            if (Username.equals(adminData.getUsername()) && Password.equals(adminData.getPassword())) {
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
        } else {
            System.out.println("ID atau Password Salah!");
        }
    }

    @FXML
    void onBtnClick(ActionEvent event) {
        login();
    }

    public void setMainWindow(Stage primaryStage) {
    }
}
