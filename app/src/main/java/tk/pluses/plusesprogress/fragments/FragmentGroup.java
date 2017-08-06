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

import java.util.ArrayList;
import java.util.List;

import tk.pluses.plusesprogress.IndexPage;
import tk.pluses.plusesprogress.JoinGroupPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.lists.groups.GroupEntity;
import tk.pluses.plusesprogress.lists.groups.GroupsAdapter;
import tk.pluses.plusesprogress.lists.topics.TopicEntity;
import tk.pluses.plusesprogress.lists.topics.TopicsAdapter;
import tk.pluses.plusesprogress.user.UserEntity;

public class FragmentGroup extends Fragment implements LoaderManager.LoaderCallbacks <RequestResult> {

    private TextView answerMessage;
    private ProgressBar progressBar;
    private RecyclerView topicsRecycler;

    private List <TopicEntity> topicsList;

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

        progressBar.setVisibility (View.VISIBLE);

        if (UserEntity.getProperty ("token") == null
                || UserEntity.getProperty ("id") == null) {
            answerMessage.setText ("You are not log in");
            answerMessage.setTextColor (Color.RED);
            progressBar.setVisibility (View.GONE);
            Log.e ("DiaryFragment", "User not logined");
            return;
        }

        RequestForm form = new RequestForm ("http://pluses.tk/api.groups.getGroupTopics");
        form.addParam ("group_id", Integer.toString(IndexPage.page.currentGroup));
        form.addParam("token", UserEntity.getProperty("token"));

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
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

        if (data.TYPE.equals ("Error")) {
            Log.e ("AuthFragment", "Message: " + data.getField ("name")
                    + " (comment: " + data.getField ("message") + ")");
            if (data.CODE >= 3000) {
                String message = data.getField ("message");
                answerMessage.setText (data.getField ("name")
                        + (message != null && message.length () > 0
                        ? ": " + message
                        : ""));
                answerMessage.setTextColor (Color.RED);
            }
        } else if (data.TYPE.equals ("Success") && data.CODE == 1000) {
            topicsList = new ArrayList <> ();

            try {
                String topics = data.getField ("message");
                JSONArray topicsArray = new JSONArray (topics);
                int length = topicsArray.length ();

                for (int i = 0; i < length; i ++) {
                    topicsList.add (new TopicEntity (topicsArray.getInt (i)));
                }

                topicsRecycler.setLayoutManager (new LinearLayoutManager (getActivity ()));
                TopicsAdapter adapter = new TopicsAdapter(getActivity (), topicsList);
                topicsRecycler.setAdapter (adapter);
            } catch (JSONException jsone) {

            }
        }
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {}

}
