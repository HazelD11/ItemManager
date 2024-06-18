package data;

import java.sql.*;

import conn.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Pemasok {
    int Id;
    String Nama, Alamat;

    public Pemasok(int Id, String Nama, String Alamat) {
        this.Id = Id;
        this.Nama = Nama;
        this.Alamat = Alamat;
    }

    @Override
    public String toString() {
        return "Pemasok{" +
                "id=" + Id +
                ", nama=" + Nama +
                ", alamat=" + Alamat +
                '}';
    }

    public static ObservableList<Pemasok> getPemasoksFromDatabase() {
        ObservableList<Pemasok> pemasoks = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id, nama, alamat FROM pemasok");

            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                String Nama = resultSet.getString("nama");
                String Alamat = resultSet.getString("alamat");
                pemasoks.add(new Pemasok(Id, Nama, Alamat));
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

        return pemasoks;
    }

    public static boolean deletePemasok(int id) {
        String query = "DELETE FROM pemasok WHERE id = ?";
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

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    
}
