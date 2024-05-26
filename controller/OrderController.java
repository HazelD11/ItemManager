package controller;

import java.io.IOException;

import data.Order;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Callback;

public class OrderController {

    @FXML
    private Button DashboardBtn;

    @FXML
    private Button DatabaseBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button OrderBtn;

    @FXML
    private Button KeluarBtn;

    @FXML
    private MenuButton FilterBtn;

    @FXML
    private Button TambahOrder;

    @FXML
    private TableView<Order> OrderTable;

    @FXML
    private TableColumn<Order, String> IdColumn;

    @FXML
    private TableColumn<Order, String> PemasokColumn;

    @FXML
    private TableColumn<Order, String> BarangColumn;

    @FXML
    private TableColumn<Order, Integer> JumlahColumn;

    @FXML
    private TableColumn<Order, String> TanggalColumn;

    @FXML
    private TableColumn<Order, String> StatusColumn;

    @FXML
    private TableColumn<Order, Void> AksiColumn;

    @FXML
    private VBox root;

    public void initialize() {
        // Menghubungkan setiap TableColumn dengan properti dari objek Order
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PemasokColumn.setCellValueFactory(new PropertyValueFactory<>("pemasok"));
        BarangColumn.setCellValueFactory(new PropertyValueFactory<>("barang"));
        JumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        TanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Menambahkan tombol ke dalam TableColumn Aksi
        class ActionButtonTableCellFactory implements Callback<TableColumn<Order, Void>, TableCell<Order, Void>> {

            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                return new TableCell<Order, Void>() {
                    private final Button btn = new Button("Edit");
        
                    {
                        btn.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            // Lakukan aksi sesuai kebutuhan dengan order yang terkait
                            System.out.println("Aksi dijalankan untuk order dengan ID: " + order.getId());
                        });
                    }
        
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        }
        // Anda dapat menyesuaikan implementasi tombol sesuai kebutuhan
        AksiColumn.setCellFactory(new ActionButtonTableCellFactory());

        // Mengambil data dummy dari kelas Order
        ObservableList<Order> orderList = Order.getOrderList();

        // Menambahkan data ke dalam TableView
        OrderTable.setItems(orderList);
    }

    

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        switchScene("Dashboard.fxml", event);
    }

    @FXML
    public void switchToDatabase(ActionEvent event) throws IOException {
        switchScene("Database.fxml", event);
    }

    @FXML
    public void switchToPesanan(ActionEvent event) throws IOException {
        switchScene("PesananView.fxml",event);
    }

    @FXML
    public void switchToOrder(ActionEvent event) throws IOException {
        switchScene("Order.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        switchScene("login.fxml", event);
    }

    @FXML
    public void TambahDataO(ActionEvent event) throws IOException {
    switchScene("OrderCrud.fxml", event);
    
    // Setelah menambah data baru, perbarui tabel
    OrderTable.getItems().clear(); // Bersihkan data yang ada
    OrderTable.getItems().addAll(Order.getOrderList()); // Tambahkan kembali semua data dari Order.getOrderList()
    OrderTable.refresh(); // Perbarui tampilan tabel
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
