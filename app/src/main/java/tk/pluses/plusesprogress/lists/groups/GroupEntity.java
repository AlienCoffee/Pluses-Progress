package tk.pluses.plusesprogress.lists.groups;

/**
 * Created by Андрей on 05.08.2017.
 */

public class GroupEntity {

    /**
     *
     * */
    public final int ID;

    private String name;
    private int headTeacherID,
                groupSize,
                topicsNumber;

    public GroupEntity (int id) {
        this.ID = id;
    }

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }

    public int getHeadTeacherID () {
        return headTeacherID;
    }
    public void setHeadTeacherID (int headTeacherID) {
        this.headTeacherID = headTeacherID;
    }

    public int getGroupSize () {
        return groupSize;
    }
    public void setGroupSize (int groupSize) {
        this.groupSize = groupSize;
    }

    public int getTopicsNumber () {
        return topicsNumber;
    }
    public void setTopicsNumber (int topicsNumber) {
        this.topicsNumber = topicsNumber;
    }

}
