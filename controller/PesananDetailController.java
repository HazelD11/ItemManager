package controller;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import data.PesananDetail;
import data.SharedData;

public class PesananDetailController {

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private Label identitas;

    @FXML
    private Button AdminBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button KeluarBtn;

    @FXML
    private Button KembaliBtn;

    @FXML
    private Button PelangganBtn;

    @FXML
    private Button PemasokBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private TableView<PesananDetail> PesananDetailTable;

    @FXML
    private TableColumn<PesananDetail, Integer> Id;

    @FXML
    private TableColumn<PesananDetail, Integer> NomorPesanan;

    @FXML
    private TableColumn<PesananDetail, String> Barang;

    @FXML
    private TableColumn<PesananDetail, Integer> Jumlah;

    @FXML
    private void switchToDashboard(ActionEvent event) throws IOException {
        switchScene("Dashboard.fxml", event);
    }

    @FXML
    private void switchToAdmin(ActionEvent event) throws IOException {
        switchScene("AdminView.fxml", event);
    }

    @FXML
    private void switchToBarang(ActionEvent event) throws IOException {
        switchScene("BarangView.fxml", event);
    }

    @FXML
    private void switchToBarangCrud(ActionEvent event) throws IOException {
        switchScene("BarangCrud.fxml", event);
    }

    @FXML
    private void switchToPemasok(ActionEvent event) throws IOException {
        switchScene("PemasokView.fxml", event);
    }

    @FXML
    private void switchToPelanggan(ActionEvent event) throws IOException {
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
    private void switchToPesanan(ActionEvent event) throws IOException {
        switchScene("PesananView.fxml", event);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
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
    private int pesananId;

    public void setPesananId(int pesananId) {
        this.pesananId = pesananId;
        loadPesananDetails();
    }

    @FXML
    public void initialize() {
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        NomorPesanan.setCellValueFactory(new PropertyValueFactory<>("nomorPesanan"));
        Barang.setCellValueFactory(new PropertyValueFactory<>("barang"));
        Jumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    }

    private void loadPesananDetails() {
        ObservableList<PesananDetail> details = PesananDetail.getPesananDetailsFromDatabase(pesananId);
        PesananDetailTable.setItems(details);
    }
}

