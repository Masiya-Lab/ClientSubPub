package nmu.wrpv.clientsubpub.Messages.client;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class PublishMessage extends Message {

    private static final  long serialVersionUID=5L;

    public String publishMessage;

    public PublishMessage(String PublishMessage) {
        this.publishMessage = PublishMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("PublishMessage('%s')",publishMessage);
    }

}
