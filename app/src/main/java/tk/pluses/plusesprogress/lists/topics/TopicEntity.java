package tk.pluses.plusesprogress.lists.topics;

public class TopicEntity {
    public final int ID;

    private String name = null;

    private int ratedCounter= -1,
            totalCounter= -1,
            authorID = -1;


    public TopicEntity(int id) {
        this.ID = id;
    }

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }

    public int getAuthorID () {
        return authorID;
    }
    public void setAuthorID (int authorID) {
        this.authorID= authorID;
    }

    public int getRatedCounter () {
        return ratedCounter;
    }
    public void setRatedCounter (int ratedCounter ) {
        this.ratedCounter = ratedCounter ;
    }

    public int getTotalCounter () {
        return totalCounter;
    }
    public void setTotalCounter (int totalCounter ) {
        this.totalCounter = totalCounter ;
    }


}
