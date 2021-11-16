package nmu.wrpv.clientsubpub.Messages.server;

import java.util.Date;

import nmu.wrpv.clientsubpub.Messages.Message;

public class PublishMessageReceived extends Message {

    private static final  long serialVersionUID=100L;

    public Date timeStamp;
    public String topicName;
    public String publishMessage;

    public PublishMessageReceived(Date timeStamp, String topicName, String publishMessage) {
        this.timeStamp = timeStamp;
        this.topicName = topicName;
        this.publishMessage = publishMessage;
    }

    public PublishMessageReceived(String publishMessage) {
        timeStamp=null;
        topicName ="";
        this.publishMessage = publishMessage;
    }
}
