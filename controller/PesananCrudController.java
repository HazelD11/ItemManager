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
import data.Pemasok;
import data.SharedData;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class PesananCrudController {

    @FXML
    private Button AdminBtn, BarangBtn, DashboardBtn, KeluarBtn, KembaliBtn, PelangganBtn, PemasokBtn, PesananBtn, TambahPesanan, TransaksiInBtn, TransaksiOutBtn, TambahBarang;

    @FXML
    private TableView<Barang> BarangTable;

    @FXML
    private TableColumn<Barang, String> KodeBarang, NamaBarang;
    
    @FXML
    private TableColumn<Barang, Integer> JumlahStok;

    @FXML
    private ComboBox<Barang> idBarang;

    @FXML
    private ComboBox<Pemasok> idPemasok;

    @FXML
    private ComboBox<String> metodePembayaran;

    @FXML
    private TextField id;

    @FXML
    private TextField jumlah;

    @FXML
    private TextArea Alamat;

    @FXML
    private Label identitas;
    
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

        // Set default value for tanggal DatePicker to today's date in GMT+7 timezone
        LocalDate today = LocalDate.now(ZoneId.of("GMT+7"));
        tanggal.setValue(today);
        
        // Populate ComboBoxes
        ObservableList<Barang> barangList = Barang.getBarangsFromDatabase();
        ObservableList<Pemasok> pemasokList = Pemasok.getPemasoksFromDatabase();

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

        idPemasok.setItems(pemasokList);
        idPemasok.setConverter(new javafx.util.StringConverter<Pemasok>() {
            @Override
            public String toString(Pemasok pemasok) {
                return pemasok.getNama();
            }

            @Override
            public Pemasok fromString(String string) {
                return pemasokList.stream().filter(p -> p.getNama().equals(string)).findFirst().orElse(null);
            }
        });

        metodePembayaran.setItems(FXCollections.observableArrayList("Cash", "Transfer"));

        // Set event listener for idPemasok ComboBox
        idPemasok.setOnAction(event -> {
            Pemasok selectedPemasok = idPemasok.getSelectionModel().getSelectedItem();
            if (selectedPemasok != null) {
                Alamat.setText(selectedPemasok.getAlamat());
            }
        });

        // Set up the BarangTable
        KodeBarang.setCellValueFactory(new PropertyValueFactory<>("Kode"));
        NamaBarang.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        JumlahStok.setCellValueFactory(new PropertyValueFactory<>("Stok"));
        BarangTable.setItems(selectedBarang);
    }

    @FXML
    void createPesanan(ActionEvent event) {
        System.out.println("Admin ID: " + currentAdmin);
        System.out.println("Pemasok ID: " + idPemasok.getSelectionModel().getSelectedItem().getId());
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "")) {
            String insertPesananQuery = "INSERT INTO pesanan (idAdmin, idPemasok, tanggal, metodePembayaran, tipe, status) VALUES (?, ?, ?, ?, 'disetujui', 'menunggu')";
            try (PreparedStatement psPesanan = connection.prepareStatement(insertPesananQuery, Statement.RETURN_GENERATED_KEYS)) {
                psPesanan.setInt(1, currentAdmin);
                psPesanan.setInt(2, idPemasok.getSelectionModel().getSelectedItem().getId());
                psPesanan.setDate(3, Date.valueOf(tanggal.getValue()));
                psPesanan.setString(4, metodePembayaran.getSelectionModel().getSelectedItem());

                psPesanan.executeUpdate();
                ResultSet rs = psPesanan.getGeneratedKeys();
                if (rs.next()) {
                    int pesananId = rs.getInt(1);
                    String insertDetailQuery = "INSERT INTO pesanandetail (idPesanan, idBarang, jumlah) VALUES (?, ?, ?)";
                    try (PreparedStatement psDetail = connection.prepareStatement(insertDetailQuery)) {
                        for (Barang barang : selectedBarang) {
                            psDetail.setInt(1, pesananId);
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
        idPemasok.getSelectionModel().clearSelection();
        metodePembayaran.getSelectionModel().clearSelection();
        id.clear();
        jumlah.clear();
        Alamat.clear();
        tanggal.setValue(null);
        selectedBarang.clear();
        BarangTable.refresh();
    }

    @FXML
    void kembaliBtn(ActionEvent event) throws IOException {
        switchToPesanan(event);
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
