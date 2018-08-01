package ru.shemplo.pluses.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.layout.DiaryMainActivity;
import ru.shemplo.pluses.network.DataProvider;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<GroupEntity> groups;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView groupName, teacherName, studentsCount; //TODO: anything more?
        private int id;

        public ViewHolder(View v) {
            super(v);

            groupName = (TextView) itemView.findViewById(R.id.group_item_name);
            teacherName = (TextView) itemView.findViewById(R.id.group_item_teacher);
            studentsCount = (TextView) itemView.findViewById(R.id.group_item_students_count);

            v.setOnClickListener (new View.OnClickListener () {

                @Override
                public void onClick (View v) {
                    DiaryMainActivity.group = id;
                    DiaryMainActivity.page.switchFragment(R.id.student_recycler_view, id);
                }

            });
        }
    }

    public GroupAdapter(List<GroupEntity> groups) {
        this.groups = groups;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.e("dbg: adapter", "onCreate");
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Log.e("dbg: adapter", "onBind " + position);
        GroupEntity group = groups.get(position);

        holder.id = group.ID;
        holder.groupName.setText(group.getName());
        holder.teacherName.setText(group.getTeacher());

        DataProvider provider = new DataProvider (holder.groupName.getContext ());
        holder.studentsCount.setText ("" + provider.getStudents (group.ID).size ());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

}