package tk.pluses.plusesprogress.fragments;

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

import tk.pluses.plusesprogress.DiaryMenuPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.lists.topics.TopicEntity;
import tk.pluses.plusesprogress.lists.topics.TopicsAdapter;
import tk.pluses.plusesprogress.user.UserEntity;

public class FragmentTopics extends Fragment implements LoaderManager.LoaderCallbacks <RequestResult> {

    private TextView answerMessage;
    private ProgressBar progressBar;
    private RecyclerView topicsRecycler;

    private List <TopicEntity> topicsList;
    private int topicsDataLoaded = 0;

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

        answerMessage  = (TextView) view.findViewById (R.id.answerMessageView);
        progressBar    = (ProgressBar) view.findViewById (R.id.loadingProgress);
        topicsRecycler = (RecyclerView) view.findViewById (R.id.topicsRecyclerView);
        topicsRecycler.setLayoutManager (new LinearLayoutManager (getActivity ()));
    }

    @Override
    public void onResume () {
        super.onResume ();

        Log.i (this.getClass ().getSimpleName (), "Resume");
        progressBar.setVisibility (View.VISIBLE);

        RequestForm form = new RequestForm ("http://pluses.tk/api.groups.getGroupTopics");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("group_id", DiaryMenuPage.page.currentGroup + "");

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public Loader<RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i ("TopicsFragment", data.TYPE + " (code: " + data.CODE + ")");
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
                File topicsListFile = new File (dataFolder, "group-" + DiaryMenuPage.page.currentGroup+ "-topics-list.dat");
                if (!topicsListFile.exists ()) {
                    // Nothing was cached -> stop working
                    answerMessage.setText ("No internet and nothing was saved before");
                    answerMessage.setTextColor (Color.RED);
                    return;
                }

                try {
                    BufferedReader br = new BufferedReader (new FileReader (topicsListFile));

                   // Data must be stored in single line in JSON Array format
                    String topicsListString =  br.readLine ();
                    JSONArray topicsArray = new JSONArray (topicsListString);

                    // Close reader
                    br.close ();

                    topicsList = new ArrayList <> ();
                    topicsDataLoaded = 0;

                    for (int i = 0; i < topicsArray.length (); i ++) {
                        topicsList.add (new TopicEntity (topicsArray.getInt (i)));
                    }

                    for (int i = 0; i < topicsList.size (); i ++) {
                        TopicEntity entity = topicsList.get (i);
                        File topicEntityFile = new File (dataFolder,
                                "topic-" + entity.ID
                                        +"-data.dat");
                        if (!topicEntityFile.exists ()) { continue; }

                        br = new BufferedReader (new FileReader (topicEntityFile));
                        entity.setName (br.readLine ());
                        Log.i("qwe",entity.getName());
                        entity.setAuthorID (Integer.parseInt (br.readLine ()));
                        Log.i("qwe",entity.getAuthorID()+"");
                        entity.setRatedCounter (Integer.parseInt (br.readLine ()));
                        Log.i("qwe",entity.getRatedCounter()+"");
                        entity.setTotalCounter (Integer.parseInt (br.readLine ()));
                        Log.i("qwe",entity.getTotalCounter()+"");
                        br.close ();
                    }
                    TopicsAdapter adapter = new TopicsAdapter (getActivity (), topicsList);
                    topicsRecycler.setAdapter (adapter);
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
            Log.i(data.HOST,"w");
            if (data.HOST.equals ("http://pluses.tk/api.groups.getGroupTopics")) {
                topicsList = new ArrayList <> ();
                topicsDataLoaded = 0;

                try {
                    String topics = data.getField ("message");
                    JSONArray topicsArray = new JSONArray (topics);
                    int length = topicsArray.length ();

                    File topicsListFile = new File (dataFolder, "group-" + DiaryMenuPage.page.currentGroup + "-topics-list.dat");
                    if (!topicsListFile.exists ()) {
                        try {
                            topicsListFile.createNewFile ();
                        } catch (IOException e) { /* STUB */ }
                    }

                    try {
                        PrintWriter pw = new PrintWriter (topicsListFile);
                        pw.println (topicsArray.toString ());

                        Log.i (this.getClass ().getSimpleName (), "Data was stored in file");
                        pw.close ();
                    } catch (IOException e) {}

                    for (int i = 0; i < length; i ++) {
                        topicsList.add (new TopicEntity (topicsArray.getInt (i)));
                    }

                    TopicsAdapter adapter = new TopicsAdapter (getActivity (), topicsList);
                    topicsRecycler.setAdapter (adapter);
                } catch (JSONException jsone) {
                }

                if (topicsList.size () > 0) {
                    RequestForm form = new RequestForm ("http://pluses.tk/api.topics.getTopicData");
                    form.addParam ("token", UserEntity.getProperty ("token"));
                    form.addParam ("topic_id", topicsList.get (topicsDataLoaded).ID + "");

                    Bundle args = new Bundle ();
                    args.putSerializable ("form", form);
                    getLoaderManager ().restartLoader (0, args, this);
                }
            } else if (data.HOST.equals ("http://pluses.tk/api.topics.getTopicData")) {
                Log.i (this.getClass ().getSimpleName (), "Data for topic loaded: "
                        + data.getField ("message"));
                try {
                    String message = data.getField ("message");
                    JSONObject json = new JSONObject (message);

                    TopicEntity entity = topicsList.get (topicsDataLoaded);
                    entity.setName (json.getString ("name"));
                    entity.setAuthorID (json.getInt ("author"));

                    JSONObject tasks = json.getJSONObject("tasks");
                    entity.setTotalCounter(tasks.getInt("total"));

                    entity.setRatedCounter(tasks.getInt("rating"));
                    ((TopicsAdapter) topicsRecycler.getAdapter ()).updateItem (topicsDataLoaded);

                    // Saving data in files
                    File topicEntityFile = new File (dataFolder,
                            "topic-" + entity.ID
                                    +"-data.dat");
                    if (!topicEntityFile.exists ()) {
                        try {
                            topicEntityFile.createNewFile ();
                        } catch (IOException e) { /* STUB */ }
                    }

                    try {
                        PrintWriter pw = new PrintWriter (topicEntityFile);
                        pw.println (entity.getName ());
                        pw.println (entity.getAuthorID() + "");
                        pw.println (entity.getRatedCounter () + "");
                        pw.println (entity.getTotalCounter () + "");
                        Log.i (this.getClass ().getSimpleName (), "Topic #" + entity.ID
                                + " data was stored in file");
                        pw.close ();
                    } catch (IOException e) {}
                } catch (JSONException jsone) {
                    jsone.printStackTrace ();
                } catch (IndexOutOfBoundsException iobe) {
                    Log.e (this.getClass ().getSimpleName (), "Bing index: " + topicsDataLoaded);
                }

                topicsDataLoaded++;
                if (topicsDataLoaded < topicsList.size ()) {
                    RequestForm form = new RequestForm ("http://pluses.tk/api.topics.getTopicData");
                    form.addParam ("token", UserEntity.getProperty ("token"));
                    form.addParam ("topic_id", topicsList.get (topicsDataLoaded).ID + "");

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
