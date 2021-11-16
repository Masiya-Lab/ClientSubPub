package nmu.wrpv.clientsubpub.Messages.client;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class AddTopic extends Message {
    private static final long serialVersionUID=7L;

    public String topicName;

    public AddTopic(String topicName) {
        this.topicName = topicName;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("AddTopic('%s')", topicName);
    }

}
