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
public class LoginActivity extends Activity{
    PendingIntent pendingIntent;
    Context mContext;
    String ID = "Android";
    String PW = "123456";
    EditText IDText,PWText;
    Button Login,Join;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwindow);
        mContext = this.getApplicationContext();

        IDText = (EditText)findViewById(R.id.IDText);
        PWText = (EditText)findViewById(R.id.PWText);
        Login = (Button)findViewById(R.id.LoginButton);
        Join = (Button)findViewById(R.id.JoinButton);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID.equals(IDText.getText().toString()) == true && PW.equals(PWText.getText().toString()) == true)
                    Toast.makeText(getBaseContext(),"Login Success!",Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getBaseContext(),"Wrong Information!",Toast.LENGTH_SHORT).show();
            }
        });


        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext,JoinActivity.class);
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
