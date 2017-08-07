package tk.pluses.plusesprogress.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import tk.pluses.plusesprogress.JoinGroupPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.lists.groups.GroupEntity;
import tk.pluses.plusesprogress.lists.groups.GroupsAdapter;
import tk.pluses.plusesprogress.user.UserEntity;

public class FragmentDiary extends Fragment implements LoaderManager.LoaderCallbacks <RequestResult> {

    private TextView answerMessage;
    private ProgressBar progressBar;
    private RecyclerView groupsRecycler;

    private List <GroupEntity> groupsList;
    private int groupsDataLoaded = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        Log.i (this.getClass ().getSimpleName (), "Create");

        answerMessage  = (TextView) view.findViewById (R.id.answerMessageView);
        progressBar    = (ProgressBar) view.findViewById (R.id.loadingProgress);
        groupsRecycler = (RecyclerView) view.findViewById (R.id.groupsRecyclerView);
        groupsRecycler.setLayoutManager (new LinearLayoutManager (getActivity ()));

        progressBar.setVisibility (View.VISIBLE);

        if (UserEntity.getProperty ("token") == null
                || UserEntity.getProperty ("id") == null) {
            // User not logined
            // Here must be pop-up notification about it
            answerMessage.setText ("You are not log in");
            answerMessage.setTextColor (Color.RED);
            progressBar.setVisibility (View.GONE);
            Log.e ("DiaryFragment", "User not logined");
            return;
        }

        view.findViewById (R.id.linkToJoinGroup).setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                Intent intent = new Intent (v.getContext (), JoinGroupPage.class);
                startActivity (intent);
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume ();

        Log.i (this.getClass ().getSimpleName (), "Resume");

        RequestForm form = new RequestForm ("http://pluses.tk/api.users.getUserGroups");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("user_id", UserEntity.getProperty ("id"));

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public Loader<RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i ("DiaryFragment", data.TYPE + " (code: " + data.CODE + ")");
        progressBar.setVisibility (View.GONE);
        answerMessage.setTextColor (Color.BLACK);
        answerMessage.setText ("");

        File root = getActivity ().getFilesDir ();
        File dataFolder = new File (root, "data");
        if (!dataFolder.exists ()) { dataFolder.mkdir (); }

