package com.example.khk.lknmessenger;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;


public class PacketCodec {

	public static String readBuffReader(BufferedReader in) throws IOException{
		char charBuf[] = new char[1];

		String src = "";

		while(in.read(charBuf, 0, 1) != -1)
		{
			if(charBuf[0] == '\n')
				break;
			src += charBuf[0];

		}
		//System.out.println("readBufferReader step two...while state END" + charBuf[0]);
		if(src.equals(""))
			return null;
		return src;
	}


	public static Packet decodeHeader(String src) throws IOException{
		String type, data;
		System.out.println("Decode : START...");
		Scanner s = new Scanner(src).useDelimiter("\\"+Packet.FIELD_DELIM);

		type = s.next();
		s.skip(Packet.FIELD_DELIM);
		s.useDelimiter("\\"+Packet.PK_DELIM);
		data = s.next();
		System.out.println("Decode : END...");
		return new Packet(type, data);
	}

	// About join request
	// Dncode join request packet data
	public static String encodeJoinReq(JoinReq pk_data){
		String data = Packet.JOIN_REQ
				+ Packet.FIELD_DELIM + pk_data.getName()
				+ Packet.FIELD_DELIM + pk_data.getId()
				+ Packet.FIELD_DELIM + pk_data.getPassword()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static JoinReq decodeJoinReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinReq dst = new JoinReq();

		dst.setName(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setId(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setPassword(s.next());

		return dst;
	}

	public static String encodeJoinAck(JoinAck pk_data){
		String data = Packet.LOG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getAnswer()) + Packet.FIELD_DELIM;

		/*if (pk_data.getAnswer() == Packet.SUCCESS){
		}*/

		data += Packet.PK_DELIM;

		return data;
	}

	public static JoinAck decodeJoinAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinAck dst = new JoinAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();
		return dst;
	}

	public static String encodeLoginReq(LoginReq pk_data){
		String data = Packet.LOG_REQ
				+ Packet.FIELD_DELIM + pk_data.getId()
				+ Packet.FIELD_DELIM + pk_data.getPassword()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}

	public static LoginReq decodeLoginReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginReq dst = new LoginReq();

		dst.setId(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setPassword(s.next());

		return dst;
	}

	public static String encodeLoginAck(LoginAck pk_data){
		String data = Packet.LOG_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM;

		/*if (pk_data.getAnswer() == Packet.SUCCESS){
		}*/

		data += Packet.PK_DELIM;

		return data;
	}

	public static LoginAck decodeLoginAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginAck dst = new LoginAck();

