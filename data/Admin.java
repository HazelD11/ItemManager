package data;

import javafx.collections.ObservableList;

public class Admin {
    private int Id;
    private String Username;
    private String Nickname;
    private String Password;

    public Admin(int Id, String Username, String Nickname, String Password) {
        this.Id = Id;
        this.Username = Username;
        this.Nickname = Nickname;
        this.Password = Password;
    }

    // Hapus metode acc()

    // Getter dan setter

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
}
