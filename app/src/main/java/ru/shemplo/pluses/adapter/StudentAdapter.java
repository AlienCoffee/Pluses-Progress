package ru.shemplo.pluses.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.StudentEntity;
import ru.shemplo.pluses.layout.DiaryMainActivity;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<StudentEntity> students;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView studentName;
        private int id;


        public ViewHolder(View v) {
            super(v);

            studentName = (TextView) itemView.findViewById(R.id.student_name);

            v.setOnClickListener (new View.OnClickListener () {

                @Override
                public void onClick (View v) {
                    DiaryMainActivity.page.switchFragment(R.id.topic_recycler_view, id, studentName.getText());
                }

            });
        }

    }

    public StudentAdapter(List<StudentEntity> data) {
        Collections.sort(data, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity a, StudentEntity b) {
                int tmp = a.getLastName().compareTo(b.getLastName());
                if (tmp != 0) {
                    return tmp;
                }
                return a.getFirstName().compareTo(b.getLastName());
            }
        });
        students = data;
    }

    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.e("dbg: adapter", "onCreate");
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Log.e("dbg: adapter", "onBind " + position);
        StudentEntity student = students.get(position);

        holder.studentName.setText(student.getLastName() + " " + student.getFirstName ());
        holder.id = student.ID;
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

}