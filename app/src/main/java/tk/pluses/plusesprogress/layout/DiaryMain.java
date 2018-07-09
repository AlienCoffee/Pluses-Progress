package tk.pluses.plusesprogress.layout;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tk.pluses.plusesprogress.adapters.GroupAdapter;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.entities.GroupEntity;

public class DiaryMain extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List<GroupEntity> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        groups = new ArrayList<>();//TODO: remove
        for (int i = 0; i < 30; i++) {
            groups.add(new GroupEntity());
        }
        //TODO: add favourites

        recyclerView = (RecyclerView) findViewById(R.id.group_recycler_view);

        layoutManager = new LinearLayoutManager(this); //TODO: constraint layout manager?
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GroupAdapter(groups);
        recyclerView.setAdapter(adapter);

    }
}
