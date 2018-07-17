package ru.shemplo.pluses.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.TopicEntity;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private List<TopicEntity> topics;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name; //TODO: anything more?
        private RecyclerView taskRecycler;


        public ViewHolder(View v) {
            super(v);
            name = (TextView) itemView.findViewById(R.id.topic_name);
            taskRecycler = (RecyclerView) itemView.findViewById(R.id.task_recycler_view);
        }
    }

    public TopicAdapter(List<TopicEntity> data) {
        topics = data;
    }

    //private parent.getContext();

    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("dbg: adapter", "onCreate");
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("dbg: adapter", "onBind " + position);
        TopicEntity topic = topics.get(position);
        holder.name.setText(topic.getName());

        holder.taskRecycler.setLayoutManager(new GridLayoutManager(context, 7));
        TaskAdapter tmp = new TaskAdapter(topic.getTasks());
        holder.taskRecycler.setAdapter(tmp);
        Log.e("dbg: TopicAdapter", tmp.getItemCount() + "");
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}