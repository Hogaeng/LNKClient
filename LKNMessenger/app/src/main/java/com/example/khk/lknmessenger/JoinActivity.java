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
public class JoinActivity extends Activity {
    PendingIntent pendingIntent;
    Context mContext;
    EditText InputName, InputID, InputPW, InputRepeat;
    String UserName, UserID, UserPW, UserRepeat;
    String sendMsg,recvMsg;
    Button SignUp, Cancel;
    Packet packet;
    private JoinReq joinReq;
    private JoinAck joinAck;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinwindow);
        mContext = this.getApplicationContext();

        InputName = (EditText) findViewById(R.id.NameEdit);
        InputID = (EditText) findViewById(R.id.IDEdit);
        InputPW = (EditText) findViewById(R.id.PWEdit);
        InputRepeat = (EditText) findViewById(R.id.RepeatEdit);

        SignUp = (Button) findViewById(R.id.SignupButton);
        Cancel = (Button) findViewById(R.id.CancelButton);

        SignUp.setOnClickListener(OnClickListener);
        Cancel.setOnClickListener(OnClickListener);



        joinReq = new JoinReq();
        joinAck = new JoinAck();


    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Intent intent;
            switch (id) {
                case R.id.SignupButton:
                    if (!((InputName.getText().toString() != null) && (!InputName.getText().toString().equals("")) &&
                            (InputID.getText().toString() != null) && (!InputID.getText().toString().equals("")) &&
                            (InputPW.getText().toString() != null) && (!InputPW.getText().toString().equals("")) &&
                            (InputRepeat.getText().toString() != null) && (!InputRepeat.getText().toString().equals("")))) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder( JoinActivity.this );
                        dialog.setMessage("You have void space somewhere");
                        dialog.setPositiveButton("ok", null);
                        dialog.show();
                        //  if text edits have no data, create an alert dialog
                    }

                    else
                    {
                        UserName = InputName.getText().toString();
                        UserID = InputID.getText().toString();
                        UserPW = InputPW.getText().toString();
                        UserRepeat = InputRepeat.getText().toString();

                        joinReq.setName(UserName);
                        joinReq.setId(UserID);
                        joinReq.setPassword(UserPW);

                        Log.e("UserName", UserName);
                        Log.e("UserID", UserID);
                        Log.e("UserPW",UserPW);

                        sendMsg = PacketCodec.encodeJoinReq(joinReq);
                        try {
                            Log.e("2sendMsg", "Link Start!!"); //Toast.makeText(getBaseContext(),"Link Start!!",Toast.LENGTH_SHORT).show();
                            SocketManager.sendMsg(sendMsg);
                            Log.e("3sendMsg", sendMsg);
                            recvMsg = SocketManager.receiveMsg();
                            Log.e("recvMsg", recvMsg);
                            packet = PacketCodec.decodeHeader(recvMsg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        joinAck = PacketCodec.decodeJoinAck(packet.getData());
                        //  analyzing the ACK sent from server
                        if( joinAck.getAnswer() == Packet.FAIL ) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder( JoinActivity.this );
                            dialog.setMessage("It's already have same ID");
                            dialog.setPositiveButton("ok", null);
                            dialog.show();

                        }                   //  if the ACK means login fail, create the alert dialog

                        else if( joinAck.getAnswer() == Packet.SUCCESS ) {
                            Toast.makeText(getBaseContext(), "Sign up Clear!", Toast.LENGTH_SHORT).show();
                            intent = new Intent( JoinActivity.this, LoginActivity.class );
                            startActivity(intent);
                            finish();
                        }

                    }
                    break;

                case R.id.CancelButton:
                    intent = new Intent(JoinActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
            }

        }
    };
}


