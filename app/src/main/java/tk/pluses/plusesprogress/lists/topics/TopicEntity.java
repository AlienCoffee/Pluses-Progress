package tk.pluses.plusesprogress.lists.topics;

public class TopicEntity {
    public final int ID;
    private String name = null;

    public TopicEntity (int id) {
        this.ID = id;
    }

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
}
