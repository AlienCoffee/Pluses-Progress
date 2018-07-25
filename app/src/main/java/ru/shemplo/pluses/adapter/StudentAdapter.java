package ru.shemplo.pluses.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.StudentEntity;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentEntity> students;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentName;


        public ViewHolder(View v) {
            super(v);
            studentName = (TextView) itemView.findViewById(R.id.student_name);
        }
    }

    public StudentAdapter(List<StudentEntity> data) {
        students = data;
    }

    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("dbg: adapter", "onCreate");
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("dbg: adapter", "onBind " + position);
        StudentEntity student = students.get(position);
        holder.studentName.setText(student.getName());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}