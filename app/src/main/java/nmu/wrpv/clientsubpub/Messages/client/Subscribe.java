package nmu.wrpv.clientsubpub.Messages.client;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class Subscribe extends Message {
    private static final long serialVersionUID=1L;

    public String topicName;

    public Subscribe(String topicName) {
        this.topicName = topicName;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Subscribe('%s')",topicName);
    }

}
