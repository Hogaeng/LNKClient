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

/**
 * Created by KHK on 2015-12-07.
 */
public class LobbyActivity extends TabActivity {
    private String sendMsg,recvMsg;
    private TabHost mTabHost;
    private ListView friendlist,talklist;
    private Button makeRoom;
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

        arrAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        arrAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2);

        makeRoom = (Button)findViewById(R.id.MakeRoom);
        talklist = (ListView)findViewById(R.id.Talklist);
        friendlist = (ListView)findViewById(R.id.Friendlist);


        makeRoom.setOnClickListener(OnClickListener);
        talklist.setOnItemClickListener(setOnItemClickListener);
        friendlist.setAdapter(arrAdapter1);
        talklist.setAdapter(arrAdapter2);
        mTabHost = getTabHost();

        // 탭 1, 2, 3 을 추가하면서 태그를 달아주고, 제목(또는 아이콘)을 설정한다.
        mTabHost.addTab(mTabHost.newTabSpec("Friendlist").setContent(R.id.Friendlist).setIndicator("Friendlist"));
        mTabHost.addTab(mTabHost.newTabSpec("Talklist").setContent(R.id.Talklist).setIndicator("Talklist"));


        lobbyReq = new LobbyReq();
        lobbyAck = new LobbyAck();
        makeRoomReq = new MakeRoomReq();
        makeRoomAck = new MakeRoomAck();

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AlertMakeRoom room = new AlertMakeRoom();
            room.show(fm, " ");

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
                AlertDialog.Builder dialog = new AlertDialog.Builder( LobbyActivity.this );
                dialog.setMessage("Failed to make room");
                dialog.setPositiveButton("ok", null);
                dialog.show();

            }                   //  if the ACK means login fail, create the alert dialog

            else if( makeRoomAck.getAnswer() == Packet.SUCCESS ) {
                arrAdapter2.add(Roomname);
                Toast.makeText(getBaseContext(), "Make New Room", Toast.LENGTH_SHORT).show();
            }

        }


    };

    private AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent = new Intent( LobbyActivity.this, MessageActivity.class );
            startActivity(intent);
        }


    };

    class AlertMakeRoom extends DialogFragment {
        EditText mEdit;
        public String Name;
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.alertmakeroom, null);
            builder.setView(layout);
            mEdit = (EditText)layout.findViewById(R.id.name);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Name = mEdit.getText().toString();
                    Toast.makeText(getActivity(),"Setting Room Name : "+Name,Toast.LENGTH_SHORT).show();
                    Roomname = Name;
                }
            });

            return builder.create();

        }
    }



}
