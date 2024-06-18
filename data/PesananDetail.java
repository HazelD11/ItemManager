package data;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PesananDetail {
    int id, jumlah,nomorPesanan;
    String Barang;
    public PesananDetail(int id, int nomorPesanan,String Barang, int jumlah) {
        this.id = id;
        this.nomorPesanan = nomorPesanan;
        this.Barang = Barang;
        this.jumlah = jumlah;
    }

    @Override
    public String toString() {
        return "TransaksiDetail{" +
                "id=" + id +
                ", nomorPesanan=" + nomorPesanan +
                ", barang=" + Barang +
                ", jumlah=" + jumlah +
                '}';
    }

    public static ObservableList<PesananDetail> getPesananDetailsFromDatabase(int pesananId) {
        ObservableList<PesananDetail> PesananDetails = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            String query = "SELECT pd.id AS pdid, pd.idPesanan AS pid,"+
                            " b.nama AS bn , pd.jumlah AS pdj FROM pesanandetail pd "+
                            "JOIN pesanan p ON p.id = pd.idPesanan "+
                            "JOIN barang b ON b.id = pd.idBarang WHERE idPesanan = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, pesananId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int Id = resultSet.getInt("pdid");
                int NomorPesanan = resultSet.getInt("pid");
                String Barang = resultSet.getString("bn");
                int Jumlah = resultSet.getInt("pdj");
                PesananDetails.add(new PesananDetail(Id, NomorPesanan, Barang, Jumlah));
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

        return PesananDetails;
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

    public int getNomorPesanan() {
        return nomorPesanan;
    }

    public void setNomorPesanan(int nomorPesanan) {
        this.nomorPesanan = nomorPesanan;
    }

    public String getBarang() {
        return Barang;
    }

    public void setBarang(String barang) {
        Barang = barang;
    }
    
   
}
