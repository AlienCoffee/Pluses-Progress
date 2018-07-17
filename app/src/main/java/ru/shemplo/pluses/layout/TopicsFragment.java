package ru.shemplo.pluses.layout;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.adapter.GroupAdapter;
import ru.shemplo.pluses.adapter.TopicAdapter;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.TaskEntity;
import ru.shemplo.pluses.entity.TopicEntity;


public class TopicsFragment extends Fragment {
    private RecyclerView topicsRecycler;
    private TopicAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<TopicEntity> topics;
    Context context;

    public TopicsFragment() {}

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<TopicEntity> data) {
        topics = data;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("dbg: TopicsFragment", "onCreate");
        if (topics == null || context == null) {
            throw new NullPointerException("Error: data/context haven't initialized in GroupsFragment");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("dbg: TopicsFragment", "onCreateView");
        return inflater.inflate(R.layout.topic_recycler_view, null);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        Log.e("dbg: TopicsFragment", "onViewCreated");
        super.onViewCreated (view, savedInstanceState);

        topicsRecycler = (RecyclerView) view.findViewById(R.id.topic_recycler_view);
        layoutManager = new LinearLayoutManager(context);
        topicsRecycler.setLayoutManager(layoutManager);
        adapter = new TopicAdapter(topics);
        topicsRecycler.setAdapter(adapter);
    }
}
