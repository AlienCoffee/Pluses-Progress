package ru.shemplo.pluses.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.MyEntity;
import ru.shemplo.pluses.entity.TaskEntity;
import ru.shemplo.pluses.entity.TopicEntity;
import ru.shemplo.pluses.network.DataProvider;

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyEntity> data;

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        private TextView topicName;

        public TopicViewHolder(View view) {
            super(view);
            topicName = (TextView) itemView.findViewById(R.id.topic_name);
        }

        public void setName(String name) {
            topicName.setText(name);
        }

    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView taskName;
        private CheckBox solved;

        public TaskViewHolder(View view) {
            super(view);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            solved = (CheckBox) itemView.findViewById(R.id.box_solved);
        }

        public void setName(String name) {
            taskName.setText(name);
        }

        public void setSolved(boolean solved) {
            this.solved.setChecked(solved);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof TopicEntity) {
            return R.id.topic_item;
        }
        if (data.get(position) instanceof TaskEntity) {
            return R.id.task_item;
        }
        return -1;
    }

    public TopicAdapter(List<TopicEntity> topics, Context context) {
        data = new ArrayList<>();
        DataProvider provider = new DataProvider(context);
        for (TopicEntity topic : topics) {
            data.add(topic);
            data.addAll(provider.getTasks(topic.getID()));
            // TODO: add tasks here
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.e("dbg: adapter", "onCreate");
        if (viewType == R.id.topic_item) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.topic_item, parent, false);
            return new TopicViewHolder(view);
        }
        if (viewType == R.id.task_item) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_item, parent, false);
            return new TaskViewHolder(view);
        }
        Log.e("dbg: ERROR", "undefined");
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Log.e("dbg: adapter", "onBind " + position);

        if (getItemViewType(position) == R.id.topic_item) {
            TopicEntity topic = (TopicEntity) data.get(position);
            TopicViewHolder viewHolder = (TopicViewHolder) holder;
            viewHolder.setName(topic.getTitle ());
            StaggeredGridLayoutManager.LayoutParams layoutParams
                = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else if (getItemViewType(position) == R.id.task_item) {
            TaskEntity task = (TaskEntity) data.get(position);
            TaskViewHolder viewHolder = (TaskViewHolder) holder;
            viewHolder.setName(task.getName());
        } else {
            Log.e("dbg: ERROR", "undefined");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}