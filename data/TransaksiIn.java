package data;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransaksiIn {
    int id;
    String admin, nama, nomorPesanan, alamat, metodePembayaran, status, tipe, nomorReferensi, tanggal;

    public TransaksiIn(int id, String admin, String nama, String nomorPesanan, String alamat, String metodePembayaran, String status, String tipe, String nomorReferensi,String tanggal) {
                        this.id = id;
                        this.admin = admin;
                        this.nama = nama;
                        this.nomorPesanan = nomorPesanan;
                        this.alamat = alamat;
                        this.metodePembayaran = metodePembayaran;
                        this.status = status;
                        this.tipe = tipe;
                        this.nomorReferensi = nomorReferensi;
                        this.tanggal = tanggal;
    }

    @Override
    public String toString() {
        return "Transaksi{" +
                "id=" + id +
                ", admin=" + admin +
                ", nama=" + nama +
                ", nomorPesanan=" + nomorPesanan +
                ", alamat=" + alamat +
                ", metodePembayaran=" + metodePembayaran +
                ", status=" + status +
                ", tipe=" + tipe +
                ", nomorReferensi=" + nomorReferensi +
                ", tanggal=" + tanggal +
                '}';
    }

    public static ObservableList<TransaksiIn> getTransaksiInsFromDatabase() {
        ObservableList<TransaksiIn> TransaksiIns = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT t.id AS tid, t.metodePembayaran AS mtdpb, t.status AS ts,"+
                                                " t.tipe AS tp, t.nomorReferensi AS tnr, t.tanggal AS tgl, p.id AS pid,"+
                                                " ad.nickname AS adn, pmk.nama AS pmknm, pmk.alamat AS pmkalmt FROM transaksi t "+
                                                "JOIN admin ad ON ad.id = t.idAdmin"+
                                                " JOIN pemasok pmk ON pmk.id = t.idPemasok "+
                                                "JOIN pesanan p ON p.id = t.idPesanan"+
                                                " WHERE t.tipe='masuk'"+
                                                " ORDER BY CASE WHEN t.status='menunggu' THEN 1 ELSE 2 END, t.id ASC");

            while (resultSet.next()) {
                int Id = resultSet.getInt("tid");
                String Admin = resultSet.getString("adn");
                String Nama = resultSet.getString("pmknm");
                String NomorPesanan = resultSet.getString("pid");
                String Alamat = resultSet.getString("pmkalmt");
                String MetodePembayaran = resultSet.getString("mtdpb");
                String Status = resultSet.getString("ts");
                String Tipe = resultSet.getString("tp");
                String NomorReferensi = resultSet.getString("tnr");
                String Tanggal = resultSet.getString("tgl");
                TransaksiIns.add(new TransaksiIn(Id,Admin,Nama,NomorPesanan,Alamat,MetodePembayaran,Status,Tipe,NomorReferensi,Tanggal));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return TransaksiIns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorPesanan() {
        return nomorPesanan;
    }

    public void setNomorPesanan(String nomorPesanan) {
        this.nomorPesanan = nomorPesanan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getNomorReferensi() {
        return nomorReferensi;
    }

    public void setNomorReferensi(String nomorReferensi) {
        this.nomorReferensi = nomorReferensi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }      
}
