package nmu.wrpv.clientsubpub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import nmu.wrpv.clientsubpub.Messages.Message;
import nmu.wrpv.clientsubpub.Messages.server.PublishMessageReceived;
import nmu.wrpv.clientsubpub.Messages.server.TopicsListed;

public class Publisher_view extends AppCompatActivity {
    //The chat client that will communicate with the server
    private Client client;

    //cached references to views
    private RecyclerView lstTopics;
    private RecyclerView lstMessages;
    private View toDisConnect;
    private View toTopic;
    private View toPublish;
    private View toSub;
    private Spinner spnTopics;

    //Adapter for the content of the topic name spinner and
    //Message recycler view
    private PubSubAdapter adapter;
    private SubAdapter subAdapter;
    private ArrayAdapter<String> topicNameAdapter;

    //Fields used to remember when updating the topic name spinner
    //versus user selecting an item in it
    private boolean updatingTopicNames =false;
    private boolean isSubscriber = false;

    //IP address
    private String IPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_view);

        //set up recycler View
        lstMessages = findViewById(R.id.lstMessages);
        adapter = new PubSubAdapter();


        //how will the item be displayed
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getApplicationContext());

        //Assign adapter to list Viewer
        lstMessages.setLayoutManager(layoutManager);
        lstMessages.setAdapter(adapter);

        //check whether there is permission to use the internet
        boolean hasPermission = checkSelfPermission(Manifest.permission.INTERNET)==
                PackageManager.PERMISSION_GRANTED;

        addLogMessage("Internet Permission = "+hasPermission);

        //Get cached references
        toDisConnect = findViewById(R.id.toDisconnect);
        toTopic = findViewById(R.id.toTopic);
        toPublish = findViewById(R.id.toPublishMessage);
        toSub = findViewById(R.id.toSub);
        spnTopics = findViewById(R.id.spnTopics);
        //Get Adapter to display a topic name

        topicNameAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new ArrayList<String>());

        spnTopics.setAdapter(topicNameAdapter);
        //set on selected listener
        spnTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(updatingTopicNames) return;

                String topicName =  topicNameAdapter.getItem(position);
                client.SubscribeToTopic(topicName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toDisConnect.setVisibility(View.GONE);
        toTopic.setVisibility(View.GONE);
        toPublish.setVisibility(View.GONE);
        toSub.setVisibility(View.GONE);
        Intent intent = getIntent();

        if(intent!=null){

            if(client==null){
                IPAddress = intent.getStringExtra(MainActivity.CONNECT).trim();
            }

            isSubscriber = intent.getBooleanExtra(MainActivity.KEY,false);

        }else{
            return;
        }

        EditText editMessage = findViewById(R.id.editPublishMessage);

        editMessage.setOnKeyListener((view, i, keyEvent) -> {
            if(keyEvent.getKeyCode()!=keyEvent.KEYCODE_ENTER || (keyEvent.getAction()!= KeyEvent.ACTION_DOWN)) return false;

            //send text to group
            String chatMessage = editMessage.getText().toString();
            client.publishMessage(chatMessage);

            //clear text in edit view
            editMessage.setText("");

            return true;
        });

        //Connect to server
            ConnectToServer();

           //to display list of topics subscriber can subscribe to
        lstTopics = findViewById(R.id.lstTopics);
        //set adaptor
        subAdapter = new SubAdapter(client);

        lstTopics.setAdapter(subAdapter);
        lstTopics.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public void ConnectToServer(){
        Log.i("Client","Connecting to"+IPAddress);

        client = new Client(
                message->runOnUiThread(()->onMessageReceived(message))
        );

        client.connect(IPAddress);

        addLogMessage("Connected to "+IPAddress);

       if(isSubscriber){
           toDisConnect.setVisibility(View.GONE);
           toTopic.setVisibility(View.GONE);
           toPublish.setVisibility(View.GONE);
           toSub.setVisibility(View.VISIBLE);
       }else{
           toDisConnect.setVisibility(View.VISIBLE);
           toTopic.setVisibility(View.VISIBLE);
           toPublish.setVisibility(View.VISIBLE);
           toSub.setVisibility(View.GONE);
       }
    }

    public void onMessageReceived(Message msg){
        if(msg instanceof PublishMessageReceived){
            addPublishMessage((PublishMessageReceived)msg);
        }else{
            addLogMessage(msg.toString());
        }
        //if new group listing, then update the spinner
        if(msg instanceof TopicsListed){

            updatingTopicNames= true;

           TopicsListed tl = (TopicsListed)msg;
            topicNameAdapter.clear();
            topicNameAdapter.addAll(tl.topicNames);

            updatingTopicNames=false;
        }
    }

    protected void addLogMessage(String text){
        adapter.addLogMessage(text);
        lstMessages.smoothScrollToPosition(adapter.getItemCount()-1);

    }

    protected  void addPublishMessage(PublishMessageReceived message){
        adapter.addPublishMessage(message);
        lstMessages.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    public void onAddTopicClicked(View view){
        //get the new name
        EditText editTopicName = findViewById(R.id.editTopicName);

        String topicName = editTopicName.getText().toString();

        client.addTopic(topicName);
        //Clear edit view
        editTopicName.setText("");

        //update Topic List
        subAdapter.updateTopics(new TopicsListed());
    }

    public void onDisconnectedClicked(View view){
        addLogMessage("Disconnecting...");

        client.disconnect();

        toDisConnect.setVisibility(View.GONE);
        toTopic.setVisibility(View.GONE);
        toPublish.setVisibility(View.GONE);
        toSub.setVisibility(View.GONE);

        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
    }

}