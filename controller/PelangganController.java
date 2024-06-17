package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.IOException;

import data.Pelanggan;
import data.SharedData;

public class PelangganController {
    @FXML
    private Button DashboardBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button PelangganCrudBtn;

    @FXML
    private Button KeluarBtn;
    
    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private Label identitas;
    
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
    void switchToPemasok(ActionEvent event) throws IOException {
        switchScene("PemasokView.fxml", event);
    }

    @FXML
    void switchToPelanggan(ActionEvent event) throws IOException {
        switchScene("PelangganView.fxml", event);
    }

    @FXML
    void switchToPelangganCrud(ActionEvent event) throws IOException {
        switchScene("PelangganCrud.fxml", event);
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

    @FXML
    private TableView<Pelanggan> PelangganTable;

    @FXML
    private TableColumn<Pelanggan, Integer> IdPelanggan;

    @FXML
    private TableColumn<Pelanggan, String> NamaPelanggan;

    @FXML
    private TableColumn<Pelanggan, String> NamaToko;

    @FXML
    private TableColumn<Pelanggan, String> Alamat;

    @FXML
    private TableColumn<Pelanggan, String> AksiPelanggan;

    ObservableList<Pelanggan> pelanggans = Pelanggan.getPelanggansFromDatabase();
    
    
    public void initialize() {
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);
        
        IdPelanggan.setCellValueFactory(new PropertyValueFactory<>("Id"));
        NamaPelanggan.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        NamaToko.setCellValueFactory(new PropertyValueFactory<>("NamaToko"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("Alamat"));

        // Add buttons to the AksiBarang column
        Callback<TableColumn<Pelanggan, String>, TableCell<Pelanggan, String>> cellFactory = (param) -> new TableCell<Pelanggan, String>() {
            final Button editButton = new Button("Edit");
            final Button deleteButton = new Button("Delete");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    editButton.setOnAction(event -> {
                        Pelanggan pelanggan = getTableView().getItems().get(getIndex());
                        editPelangganCrud(event, pelanggan, true);
                    });

                    deleteButton.setOnAction(event -> {
                        Pelanggan pelanggan = getTableView().getItems().get(getIndex());
                        deletePelanggan(pelanggan);
                    });

                    GridPane pane = new GridPane();
                    pane.add(editButton, 0, 0);
                    pane.add(deleteButton, 1, 0);
                    setGraphic(pane);
                    setText(null);
                }
            }
        };

        AksiPelanggan.setCellFactory(cellFactory);

        PelangganTable.setItems(pelanggans);

    }

    private void deletePelanggan(Pelanggan pelanggan) {
        if (Pelanggan.deletePelanggan(pelanggan.getId())) {
            pelanggans.remove(pelanggan);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete Pelanggan");
        }
    }

    private void editPelangganCrud(ActionEvent event, Pelanggan pelanggan, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/PelangganCrud.fxml"));
            Parent root = loader.load();
    
            PelangganCrudController controller = loader.getController();
            controller.setPelanggan(pelanggan);
            controller.setIsEdit(isEdit); // Pass the flag indicating edit operation
    
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
