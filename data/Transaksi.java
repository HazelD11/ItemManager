package data;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Transaksi {
    int id;
    String admin, nama, toko, alamat, metodePembayaran, status, tipe, nomorReferensi, tanggal;

    public Transaksi(int id, String admin, String nama, String toko, String alamat, String metodePembayaran, String status, String tipe, String nomorReferensi,String tanggal) {
                        this.id = id;
                        this.admin = admin;
                        this.nama = nama;
                        this.toko = toko;
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
                ", toko=" + toko +
                ", alamat=" + alamat +
                ", metodePembayaran=" + metodePembayaran +
                ", status=" + status +
                ", tipe=" + tipe +
                ", nomorReferensi=" + nomorReferensi +
                ", tanggal=" + tanggal +
                '}';
    }

    public static ObservableList<Transaksi> getTransaksisFromDatabase() {
        ObservableList<Transaksi> Transaksis = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT t.id AS tid, t.metodePembayaran AS mtdpb, t.status AS ts,"+
                                                " t.tipe AS tp, t.nomorReferensi AS tnr, t.tanggal AS tgl,"+
                                                " ad.nickname AS adn, pl.nama AS pnama, pl.namaToko AS pntoko,"+
                                                " pl.alamat AS palmt FROM transaksi t JOIN admin ad ON ad.id = t.idAdmin"+
                                                " JOIN pelanggan pl ON pl.id = t.idPelanggan WHERE t.tipe='keluar'"+
                                                " ORDER BY CASE WHEN status='menunggu' THEN 1 ELSE 2 END, t.id ASC");

            while (resultSet.next()) {
                int Id = resultSet.getInt("tid");
                String Admin = resultSet.getString("adn");
                String Nama = resultSet.getString("pnama");
                String Toko = resultSet.getString("pntoko");
                String Alamat = resultSet.getString("palmt");
                String MetodePembayaran = resultSet.getString("mtdpb");
                String Status = resultSet.getString("ts");
                String Tipe = resultSet.getString("tp");
                String NomorReferensi = resultSet.getString("tnr");
                String Tanggal = resultSet.getString("tgl");
                Transaksis.add(new Transaksi(Id,Admin,Nama,Toko,Alamat,MetodePembayaran,Status,Tipe,NomorReferensi,Tanggal));
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

        return Transaksis;
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

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
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
