package nmu.wrpv.clientsubpub.Messages.server;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class Unsubscribed extends Message {
    private static final long serialVersionUID=103L;

    public String topicName;

    @NonNull
    @Override
    public String toString() {
        return  String.format("Unsubscribed(%s)", topicName);
    }

}
