package data;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransaksiDetail {
    int id, jumlah;
    String  nomorReferensi, Barang;
    public TransaksiDetail(int id, String nomorReferensi,String Barang, int jumlah) {
        this.id = id;
        this.nomorReferensi = nomorReferensi;
        this.Barang = Barang;
        this.jumlah = jumlah;
    }

    @Override
    public String toString() {
        return "TransaksiDetail{" +
                "id=" + id +
                ", nomorReferensi=" + nomorReferensi +
                ", barang=" + Barang +
                ", jumlah=" + jumlah +
                '}';
    }

    public static ObservableList<TransaksiDetail> getTransaksiDetailsFromDatabase(int transaksiId) {
        ObservableList<TransaksiDetail> TransaksiDetails = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            String query = "SELECT td.id AS tdid, t.nomorReferensi AS tnr,"+
                            " b.nama AS bn , td.jumlah AS tdj FROM transaksidetail td "+
                            "JOIN transaksi t ON t.id = td.idTransaksi "+
                            "JOIN barang b ON b.id = td.idBarang WHERE idTransaksi = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, transaksiId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int Id = resultSet.getInt("tdid");
                String NomorReferensi = resultSet.getString("tnr");
                String Barang = resultSet.getString("bn");
                int Jumlah = resultSet.getInt("tdj");
                TransaksiDetails.add(new TransaksiDetail(Id, NomorReferensi, Barang, Jumlah));
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

        return TransaksiDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getNomorReferensi() {
        return nomorReferensi;
    }

    public void setNomorReferensi(String nomorReferensi) {
        this.nomorReferensi = nomorReferensi;
    }

    public String getBarang() {
        return Barang;
    }

    public void setBarang(String barang) {
        Barang = barang;
    }

   
}
