package tk.pluses.plusesprogress.lists.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.lists.groups.GroupEntity;
import tk.pluses.plusesprogress.lists.groups.GroupsAdapter;

/**
 * Created by Андрей on 05.08.2017.
 */

public class ProblemsAdapter extends RecyclerView.Adapter {

    private final Map <Integer, RecyclerView.ViewHolder> holders;

    private final List <ProblemEntity> problemsList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public ProblemsAdapter (Context context, List <ProblemEntity> list) {
        this.layoutInflater = LayoutInflater.from (context);
        this.holders = new HashMap<> ();
        this.problemsList = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return ProblemsViewHolder.getInstance (layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount () {
        return problemsList.size ();
    }

    private static class ProblemsViewHolder extends RecyclerView.ViewHolder {

        public ProblemsViewHolder (View itemView) {
            super (itemView);
        }

        public static ProblemsViewHolder getInstance (LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_set_plus, parent, false);
            return new ProblemsViewHolder (view);
        }

    }

}
