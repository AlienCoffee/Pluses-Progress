package ru.shemplo.pluses.layout;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.shemplo.pluses.adapter.GroupAdapter;
import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.network.DataProvider;

public class DiaryMain extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List<GroupEntity> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        groups = DataProvider.getGroups ();
        //TODO: add favourites

        recyclerView = (RecyclerView) findViewById(R.id.group_recycler_view);

        layoutManager = new LinearLayoutManager(this); //TODO: constraint layout manager?
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GroupAdapter(groups);
        recyclerView.setAdapter(adapter);

    }
}
