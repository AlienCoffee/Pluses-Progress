package ru.shemplo.pluses.layout;
import ru.shemplo.pluses.R;
import ru.shemplo.pluses.adapters.GroupAdapter;
import ru.shemplo.pluses.entities.GroupEntity;

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
        View tmp = inflater.inflate(R.layout.group_recycler_view, container);
        recyclerView = (RecyclerView) tmp.findViewById(R.id.group_recycler_view);
        return tmp;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        Log.e("dbg", "onViewCreated");
        super.onViewCreated (view, savedInstanceState);

//        recyclerView = (RecyclerView) getView().findViewById(R.id.group_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GroupAdapter(groups);
        recyclerView.setAdapter(adapter);
    }
}
