package tk.pluses.plusesprogress.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.lists.users.ProblemEntity;
import tk.pluses.plusesprogress.lists.users.ProblemsAdapter;

/**
 * Created by Андрей on 05.08.2017.
 */

public class FragmentUsers extends Fragment {

    private RecyclerView problemsRecycler;

    private List <ProblemEntity> problemsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        problemsRecycler = (RecyclerView) view.findViewById (R.id.problemsListView);
        problemsRecycler.setLayoutManager (new LinearLayoutManager (getActivity (),
                                                LinearLayoutManager.HORIZONTAL,
                                                false));
        problemsList = new ArrayList <> ();
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());
        problemsList.add (new ProblemEntity ());

        ProblemsAdapter adapter = new ProblemsAdapter (getActivity (), problemsList);
        problemsRecycler.setAdapter (adapter);
    }

}
