package com.example.khk.lknmessenger;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadTcp implements Runnable{

	//private Base64Codec bs64 = new Base64Codec();//
	private Socket clientSocket = null;//

	private PrintWriter out = null;
	private BufferedReader in = null;

	private boolean isContinous = false;
	private int user = 0;
	private String user_id;
	private int presentRoom=0;
	private String RoomName;
	private Database db;
	String inputData;
	Packet rcvPacket;
	ResultSet rs;
	public ThreadTcp(Socket clientSocket, boolean isContinous) throws IOException{
		this.clientSocket = clientSocket;
		this.isContinous = isContinous;

		System.out.println("Client Connect");
		db = new Database();
		if(db.connect())
			System.out.println("DB Connect");
		else
			System.out.println("DB Fail...");
	}

	public ThreadTcp(ServerSocket serverSocket, boolean isContinous) throws IOException{
		clientSocket = serverSocket.accept();
		this.isContinous = isContinous;

		System.out.println("Client Connect");
		if(db.connect())
			System.out.println("DB Connect");
		else
			System.out.println("DB Fail...");
	}


	public void run(){
		try{
			System.out.println("ThreadTcp run step zero");
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			System.out.println("ThreadTcp run step one");
			while(isContinous){
				inputData = PacketCodec.readBuffReader(in);
				if(inputData==null){
					continue;
				}
				rcvPacket = PacketCodec.decodeHeader(inputData);//
				System.out.println("ThreadTcp step four");
				isContinous = handler(rcvPacket, out);//
				rcvPacket=null;
			}//
			in.close();
			out.close();
			clientSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean handler(Packet src, PrintWriter out)throws IOException
	{
		String sendString;

		switch(src.getType()){
			case Packet.LOG_REQ:
				System.out.println("Log REQ recevied.");
				LoginReq lo_req = PacketCodec.decodeLoginReq(src.getData());
				LoginAck lo_ack = new LoginAck();
				db.query = "select Id, Pw, Dbid from "+Database.memberData;//+" where * Dbid";
				rs = db.excuteStatementReturnRs();
				try{
					while(rs.next())
					{
						if(lo_req.getId().equals(rs.getString("Id"))){
							System.out.println("Login Ack : Id_Success");
							if(lo_req.getPassword().equals(rs.getString("Pw"))){
								lo_ack.setAnswerOk();
								user=rs.getInt("Dbid");
								user_id = rs.getString("Id");
								System.out.println("Login Ack : Success");
								break;
							}
						}
					}
					if(lo_ack.getAnswer()!=Packet.SUCCESS){
						lo_ack.setAnswerFail();
						System.out.println("Login Ack : Fail");
					}
					sendString = PacketCodec.encodeLoginAck(lo_ack);
					try{
						out.println(sendString);
						System.out.println("Login Ack dispatched.");
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;

			case Packet.JOIN_REQ:
				System.out.println("Join REQ recevied");
				JoinReq jo_req = PacketCodec.decodeJoinReq(src.getData());
				JoinAck jo_ack = new JoinAck();
				db.query = "select Id from "+Database.memberData;//+" where * Dbid";
				rs = db.excuteStatementReturnRs();
				jo_ack.setAnswerOk();
				try{
					while(rs.next())
					{
						if(jo_req.getId().equals(rs.getString("Id"))){
							jo_ack.setAnswerFail();
							System.out.println("Join Ack : Fail");
							break;
						}
					}

					if(jo_ack.getAnswer()!=Packet.FAIL){
						jo_ack.setAnswerOk();
						db.query = "insert into "+Database.memberData+" (Name,Id,Pw) values "
								+"('"+jo_req.getName()+"','"+jo_req.getId()+"','"+jo_req.getPassword()+"')";
						db.excuteStatement();
						System.out.println("Join Ack : Success");
					}
					sendString = PacketCodec.encodeJoinAck(jo_ack);
					try{
						out.println(sendString);
						System.out.println("JOin Ack dispatched.");
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				break;
			case Packet.MSS_REQ:
				System.out.println("MSS REQ recevied");
				MssReq mss_req = PacketCodec.decodeMssReq(src.getData());
				MssAck mss_ack = new MssAck();
				SimpleDateFormat sdf =null;
				Date dt = new Date();
				if(!mss_req.getMessage().equals(null)){
					sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
					try{
						db.query = "insert into "+Database.messBoard+" (RoomId, Id, SendStr, ArriveTime) values "
								+"('"+Integer.toString(presentRoom)+"','"+user_id+"','"+mss_req.getMessage()+"','"+sdf.format(dt).toString()+"')";
						db.excuteStatement();
						mss_ack.setArrtime(sdf.format(dt).toString());
					}
					catch(Exception e){
						e.printStackTrace();
						System.out.println("MSS Ack Fail..");
						mss_ack.setAnswerFail();
						mss_ack.setArrtime(null);
						sendString = PacketCodec.encodeMssAck(mss_ack);
						try{
							out.println(sendString);
							System.out.println("Mss Ack dispatched.");
						}
						catch(Exception t){
							t.printStackTrace();
						}

						break;
					}

				}
				db.query = "select Id, SendStr, ArriveTime from "+Database.messBoard+" where RoomId = '"+presentRoom+"'";
				db.rs = db.excuteStatementReturnRs();
				try{
					int DCount = 0;
					String Dd = "";
					while(db.rs.next())
					{
					/*Date to = sdf.parse(db.rs.getString("ArriveTime"));
					if(dt.before(to))
					{*/
						DCount++;
						Dd+=rs.getString("Id");
						Dd+=Packet.TINYDELIM;
						Dd+=rs.getString("SendStr");
						Dd+=Packet.TINYDELIM;
						Dd+=rs.getString("ArriveTime");
						Dd+=Packet.TINYDELIM;
						Dd+=Packet.SMALLDELIM;
						//}
					}
					mss_ack.setListNum(DCount);
					mss_ack.setlist(Dd);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				mss_ack.setAnswerOk();
				System.out.println("MSS Ack Success..");
				sendString = PacketCodec.encodeMssAck(mss_ack);
				try{
					out.println(sendString);
					System.out.println("Mss Ack dispatched.");
				}
				catch(Exception t){
					t.printStackTrace();
				}
				break;

			case Packet.GIVEMEM_REQ:
				System.out.println("GiveMem REQ recevied");
				GiveMemReq give_req = PacketCodec.decodeGiveMemReq(src.getData());
				GiveMemAck give_ack = new GiveMemAck();
				db.query = "select Id, Name from "+Database.memberData;//+" where * Dbid";
				rs = db.excuteStatementReturnRs();
				int count = 0;
				String name="";
				String Id="";
				try{
					while(rs.next())
					{
						count++;
						Id+=rs.getString("Id");
						Id+=Packet.SMALLDELIM;
						name+=rs.getString("Name");
						name+=Packet.SMALLDELIM;
					}

					give_ack.setmemberNum(count);
					give_ack.setmemberName(name);
					give_ack.setmemberId(Id);
					sendString = PacketCodec.encodeGiveMemAck(give_ack);
					try{
						out.println(sendString);
						System.out.println("GiveMem Ack dispatched.");
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case Packet.MAKEROOM_REQ:
				System.out.println("MAKEROOM REQ recevied");
				MakeRoomReq make_req = PacketCodec.decodeMakeRoomReq(src.getData());
				MakeRoomAck make_ack = new MakeRoomAck();

				try{
					db.query = "insert into "+Database.roomList+" (RoomName) values "
							+"('"+make_req.getRoomName()+"')";
					db.excuteStatement();
					RoomName = make_req.getRoomName();
					System.out.println("MakeRoom...");

					db.query = "select RoomId from "+Database.roomList+" where RoomName = '"+RoomName+"'";
					rs = db.excuteStatementReturnRs();
					rs.next();
					presentRoom = rs.getInt("RoomId");
					System.out.println("and Server recognize user is in the Room!");

				/*db.query = "insert into "+Database.messBoard+" (RoomId, Id) values "
						+"('"+presentRoom+"','"+user_id+"')";
				db.excuteStatement();
				System.out.println("and put user in the Room!");*/

				}
				catch(Exception e)
				{
					e.printStackTrace();
					make_ack.setAnswerFail();
					System.out.println("MAKEROOM Ack Fail...");
					sendString = PacketCodec.encodeMakeRoomAck(make_ack);
					try{
						out.println(sendString);
						System.out.println("MAKEROOM Ack dispatched");
					}
					catch(Exception t)
					{
						t.printStackTrace();
					}
					break;
				}
				make_ack.setAnswerOk();
				System.out.println("MAKEROOM Ack Success...");
				sendString = PacketCodec.encodeMakeRoomAck(make_ack);
				try{
					out.println(sendString);
					System.out.println("MAKEROOM Ack dispatched");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				break;

			case Packet.ENTERROOM_REQ:
				System.out.println("EnterRoom REQ recevied");
				EnterroomReq enterRoom_req = PacketCodec.decodeEnterroomReq(src.getData());
				EnterroomAck enterRoom_ack = new EnterroomAck();

				presentRoom=enterRoom_req.getRoomid();

				db.query = "select RoomName from "+Database.roomList+" where RoomId = '"+presentRoom+"'";
				rs = db.excuteStatementReturnRs();
				try{
					rs.next();
					RoomName = rs.getString("RoomName");
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println("EnterRoom select Fail...");
					enterRoom_ack.setAnswerFail();
					sendString=PacketCodec.encodeEnterroomAck(enterRoom_ack);
					try{
						out.println(sendString);
						System.out.println("EnterRoom Ack dispatched");
					}
					catch(Exception t){
						t.printStackTrace();
					}
					break;
				}
				try{
					enterRoom_ack.setAnswerOk();
					sendString=PacketCodec.encodeEnterroomAck(enterRoom_ack);
					out.println(sendString);
					System.out.println("EnterRoom Ack dispatched");
				}
				catch(Exception t){
					t.printStackTrace();
				}
				break;

			case Packet.LOBBY_REQ:
				System.out.println("LOBBy REQ recevied");
				LobbyReq lobby_req = PacketCodec.decodeLobbyReq(src.getData());
				LobbyAck lobby_ack = new LobbyAck();

				db.query = "select RoomName from "+Database.roomList;
				rs = db.excuteStatementReturnRs();
				int lobbyCount = 0;
				String lobbyRoom="";
				try{
					while(rs.next())
					{
						lobbyCount++;
						lobbyRoom+=rs.getString("RoomName");
						lobbyRoom+=Packet.SMALLDELIM;
					}
					lobby_ack.setRoomNum(lobbyCount);
					lobby_ack.setRoomName(lobbyRoom);
					db.query = "select FriendId from "+Database.friendList+" where Id = '"+user_id+"'";
					rs = db.excuteStatementReturnRs();
					int lobbyFCount = 0;
					String lobbyFriend="";

					while(rs.next())
					{
						lobbyFCount++;
						lobbyFriend+=rs.getString("FriendId");
						lobbyFriend+=Packet.SMALLDELIM;
					}
					lobby_ack.setFriendNum(lobbyFCount);
					lobby_ack.setFriendName(lobbyFriend);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("LOBBY_Ack Fail...");
					lobby_ack.setAnswerFail();
					sendString=PacketCodec.encodeLobbyAck(lobby_ack);
					try{
						out.println(sendString);
						System.out.println("Lobby Ack dispatched");
					}
					catch(Exception t){
						t.printStackTrace();
					}
					break;
				}
				try{
					lobby_ack.setAnswerOk();
					System.out.println("LOBBY_Ack success...");
					sendString=PacketCodec.encodeLobbyAck(lobby_ack);
					out.println(sendString);
					System.out.println("EnterRoom Ack dispatched");
				}
				catch(Exception t){
					t.printStackTrace();
				}
				break;
			case Packet.ROOM_REQ:
				System.out.println("Room REQ recevied");
				RoomReq room_req = PacketCodec.decodeRoomReq(src.getData());
				RoomAck room_ack = new RoomAck();
				System.out.println("Room step one");
				db.query = "select Id, SendStr, ArriveTime from "+Database.messBoard+" where RoomName = '"+room_req.getRoomname()+"'";
				rs = db.excuteStatementReturnRs();
				int roomCount = 0;
				String roomId="";
				try{
					while(rs.next())
					{
						roomCount++;
						roomId+=rs.getString("Id");
						roomId+=Packet.TINYDELIM;
						roomId+=rs.getString("SendStr");
						roomId+=Packet.TINYDELIM;
						roomId+=rs.getString("ArriveTime");
						roomId+=Packet.TINYDELIM;
						roomId+=Packet.SMALLDELIM;

					}
					room_ack.setMauNum(roomCount);
					room_ack.setMau(roomId);
					String[] allRoomId;
					allRoomId=PacketCodec.nextDecode(roomCount, roomId);
					System.out.println("Room step Two");
					db.query = "select Id from "+Database.memberData;
					rs = db.excuteStatementReturnRs();
					int memCount = 0;
					String memId="";

					while(rs.next())
					{
						memCount++;
						memId+=rs.getString("FriendId");
						memId+=Packet.SMALLDELIM;
					}
					String[] allId;
					allId=PacketCodec.nextDecode(memCount, memId);
					System.out.println("Room step Threee");
					boolean[] check = new boolean[memCount];
					for(int i = 0; i<memCount; i++)
					{
						check[i] = false;
					}
					for(int i = 0; i<roomCount;i++)
					{
						for(int j = 0; j<memCount; j++)
							if(allRoomId.equals(allId[j]))
								check[j]=true;
					}
					System.out.println("Room step Four");
					String mem="";
					int memNum = 0;
					for(int j = 0; j<memCount; j++)
						if(check[j]==true)
						{
							memNum++;
							mem+=allId[j];
							mem+=Packet.SMALLDELIM;
						}
					room_ack.setMemberNum(memNum);
					room_ack.setMember(mem);

				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("Room_Ack Fail...");
					room_ack.setAnswerFail();
					sendString=PacketCodec.encodeRoomAck(room_ack);
					try{
						out.println(sendString);
						System.out.println("Room Ack dispatched");
					}
					catch(Exception t){
						t.printStackTrace();
					}
					break;
				}
				try{
					room_ack.setAnswerOk();
					System.out.println("Room_Ack success...");
					sendString=PacketCodec.encodeRoomAck(room_ack);
					out.println(sendString);
					System.out.println("Room Ack dispatched");
				}
				catch(Exception t){
					t.printStackTrace();
				}
				break;
		}

		return isContinous;
	}
}

