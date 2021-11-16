package nmu.wrpv.clientsubpub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nmu.wrpv.clientsubpub.Messages.server.PublishMessageReceived;

public class PubSubAdapter extends RecyclerView.Adapter<PubSubAdapter.MessageViewHolder> {
     private final List<PublishMessageReceived> messages;

    public PubSubAdapter (){
        messages = new ArrayList<>();
    }
    public void addLogMessage(String text) {
        addPublishMessage(new PublishMessageReceived(text));
    }

    public void addPublishMessage(PublishMessageReceived message) {
        messages.add(message);

        notifyItemChanged(messages.size()-1);
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pub_message_view,
                        parent,false);

        MessageViewHolder mvh= new MessageViewHolder(view);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        PublishMessageReceived message = messages.get(position);

        holder.setMessage(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTopic;
        public TextView txtTimeStamp;
        public TextView txtMessage;

        public TextView txtLogMessage;

        public PublishMessageReceived message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTopic = itemView.findViewById(R.id.txtPublishTopic);
            txtTimeStamp = itemView.findViewById(R.id.txtTimeStamp);
            txtMessage = itemView.findViewById(R.id.txtPublishMessage);
            txtLogMessage = itemView.findViewById(R.id.txtLogMessage);

        }

        public void setMessage(PublishMessageReceived message) {
                   this.message = message;
            if(message.topicName.equals("")){
                txtMessage.setVisibility(View.GONE);
                txtTopic.setVisibility(View.GONE);
                txtTimeStamp.setVisibility(View.GONE);
                txtLogMessage.setVisibility(View.VISIBLE);
                txtLogMessage.setText(message.publishMessage);
            }else{
                txtMessage.setVisibility(View.VISIBLE);
                txtTopic.setVisibility(View.VISIBLE);
                txtTimeStamp.setVisibility(View.VISIBLE);
                txtLogMessage.setVisibility(View.GONE);

                txtTopic.setText(message.topicName);
                txtTimeStamp.setText(message.timeStamp.toString());
                txtMessage.setText(message.publishMessage);

            }
        }
    }
}
