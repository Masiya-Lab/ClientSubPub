package nmu.wrpv.clientsubpub.Messages;
@FunctionalInterface
public interface PublishReceiver {
    void messagedReceived(Message message);
}
