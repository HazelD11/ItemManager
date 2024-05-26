package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;

public class OrderCrudController {

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
    private ComboBox<Supplier> pemasokComboBox;

    @FXML
    private TextField namaPemasokField;

    @FXML
    private TextField alamatPemasokField;

    @FXML
    private DatePicker tanggalOrderPicker;

    @FXML
    private TextField itemField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<Barang> itemsTableView;

    @FXML
    private TableColumn<Barang, String> itemColumn;

    @FXML
    private TableColumn<Barang, Integer> quantityColumn;

    private ObservableList<Barang> barangList = FXCollections.observableArrayList();

    public void initialize() {
        // Initialize the suppliers list (mock data for now)
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(
                new Supplier("Pemasok 1", "Alamat 1"),
                new Supplier("Pemasok 2", "Alamat 2")
        );

        pemasokComboBox.setItems(suppliers);
        pemasokComboBox.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                return supplier != null ? supplier.getName() : "";
            }

            @Override
            public Supplier fromString(String string) {
                for (Supplier supplier : suppliers) {
                    if (supplier.getName().equals(string)) {
                        return supplier;
                    }
                }
                return null;
            }
        });

        itemColumn.setCellValueFactory(data -> data.getValue().barangNameProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        itemsTableView.setItems(barangList);
    }

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

    @FXML
    void displayPemasokDetails(ActionEvent event) {
        Supplier selectedSupplier = pemasokComboBox.getSelectionModel().getSelectedItem();
        if (selectedSupplier != null) {
            namaPemasokField.setText(selectedSupplier.getName());
            alamatPemasokField.setText(selectedSupplier.getAddress());
        }
    }

    @FXML
    void addItem(ActionEvent event) {
        String itemName = itemField.getText();
        String quantityStr = quantityField.getText();
        if (!itemName.isEmpty() && !quantityStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                Barang newBarang = new Barang(itemName, quantity);
                barangList.add(newBarang);
                itemField.clear();
                quantityField.clear();
                refreshTableView();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for quantity.");
            }
        } else {
            showAlert("Input Missing", "Please fill in both item name and quantity.");
        }
    }

    @FXML
void submitOrder(ActionEvent event) {
    Supplier selectedSupplier = pemasokComboBox.getSelectionModel().getSelectedItem();
    LocalDate orderDate = tanggalOrderPicker.getValue();

    if (selectedSupplier == null || orderDate == null || barangList.isEmpty()) {
        showAlert("Incomplete Order", "Please fill in all fields and add at least one item.");
    } else {
        // Logic to handle order submission (e.g., save to database)
        showAlert("Order Submitted", "Your order has been successfully submitted.");

        // Clear the form
        clearForm();

        // Refresh the order page
        refreshOrderPage(event);
        refreshTableView();
    }
}

private void refreshOrderPage(ActionEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Order.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GridPane grid = new GridPane();
        grid.getChildren().add(root);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void refreshTableView() {
    itemsTableView.setItems(null);
    itemsTableView.layout();
    itemsTableView.setItems(barangList);
}


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        pemasokComboBox.getSelectionModel().clearSelection();
        namaPemasokField.clear();
        alamatPemasokField.clear();
        tanggalOrderPicker.setValue(null);
        barangList.clear();
    }

    public static class Supplier {
        private final String name;
        private final String address;

        public Supplier(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }

    public static class Barang {
        private final SimpleStringProperty barangName;
        private final SimpleIntegerProperty quantity;

        public Barang(String barangName, int quantity) {
            this.barangName = new SimpleStringProperty(barangName);
            this.quantity = new SimpleIntegerProperty(quantity);
        }

        public String getBarangName() {
            return barangName.get();
        }

        public SimpleStringProperty barangNameProperty() {
            return barangName;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public SimpleIntegerProperty quantityProperty() {
            return quantity;
        }
    }
}
