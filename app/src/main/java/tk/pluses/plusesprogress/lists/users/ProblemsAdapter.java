package tk.pluses.plusesprogress.lists.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.pluses.plusesprogress.DiaryMenuPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.fragments.FragmentUsers;

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
        this.holders = new HashMap <> ();
        this.problemsList = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return ProblemsViewHolder.getInstance (layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        holders.put (position, holder);

        ProblemEntity entity = problemsList.get (position);
        ProblemsViewHolder customHolder = (ProblemsViewHolder) holder;

        customHolder.entity   = entity;
        customHolder.position = position;

        String name = entity.NAME.toUpperCase ();
        if (name.length () > 2) {
            name = name.substring (0, 2) + "...";
        }
        customHolder.nameValue.setText (name);
        customHolder.problemIndexValue.setText (entity.INDEX + "");
        customHolder.solvedCheckBox.setChecked (entity.isSolved ());
    }

    @Override
    public int getItemCount () {
        return problemsList.size ();
    }

    private static class ProblemsViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public TextView nameValue,
                        problemIndexValue;
        private CheckBox solvedCheckBox;
        public ProblemEntity entity;

        public ProblemsViewHolder (View itemView) {
            super (itemView);

            nameValue = (TextView) itemView.findViewById (R.id.problemNameValue);
            solvedCheckBox = (CheckBox) itemView.findViewById (R.id.plusesCheckBoxView);
            problemIndexValue = (TextView) itemView.findViewById (R.id.problemIndexValue);

            solvedCheckBox.setOnClickListener (new View.OnClickListener () {
                public void onClick (View v) {
                    if (DiaryMenuPage.page.currentUser == -1) {
                        solvedCheckBox.setChecked (false);
                        return;
                    }

                    int index = Integer.parseInt (problemIndexValue.getText ().toString ());
                    FragmentUsers.fragment.registerAttempt (entity.NAME, solvedCheckBox.isChecked ());
                    Log.i (this.getClass ().getSimpleName (), "Attempt registered: problem "
                                                                + index + "(" + entity.NAME + ")"
                                                                + " - value " + solvedCheckBox.isChecked ()
                                                                + " - user " + DiaryMenuPage.page.currentUser);
                }
            });
        }

        public static ProblemsViewHolder getInstance (LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_set_plus, parent, false);
            return new ProblemsViewHolder (view);
        }

    }

}
