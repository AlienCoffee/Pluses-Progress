package ru.shemplo.pluses.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.TaskEntity;
import ru.shemplo.pluses.entity.TopicEntity;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<TaskEntity> tasks;//TODO: rewrite

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName; //TODO: anything more?
        private CheckBox solved;


        public ViewHolder(View view) {
            super(view);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            solved = (CheckBox) itemView.findViewById(R.id.box_solved);
        }
    }

    public TaskAdapter(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("dbg: TaskAdapter", "onCreate");
        View item = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("dbg: TaskAdapter", "onBind " + position);
        TaskEntity task = tasks.get(position);
        holder.taskName.setText(task.getName());
        holder.solved.setChecked(task.getSolved());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}