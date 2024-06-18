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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.scene.Node;
import java.io.IOException;

import data.Barang;
import data.SharedData;

public class DashboardController {

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button AdminBtn;

    @FXML
    private Button BarangBtn;

    @FXML
    private Button PemasokBtn;
    
    @FXML
    private Button PelangganBtn;
    
    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;
 
    @FXML
    private Button PesananBtn;

    @FXML
    private Button KeluarBtn;
    
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
    private TableView<Barang> DataTable;

    @FXML
    private TableColumn<Barang, Integer> IdData;

    @FXML
    private TableColumn<Barang, Integer> JumlahData;

    @FXML
    private TableColumn<Barang, String> NamaData;
    
    @FXML
    private TableColumn<Barang, String> KodeData;

    @FXML
    private TableColumn<Barang, String> StatusData;

    @FXML
    private MenuButton FilterBtn;

    @FXML
    private TableView<Barang> StockTable;

    @FXML
    private TableColumn<Barang, String> IdStock;

    @FXML
    private TableColumn<Barang, Integer> JumlahStock;

    @FXML
    private TableColumn<Barang, String> NamaStock;

    
    @FXML
    private TableColumn<Barang, String> KodeStock;

    @FXML
    private TableColumn<Barang, String> AksiStock;

    @FXML
    private Label identitas;

    // public Integer checker;
    
    ObservableList<Barang> barangs = Barang.getBarangsFromDatabase();

    public void initialize() {
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);

        IdData.setCellValueFactory(new PropertyValueFactory<Barang, Integer>("Id"));
        JumlahData.setCellValueFactory(new PropertyValueFactory<Barang, Integer>("Stok"));
        NamaData.setCellValueFactory(new PropertyValueFactory<Barang, String>("Nama"));
        KodeData.setCellValueFactory(new PropertyValueFactory<Barang, String>("Kode"));
        StatusData.setCellValueFactory(new PropertyValueFactory<Barang, String>("Status"));
        DataTable.setItems(barangs);

        // Set up columns for StockTable
        IdStock.setCellValueFactory(new PropertyValueFactory<>("Id"));
        JumlahStock.setCellValueFactory(new PropertyValueFactory<>("Stok"));
        NamaStock.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        KodeStock.setCellValueFactory(new PropertyValueFactory<>("Kode"));

        // Filter static data for StockTable
        ObservableList<Barang> stockItems = FXCollections.observableArrayList();
        for (Barang barang : barangs) {
            if (barang.getStok() <= barang.getMinimal()) {
                stockItems.add(barang);
            }
        }

        // Populate StockTable with filtered data
        StockTable.setItems(stockItems);

        AksiStock.setCellFactory(new Callback<TableColumn<Barang, String>, TableCell<Barang, String>>() {
            @Override
            public TableCell<Barang, String> call(TableColumn<Barang, String> param) {
                return new TableCell<Barang, String>() {
                    final Button pesanButton = new Button("PESAN");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(pesanButton);
                            setText(null);

                            pesanButton.setOnAction(event -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/PesananCrud.fxml"));
                                    Parent root = loader.load();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }                            
                            });
                        }
                    }
                };
            }
        });

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
