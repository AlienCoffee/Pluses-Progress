package tk.pluses.plusesprogress.lists.users;

public class UserListEntity {

    public final int ID;
    private String name = null;
    private int totalPluses = -1,
                localPluses = -1;

    private String [] results;
    private String mask;

    public UserListEntity (int id, int size) {
        this.results = new String [size];
        this.ID = id;
    }

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }

    public int getTotalPluses () { return totalPluses; }
    public void setLocalPluses (int localPluses) { this.localPluses = localPluses; }

    public int getLocalPluses () { return localPluses; }
    public void setTotalPluses (int localPluses) { this.localPluses = localPluses; }

    public void registerAttempt (int index, String result) {
        if (index < 0 && index >= results.length) {
            throw new ArrayIndexOutOfBoundsException (index);
        }

        if (!result.equals ("true") && !result.equals ("false")) {
            throw new IllegalArgumentException (result);
        }

        results [index] = result;
        if (result.equals ("true") && results [index].equals ("false")) {
            totalPluses ++;
            localPluses ++;
        } else if (result.equals ("false") && results [index].equals ("true")) {
            totalPluses --;
            localPluses --;
        }
    }

    public String getMask () {
        StringBuilder sb = new StringBuilder ();
        for (int i = 0; i < results.length; i ++) {
            if (results [i].equals ("true")) {
                sb.append ("+ ");
            } else {
                sb.append ("- ");
            }
        }

        return sb.toString ();
    }

}
