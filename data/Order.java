package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Order {
    private String id;
    private String pemasok;
    private String barang;
    private int jumlah;
    private String tanggal;
    private String status;

    public Order(String id, String pemasok, String barang, int jumlah, String tanggal, String status) {
        this.id = id;
        this.pemasok = pemasok;
        this.barang = barang;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.status = status;
    }

    public static ObservableList<Order> getOrderList() {
        Order order1 = new Order("001", "Supplier A", "Product X", 10, "2024-05-01", "Pending");
        Order order2 = new Order("002", "Supplier B", "Product Y", 15, "2024-05-02", "Approved");
        
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        orderList.add(order1);
        orderList.add(order2);
        
        return orderList;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPemasok() {
        return pemasok;
    }

    public void setPemasok(String pemasok) {
        this.pemasok = pemasok;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
