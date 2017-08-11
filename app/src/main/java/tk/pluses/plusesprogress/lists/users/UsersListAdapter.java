package tk.pluses.plusesprogress.lists.users;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.pluses.plusesprogress.DiaryMenuPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.fragments.FragmentUsers;
import tk.pluses.plusesprogress.user.UserEntity;

/**
 * Created by Андрей on 05.08.2017.
 */

public class UsersListAdapter extends RecyclerView.Adapter {

    private final Map <Integer, RecyclerView.ViewHolder> holders;

    private final List <UserListEntity> usersList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public UsersListAdapter (Context context, List <UserListEntity> list) {
        this.layoutInflater = LayoutInflater.from (context);
        this.holders = new HashMap <> ();
        this.usersList = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return UsersListViewHolder.getInstance (layoutInflater, parent, this);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        holders.put (position, holder);

        UserListEntity entity = usersList.get (position);
        UsersListViewHolder customHolder = (UsersListViewHolder) holder;

        customHolder.entity   = entity;
        customHolder.position = position;
        customHolder.userIDValue.setText (entity.ID + "");

        if (entity.getName () != null) {
            customHolder.userNameValue.setText (entity.getName ());
        }
        if (entity.getTotalPluses () != -1) {
            customHolder.userTotalValue.setText (entity.getTotalPluses () + "");

            String mask = entity.getMask ();
            mask = mask + " (" + entity.getLocalPluses () + ")";
            customHolder.userSolvedValue.setText (mask);
        }

        if (entity.isSelected ()) {
            customHolder.userBox.setBackgroundResource (R.drawable.border_rectangle_selected);
        } else {
            customHolder.userBox.setBackgroundResource (R.drawable.border_rectangle);
        }
    }

    public void resetUserSelection () {
        for (UserListEntity entity: usersList) {
            entity.setSelected (false);
        }

        this.notifyDataSetChanged ();
    }

    @Override
    public int getItemCount () {
        return usersList.size ();
    }

    private static class UsersListViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public TextView userNameValue,
                            userIDValue,
                            userTotalValue,
                            userSolvedValue;
        public LinearLayout userBox;

        private static UsersListAdapter adapter;
        public UserListEntity entity;

        public UsersListViewHolder (View itemView) {
            super (itemView);

            itemView.setOnClickListener (new View.OnClickListener () {
                public void onClick (View v) {
                    adapter.resetUserSelection ();
                    entity.setSelected (true);

                    int userID = Integer.parseInt (userIDValue.getText ().toString ());
                    if (DiaryMenuPage.page.currentUser != userID) {
                        // Or may be put next 2 lines here to prevent too often loadings
                    }
                    Log.i (this.getClass ().getSimpleName (), "Selected user: " + userID);
                    FragmentUsers.fragment.updateProblemsMask (userID);
                }
            });

            userBox         = (LinearLayout) itemView.findViewById (R.id.userItemBox);

            userNameValue   = (TextView) itemView.findViewById (R.id.userName);
            userIDValue     = (TextView) itemView.findViewById (R.id.userIDValue);
            userTotalValue  = (TextView) itemView.findViewById (R.id.userTotalSolved);
            userSolvedValue = (TextView) itemView.findViewById (R.id.userProblemsSolved);
            Log.i (this.getClass ().getSimpleName (), "Registered");
        }

        public static UsersListViewHolder getInstance (LayoutInflater layoutInflater,
                                                       ViewGroup parent,
                                                       UsersListAdapter adapter) {
            UsersListViewHolder.adapter = adapter;

            final View view = layoutInflater.inflate(R.layout.item_user, parent, false);
            return new UsersListViewHolder (view);
        }

    }

}
