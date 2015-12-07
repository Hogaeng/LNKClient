package com.example.khk.lknmessenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by KHK on 2015-12-06.
 */
public class MessageActivity extends Activity {

    private Button Send;
    private EditText editText;
    private ListView listView;
    private String message;
    private String sendMsg,recvMsg;
    private MssReq mssReq;
    private MssAck mssAck;
    private Packet packet;
    private ArrayAdapter<String> arrAdapter ;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagewindow);

        arrAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

        Send = (Button)findViewById(R.id.Send);
        editText = (EditText)findViewById(R.id.EditText);
        listView = (ListView)findViewById(R.id.Message);
        message = editText.getText().toString();

        /*f(message.equals(""))
            Send.setEnabled(false);
        else
            Send.setEnabled(true);*/

        Send.setOnClickListener(OnClickListener);

        mssReq = new MssReq();
        mssAck = new MssAck();

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mssReq.setMessage(message);
            sendMsg = PacketCodec.encodeMssReq(mssReq);
            Log.e("sendMsg", sendMsg);

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

            mssAck = PacketCodec.decodeMssAck(packet.getData());
            //  analyzing the ACK sent from server
            if( mssAck.getAnswer() == Packet.FAIL ) {
                AlertDialog.Builder dialog = new AlertDialog.Builder( MessageActivity.this );
                dialog.setMessage("Please Input Correct Sentence");
                dialog.setPositiveButton("ok", null);
                dialog.show();
            }                   //  if the ACK means login fail, create the alert dialog

            else if( mssAck.getAnswer() == Packet.SUCCESS ) {
                    arrAdapter.add(message) ;
                    message = "";
                    editText.setText("");
            }

        }
    };
}
