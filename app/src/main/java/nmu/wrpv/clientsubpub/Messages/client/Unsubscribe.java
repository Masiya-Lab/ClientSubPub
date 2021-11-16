package nmu.wrpv.clientsubpub.Messages.client;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class Unsubscribe extends Message {
    private static final long serialVersionUID=2L;
    public String topicName;

    public Unsubscribe(String topicName) {
        this.topicName = topicName;
    }
    @NonNull
    @Override
    public String toString() {
        return String.format("Unsubscribe(%s)", topicName);
    }

}
