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

import data.Pelanggan;
import data.SharedData;

public class PelangganCrudController {

    @FXML
    private Button AdminBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button KeluarBtn;

    @FXML
    private TextField NamaPelanggan;

    @FXML
    private Button PelangganBtn;

    @FXML
    private Button PemasokBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private TextArea AlamatToko;

    @FXML
    private TextField idPelanggan;

    @FXML
    private Button TambahPelanggan;

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

    private Pelanggan currentPelanggan;

    public void setPelanggan(Pelanggan pelanggan) {
        this.currentPelanggan = pelanggan;
        idPelanggan.setText(String.valueOf(pelanggan.getId()));
        NamaPelanggan.setText(pelanggan.getNama());
        NamaToko.setText(pelanggan.getNamaToko());
        AlamatToko.setText(String.valueOf(pelanggan.getAlamat()));
    }

    @FXML
    void kembaliBtn(ActionEvent event) throws IOException {
        switchToPelanggan(event);
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
    void switchToBarangCrud(ActionEvent event) throws IOException {
        switchScene("BarangCrud.fxml", event);
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
    void createPelanggan(ActionEvent event) {
        if (currentPelanggan != null) {
            updatePelanggan(event);
            return;
        }

        String Nama = NamaPelanggan.getText();
        String NamaTk = NamaToko.getText();
        String AlamatTk = AlamatToko.getText();

        String query = "INSERT INTO Pelanggan (nama, namaToko, alamat) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, Nama);
            stmt.setString(2, NamaTk);
            stmt.setString(3, AlamatTk);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Pelanggan created successfully!");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to create Pelanggan: " + e.getMessage());
        }
    }

    @FXML
    void updatePelanggan(ActionEvent event) {
        String Nama = NamaPelanggan.getText();
        String NamaTk = NamaToko.getText();
        String AlamatTk = AlamatToko.getText();
        int id = Integer.parseInt(idPelanggan.getText());

        String query = "UPDATE Pelanggan SET nama = ?, namaToko = ?, alamat = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, Nama);
                stmt.setString(2, NamaTk);
                stmt.setString(3, AlamatTk);            
                stmt.setInt(4, id);


            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Pelanggan updated successfully!");
                switchToPelangganView(event);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to update Pelanggan: " + e.getMessage());
        }
        
    }
    
    private void switchToPelangganView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/PelangganView.fxml"));
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
    void deletePelanggan(ActionEvent event) {
        int id = Integer.parseInt(idPelanggan.getText());

        String query = "DELETE FROM Pelanggan WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Pelanggan deleted successfully!");
                NamaPelanggan.clear();
                NamaToko.clear();
                AlamatToko.clear();
                idPelanggan.clear();
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to delete Pelanggan: " + e.getMessage());
        }
    }
    
    public void setIsEdit(boolean isEdit) {
        if (isEdit) {
            TambahPelanggan.setText("Update"); // Change the button text to "Update"
        } else {
            TambahPelanggan.setText("Tambah"); // Set the default button text to "Tambah"
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
