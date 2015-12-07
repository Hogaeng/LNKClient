package com.example.khk.lknmessenger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KHK on 2015-12-07.
 */
public class LobbyActivity extends TabActivity {

    private String sendMsg,recvMsg;
    private TabHost mTabHost;
    private TextView talklist;
    private EditText friendlist;
    private Button enterRoom;
    private ArrayAdapter<String> arrAdapter1,arrAdapter2;
    private LobbyReq lobbyReq;
    private LobbyAck lobbyAck;
    private MakeRoomReq makeRoomReq;
    private MakeRoomAck makeRoomAck;
    private Packet packet;
    private Intent intent;
    private String Roomname;


    FragmentManager fm = getFragmentManager();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobbywindow);

        final ArrayList arr = new ArrayList();
        arr.add("Room1");
        arr.add("Room2");
        arr.add("Room3");
        mTabHost = getTabHost();

        //arrAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        arrAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2,arr);
//        talklist.setAdapter(arrAdapter2);

        enterRoom = (Button)findViewById(R.id.MakeRoom);
        talklist = (TextView)findViewById(R.id.Talklist);
        friendlist = (EditText)findViewById(R.id.Friendlist);

        mTabHost = getTabHost();

        // 탭 1, 2, 3 을 추가하면서 태그를 달아주고, 제목(또는 아이콘)을 설정한다.
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setContent(R.id.Friendlist).setIndicator("Friendlist"));
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setContent(R.id.Talklist).setIndicator("Talklist"));

        enterRoom.setOnClickListener(OnClickListener);
        //talklist.setOnItemClickListener(setOnItemClickListener);
        //friendlist.setAdapter(arrAdapter1);



        lobbyReq = new LobbyReq();
        lobbyAck = new LobbyAck();
        makeRoomReq = new MakeRoomReq();
        makeRoomAck = new MakeRoomAck();

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditDialog();

        }

    };


    public void EditDialog() {
        final EditText alpha;
        AlertDialog.Builder editDialog;
        editDialog = new AlertDialog.Builder(LobbyActivity.this);
        LayoutInflater inflater = LobbyActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.alertmakeroom, null);

        alpha = (EditText) layout.findViewById(R.id.name);

        editDialog.setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Roomname = alpha.getText().toString();

                        makeRoomReq.setRoomName(Roomname);
                        Log.e("RoomName", Roomname);

                        sendMsg = PacketCodec.encodeMakeRoomReq(makeRoomReq);
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

                        makeRoomAck = PacketCodec.decodeMakeRoomAck(packet.getData());
                        //  analyzing the ACK sent from server
                        if( makeRoomAck.getAnswer() == Packet.FAIL ) {

                            Toast.makeText(getBaseContext(), "Failed to make Room", Toast.LENGTH_SHORT).show();

                        }                   //  if the ACK means login fail, create the alert dialog

                        else if( makeRoomAck.getAnswer() == Packet.SUCCESS ) {
                            Toast.makeText(getBaseContext(), "Make New Room", Toast.LENGTH_SHORT).show();
                            intent = new Intent( LobbyActivity.this, MessageActivity.class );
                            startActivity(intent);
                        }

                    }
                });

        editDialog.show();
    }


}
