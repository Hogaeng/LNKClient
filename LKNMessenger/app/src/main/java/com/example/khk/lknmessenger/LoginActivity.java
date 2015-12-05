package com.example.khk.lknmessenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by KHK on 2015-11-24.
 */
public class LoginActivity extends Activity{
    PendingIntent pendingIntent;
    Context mContext;
    String UserID,UserPW;
    String sendMsg,recvMsg;
    EditText IDText,PWText;
    Button Login,Join;
    private Packet packet;
    private LoginReq loginReq;
    private LoginAck loginAck;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwindow);
        mContext = this.getApplicationContext();

        IDText = (EditText)findViewById(R.id.IDText);
        PWText = (EditText)findViewById(R.id.PWText);
        Login = (Button)findViewById(R.id.LoginButton);
        Join = (Button)findViewById(R.id.JoinButton);

        Login.setOnClickListener(OnClickListener);
        Join.setOnClickListener(OnClickListener);

        loginReq = new LoginReq();
        loginAck = new LoginAck();

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        public void onClick( View view ) {
            int id = view.getId();
            switch(id)
            {
                case R.id.LoginButton:    //  access(log in)
                    if (!((IDText.getText().toString() != null) && (!IDText.getText().toString().equals("")) &&
                            (PWText.getText().toString() != null) && (!PWText.getText().toString().equals("")))) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder( LoginActivity.this );
                        dialog.setMessage("Invalid ID or password.");
                        dialog.setPositiveButton("ok", null);
                        dialog.show();
                        //  if text edits have no data, create an alert dialog
                    }

                    else
                    {
                        UserID = IDText.getText().toString();
                        UserPW = PWText.getText().toString();
                        loginReq.setId(UserID);
                        loginReq.setPassword(UserPW);
                        sendMsg = PacketCodec.encodeLoginReq(loginReq);

                        try {
                            SocketManager.getSocket();  //  connecting TCP socket to server

                            SocketManager.sendMsg( sendMsg );   //  sending REQ to server
                            Log.d( "sendMsg in LA", sendMsg );
                            recvMsg = SocketManager.receiveMsg();   //  receiving ACK from server
                            Log.d( "recvMsg in LA", recvMsg );
                            packet = PacketCodec.decodeHeader( recvMsg );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        loginAck = PacketCodec.decodeLoginAck( packet.getData() );
                        //  analyzing the ACK sent from server
                        if( loginAck.getAnswer() == Packet.FAIL ) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder( LoginActivity.this );
                            dialog.setMessage("Invalid address or password.");
                            dialog.setPositiveButton("ok", null);
                            dialog.show();

                        }                   //  if the ACK means login fail, create the alert dialog

                        else if( loginAck.getAnswer() == Packet.SUCCESS ) {
                            Toast.makeText(getBaseContext(),"Login Successful!",Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;

                case R.id.JoinButton:
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
                    break;

            }

        }
    };


}
