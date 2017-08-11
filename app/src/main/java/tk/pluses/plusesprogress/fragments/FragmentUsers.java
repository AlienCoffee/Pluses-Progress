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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tk.pluses.plusesprogress.DiaryMenuPage;
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

    /**
     *
     * */
    public static FragmentUsers fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUsers.fragment = this;
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
        usersRecycler.setLayoutManager (new LinearLayoutManager (getActivity ()));

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

        RequestForm form = new RequestForm ("http://pluses.tk/api.topics.getTopicContent");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("topic_id", DiaryMenuPage.page.currentTopic + "");

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
            answerMessage.setText (""); // Clearing error message view
            if (data.HOST.equals ("http://pluses.tk/api.topics.getTopicContent")) {
                problemsList = new ArrayList <> ();
                loadedProblemsData = 0;

                try {
                    String problems = data.getField ("message");
                    JSONObject problemsObject = new JSONObject (problems);
                    Iterator <String> keys = problemsObject.keys ();

                    while (keys.hasNext ()) {
                        String key = keys.next ();
                        JSONObject problem = new JSONObject (problemsObject.getString (key));

                        int integerKey = Integer.parseInt (key);
                        problemsList.add (new ProblemEntity (integerKey,
                                                                problem.getString ("name"),
                                                                problem.getInt ("rating")));
                    }

                    ProblemsAdapter adapter = new ProblemsAdapter (getActivity (), problemsList);
                    problemsRecycler.setAdapter (adapter);
                } catch (JSONException jsone) {}
                problemsRecycler.setEnabled (true);

                // Next step - to load all users from group list
                RequestForm form = new RequestForm ("http://pluses.tk/api.groups.getGroupUsers");
                form.addParam ("token", UserEntity.getProperty ("token"));
                form.addParam ("group_id", DiaryMenuPage.page.currentGroup + "");

                Bundle args = new Bundle ();
                args.putSerializable ("form", form);
                getLoaderManager ().restartLoader (0, args, this);
            } else if (data.HOST.equals ("http://pluses.tk/api.groups.getGroupUsers")) {
                usersList = new ArrayList <> ();
                loadedUsersData = 0;

                try {
                    String users = data.getField ("message");
                    JSONArray usersArray = new JSONArray (users);
                    int length = usersArray.length ();

                    for (int i = 0; i < length; i ++) {
                        usersList.add (new UserListEntity (usersArray.getInt (i), problemsList.size ()));
                    }

                    UsersListAdapter adapter = new UsersListAdapter (getActivity (), usersList);
                    usersRecycler.setAdapter (adapter);
                } catch (JSONException jsone) {}

                loadedUsersData = 0;
                Log.i (this.getClass ().getSimpleName (), "Users: " + usersList.size ());

                if (usersList.size () > 0) {
                    RequestForm form = new RequestForm ("http://pluses.tk/api.users.getUserData");
                    form.addParam ("token", UserEntity.getProperty ("token"));
                    form.addParam ("user_id", usersList.get (loadedUsersData).ID + "");

                    Bundle args = new Bundle ();
                    args.putSerializable ("form", form);
                    getLoaderManager ().restartLoader (0, args, this);
                }
            } else if (data.HOST.equals ("http://pluses.tk/api.users.getUserData")) {
                try {
                    String message = data.getField ("message");
                    JSONObject userData = new JSONObject (message);

                    String rights = userData.getString ("rights");
                    if (rights.length () >= 5 && rights.charAt (4) == 'm') {
                        int index = 0, id = userData.getInt ("id");
                        for (UserListEntity entity: usersList) {
                            if (entity.ID == id) { break; }
                            index ++;
                        }

                        usersList.remove (index);
                        // Telling RecycleView that we've lost one user
                        ((UsersListAdapter) usersRecycler.getAdapter ()).notifyDataSetChanged ();
                    } else {
                        UserListEntity entity = usersList.get (loadedUsersData);

                        String name     = userData.getString ("name");
                        String lastName = userData.getString ("last_name");
                        entity.setName (lastName + " " + name);

                        ((UsersListAdapter) usersRecycler.getAdapter ()).notifyDataSetChanged ();
                        loadedUsersData ++;
                    }
                } catch (JSONException jsone) {

                } catch (IndexOutOfBoundsException iobe) {

                }

                loadedProblemsData = 0;
                Log.i (this.getClass ().getSimpleName (), "Loaded: " + loadedUsersData + " / " + usersList.size ());

                if (loadedUsersData < usersList.size ()) {
                    RequestForm form = new RequestForm ("http://pluses.tk/api.users.getUserData");
                    form.addParam ("token", UserEntity.getProperty ("token"));
                    form.addParam ("user_id", usersList.get (loadedUsersData).ID + "");

                    Bundle args = new Bundle ();
                    args.putSerializable ("form", form);
                    getLoaderManager ().restartLoader (0, args, this);
                }
            } else if (data.HOST.equals ("http://pluses.tk/api.users.getUserResults")) {
                Log.i (this.getClass ().getSimpleName (), data.getField ("message"));
            }
        }
        loadingProblems.setVisibility (View.GONE);
        loadingUsers.setVisibility (View.GONE);
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {
        problemsRecycler.setEnabled (false);
    }

    public void registerAttempt (String problem, boolean result) {
        if (DiaryMenuPage.page.currentUser == -1) {
            return; // No user selected in list
        }

        RequestForm form = new RequestForm ("http://pluses.tk/api.topics.registerAttempt");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("user_id", DiaryMenuPage.page.currentUser + "");
        form.addParam ("group_id", DiaryMenuPage.page.currentGroup + "");
        form.addParam ("topic_id", DiaryMenuPage.page.currentTopic + "");
        form.addParam ("problem", problem);
        form.addParam ("result", (result ? "true" : "false"));

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

    public void updateProblemsMask (int nextUserID) {
        // TODO: Save previous state of mask

        this._resetProblemsMask ();
        DiaryMenuPage.page.currentUser = nextUserID;
        // TODO: Loading previous version from file

        RequestForm form = new RequestForm ("http://pluses.tk/api.users.getUserResults");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("user_id", DiaryMenuPage.page.currentUser + "");
        form.addParam ("group_id", DiaryMenuPage.page.currentGroup + "");
        form.addParam ("topic_id", DiaryMenuPage.page.currentTopic + "");

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

    private void _resetProblemsMask () {
        for (ProblemEntity entity: problemsList) {
            entity.setSolved (false);
        }

        Log.i (this.getClass ().getSimpleName (), "Mask turned to default state");
        problemsRecycler.getAdapter ().notifyDataSetChanged ();
    }

}
