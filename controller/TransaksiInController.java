package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
import data.TransaksiIn;

public class TransaksiInController {

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
    private Button TransaksiInCrudBtn;

    @FXML
    private Button TransaksiInBtn;

    @FXML
    private Button TransaksiOutBtn;

    @FXML
    private TableView<TransaksiIn> TransaksiTable;

    @FXML
    private TableColumn<TransaksiIn, Integer> Id;

    @FXML
    private TableColumn<TransaksiIn, String> NomorReferensi;

    @FXML
    private TableColumn<TransaksiIn, String> MetodePembayaran;

    @FXML
    private TableColumn<TransaksiIn, String> Status;

    @FXML
    private TableColumn<TransaksiIn, String> Tanggal;

    @FXML
    private TableColumn<TransaksiIn, String> Admin;

    @FXML
    private TableColumn<TransaksiIn, String> Nama;

    @FXML
    private TableColumn<TransaksiIn, String> NomorPesanan;

    @FXML
    private TableColumn<TransaksiIn, String> Alamat;

    @FXML
    private TableColumn<TransaksiIn, String> Tipe;

    @FXML
    private TableColumn<TransaksiIn, Void> Aksi;

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
    private void switchToTransaksiInCrud(ActionEvent event) throws IOException {
        switchScene("TransaksiInCrud.fxml", event);
    }

    @FXML
    private void switchToPesanan(ActionEvent event) throws IOException {
        switchScene("PesananView.fxml", event);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        switchScene("login.fxml", event);
    }

    @FXML
    void FilterTanggal(ActionEvent event) throws IOException {
        ObservableList<TransaksiIn> FilterList = FXCollections.observableArrayList();
        Date dariTanggal = (DariTanggal.getValue() != null) ? Date.valueOf(DariTanggal.getValue()) : null;
        Date sampaiTanggal = (SampaiTanggal.getValue() != null) ? Date.valueOf(SampaiTanggal.getValue()) : null;

        ObservableList<TransaksiIn> transaksiIns = TransaksiIn.getTransaksiInsFromDatabase();

        for (TransaksiIn transaksiIn : transaksiIns) {
            Date tgl = Date.valueOf(transaksiIn.getTanggal());
            if ((dariTanggal == null || tgl.compareTo(dariTanggal) >= 0) 
                &&
                (sampaiTanggal == null || tgl.compareTo(sampaiTanggal) <= 0)) {
                    FilterList.add(transaksiIn);
            }
        }
        TransaksiTable.setItems(FilterList);
        TransaksiTable.refresh();
    }

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
        NomorPesanan.setCellValueFactory(new PropertyValueFactory<>("nomorPesanan"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        Tipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));

        // Custom cell factory for "Aksi" column
        Aksi.setCellFactory(new Callback<TableColumn<TransaksiIn, Void>, TableCell<TransaksiIn, Void>>() {
            @Override
            public TableCell<TransaksiIn, Void> call(final TableColumn<TransaksiIn, Void> param) {
                final TableCell<TransaksiIn, Void> cell = new TableCell<TransaksiIn, Void>() {

                    private final CheckBox checkBox = new CheckBox();
                    private final Button btnDetail = new Button("Detail");

                    {
                        checkBox.setOnAction((ActionEvent event) -> {
                            TransaksiIn transaksiIn = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {
                                // Show confirmation dialog
                                boolean confirmed = showConfirmationDialog("Confirmation", "Are you sure you want to update the status to 'berhasil'?");

                                if (confirmed) {
                                    // Update the status to "berhasil" in the database
                                    updateStatus(transaksiIn.getId(), transaksiIn.getNomorPesanan(), "berhasil");
                                    transaksiIn.setStatus("berhasil");
                                    checkBox.setDisable(true);  // Disable the checkbox
                                } else {
                                    checkBox.setSelected(false);  // Uncheck the checkbox
                                }
                            }
                            getTableView().refresh();
                        });

                        btnDetail.setOnAction((ActionEvent event) -> {
                            try {
                                TransaksiIn transaksiIn = getTableView().getItems().get(getIndex());
                                switchToDetailView(transaksiIn, event);
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
                            TransaksiIn transaksiIn = getTableView().getItems().get(getIndex());
                            checkBox.setSelected("berhasil".equals(transaksiIn.getStatus()));
                            checkBox.setDisable("berhasil".equals(transaksiIn.getStatus()));  // Disable if status is "berhasil"
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

        ObservableList<TransaksiIn> transaksiIns = TransaksiIn.getTransaksiInsFromDatabase();
        TransaksiTable.setItems(transaksiIns);
    }

    private void updateStatus(int transaksiInId, String nomorPesanan, String newStatus) {
        Connection connection = null;
        PreparedStatement updateStatusStmt = null;
        PreparedStatement updatePesananStmt = null;
        PreparedStatement selectDetailStmt = null;
        PreparedStatement updateBarangStmt = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");

            // Update the status in the transaksi table
            String updateStatusQuery = "UPDATE transaksi SET status = ? WHERE id = ?";
            updateStatusStmt = connection.prepareStatement(updateStatusQuery);
            updateStatusStmt.setString(1, newStatus);
            updateStatusStmt.setInt(2, transaksiInId);
            updateStatusStmt.executeUpdate();

            // Update the status in the pesanan table only if the current status is "sedang proses"
            String updatePesananQuery = "UPDATE pesanan SET status = ? WHERE id = ? AND status = 'sedang proses'";
            updatePesananStmt = connection.prepareStatement(updatePesananQuery);
            updatePesananStmt.setString(1, newStatus);
            updatePesananStmt.setString(2, nomorPesanan);
            updatePesananStmt.executeUpdate();

            if ("berhasil".equals(newStatus)) {
                // Retrieve the TransaksiDetail records for the given transaksiId
                String selectDetailQuery = "SELECT idBarang, jumlah FROM transaksidetail WHERE idTransaksi = ?";
                selectDetailStmt = connection.prepareStatement(selectDetailQuery);
                selectDetailStmt.setInt(1, transaksiInId);
                ResultSet resultSet = selectDetailStmt.executeQuery();

                // Update the Barang table based on the TransaksiDetail records
                String updateBarangQuery = "UPDATE barang SET stok = stok + ? WHERE id = ?";
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
                if (updateStatusStmt != null) updateStatusStmt.close();
                if (updatePesananStmt != null) updatePesananStmt.close();
                if (selectDetailStmt != null) selectDetailStmt.close();
                if (updateBarangStmt != null) updateBarangStmt.close();
                if (connection != null) connection.close();
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

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void switchToDetailView(TransaksiIn transaksiIn, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/TransaksiInDetail.fxml"));
        Parent root = loader.load();

        // Get the controller of TransaksiInDetail
        TransaksiInDetailController controller = loader.getController();
        // Pass the transaksi Id to the controller
        controller.setTransaksiInId(transaksiIn.getId());

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
}
