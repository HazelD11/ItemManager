package controller;

import java.io.IOException;
import java.sql.*;
import conn.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import data.Pemasok;
import data.SharedData;

public class PemasokCrudController {

    @FXML
    private Button AdminBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button KeluarBtn;

    @FXML
    private TextField NamaPemasok;

    @FXML
    private Button PelangganBtn;

    @FXML
    private Button PemasokBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private TextArea Alamat;

    @FXML
    private TextField idPemasok;

    @FXML
    private Button TambahPemasok;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private Button kembaliBtn;

    @FXML
    private Label identitas;

    @FXML
    private TextField NamaToko;

    private Pemasok currentPemasok;

    public void setPemasok(Pemasok pemasok) {
        this.currentPemasok = pemasok;
        idPemasok.setText(String.valueOf(pemasok.getId()));
        NamaPemasok.setText(pemasok.getNama());
        Alamat.setText(String.valueOf(pemasok.getAlamat()));
    }

    @FXML
    void kembaliBtn(ActionEvent event) throws IOException {
        switchToPemasok(event);
    }

    // Existing methods (switch scenes, create, update, delete Barang, etc.)

    @FXML
    void switchToDashboard(ActionEvent event) throws IOException {
        switchScene("Dashboard.fxml", event);
    }

    @FXML
    void switchToAdmin(ActionEvent event) throws IOException {
        switchScene("AdminView.fxml", event);
    }
    
    @FXML
    void switchToBarang(ActionEvent event) throws IOException {
        switchScene("BarangView.fxml", event);
    }

    @FXML
    void switchToPemasokCrud(ActionEvent event) throws IOException {
        switchScene("PemasokCrud.fxml", event);
    }

    @FXML
    void switchToPemasok(ActionEvent event) throws IOException {
        switchScene("PemasokView.fxml", event);
    }

    @FXML
    void switchToPelanggan(ActionEvent event) throws IOException {
        switchScene("PelangganView.fxml", event);
    }
    
    @FXML
    void switchToTransaksiIn(ActionEvent event) throws IOException {
        switchScene("TransaksiInView.fxml", event);
    }

    @FXML
    void switchToTransaksiOut(ActionEvent event) throws IOException {
        switchScene("TransaksiOutView.fxml", event);
    }
    
    @FXML
    void switchToPesanan(ActionEvent event) throws IOException {
        switchScene("PesananView.fxml", event);
    }
    
    @FXML
    void logout(ActionEvent event) throws IOException {
        switchScene("login.fxml", event);
    }

    public void initialize(){
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);
    }

    private void switchScene(String sceneName, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/" + sceneName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GridPane grid = new GridPane();
        grid.getChildren().add(root);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.setFullScreen(true);
        
        stage.show();
    }

    @FXML
    void createPemasok(ActionEvent event) {
        if (currentPemasok != null) {
            updatePemasok(event);
            return;
        }

        String Nama = NamaPemasok.getText();
        String AlamatTk = Alamat.getText();

        String query = "INSERT INTO Pemasok (nama, alamat) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, Nama);
            stmt.setString(2, AlamatTk);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Pemasok created successfully!");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to create Pemasok: " + e.getMessage());
        }
    }

    @FXML
    void updatePemasok(ActionEvent event) {
        String Nama = NamaPemasok.getText();
        String AlamatTk = Alamat.getText();
        int id = Integer.parseInt(idPemasok.getText());

        String query = "UPDATE Pemasok SET nama = ?, alamat = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, Nama);
                stmt.setString(2, AlamatTk);            
                stmt.setInt(3, id);


            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Pemasok updated successfully!");
                switchToPemasokView(event);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to update Pemasok: " + e.getMessage());
        }
        
    }
    
    private void switchToPemasokView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/PemasokView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    @FXML
    void deletePemasok(ActionEvent event) {
        int id = Integer.parseInt(idPemasok.getText());

        String query = "DELETE FROM Pemasok WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Pemasok deleted successfully!");
                NamaPemasok.clear();
                Alamat.clear();
                idPemasok.clear();
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to delete Pemasok: " + e.getMessage());
        }
    }
    
    public void setIsEdit(boolean isEdit) {
        if (isEdit) {
            TambahPemasok.setText("Update"); // Change the button text to "Update"
        } else {
            TambahPemasok.setText("Tambah"); // Set the default button text to "Tambah"
        }
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
