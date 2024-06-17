package data;

public class SharedData {
    private static SharedData instance = new SharedData();
    private String nickname;
    private int adminId;

    private SharedData() {}

    public static SharedData getInstance() {
        return instance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
