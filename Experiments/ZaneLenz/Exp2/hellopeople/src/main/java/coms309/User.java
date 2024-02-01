package coms309;

public class User {
    public String userName;
    public int id;

    public User(String userName, int id) {
        this.userName = userName;
        this.id = id;
    }

    public String get_username() {
        return userName;
    }

    public int get_id() {
        return id;
    }

    public void update_username(String name) {
        userName = name;
    }

    public void update_id(int num) {
        id = num;
    }
}
