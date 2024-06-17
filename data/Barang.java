package data;

import java.sql.*;

import conn.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Barang {
    int Id, Stok, Minimal;
    String Nama, Status, Kode;

    public Barang(int Id, String Kode, String Nama, int Stok, int Minimal, String Status) {
        this.Id = Id;
        this.Nama = Nama;
        this.Kode = Kode;
        this.Stok = Stok;
        this.Minimal = Minimal;
        this.Status = Status;
    }

    public Barang(int Id, String Kode, String Nama, int Stok) {
        this.Id = Id;
        this.Kode = Kode;
        this.Nama = Nama;
        this.Stok = Stok;
        this.Minimal = 5; // Default value
        this.Status = getStatus(Stok, Minimal);
    }

    private static String getStatus(int stok, int minimal) {
        if (stok <= minimal && stok > 0) {
            return "Stok Menipis";
        } else if (stok > minimal) {
            return "Tersedia";
        } else {
            return "Tidak Tersedia";
        }
    }

    @Override
    public String toString() {
        return "Barang{" +
                "Id=" + Id +
                ", Stok=" + Stok +
                ", Minimal=" + Minimal +
                ", Nama='" + Nama + '\'' +
                ", Status='" + Status + '\'' +
                ", Kode='" + Kode + '\'' +
                '}';
    }

    public static ObservableList<Barang> getBarangsFromDatabase() {
        ObservableList<Barang> barangs = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id, kode, nama, stok FROM barang");

            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                int Stok = resultSet.getInt("stok");
                String Nama = resultSet.getString("nama");
                String Kode = resultSet.getString("kode");

                barangs.add(new Barang(Id, Kode, Nama, Stok));
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

        return barangs;
    }

    public static boolean deleteBarang(int id) {
        String query = "DELETE FROM Barang WHERE id = ?";
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

    public int getStok() {
        return Stok;
    }

    public void setStok(int stok) {
        Stok = stok;
    }

    public int getMinimal() {
        return Minimal;
    }

    public void setMinimal(int minimal) {
        Minimal = minimal;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getKode() {
        return Kode;
    }

    public void setKode(String kode) {
        Kode = kode;
    }    
    
}
