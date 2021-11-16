package nmu.wrpv.clientsubpub.Messages.client;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class ListTopics extends Message {
    private static final long serialVersionUID=3L;

    @NonNull
    @Override
    public String toString() {
        return "ListTopics";
    }

}
