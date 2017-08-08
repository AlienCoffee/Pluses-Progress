package tk.pluses.plusesprogress.lists.users;

/**
 * Created by Андрей on 05.08.2017.
 */

public class ProblemEntity {

    public final int INDEX;
    public final String NAME;
    public final boolean RATING;

    public ProblemEntity (int index, String name, int rating) {
        this.RATING = rating == 1 ? true : false;
        this.INDEX = index;
        this.NAME = name;
    }

}