		if(s.nextInt()==Packet.SUCCESS)
			dst.setAnswerOk();
		else
			dst.setAnswerFail();
		return dst;
	}

	public static String encodeMssReq(MssReq pk_data){
		String data = Packet.MSS_REQ
				+ Packet.FIELD_DELIM + pk_data.getMessage()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}

	public static MssReq decodeMssReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		MssReq dst = new MssReq();

		dst.setMessage(s.next());

		return dst;
	}

	public static String encodeMssAck(MssAck pk_data){
		String data = Packet.MSS_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM + pk_data.getArrtime()
				+ Packet.FIELD_DELIM + pk_data.getListnum()
				+ Packet.FIELD_DELIM + pk_data.getlist()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static MssAck decodeMssAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		MssAck dst = new MssAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		s.skip(Packet.FIELD_DELIM);
		dst.setArrtime(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setListNum(s.nextInt());
		if(dst.getListnum()<1)
		{
			dst.setlist(null);
			return dst;
		}
		s.skip(Packet.FIELD_DELIM);
		dst.setlist(s.next());

		return dst;
	}

	public static String encodeInviRoomReq(InviRoomReq pk_data){
		String data = Packet.INVIROOM_REQ
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static InviRoomReq decodeInviRoomReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		InviRoomReq dst = new InviRoomReq();

		return dst;
	}

	public static String encodeInviRoomAck(InviRoomAck pk_data ){
		String data = Packet.INVIROOM_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static InviRoomAck decodeInviRoomAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		InviRoomAck dst = new InviRoomAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		return dst;
	}

	public static String encodeMakeRoomReq(MakeRoomReq pk_data){
		String data = Packet.MAKEROOM_REQ
				+ Packet.FIELD_DELIM + pk_data.getRoomName()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static MakeRoomReq decodeMakeRoomReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		MakeRoomReq dst = new MakeRoomReq();
		dst.setRoomName(s.next());
		return dst;
	}

	public static String encodeMakeRoomAck(MakeRoomAck pk_data ){
		String data = Packet.MAKEROOM_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static MakeRoomAck decodeMakeRoomAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		MakeRoomAck dst = new MakeRoomAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		return dst;
	}
	public static String encodeAddFriendReq(AddFriendReq pk_data){
		String data = Packet.ADDFRIEND_REQ
				+ Packet.FIELD_DELIM + pk_data.getFriendName()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	public static AddFriendReq decodeAddFriendReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		AddFriendReq dst = new AddFriendReq();
		dst.setFriendName(s.next());
		return dst;
	}

	public static String encodeAddFrinedAck(AddFriendAck pk_data ){
		String data = Packet.ADDFRIEND_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static AddFriendAck decodeAddFriendAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		AddFriendAck dst = new AddFriendAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		return dst;
	}

	public static String encodeLobbyReq(LobbyReq pk_data){
		String data = Packet.LOBBY_REQ
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static LobbyReq decodeLobbyReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LobbyReq dst = new LobbyReq();

		return dst;
	}

	public static String encodeLobbyAck(LobbyAck pk_data ){
		String data = Packet.LOBBY_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getRoomNum())
				+ Packet.FIELD_DELIM + pk_data.getRoomName()
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getFriendNum())
				+ Packet.FIELD_DELIM + pk_data.getFriendName()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static LobbyAck decodeLobbyAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LobbyAck dst = new LobbyAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();
		s.skip(Packet.FIELD_DELIM);
		dst.setRoomNum(s.nextInt());
		s.skip(Packet.FIELD_DELIM);
		dst.setRoomName(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setFriendNum(s.nextInt());
		s.skip(Packet.FIELD_DELIM);
		dst.setFriendName(s.next());

		return dst;
	}
	public static String encodeRoomReq(RoomReq pk_data){
		String data = Packet.ROOM_REQ
				+ Packet.FIELD_DELIM + pk_data.getRoomname()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static RoomReq decodeRoomReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		RoomReq dst = new RoomReq();

		dst.setRoomname(s.next());

		return dst;
	}

	public static String encodeRoomAck(RoomAck pk_data ){
		String data = Packet.ROOM_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getMauNum())
				+ Packet.FIELD_DELIM + pk_data.getMau()
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getMemberNum())
				+ Packet.FIELD_DELIM + pk_data.getMember()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static RoomAck decodeRoomAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		RoomAck dst = new RoomAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();
		s.skip(Packet.FIELD_DELIM);
		dst.setMauNum(s.nextInt());
		s.skip(Packet.FIELD_DELIM);
		dst.setMau(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setMemberNum(s.nextInt());
		s.skip(Packet.FIELD_DELIM);
		dst.setMember(s.next());

		return dst;
	}

	public static String encodeGiveMemReq(GiveMemReq pk_data){
		String data = Packet.GIVEMEM_REQ
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static GiveMemReq decodeGiveMemReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		GiveMemReq dst = new GiveMemReq();

		return dst;
	}
	public static String encodeGiveMemAck(GiveMemAck pk_data ){
		String data = Packet.GIVEMEM_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getmemNum())
				+ Packet.FIELD_DELIM + pk_data.getmemberName()
				+ Packet.FIELD_DELIM + pk_data.getmemberId()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}

	public static String preEncode(int num, String[] mem)
	{
		String send = "";
		send += Integer.toString(num);
		for(int i = 0; i<num; i++)
		{	send+=Packet.SMALLDELIM;
			send+=mem[i];
		}
		return send;
	}

	public static GiveMemAck decodeGiveMemAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		GiveMemAck dst = new GiveMemAck();

		dst.setmemberNum(s.nextInt());
		s.skip(Packet.FIELD_DELIM);
		dst.setmemberName(s.next());
		s.skip(Packet.FIELD_DELIM);
		dst.setmemberId(s.next());

		return dst;
	}

	public static String[] nextDecode(int num, String mem)
	{
		String[] member = new String[num];
		Scanner s = new Scanner(mem).useDelimiter(Packet.SMALLDELIM);

		for(int i =0 ;i<num;i++){
			member[i] = s.next();
			s.skip(Packet.SMALLDELIM);
		}
		return member;
	}

	public static String encodeEnterroomReq(EnterroomReq pk_data){
		String data = Packet.ENTERROOM_REQ
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getRoomid())
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static EnterroomReq decodeEnterroomReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		EnterroomReq dst = new EnterroomReq();

		dst.setRoomid(s.nextInt());

		return dst;
	}

	public static String encodeEnterroomAck(EnterroomAck pk_data ){
		String data = Packet.ENTERROOM_ACK
				+ Packet.FIELD_DELIM + Integer.toString(pk_data.getAnswer())
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}
	public static EnterroomAck decodeEnterroomAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		EnterroomAck dst = new EnterroomAck();

		if(Packet.SUCCESS==s.nextInt())
			dst.setAnswerOk();
		else
			dst.setAnswerFail();

		return dst;
	}

	public static MssAndArrtimeAndUser[] nextTinydecode(int num,String[] arg)
	{
		MssAndArrtimeAndUser[] mau = new MssAndArrtimeAndUser[num];

		for(int i =0 ;i<num;i++){
			Scanner s = new Scanner(arg[i]).useDelimiter(Packet.TINYDELIM);
			for(int j =0 ;j<3;j++){
				mau[j].setName(s.next());
				s.skip(Packet.TINYDELIM);
				mau[j].setMss(s.next());
				s.skip(Packet.TINYDELIM);
				mau[j].setArrtime(s.next());
				s.skip(Packet.TINYDELIM);
			}
		}

		return mau;

	}
}
