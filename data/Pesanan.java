
package data;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Pesanan {
    int id;
    int idPemasok; // Add this field to hold the id of the Pemasok
    String admin, nama, alamat, metodePembayaran, status, tipe, tanggal;

    public Pesanan(int id, int idPemasok, String admin, String nama, String alamat, String metodePembayaran, String status, String tipe, String tanggal) {
        this.id = id;
        this.idPemasok = idPemasok;
        this.admin = admin;
        this.nama = nama;
        this.alamat = alamat;
        this.metodePembayaran = metodePembayaran;
        this.status = status;
        this.tipe = tipe;
        this.tanggal = tanggal;
    }

    @Override
    public String toString() {
        return "Pesanan{" +
                "id=" + id +
                ", idPemasok=" + idPemasok +
                ", admin=" + admin +
                ", nama=" + nama +
                ", alamat=" + alamat +
                ", metodePembayaran=" + metodePembayaran +
                ", status=" + status +
                ", tipe=" + tipe +
                ", tanggal=" + tanggal +
                '}';
    }

    public static ObservableList<Pesanan> getPesanansFromDatabase() {
        ObservableList<Pesanan> Pesanans = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT p.id AS pid, p.idPemasok AS pidPemasok, p.metodePembayaran AS mtdpb, p.status AS ts,"+
                                               " p.tipe AS tp, p.tanggal AS tgl, ad.nickname AS adn, pmk.nama AS pmknm,"+
                                               " pmk.alamat AS pmkalmt FROM pesanan p "+
                                               "JOIN admin ad ON ad.id = p.idAdmin"+
                                               " JOIN pemasok pmk ON pmk.id = p.idPemasok "+
                                               " ORDER BY CASE WHEN p.status='menunggu' THEN 1 "+
                                               " WHEN p.status='sedang proses' THEN 2 "+
                                               " WHEN p.status='berhasil' THEN 3 "+
                                               " ELSE 4 END, p.id ASC");

            while (resultSet.next()) {
                int Id = resultSet.getInt("pid");
                int idPemasok = resultSet.getInt("pidPemasok");
                String Admin = resultSet.getString("adn");
                String Nama = resultSet.getString("pmknm");
                String Alamat = resultSet.getString("pmkalmt");
                String MetodePembayaran = resultSet.getString("mtdpb");
                String Status = resultSet.getString("ts");
                String Tipe = resultSet.getString("tp");
                String Tanggal = resultSet.getString("tgl");
                Pesanans.add(new Pesanan(Id, idPemasok, Admin, Nama, Alamat, MetodePembayaran, Status, Tipe, Tanggal));
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

        return Pesanans;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPemasok() {
        return idPemasok;
    }

    public void setIdPemasok(int idPemasok) {
        this.idPemasok = idPemasok;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
