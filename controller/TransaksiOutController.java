package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import data.SharedData;
import data.Transaksi;

public class TransaksiOutController {

    @FXML
    private Label identitas;

    @FXML
    private DatePicker DariTanggal;

    @FXML
    private DatePicker SampaiTanggal;

    @FXML
    private Button FilterTanggal;

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
    private Button TransaksiOutCrudBtn;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private TableView<Transaksi> TransaksiTable;

    @FXML
    private TableColumn<Transaksi, Integer> Id;

    @FXML
    private TableColumn<Transaksi, String> NomorReferensi;

    @FXML
    private TableColumn<Transaksi, String> MetodePembayaran;

    @FXML
    private TableColumn<Transaksi, String> Status;

    @FXML
    private TableColumn<Transaksi, String> Tanggal;

    @FXML
    private TableColumn<Transaksi, String> Admin;

    @FXML
    private TableColumn<Transaksi, String> Nama;

    @FXML
    private TableColumn<Transaksi, String> Toko;

    @FXML
    private TableColumn<Transaksi, String> Alamat;

    @FXML
    private TableColumn<Transaksi, String> Tipe;

    @FXML
    private TableColumn<Transaksi, Void> Aksi;

    private ObservableList<Transaksi> transaksis;

    @FXML
    public void initialize() {
        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);

        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        NomorReferensi.setCellValueFactory(new PropertyValueFactory<>("nomorReferensi"));
        MetodePembayaran.setCellValueFactory(new PropertyValueFactory<>("metodePembayaran"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        Tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        Admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        Toko.setCellValueFactory(new PropertyValueFactory<>("toko"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        Tipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));

        Aksi.setCellFactory(new Callback<TableColumn<Transaksi, Void>, TableCell<Transaksi, Void>>() {
            @Override
            public TableCell<Transaksi, Void> call(final TableColumn<Transaksi, Void> param) {
                final TableCell<Transaksi, Void> cell = new TableCell<Transaksi, Void>() {

                    private final CheckBox checkBox = new CheckBox();
                    private final Button btnDetail = new Button("Detail");

                    {
                        checkBox.setOnAction((ActionEvent event) -> {
                            Transaksi transaksi = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {
                                boolean confirmed = showConfirmationDialog("Confirmation",
                                        "Are you sure you want to update the status to 'berhasil'?");

                                if (confirmed) {
                                    updateStatus(transaksi.getId(), "berhasil");
                                    transaksi.setStatus("berhasil");
                                    checkBox.setDisable(true);
                                } else {
                                    checkBox.setSelected(false);
                                }
                            } else {
                                boolean confirmed = showConfirmationDialog("Confirmation",
                                        "Are you sure you want to update the status to 'menunggu'?");

                                if (confirmed) {
                                    updateStatus(transaksi.getId(), "menunggu");
                                    transaksi.setStatus("menunggu");
                                } else {
                                    checkBox.setSelected(true);
                                }
                            }
                            getTableView().refresh();
                        });

                        btnDetail.setOnAction((ActionEvent event) -> {
                            try {
                                Transaksi transaksi = getTableView().getItems().get(getIndex());
                                switchToDetailView(transaksi, event);
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
                            Transaksi transaksi = getTableView().getItems().get(getIndex());
                            checkBox.setSelected("berhasil".equals(transaksi.getStatus()));
                            checkBox.setDisable("berhasil".equals(transaksi.getStatus()));
                            GridPane gridPane = new GridPane();
                            gridPane.setHgap(10);
                            gridPane.add(checkBox, 0, 0);
                            gridPane.add(btnDetail, 1, 0);
                            setGraphic(gridPane);
                        }
                    }
                };
                return cell;
            }
        });
        transaksis = Transaksi.getTransaksisFromDatabase();
        TransaksiTable.setItems(transaksis);
    }

    @FXML
    void FilterTanggal(ActionEvent event) throws IOException {
        ObservableList<Transaksi> FilterList = FXCollections.observableArrayList();
        Date dariTanggal = (DariTanggal.getValue() != null) ? Date.valueOf(DariTanggal.getValue()) : null;
        Date sampaiTanggal = (SampaiTanggal.getValue() != null) ? Date.valueOf(SampaiTanggal.getValue()) : null;

        for (Transaksi transaksiOut : transaksis) {
            Date tgl = Date.valueOf(transaksiOut.getTanggal());
            if ((dariTanggal == null || tgl.compareTo(dariTanggal) >= 0)
                    && (sampaiTanggal == null || tgl.compareTo(sampaiTanggal) <= 0)) {
                FilterList.add(transaksiOut);
            }
        }
        TransaksiTable.setItems(FilterList);
        TransaksiTable.refresh();
    }

    private void updateStatus(int transaksiId, String newStatus) {
        Connection connection = null;
        PreparedStatement updateStatusStmt = null;
        PreparedStatement selectDetailStmt = null;
        PreparedStatement updateBarangStmt = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");

            String updateStatusQuery = "UPDATE transaksi SET status = ? WHERE id = ?";
            updateStatusStmt = connection.prepareStatement(updateStatusQuery);
            updateStatusStmt.setString(1, newStatus);
            updateStatusStmt.setInt(2, transaksiId);
            updateStatusStmt.executeUpdate();

            if ("berhasil".equals(newStatus)) {
                String selectDetailQuery = "SELECT idBarang, jumlah FROM transaksidetail WHERE idTransaksi = ?";
                selectDetailStmt = connection.prepareStatement(selectDetailQuery);
                selectDetailStmt.setInt(1, transaksiId);
                ResultSet resultSet = selectDetailStmt.executeQuery();

                String updateBarangQuery = "UPDATE barang SET stok = stok - ? WHERE id = ?";
                updateBarangStmt = connection.prepareStatement(updateBarangQuery);

                while (resultSet.next()) {
                    int idBarang = resultSet.getInt("idBarang");
                    int jumlah = resultSet.getInt("jumlah");

                    updateBarangStmt.setInt(1, jumlah);
                    updateBarangStmt.setInt(2, idBarang);
                    updateBarangStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (updateStatusStmt != null)
                    updateStatusStmt.close();
                if (selectDetailStmt != null)
                    selectDetailStmt.close();
                if (updateBarangStmt != null)
                    updateBarangStmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Modality.APPLICATION_MODAL makes sure that the user cannot interact with
        // other windows until they close the dialog
        alert.initModality(Modality.APPLICATION_MODAL);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void switchToDetailView(Transaksi transaksi, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/TransaksiOutDetail.fxml"));
        Parent root = loader.load();

        TransaksiOutDetailController controller = loader.getController();
        controller.setTransaksiId(transaksi.getId());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
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
    private void switchToTransaksiOutCrud(ActionEvent event) throws IOException {
        switchScene("TransaksiOutCrud.fxml", event);
    }

    @FXML
    private void switchToPesanan(ActionEvent event) throws IOException {
        switchScene("PesananView.fxml", event);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        switchScene("login.fxml", event);
    }
}
