package com.example.khk.lknmessenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by KHK on 2015-11-24.
 */
public class JoinActivity extends Activity {
    PendingIntent pendingIntent;
    Context mContext;
    EditText InputName,InputID,InputPW,InputRepeat;
    String UserName,UserID,UserPW,UserRepeat;
    Button Join,Cancel;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinwindow);
        mContext = this.getApplicationContext();

        InputName = (EditText)findViewById(R.id.NameEdit);
        InputID = (EditText)findViewById(R.id.IDEdit);
        InputPW = (EditText)findViewById(R.id.PWEdit);
        InputRepeat = (EditText)findViewById(R.id.RepeatEdit);

        Join = (Button)findViewById(R.id.JoinButton);
        Cancel = (Button)findViewById(R.id.CancelButton);

        UserName = InputName.getText().toString();
        UserID = InputID.getText().toString();
        UserPW = InputPW.getText().toString();
        UserRepeat = InputRepeat.getText().toString();

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserName == null || UserID == null|| UserPW == null || UserRepeat == null)
                {

                    Toast.makeText(getBaseContext(),"You didn't write some information",Toast.LENGTH_SHORT).show();
                }

            }
        });


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext,LoginActivity.class);
                pendingIntent = PendingIntent.getActivity(mContext,0,intent,0);
                try
                {
                    pendingIntent.send(mContext,0,intent);
                }
                catch(PendingIntent.CanceledException e)
                {
                    System.out.println("Sending contentIntent failed: ");
                }
            }
        });


    }
}
