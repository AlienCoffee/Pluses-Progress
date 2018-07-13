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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<GroupEntity> groups;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName, teacherName, studentsCount; //TODO: anything more?


        public ViewHolder(View v) {
            super(v);
            groupName = (TextView) itemView.findViewById(R.id.group_item_name);
            teacherName = (TextView) itemView.findViewById(R.id.group_item_teacher);
            studentsCount = (TextView) itemView.findViewById(R.id.group_item_students_count);
        }
    }

    public GroupAdapter(List<GroupEntity> groups) {
        this.groups = groups;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("dbg: adapter", "onCreate");
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("dbg: adapter", "onBind " + position);
        GroupEntity group = groups.get(position);
        holder.groupName.setText(group.getName());
        holder.teacherName.setText(group.getTeacher());
        holder.studentsCount.setText(Integer.toString(group.getSize()));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}