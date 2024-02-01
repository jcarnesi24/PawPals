package coms309;

public class UserRepository {

    public User[] users;

    public UserRepository(User[] users) {
        this.users = users;
    }
    public User[] list_users() {
        return users;
    }

    public User find_by_id(int userId) {
        int i = 0;

        for (i = 0; i < users.length; ++i) {
            if (users[i].id == userId) {
                return users[i];
            }
        }
        return null;
    }
}
