package nmu.wrpv.clientsubpub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
  public static final String CONNECT ="ConnectKey";
  public static final String KEY="SubKey";
   private EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editAddress = findViewById(R.id.editIPAddress);
    }

    public void publisherClicked(View view){
        Intent intent = new Intent(this,Publisher_view.class);
        String ipAddress = editAddress.getText().toString();

        if(ipAddress.equals("")){
           Toast.makeText(this,"Enter Correct IP Address to Continue",Toast.LENGTH_LONG).show();
        }else{
            intent.putExtra(CONNECT,ipAddress);
            startActivity(intent);
        }

    }


    public void subscriberClicked(View view){
        Intent intent = new Intent(this,Publisher_view.class);

        String ipAddress = editAddress.getText().toString();

        if(ipAddress.equals("")){
            Toast.makeText(this,"Enter Correct IP Address to Continue",Toast.LENGTH_LONG).show();
        }else{
            intent.putExtra(CONNECT,ipAddress);
            intent.putExtra(KEY,true);
            startActivity(intent);
        }
    }
}