        if (data.TYPE.equals ("Error")) {
            Log.e ("AuthFragment", "Message: " + data.getField ("name")
                    + " (comment: " + data.getField ("message") + ")");
            if (data.CODE == 6000 /* NO INTERNET CONNECTION */) {
                // It's especial situation when data must be loaded from data-file
                File groupsListFile = new File (dataFolder, "groups-list.dat");
                if (!groupsListFile.exists ()) {
                    // Nothing was cached -> stop working
                    answerMessage.setText ("No internet and nothing was saved before");
                    answerMessage.setTextColor (Color.RED);
                    return;
                }

                try {
                    BufferedReader br = new BufferedReader (new FileReader (groupsListFile));

                    // ID of the user that this list belongs to
                    String userIdentification = br.readLine ();
                    if (!userIdentification.equals (UserEntity.getProperty ("id"))) {
                        answerMessage.setText ("No internet and nothing was saved before");
                        answerMessage.setTextColor (Color.RED);
                        return;
                    }

                    // Data must be stored in single line in JSON Array format
                    String groupsListString =  br.readLine ();
                    JSONArray groupsArray = new JSONArray (groupsListString);

                    // Close reader
                    br.close ();

                    groupsList = new ArrayList <> ();
                    groupsDataLoaded = 0;

                    for (int i = 0; i < groupsArray.length (); i ++) {
                        groupsList.add (new GroupEntity (groupsArray.getInt (i)));
                    }

                    for (int i = 0; i < groupsList.size (); i ++) {
                        GroupEntity entity = groupsList.get (i);
                        File groupEntityFile = new File (dataFolder,
                                "group-" + entity.ID
                                        +"-data.dat");
                        if (!groupEntityFile.exists ()) { continue; }

                        br = new BufferedReader (new FileReader (groupEntityFile));
                        entity.setName (br.readLine ());
                        entity.setTopicsNumber (Integer.parseInt (br.readLine ()));
                        entity.setGroupSize (Integer.parseInt (br.readLine ()));
                        entity.setHeadTeacherID (Integer.parseInt (br.readLine ()));
                        br.close ();
                    }

                    GroupsAdapter adapter = new GroupsAdapter (getActivity (), groupsList);
                    groupsRecycler.setAdapter (adapter);
                } catch (FileNotFoundException fnfe) {

                } catch (IOException ioe) {

                } catch (JSONException jsone) {
                    jsone.printStackTrace ();
                }
            } else if (data.CODE >= 3000) {
                String message = data.getField ("message");
                answerMessage.setText (data.getField ("name")
                                        + (message != null && message.length () > 0
                                            ? ": " + message
                                            : ""));
                answerMessage.setTextColor (Color.RED);
            }
        } else if (data.TYPE.equals ("Success") && data.CODE == 1000) {
            if (data.HOST.equals ("http://pluses.tk/api.users.getUserGroups")) {
                Log.i (this.getClass ().getSimpleName (), "To loaded list of groups");
                groupsList = new ArrayList <> ();
                groupsDataLoaded = 0;

                try {
                    String groups = data.getField ("message");
                    JSONArray groupsArray = new JSONArray (groups);
                    int length = groupsArray.length ();

                    File groupsListFile = new File (dataFolder, "groups-list.dat");
                    if (!groupsListFile.exists ()) {
                        try {
                            groupsListFile.createNewFile ();
                        } catch (IOException e) { /* STUB */ }
                    }

                    try {
                        PrintWriter pw = new PrintWriter (groupsListFile);
                        pw.println (UserEntity.getProperty ("id"));
                        pw.println (groupsArray.toString ());

                        Log.i (this.getClass ().getSimpleName (), "Data was stored in file");
                        pw.close ();
                    } catch (IOException e) {}

                    for (int i = 0; i < length; i ++) {
                        groupsList.add (new GroupEntity (groupsArray.getInt (i)));
                    }

                    GroupsAdapter adapter = new GroupsAdapter (getActivity (), groupsList);
                    groupsRecycler.setAdapter (adapter);
                } catch (JSONException jsone) {
                }

                if (groupsList.size () > 0) {
                    RequestForm form = new RequestForm ("http://pluses.tk/api.groups.getGroupData");
                    form.addParam ("token", UserEntity.getProperty ("token"));
                    form.addParam ("group_id", groupsList.get (groupsDataLoaded).ID + "");

                    Bundle args = new Bundle ();
                    args.putSerializable ("form", form);
                    getLoaderManager ().restartLoader (0, args, this);
                }
            } else if (data.HOST.equals ("http://pluses.tk/api.groups.getGroupData")) {
                Log.i (this.getClass ().getSimpleName (), "To loaded data of group");
                Log.i (this.getClass ().getSimpleName (), "Data for group loaded: "
                                                            + data.getField ("message"));
                try {
                    String message = data.getField ("message");
                    JSONObject json = new JSONObject (message);

                    GroupEntity entity = groupsList.get (groupsDataLoaded);
                    entity.setName (json.getString ("name"));
                    entity.setHeadTeacherID (json.getInt ("head_teacher"));

                    JSONObject jsonList = new JSONObject (json.getString ("list"));
                    entity.setGroupSize (jsonList.getInt ("size"));

                    JSONObject jsonTopics = new JSONObject (json.getString ("topics"));
                    entity.setTopicsNumber (jsonTopics.getInt ("size"));

                    ((GroupsAdapter) groupsRecycler.getAdapter ()).updateItem (groupsDataLoaded);

                    // Saving data in files
                    File groupEntityFile = new File (dataFolder,
                                                        "group-" + entity.ID
                                                                +"-data.dat");
                    if (!groupEntityFile.exists ()) {
                        try {
                            groupEntityFile.createNewFile ();
                        } catch (IOException e) { /* STUB */ }
                    }

                    try {
                        PrintWriter pw = new PrintWriter (groupEntityFile);
                        pw.println (entity.getName ());
                        pw.println (entity.getTopicsNumber () + "");
                        pw.println (entity.getGroupSize () + "");
                        pw.println (entity.getHeadTeacherID () + "");

                        Log.i (this.getClass ().getSimpleName (), "Group #" + entity.ID
                                                                    + " data was stored in file");
                        pw.close ();
                    } catch (IOException e) {}
                } catch (JSONException jsone) {
                    jsone.printStackTrace ();
                } catch (IndexOutOfBoundsException iobe) {
                    Log.e (this.getClass ().getSimpleName (), "Bing index: " + groupsDataLoaded);
                }

                groupsDataLoaded ++;
                if (groupsDataLoaded < groupsList.size ()) {
                    RequestForm form = new RequestForm ("http://pluses.tk/api.groups.getGroupData");
                    form.addParam ("token", UserEntity.getProperty ("token"));
                    form.addParam ("group_id", groupsList.get (groupsDataLoaded).ID + "");

                    Bundle args = new Bundle ();
                    args.putSerializable ("form", form);
                    getLoaderManager ().restartLoader (0, args, this);
                }
            }
        }
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {}

}
