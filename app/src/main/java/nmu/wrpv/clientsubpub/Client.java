package nmu.wrpv.clientsubpub;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nmu.wrpv.clientsubpub.Messages.Message;
import nmu.wrpv.clientsubpub.Messages.PublishReceiver;
import nmu.wrpv.clientsubpub.Messages.client.AddTopic;
import nmu.wrpv.clientsubpub.Messages.client.PublishMessage;
import nmu.wrpv.clientsubpub.Messages.client.Quit;
import nmu.wrpv.clientsubpub.Messages.client.Subscribe;
import nmu.wrpv.clientsubpub.Messages.client.Unsubscribe;

public class Client implements Serializable {
    private String serverAddress;

    //I/O streams for communication
    private ObjectInputStream in =null;

    private ObjectOutputStream out = null;

    //using a thread safe queue to handle multiple thread adding to the same
    //queue (potentially) and a single thread de-queueing and sending messages
    //across the network/internet
    private BlockingQueue<Message> outGoingMessages;

    private PublishReceiver messageReceiver;

    private Thread readThread;

    private Thread writeThread;

    /**
     * Enqueues a message to be sent to the server
     * @param message the message to be sent
     */
    private void send(Message message){
        try {
            outGoingMessages.put(message);
        }catch (InterruptedException e){
            Log.e("Client", e.getMessage());
        }
    }

    public void publishMessage(String message){
        send(new PublishMessage(message));
    }

    public Client(PublishReceiver messageReceiver){
      super();
      outGoingMessages = new LinkedBlockingQueue<>();
      this.messageReceiver = messageReceiver;
    }

    public void connect(String serverAddress) {

        Log.i("Client", "connecting : "+serverAddress+"...");
        //cached information
        this.serverAddress = serverAddress;
        //details about the client

        //Start the read thread (which establishes a connection)
        Log.i("Client","Starting Read loop thread");

        readThread = new ReadThread();
        readThread.start();

    }

    public void disconnect(){
        send(new Quit());
    }

    /**
     * Add a new topic to the server's list of topic
     * @param topicName the name of the new topic to publish to
     */
    public void addTopic(String topicName) {
        send(new AddTopic(topicName));
    }

    public void SubscribeToTopic(String topic){
        send(new Subscribe(topic));
    }

    public void UnsubscribeFromTopic(String topic){send(new Unsubscribe(topic));}

    private class ReadThread extends  Thread{
        @Override
        public void run() {
            Log.i("Client", "Started Read loop thread: ");

            readThread = this;

            try{
                Socket connection = new Socket(serverAddress,5050);
                Log.i("Client", "Connected to "+serverAddress+"...");

                in = new ObjectInputStream(connection.getInputStream());

                out = new ObjectOutputStream(connection.getOutputStream());

                out.flush();

                Log.i("Client", "Obtained I/O streams...");

                Log.i("Client", "Starting write loop thread");

                writeThread = new WriteThread();
                writeThread.start();
                Log.i("Client", "Starting Read loop");

                Message msg;

                do {
                    msg = (Message) in.readObject();
                    Log.i("Client", ">> "+msg);

                    if(messageReceiver!=null){
                        messageReceiver.messagedReceived(msg);
                    }

                }while (msg.getClass()!=Quit.class);

                // Done, so close connection.
                connection.close();

                Log.i("Client", "Closed Connection...");
            }
            catch (Exception e)
            {
                Log.i("Client", "Exception: "+e.getMessage());
            }
            finally {
                readThread = null;

                if(writeThread!=null) writeThread.interrupt();
            }
        }
    }

    private class WriteThread extends  Thread{
        @Override
        public void run() {
            Log.i("Client", "Started write loop thread: ");

            try{
                while (true){
                    Message msg = outGoingMessages.take();

                    out.writeObject(msg);
                    out.flush();

                    Log.i("Client", "<< "+msg);
                }
            }catch (Exception e){
                writeThread=null;
            }
        }
    }


}
