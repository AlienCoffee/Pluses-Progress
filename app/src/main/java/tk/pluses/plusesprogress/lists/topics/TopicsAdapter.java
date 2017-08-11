package tk.pluses.plusesprogress.lists.topics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.pluses.plusesprogress.DiaryMenuPage;
import tk.pluses.plusesprogress.R;


public class TopicsAdapter extends RecyclerView.Adapter {

    private final List<TopicEntity> topicsList;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final Map<Integer, RecyclerView.ViewHolder> holders;

    public TopicsAdapter(Context context, List<TopicEntity> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.topicsList = list;
        this.context = context;
        this.holders = new HashMap<>();

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TopicViewHolder.getInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holders.put(position, holder);

        TopicEntity topicEntity = topicsList.get(position);
        TopicViewHolder customHolder = (TopicViewHolder) holder;

        customHolder.position = position;
        customHolder.topicIDValue.setText(topicEntity.ID + "");

        if (topicEntity.getName() != null) {
            customHolder.topicTitle.setText(topicEntity.getName());
        }//??
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }

    private static class TopicViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public final TextView topicTitle,
                topicIDValue,
                topicCountValue,
                topicAuthor;

        public TopicViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener () {
                public void onClick (View v) {
                    int topicID = Integer.parseInt (topicIDValue.getText ().toString ());
                    Log.i(this.getClass().getSimpleName(), "Position: " + position);
                    DiaryMenuPage.page.currentTopic = topicID;

                    DiaryMenuPage.page.switchFragment (R.id.navigation_item_users);
                }
            });

            topicTitle = (TextView) itemView.findViewById(R.id.topicTitle);
            topicIDValue = (TextView) itemView.findViewById(R.id.topicIDValue);
            topicCountValue = (TextView) itemView.findViewById(R.id.topicProblemsCount);
            topicAuthor = (TextView) itemView.findViewById(R.id.topicAuthorValue);

            Log.i(this.getClass().getSimpleName(), "Registered");
        }

        public static TopicViewHolder getInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_topic, parent, false);
            return new TopicViewHolder(view);
        }

    }

    public void updateItem(int position) {
        TopicEntity topicEntity = topicsList.get(position);
        TopicsAdapter.TopicViewHolder customHolder = (TopicsAdapter.TopicViewHolder) holders.get(position);
        customHolder.topicTitle.setText(topicEntity.getName());
        customHolder.topicIDValue.setText(topicEntity.ID + "");
        customHolder.topicAuthor.setText(topicEntity.getAuthorID()+"");
        customHolder.topicCountValue.setText(topicEntity.getRatedCounter() + "/" + topicEntity.getTotalCounter());
    }

}
