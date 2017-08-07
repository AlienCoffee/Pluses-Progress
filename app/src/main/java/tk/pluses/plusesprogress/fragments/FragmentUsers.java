package tk.pluses.plusesprogress.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import tk.pluses.plusesprogress.IndexPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.lists.users.ProblemEntity;
import tk.pluses.plusesprogress.lists.users.ProblemsAdapter;
import tk.pluses.plusesprogress.lists.users.UserListEntity;
import tk.pluses.plusesprogress.lists.users.UsersListAdapter;
import tk.pluses.plusesprogress.user.UserEntity;

/**
 * Created by Андрей on 05.08.2017.
 */

public class FragmentUsers extends Fragment implements LoaderManager.LoaderCallbacks <RequestResult> {

    private RecyclerView problemsRecycler,
                            usersRecycler;
    private ProgressBar loadingProblems,
                            loadingUsers;
    private TextView answerMessage;

    private List <UserListEntity> usersList;
    private List <ProblemEntity> problemsList;

    private int loadedUsersData = 0,
                loadedProblemsData = 0;

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
        usersRecycler    = (RecyclerView) view.findViewById (R.id.usersListView);
        loadingProblems  = (ProgressBar) view.findViewById (R.id.loadingProblemsProgress);
        loadingUsers     = (ProgressBar) view.findViewById (R.id.loadingUsersProgress);
        answerMessage    = (TextView) view.findViewById (R.id.answerMessageView);
    }

    @Override
    public void onResume () {
        super.onResume ();

        Log.i (this.getClass ().getSimpleName (), "Resume");
        loadingProblems.setVisibility (View.VISIBLE);
        loadingUsers.setVisibility (View.VISIBLE);

        RequestForm form = new RequestForm ("http://pluses.tk/api.groups.getGroupUsers");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("group_id", IndexPage.page.currentGroup + "");

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

    @Override
    public Loader <RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i (this.getClass ().getSimpleName (), data.TYPE + " (code: " + data.CODE + ")");
        if (data.TYPE.equals ("Error")) {
            Log.e ("AuthFragment", "Message: " + data.getField ("name")
                                    + " (comment: " + data.getField ("message") + ")");
            if (data.CODE == 6000) {

            } else if (data.CODE >= 3000) {
                String message = data.getField ("message");
                answerMessage.setText (data.getField ("name")
                                        + (message != null && message.length () > 0
                                            ? ": " + message
                                            : ""));
                answerMessage.setTextColor (Color.RED);
            }
        } else if (data.TYPE.equals ("Success") && data.CODE == 1000) {
            if (data.HOST.equals ("http://pluses.tk/api.groups.getGroupUsers")) {
                usersList = new ArrayList <> ();
                loadedUsersData = 0;

                try {
                    String users = data.getField ("message");
                    JSONArray usersArray = new JSONArray (users);
                    int length = usersArray.length ();

                    for (int i = 0; i < length; i ++) {
                        usersList.add (new UserListEntity (usersArray.getInt (i)));
                    }

                    UsersListAdapter adapter = new UsersListAdapter ();
                    usersRecycler.setAdapter (adapter);
                } catch (JSONException jsone) {
                }
            }
        }
        loadingProblems.setVisibility (View.GONE);
        loadingUsers.setVisibility (View.GONE);
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {

    }

}
