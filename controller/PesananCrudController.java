package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.Transaksi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PesananCrudController {

    private List<Transaksi> transaksiList = new ArrayList<>(); // Temporary list

    @FXML
    private TextArea Alamat;

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button DatabaseBtn;

    @FXML
    private TextField Id;

    @FXML
    private TextField IdFaktur;

    @FXML
    private TextField Jumlah;

    @FXML
    private Button KeluarBtn;

    @FXML
    private TextField Nama;

    @FXML
    private TextField Pembeli;

    @FXML
    private TextField Penjual;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button Tambah;

    @FXML
    private DatePicker Tanggal;

    @FXML
    private MenuButton Tipe;

    @FXML
void create(ActionEvent event) throws IOException {
    // Gather data from input fields
    int id = Integer.parseInt(Id.getText());
    String idFaktur = IdFaktur.getText();
    String nama = Nama.getText();
    int jumlah = Integer.parseInt(Jumlah.getText());
    String pembeli = Pembeli.getText();
    String penjual = Penjual.getText();
    String alamat = Alamat.getText();
    String tipe = Tipe.getText();
    String tanggal = "02-05-2024";

    // Create a new Transaksi object (temporary)
    Transaksi newTransaksi = new Transaksi(id, idFaktur, nama, jumlah, pembeli, tanggal, penjual, alamat, tipe);

    // Add the new Transaksi object to the temporary list
    transaksiList.add(newTransaksi);

    // Print success message
    System.out.println("Transaction created successfully (temporary).");

    // Switch back to PesananView.fxml and refresh the table
    switchToPesananView(event);
}

private void switchToPesananView(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/PesananView.fxml"));
    Parent root = loader.load();
    PesananController controller = loader.getController();
    
    // Refresh the table in PesananController with the updated transaksiList
    controller.refreshTable(transaksiList);

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
    void switchToDashboard(ActionEvent event) throws IOException {
        switchScene("Dashboard.fxml", event);
    }

    @FXML
    void switchToDatabase(ActionEvent event) throws IOException {
        switchScene("Database.fxml", event);
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
}