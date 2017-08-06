package tk.pluses.plusesprogress.lists.users;

public class UserListEntity {
    public final int ID;
    private String name = null;

    public UserListEntity (int id) {
        this.ID = id;
    }

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
}
