package ru.shemplo.pluses.layout;
import ru.shemplo.pluses.R;
import ru.shemplo.pluses.adapter.GroupAdapter;
import ru.shemplo.pluses.entity.GroupEntity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class GroupsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<GroupEntity> groups;
    Context context;

    public GroupsFragment() {}

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<GroupEntity> data) {
        groups = data;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("dbg", "onCreate");
        if (groups == null || context == null) {
            throw new NullPointerException("Error: data/context haven't initialized in GroupsFragment");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("dbg", "onCreateView");
        return inflater.inflate(R.layout.group_recycler_view, null);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        Log.e("dbg", "onViewCreated");
        super.onViewCreated (view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GroupAdapter(groups);
        recyclerView.setAdapter(adapter);
    }
}
