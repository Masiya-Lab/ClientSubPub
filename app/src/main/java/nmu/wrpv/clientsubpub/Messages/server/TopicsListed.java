package nmu.wrpv.clientsubpub.Messages.server;

import androidx.annotation.NonNull;

import java.util.List;

import nmu.wrpv.clientsubpub.Messages.Message;

public class TopicsListed extends Message {
    private static final long serialVersionUID =101L;

    public List<String> topicNames;

    @NonNull
    @Override
    public String toString() {
        return  String.format("TopicListed(%s)", topicNames);
    }

}
