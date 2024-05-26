package controller;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import data.Transaksi;

public class PesananController {

    @FXML
    private TableColumn<Transaksi, String> AksiData;

    @FXML
    private TableColumn<Transaksi, String> AlamatData;

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button TambahData;

    @FXML
    private TableView<Transaksi> DataTable;

    @FXML
    private Button DatabaseBtn;

    @FXML
    private MenuButton FilterBtn;

    @FXML
    private TableColumn<Transaksi, Integer> IdData;

    @FXML
    private TableColumn<Transaksi, Integer> IdFakturData;

    @FXML
    private TableColumn<Transaksi, Integer> JumlahData;

    @FXML
    private Button KeluarBtn;

    @FXML
    private TableColumn<Transaksi, String> TanggalData;

    @FXML
    private TableColumn<Transaksi, String> NamaData;

    @FXML
    private TableColumn<Transaksi, String> PembeliData;

    @FXML
    private TableColumn<Transaksi, String> PenjualData;

    @FXML
    private Button PesananBtn;

    @FXML
    private TableColumn<Transaksi, String> TipeData;

    @FXML
    void TambahDataP(ActionEvent event) throws IOException {
        switchScene("PesananCrud.fxml", event);
    }

    @FXML
    void switchToDashboard(ActionEvent event) throws IOException {
        switchScene("Dashboard.fxml", event);
    }

    @FXML
    void switchToDatabase(ActionEvent event) throws IOException {
        switchScene("Database.fxml", event);
    }
public void refreshTable(List<Transaksi> updatedList) {
    transaksis.clear(); // Clear the existing list
    transaksis.addAll(updatedList); // Add the updated list
    DataTable.refresh(); // Refresh the TableView
}

    @FXML
    void switchToPesanan(ActionEvent event) throws IOException {
        switchScene("PesananView.fxml", event);
    }

    @FXML
    void switchToOrder(ActionEvent event) throws IOException {
        switchScene("Order.fxml", event);
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
    ObservableList<Transaksi> transaksis = Transaksi.Tlist();
    
    public void initialize() {
        
        IdData.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("Id"));
        IdFakturData.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("IdFaktur"));
        JumlahData.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("Jumlah"));
        NamaData.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("Nama"));
        PembeliData.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("Pembeli"));
        PenjualData.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("Penjual"));
        TanggalData.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("Tanggal"));
        TipeData.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("Tipe"));
        AlamatData.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("Alamat"));
        DataTable.setItems(transaksis);

        AksiData.setCellFactory(new Callback<TableColumn<Transaksi, String>, TableCell<Transaksi, String>>() {
            @Override
            public TableCell<Transaksi, String> call(TableColumn<Transaksi, String> param) {
                return new TableCell<Transaksi, String>() {
                    final Button pesanButton = new Button("EDIT");

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
                    DataTable.setItems(transaksis);
                } else {
                    filterTable(filterValue); // Call the method to filter the table
                }
            });
        });
    }

    private void filterTable(String filterValue) {
        ObservableList<Transaksi> filteredList = FXCollections.observableArrayList();

        // Iterate through the original list and add items that match the filter
        for (Transaksi transaksi : transaksis) {
            if (transaksi.getTipe().equals(filterValue)) {
                filteredList.add(transaksi);
            }
        }

        // Update the table with the filtered data
        DataTable.setItems(filteredList);
    }
}
