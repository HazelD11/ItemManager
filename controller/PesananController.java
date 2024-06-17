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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import data.Pesanan;
import data.SharedData;

public class PesananController {

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
    private Button PelangganBtn;

    @FXML
    private Button PemasokBtn;

    @FXML
    private Button PesananBtn;

    @FXML
    private Button PesananCrudBtn;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private TableView<Pesanan> PesananTable;

    @FXML
    private TableColumn<Pesanan, Integer> Id;

    @FXML
    private TableColumn<Pesanan, String> MetodePembayaran;

    @FXML
    private TableColumn<Pesanan, String> Status;

    @FXML
    private TableColumn<Pesanan, String> Tanggal;

    @FXML
    private TableColumn<Pesanan, String> Admin;

    @FXML
    private TableColumn<Pesanan, String> Nama;

    @FXML
    private TableColumn<Pesanan, String> Alamat;

    @FXML
    private TableColumn<Pesanan, String> Tipe;

    @FXML
    private TableColumn<Pesanan, Void> Aksi;

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
    private void switchToPesananCrud(ActionEvent event) throws IOException {
        switchScene("PesananCrud.fxml", event);
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
    
    @FXML
    public void initialize() {
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);

        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        MetodePembayaran.setCellValueFactory(new PropertyValueFactory<>("metodePembayaran"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        Tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        Admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        Tipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));

        // Custom cell factory for "Aksi" column
        Aksi.setCellFactory(new Callback<TableColumn<Pesanan, Void>, TableCell<Pesanan, Void>>() {
            @Override
            public TableCell<Pesanan, Void> call(final TableColumn<Pesanan, Void> param) {
                final TableCell<Pesanan, Void> cell = new TableCell<Pesanan, Void>() {
        
                    private final Button btnDetail = new Button("Detail");
        
                    {        
                        btnDetail.setOnAction((ActionEvent event) -> {
                            try {
                                Pesanan pesanan = getTableView().getItems().get(getIndex());
                                switchToDetailView(pesanan, event);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                     @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            GridPane gridPane = new GridPane();
                            gridPane.setHgap(10);
                            gridPane.add(btnDetail, 1, 0);
                            setGraphic(gridPane);
                        }
                    }
                };
                return cell;
            }
        });

        ObservableList<Pesanan> pesanans = Pesanan.getPesanansFromDatabase();
        PesananTable.setItems(pesanans);
    }

    private void switchToDetailView(Pesanan pesanan, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/PesananDetail.fxml"));
        Parent root = loader.load();

        // Get the controller of TransaksiInDetail
        PesananDetailController controller = loader.getController();
        // Pass the transaksi Id to the controller
        controller.setPesananId(pesanan.getId());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
