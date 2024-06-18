package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import data.Barang;
import data.Pelanggan;
import data.SharedData;

import java.io.IOException;
import java.sql.*;
public class TransaksiOutCrudController {

    @FXML
    private Button AdminBtn,BarangBtn,DashboardBtn, TransaksiOutBtn, TransaksiInBtn, TambahTransaksiOut, TambahBarang, PesananBtn, PemasokBtn, PelangganBtn, KembaliBtn, KeluarBtn;

    @FXML
    private TableView<Barang> BarangTable;

    @FXML
    private TableColumn<Barang, String> KodeBarang, NamaBarang;
    
    @FXML
    private TableColumn<Barang, Integer> JumlahStok;

    @FXML
    private ComboBox<Barang> idBarang;

    @FXML
    private ComboBox<Pelanggan> idPelanggan;

    @FXML
    private ComboBox<String> metodePembayaran;

    @FXML
    private Label identitas;

    @FXML
    private TextField jumlah;

    @FXML
    private TextField id;

    @FXML
    private TextArea Alamat;

    @FXML
    private TextField namaToko;

    @FXML
    private TextField nomorReferensi;

    @FXML
    private DatePicker tanggal;

    private ObservableList<Barang> selectedBarang = FXCollections.observableArrayList();
    public int currentAdmin;

    @FXML
    public void initialize() {
        currentAdmin = SharedData.getInstance().getAdminId();
        System.out.println("Admin ID: " + currentAdmin);

        String currentNickname = SharedData.getInstance().getNickname();
        identitas.setText("Hello, " + currentNickname);

        // Populate ComboBoxes
        ObservableList<Barang> barangList = Barang.getBarangsFromDatabase();
        ObservableList<Pelanggan> pelangganList = Pelanggan.getPelanggansFromDatabase();

        idBarang.setItems(barangList);
        idBarang.setConverter(new javafx.util.StringConverter<Barang>() {
            @Override
            public String toString(Barang barang) {
                return barang.getNama();
            }

            @Override
            public Barang fromString(String string) {
                return barangList.stream().filter(b -> b.getNama().equals(string)).findFirst().orElse(null);
            }
        });

        idPelanggan.setItems(pelangganList);
        idPelanggan.setConverter(new javafx.util.StringConverter<Pelanggan>() {
            @Override
            public String toString(Pelanggan pelanggan) {
                return pelanggan.getNama();
            }

            @Override
            public Pelanggan fromString(String string) {
                return pelangganList.stream().filter(p -> p.getNama().equals(string)).findFirst().orElse(null);
            }
        });

        metodePembayaran.setItems(FXCollections.observableArrayList("Cash", "Transfer"));

        // Set event listener for idPelanggan ComboBox
        idPelanggan.setOnAction(event -> {
            Pelanggan selectedPelanggan = idPelanggan.getSelectionModel().getSelectedItem();
            if (selectedPelanggan != null) {
                namaToko.setText(selectedPelanggan.getNamaToko());
                Alamat.setText(selectedPelanggan.getAlamat());
            }
        });

        // Set up the BarangTable
        KodeBarang.setCellValueFactory(new PropertyValueFactory<>("Kode"));
        NamaBarang.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        JumlahStok.setCellValueFactory(new PropertyValueFactory<>("Stok"));
        BarangTable.setItems(selectedBarang);
    }

    @FXML
    void createTransaksiOut(ActionEvent event) {
        System.out.println("Admin ID: " + currentAdmin);
        System.out.println("Pelanggan ID: " + idPelanggan.getSelectionModel().getSelectedItem().getId());
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "")) {
            String insertTransaksiOutQuery = "INSERT INTO transaksi (idAdmin, idPelanggan, tanggal, metodePembayaran,nomorReferensi, tipe, status) VALUES (?, ?, ?, ?, ?,'keluar', 'menunggu')";
            try (PreparedStatement psTransaksiOut = connection.prepareStatement(insertTransaksiOutQuery, Statement.RETURN_GENERATED_KEYS)) {
                psTransaksiOut.setInt(1, currentAdmin);
                psTransaksiOut.setInt(2, idPelanggan.getSelectionModel().getSelectedItem().getId());
                psTransaksiOut.setDate(3, Date.valueOf(tanggal.getValue()));
                psTransaksiOut.setString(4, metodePembayaran.getSelectionModel().getSelectedItem());
                psTransaksiOut.setString(5, nomorReferensi.getText());

                psTransaksiOut.executeUpdate();
                ResultSet rs = psTransaksiOut.getGeneratedKeys();
                if (rs.next()) {
                    int transaksiOutId = rs.getInt(1);
                    String insertDetailQuery = "INSERT INTO transaksidetail (idTransaksi, idBarang, jumlah) VALUES (?, ?, ?)";
                    try (PreparedStatement psDetail = connection.prepareStatement(insertDetailQuery)) {
                        for (Barang barang : selectedBarang) {
                            psDetail.setInt(1, transaksiOutId);
                            psDetail.setInt(2, barang.getId());
                            psDetail.setInt(3, barang.getStok());
                            psDetail.addBatch();
                        }
                        psDetail.executeBatch();
                    }
                }
                // If successful, clear the form and reset
                clearForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void tambahBarang(ActionEvent event) {
        Barang selected = idBarang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                int jumlahStok = Integer.parseInt(jumlah.getText());
                if (jumlahStok > 0) {
                    Barang barangWithJumlah = new Barang(selected.getId(), selected.getKode(), selected.getNama(), jumlahStok);
                    selectedBarang.add(barangWithJumlah);
                    BarangTable.refresh();
                } else {
                    showAlert("Invalid Amount", "Jumlah must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Jumlah must be a number.");
            }
        } else {
            showAlert("No Selection", "Please select a barang.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        idBarang.getSelectionModel().clearSelection();
        idPelanggan.getSelectionModel().clearSelection();
        metodePembayaran.getSelectionModel().clearSelection();
        id.clear();
        jumlah.clear();
        Alamat.clear();
        tanggal.setValue(null);
        namaToko.clear();
        selectedBarang.clear();
        BarangTable.refresh();
    }

    @FXML
    void kembaliBtn(ActionEvent event) throws IOException {
        switchToTransaksiOut(event);
    }

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
    void switchToPemasokCrud(ActionEvent event) throws IOException {
        switchScene("PemasokCrud.fxml", event);
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
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

}
