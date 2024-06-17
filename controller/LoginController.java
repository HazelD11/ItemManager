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
import data.SharedData;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button fxLogBtn;

    @FXML
    private PasswordField fxPass;

    @FXML
    private TextField fxUser;
    
    private String hashedPass;

    private ObservableList<Admin> admin;
    
    public void initialize() {
        admin = Admin.getAdminsFromDatabase();
    }
   
    public void login() {
        String Username = fxUser.getText();
        
        try {
            hashedPass = toHexString(getSHA(fxPass.getText()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }

        boolean loggedIn = false;
        String nick = "";
        int adId = -1; // Initialize adId
        for (Admin adminData : admin) {
            if (Username.equals(adminData.getUsername()) && hashedPass.equals(adminData.getPassword())) {
                loggedIn = true;
                adId = adminData.getId();
                nick = adminData.getNickname();
                break;
            }
        }

        if (loggedIn) {
            System.out.println("berhasil");
            SharedData.getInstance().setNickname(nick); // Store the nickname in SharedData
            SharedData.getInstance().setAdminId(adId); // Store the adminId in SharedData
            Dashboard dashboard = new Dashboard();
            try {
                dashboard.start(new Stage());
            } catch (Exception e) {
                System.err.println("Error opening dashboard: " + e.getMessage());
            }
            ((Stage) fxLogBtn.getScene().getWindow()).close();
        } else {
            System.out.println("ID atau Password Salah!");
            System.out.println(hashedPass);
        }
    }
    
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
     
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    @FXML
    void onBtnClick(ActionEvent event) {
        login();
    }

    public void setMainWindow(Stage primaryStage) {}
}
