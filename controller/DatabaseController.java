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
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

import data.Barang;

public class DatabaseController {

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button DatabaseBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button KeluarBtn;
    
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

    @FXML
    private TableView<Barang> DataTable;

    @FXML
    private TableColumn<Barang, Integer> IdData;

    @FXML
    private TableColumn<Barang, Integer> JumlahData;

    @FXML
    private TableColumn<Barang, String> NamaData;

    @FXML
    private TableColumn<Barang, String> StatusData;
    
    @FXML
    private TableColumn<Barang, Integer> MinimalSData;

    @FXML
    private MenuButton FilterBtn;

    
    ObservableList<Barang> barangs = Barang.list();

    public void initialize() {
        IdData.setCellValueFactory(new PropertyValueFactory<Barang, Integer>("Id"));
        JumlahData.setCellValueFactory(new PropertyValueFactory<Barang, Integer>("Stok"));
        NamaData.setCellValueFactory(new PropertyValueFactory<Barang, String>("Nama"));
        StatusData.setCellValueFactory(new PropertyValueFactory<Barang, String>("Status"));
        MinimalSData.setCellValueFactory(new PropertyValueFactory<Barang, Integer>("Minimal"));
        DataTable.setItems(barangs);

        FilterBtn.getItems().forEach(item -> {
            item.setOnAction(event -> {
                String filterValue = item.getText(); // Get the selected filter value
                if (filterValue.equals("Semua")) {
                    DataTable.setItems(barangs);
                } else {
                    filterTable(filterValue); // Call the method to filter the table
                }
            });
        });
        
    }

    private void filterTable(String filterValue) {
        ObservableList<Barang> filteredList = FXCollections.observableArrayList();

        // Iterate through the original list and add items that match the filter
        for (Barang barang : barangs) {
            if (barang.getStatus().equals(filterValue)) {
                filteredList.add(barang);
            }
        }

        // Update the table with the filtered data
        DataTable.setItems(filteredList);
    }
}
