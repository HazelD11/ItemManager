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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import data.Barang;
import data.SharedData;

public class BarangCrudController {

    @FXML
    private Button AdminBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button DashboardBtn1;

    @FXML
    private Button KeluarBtn;

    @FXML
    private TextField NamaBarang;

    @FXML
    private Button PelangganBtn;

    @FXML
    private Button PemasokBtn;

    @FXML
    private Button PesananBtn1;

    @FXML
    private TextField Stok;

    @FXML
    private TextField idBarang;

    @FXML
    private Button TambahBarang;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private Button kembaliBtn;

    @FXML
    private Label identitas;

    @FXML
    private TextField kodeBarang;

    private Barang currentBarang;

    public void setBarang(Barang barang) {
        this.currentBarang = barang;
        idBarang.setText(String.valueOf(barang.getId()));
        NamaBarang.setText(barang.getNama());
        kodeBarang.setText(barang.getKode());
        Stok.setText(String.valueOf(barang.getStok()));
    }

    @FXML
    void kembaliBtn(ActionEvent event) throws IOException {
        switchToBarang(event);
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
    public void initialize(){
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);
    }
    
    @FXML
    void createBarang(ActionEvent event) {
        if (currentBarang != null) {
            updateBarang(event);
            return;
        }

        String nama = NamaBarang.getText();
        String kode = kodeBarang.getText();
        int jumlah = Integer.parseInt(Stok.getText());

        String query = "INSERT INTO Barang (kode, nama, stok) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kode);
            stmt.setString(2, nama);
            stmt.setInt(3, jumlah);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Barang created successfully!");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to create Barang: " + e.getMessage());
        }
    }

    @FXML
    void updateBarang(ActionEvent event) {
        String nama = NamaBarang.getText();
        String kode = kodeBarang.getText();
        int jumlah = Integer.parseInt(Stok.getText());
        int id = Integer.parseInt(idBarang.getText());

        String query = "UPDATE Barang SET nama = ?, kode = ?, stok = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nama);
            stmt.setString(2, kode);
            stmt.setInt(3, jumlah);
            stmt.setInt(4, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Barang updated successfully!");
                switchToBarangView(event);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to update Barang: " + e.getMessage());
        }
        
    }
    
    private void switchToBarangView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/BarangView.fxml"));
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
    void deleteBarang(ActionEvent event) {
        int id = Integer.parseInt(idBarang.getText());

        String query = "DELETE FROM Barang WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Barang deleted successfully!");
                NamaBarang.clear();
                Stok.clear();
                kodeBarang.clear();
                idBarang.clear();
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to delete Barang: " + e.getMessage());
        }
    }
    
    public void setIsEdit(boolean isEdit) {
        if (isEdit) {
            TambahBarang.setText("Update"); // Change the button text to "Update"
        } else {
            TambahBarang.setText("Tambah"); // Set the default button text to "Tambah"
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
