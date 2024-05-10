package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class AdminDAO {

    public static ObservableList<Admin> getAdminsFromDatabase() {
        ObservableList<Admin> admins = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Menghubungkan ke database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itemmanager", "root", "");

            // Menjalankan query untuk mengambil data admin
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT Id, Username, Nickname, Password FROM admin");

            // Memproses hasil query dan menambahkannya ke dalam ObservableList
            while (resultSet.next()) {
                int Id = resultSet.getInt("Id");
                String Username = resultSet.getString("Username");
                String Nickname = resultSet.getString("Nickname");
                String Password = resultSet.getString("Password");

                admins.add(new Admin(Id, Username, Nickname, Password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Menutup koneksi, statement, dan resultSet
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return admins;
    }
}
