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
import data.Pesanan;
import data.SharedData;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class TransaksiInCrudController {

    @FXML
    private Button AdminBtn, BarangBtn, DashboardBtn, TransaksiOutBtn, TransaksiInBtn, TambahTransaksiOut, PesananBtn, PemasokBtn, PelangganBtn, KembaliBtn, KeluarBtn;

    @FXML
    private TableView<Barang> BarangTable;

    @FXML
    private TableColumn<Barang, String> KodeBarang, NamaBarang;

    @FXML
    private TableColumn<Barang, Integer> JumlahStok;

    @FXML
    private ComboBox<Pesanan> idPesanan;

    @FXML
    private ComboBox<String> metodePembayaran;

    @FXML
    private Label identitas;

    @FXML
    private TextField id;

    @FXML
    private TextArea Alamat;

    @FXML
    private TextField idPemasok;

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
        ObservableList<Pesanan> pesananList = Pesanan.getPesanansFromDatabase();
        ObservableList<Pesanan> filteredPesananList = pesananList.filtered(p -> "menunggu".equals(p.getStatus()));

        idPesanan.setItems(filteredPesananList);
        idPesanan.setConverter(new javafx.util.StringConverter<Pesanan>() {
            @Override
            public String toString(Pesanan pesanan) {
                return String.valueOf(pesanan.getId());
            }

            @Override
            public Pesanan fromString(String string) {
                return filteredPesananList.stream().filter(p -> String.valueOf(p.getId()).equals(string)).findFirst().orElse(null);
            }
        });

        // Set event listener for idPesanan ComboBox
        idPesanan.setOnAction(event -> {
            Pesanan selectedPesanan = idPesanan.getSelectionModel().getSelectedItem();
            if (selectedPesanan != null) {
                idPemasok.setText(String.valueOf(selectedPesanan.getNama())); // Use getId() instead of getNama()
                Alamat.setText(selectedPesanan.getAlamat());
                metodePembayaran.setValue(selectedPesanan.getMetodePembayaran());
                tanggal.setValue(LocalDate.parse(selectedPesanan.getTanggal()));

                // Load Barang for the selected Pesanan
                loadBarangForPesanan(selectedPesanan.getId());
            }
        });

        // Set up the BarangTable
        KodeBarang.setCellValueFactory(new PropertyValueFactory<>("Kode"));
        NamaBarang.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        JumlahStok.setCellValueFactory(new PropertyValueFactory<>("Stok"));
        BarangTable.setItems(selectedBarang);
    }

    private void loadBarangForPesanan(int idPesanan) {
        selectedBarang.clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "")) {
            String query = "SELECT b.id, b.kode, b.nama, pd.jumlah FROM barang b " +
                           "JOIN pesanandetail pd ON b.id = pd.idBarang WHERE pd.idPesanan = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, idPesanan);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String kode = rs.getString("kode");
                    String nama = rs.getString("nama");
                    int stok = rs.getInt("jumlah");
                    selectedBarang.add(new Barang(id, kode, nama, stok));
                }
                BarangTable.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void createTransaksiIn(ActionEvent event) {
        System.out.println("Admin ID: " + currentAdmin);
        Pesanan selectedPesanan = idPesanan.getSelectionModel().getSelectedItem();
        if (selectedPesanan == null) {
            showAlert("No Pesanan Selected", "Please select a Pesanan.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "")) {
            String insertTransaksiInQuery = "INSERT INTO transaksi (idAdmin, tanggal, metodePembayaran, status, tipe, nomorReferensi, idPemasok, idPesanan) VALUES (?, ?, ?, 'menunggu', 'masuk', ?, ?, ?)";
            try (PreparedStatement psTransaksiIn = connection.prepareStatement(insertTransaksiInQuery, Statement.RETURN_GENERATED_KEYS)) {
                psTransaksiIn.setInt(1, currentAdmin);
                psTransaksiIn.setDate(2, Date.valueOf(tanggal.getValue()));
                psTransaksiIn.setString(3, metodePembayaran.getSelectionModel().getSelectedItem());
                psTransaksiIn.setString(4, nomorReferensi.getText());
                psTransaksiIn.setInt(5, selectedPesanan.getIdPemasok()); // Correctly insert idPemasok
                psTransaksiIn.setInt(6, selectedPesanan.getId());

                psTransaksiIn.executeUpdate();
                ResultSet rs = psTransaksiIn.getGeneratedKeys();
                if (rs.next()) {
                    int transaksiInId = rs.getInt(1);
                    String insertDetailQuery = "INSERT INTO transaksidetail (idTransaksi, idBarang, jumlah) VALUES (?, ?, ?)";
                    try (PreparedStatement psDetail = connection.prepareStatement(insertDetailQuery)) {
                        for (Barang barang : selectedBarang) {
                            psDetail.setInt(1, transaksiInId);
                            psDetail.setInt(2, barang.getId());
                            psDetail.setInt(3, barang.getStok());
                            psDetail.addBatch();
                        }
                        psDetail.executeBatch();
                    }
                }

                // Update the status of the selected Pesanan
                String updatePesananQuery = "UPDATE pesanan SET status = 'sedang proses' WHERE id = ?";
                try (PreparedStatement psUpdatePesanan = connection.prepareStatement(updatePesananQuery)) {
                    psUpdatePesanan.setInt(1, selectedPesanan.getId());
                    psUpdatePesanan.executeUpdate();
                }

                // If successful, clear the form and reset
                clearForm();
                // Refresh the page by reloading the scene
                switchScene("TransaksiInView.fxml", event);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
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
            idPesanan.getSelectionModel().clearSelection();
            metodePembayaran.getSelectionModel().clearSelection();
            id.clear();
            nomorReferensi.clear();
            Alamat.clear();
            tanggal.setValue(null);
            idPemasok.clear();
            selectedBarang.clear();
            BarangTable.refresh();
        }
        
        @FXML
        void kembaliBtn(ActionEvent event) throws IOException {
            switchScene("TransaksiOutView.fxml", event);
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