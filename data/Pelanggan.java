package data;

import java.sql.*;

import conn.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Pelanggan {
    int Id;
    String Nama, NamaToko, Alamat;

    public Pelanggan(int Id, String Nama, String NamaToko, String Alamat) {
        this.Id = Id;
        this.Nama = Nama;
        this.NamaToko = NamaToko;
        this.Alamat = Alamat;
    }

    @Override
    public String toString() {
        return "Pelanggan{" +
                "id=" + Id +
                ", nama=" + Nama +
                ", namaToko=" + NamaToko +
                ", alamat=" + Alamat +
                '}';
    }

    public static ObservableList<Pelanggan> getPelanggansFromDatabase() {
        ObservableList<Pelanggan> pelanggans = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id, nama, namaToko, alamat FROM pelanggan");

            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                String Nama = resultSet.getString("nama");
                String NamaToko = resultSet.getString("namaToko");
                String Alamat = resultSet.getString("alamat");

                pelanggans.add(new Pelanggan(Id, Nama, NamaToko, Alamat));
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

        return pelanggans;
    }

    public static boolean deletePelanggan(int id) {
        String query = "DELETE FROM pelanggan WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getNamaToko() {
        return NamaToko;
    }

    public void setNamaToko(String namaToko) {
        this.NamaToko = namaToko;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }
   
    
}
