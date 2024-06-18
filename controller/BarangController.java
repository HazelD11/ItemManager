package controller;

import javafx.collections.FXCollections;
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

import data.Barang;
import data.SharedData;

public class BarangController {
    @FXML
    private Button DashboardBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button BarangCrudBtn;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private Button KeluarBtn;
    
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

    @FXML
    private TableView<Barang> BarangTable;

    @FXML
    private TableColumn<Barang, Integer> IdBarang;

    @FXML
    private TableColumn<Barang, Integer> JumlahStok;

    @FXML
    private TableColumn<Barang, String> NamaBarang;

    @FXML
    private TableColumn<Barang, String> KodeBarang;

    @FXML
    private TableColumn<Barang, String> StatusBarang;
    
    @FXML
    private TableColumn<Barang, Integer> MinimalStok;

    @FXML
    private TableColumn<Barang, String> AksiBarang;

    @FXML
    private MenuButton FilterBtn;

    ObservableList<Barang> barangs = Barang.getBarangsFromDatabase();
    
    
    public void initialize() {
       String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);
        
        IdBarang.setCellValueFactory(new PropertyValueFactory<>("Id"));
        JumlahStok.setCellValueFactory(new PropertyValueFactory<>("Stok"));
        NamaBarang.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        KodeBarang.setCellValueFactory(new PropertyValueFactory<>("Kode"));
        StatusBarang.setCellValueFactory(new PropertyValueFactory<>("Status"));
        MinimalStok.setCellValueFactory(new PropertyValueFactory<>("Minimal"));

        // Add buttons to the AksiBarang column
        Callback<TableColumn<Barang, String>, TableCell<Barang, String>> cellFactory = (param) -> new TableCell<Barang, String>() {
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
                        Barang barang = getTableView().getItems().get(getIndex());
                        editBarangCrud(event, barang, true);
                    });

                    deleteButton.setOnAction(event -> {
                        Barang barang = getTableView().getItems().get(getIndex());
                        deleteBarang(barang);
                    });

                    GridPane pane = new GridPane();
                    pane.add(editButton, 0, 0);
                    pane.add(deleteButton, 1, 0);
                    setGraphic(pane);
                    setText(null);
                }
            }
        };

        AksiBarang.setCellFactory(cellFactory);

        BarangTable.setItems(barangs);

        FilterBtn.getItems().forEach(item -> {
            item.setOnAction(event -> {
                String filterValue = item.getText();
                if (filterValue.equals("Semua")) {
                    BarangTable.setItems(barangs);
                } else {
                    filterTable(filterValue);
                }
            });
        });
    }

    private void filterTable(String filterValue) {
        ObservableList<Barang> filteredList = FXCollections.observableArrayList();

        for (Barang barang : barangs) {
            if (barang.getStatus().equals(filterValue)) {
                filteredList.add(barang);
            }
        }

        BarangTable.setItems(filteredList);
    }

    private void deleteBarang(Barang barang) {
        if (Barang.deleteBarang(barang.getId())) {
            barangs.remove(barang);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete Barang");
        }
    }

    private void editBarangCrud(ActionEvent event, Barang barang, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BarangCrud.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            GridPane grid = new GridPane();
            grid.getChildren().add(root);
            grid.setAlignment(Pos.CENTER);
            Scene scene = new Scene(grid);
            BarangCrudController controller = loader.getController();
            controller.setBarang(barang);
            controller.setIsEdit(isEdit); // Pass the flag indicating edit operation
    
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
