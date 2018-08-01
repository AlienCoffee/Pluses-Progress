package ru.shemplo.pluses.layout;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.adapter.StudentAdapter;
import ru.shemplo.pluses.entity.StudentEntity;


public class StudentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<StudentEntity> students;
    Context context;

    public StudentsFragment() {}

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<StudentEntity> data) {
        students = data;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.e("dbg", "onCreate");
        if (students == null || context == null) {
            throw new NullPointerException("Error: data/context haven't initialized in StudentsFragment");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.e("dbg", "onCreateView");
        return inflater.inflate(R.layout.student_recycler_view, null);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        //Log.e("dbg", "onViewCreated");
        super.onViewCreated (view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.student_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new StudentAdapter(students);
        recyclerView.setAdapter(adapter);
    }
}
