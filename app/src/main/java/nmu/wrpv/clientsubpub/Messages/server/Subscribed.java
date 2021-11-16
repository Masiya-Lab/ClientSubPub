package nmu.wrpv.clientsubpub.Messages.server;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class Subscribed extends Message {
    private static final long serialVersionUID = 102L;

    public String topicName;

    @NonNull
    @Override
    public String toString() {
        return  String.format("Subscribed (%s)", topicName);
    }

}
