package nmu.wrpv.clientsubpub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nmu.wrpv.clientsubpub.Messages.server.TopicsListed;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubscribeViewHolder>{

    private List<String> topics;

    private Client client;

    public SubAdapter(Client client) {

        this.topics = new ArrayList<>();
        this.client=client;
    }

    public void updateTopics(TopicsListed listed){
        topics.addAll(listed.topicNames);
    }

    @NonNull
    @Override
    public SubscribeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_topic,
                        parent,false);

        SubAdapter.SubscribeViewHolder mvh= new SubAdapter.SubscribeViewHolder(view);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribeViewHolder holder, int position) {
        String topic = topics.get(position);

        holder.setTopic(topic);

        holder.btnSubscribe.setOnClickListener(view->{
            String name = ((Button)view).getText().toString().trim().toLowerCase();
            if(name.equals("subscribe")){
                client.SubscribeToTopic(topic);
                ((Button)view).setText(R.string.btnPlaceHolderUnsubscribe);
            }else{
                client.UnsubscribeFromTopic(topic);
                ((Button)view).setText(R.string.btnPlaceHolderSubscribe);
            }

        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class SubscribeViewHolder extends RecyclerView.ViewHolder{

        public TextView txtRecyclerTopic;
        public Button btnSubscribe;
        public SubscribeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRecyclerTopic =itemView.findViewById(R.id.txtRecyclerTopic);

            btnSubscribe = itemView.findViewById(R.id.btnSubscribe);

        }

        public void setTopic(String message) {
            txtRecyclerTopic.setText(message);
        }
    }
}
