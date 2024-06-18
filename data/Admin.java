package data;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Admin {
    private int Id;
    private String Username;
    private String Nickname;
    private String Password;
    private String Role;

    public Admin(int Id, String Username, String Nickname, String Password, String Role) {
        this.Id = Id;
        this.Username = Username;
        this.Nickname = Nickname;
        this.Password = Password;
        this.Role = Role;
    }

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
            resultSet = statement.executeQuery("SELECT id, username, nickname, password, role FROM admin");

            // Memproses hasil query dan menambahkannya ke dalam ObservableList
            while (resultSet.next()) {
                int Id = resultSet.getInt("Id");
                String Username = resultSet.getString("Username");
                String Nickname = resultSet.getString("Nickname");
                String Password = resultSet.getString("Password");
                String Role = resultSet.getString("Role");

                admins.add(new Admin(Id, Username, Nickname, Password, Role));
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    
}
