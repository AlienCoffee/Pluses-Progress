package tk.pluses.plusesprogress.lists.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Андрей on 05.08.2017.
 */

public class UsersListAdapter extends RecyclerView.Adapter {

    private final Map<Integer, RecyclerView.ViewHolder> holders;

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
        return null;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount () {
        return 0;
    }

    private static class UsersListViewHolder extends RecyclerView.ViewHolder {
        public UsersListViewHolder (View itemView) {
            super (itemView);
        }
    }

}
