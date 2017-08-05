package tk.pluses.plusesprogress.lists.groups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.pluses.plusesprogress.IndexPage;
import tk.pluses.plusesprogress.R;

/**
 * Created by Андрей on 05.08.2017.
 */

public class GroupsAdapter extends RecyclerView.Adapter {

    private final Map <Integer, RecyclerView.ViewHolder> holders;

    private final List <GroupEntity> groupsList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public GroupsAdapter (Context context, List <GroupEntity> list) {
        this.layoutInflater = LayoutInflater.from (context);
        this.holders = new HashMap <> ();
        this.groupsList = list;
        this.context = context;
    }

    public void updateItem (int position) {
        GroupEntity groupEntity = groupsList.get (position);
        GroupViewHolder customHolder = (GroupViewHolder) holders.get (position);
        customHolder.groupTitle.setText (groupsList.get (position).getName ());
        customHolder.groupTopicsValue.setText (groupsList.get (position).getTopicsNumber () + "");
        customHolder.groupMembersValue.setText (groupsList.get (position).getGroupSize () + "");
        customHolder.groupHeadTeacherValue.setText (groupsList.get (position).getHeadTeacherID () + "");
    }

    @Override
    public int getItemViewType (int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return GroupViewHolder.getInstance (layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        holders.put (position, holder);

        GroupEntity groupEntity = groupsList.get (position);
        GroupViewHolder customHolder = (GroupViewHolder) holder;

        customHolder.position = position;
        customHolder.groupIDValue.setText (groupEntity.ID + "");
    }

    @Override
    public int getItemCount () {
        return groupsList.size ();
    }

    private static class GroupViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public final TextView groupTitle,
                                groupIDValue,
                                groupTopicsValue,
                                groupMembersValue,
                                groupHeadTeacherValue;

        public GroupViewHolder (View itemView) {
            super (itemView);

            itemView.setOnClickListener (new View.OnClickListener () {
                public void onClick (View v) {
                    int groupID = Integer.parseInt (groupIDValue.getText ().toString ());
                    Log.i (this.getClass ().getSimpleName (), "Selected group: " + groupID);
                    IndexPage.page.currentTopic = groupID;

                    IndexPage.page.switchFragment (R.id.navigation_item_auth);
                }
            });

            groupTitle            = (TextView) itemView.findViewById (R.id.groupTitle);
            groupIDValue          = (TextView) itemView.findViewById (R.id.groupIDValue);
            groupTopicsValue      = (TextView) itemView.findViewById (R.id.groupTopicsValue);
            groupMembersValue     = (TextView) itemView.findViewById (R.id.groupMembersValue);
            groupHeadTeacherValue = (TextView) itemView.findViewById (R.id.groupHeadTeacherValue);
            Log.i (this.getClass ().getSimpleName (), "Registered");
        }

        public static GroupViewHolder getInstance (LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_group, parent, false);
            return new GroupViewHolder (view);
        }

    }

}
