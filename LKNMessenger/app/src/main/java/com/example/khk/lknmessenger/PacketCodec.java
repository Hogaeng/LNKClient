package com.example.khk.lknmessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class PacketCodec {

	public static String readDelimiter(BufferedReader in) throws IOException{
		char charBuf[] = new char[1];
		String readMsg = "";
		short isdelim = 0;
		int size = 1, totalSize = 0;
		boolean isFirstDelimAppear = false;
		String strSize = "";

		// read character before packet delimiter
		while(in.read(charBuf, 0, 1) != -1){
			if(!isFirstDelimAppear){
				// read size of packet
				if(Packet.FIELD_DELIM.charAt(0) != charBuf[0]){
					strSize += charBuf[0];
				}
				else{
					totalSize = Integer.parseInt(strSize);
					if( totalSize >= 1){
						size = 1;
					}else{
						size = totalSize;
					}
					charBuf = new char[size];
					isFirstDelimAppear = true;
				}
			}

			// Packet.PK_DELIM == '?'
			else if(charBuf[0] == '?'){
				readMsg += charBuf[0];
				isdelim = 1;
				break;
			} else {
				readMsg += charBuf[0];
				totalSize -= size;
				continue;
			}
		}

		// remove '\n'
		while(in.read(charBuf, 0, 1) != -1)
		{
			if(charBuf[0] == '\n'){
				break;
			}
		}

		// if there isn't delimiter
		if(isdelim == 0 && charBuf[0]  != '\0'){
			System.out.println("MSG DELIM IS NOT FOUND!!");
		}
		return readMsg;
	}


	public static Packet decodeHeader(BufferedReader in) throws IOException{
		String type, data;
		char charBuf[] = new char[1];
		String src = "";
		System.out.println("Decode : step one");
		while(in.read(charBuf, 0, 1) != -1)
		{
			src += charBuf[0];
			System.out.println(src);
			System.out.println("Decode : step two");
		}
		System.out.println("Decode : step three");
		if(src.equals(""))
			return null;
		System.out.println("Decode : step four...");
		Scanner s = new Scanner(src).useDelimiter("\\"+Packet.FIELD_DELIM);

		type = s.next();
		s.skip(Packet.FIELD_DELIM);
		s.useDelimiter("\\"+Packet.PK_DELIM);
		data = s.next();
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
		dst.setPassword(s.next());

		return dst;
	}

	public static String encodeLoginAck(LoginAck pk_data){
		String data = Packet.LOG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getAnswer()) + Packet.FIELD_DELIM;

		/*if (pk_data.getAnswer() == Packet.SUCCESS){
		}*/

		data += Packet.PK_DELIM;

		return data;
	}

	public static LoginAck decodeLoginAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginAck dst = new LoginAck();

		dst.setAnswer(s.nextInt());

		return dst;
	}

	public static String encodeMssReq(MssReq pk_data){
		String data = Packet.MSS_REQ
				+ Packet.FIELD_DELIM + pk_data.getMessage()
				+ Packet.FIELD_DELIM
				+ Packet.PK_DELIM;

		return data;
	}



	// Decode join response packet data
	/*public static JoinAck decodeJoinAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinAck dst = new JoinAck();
		
		dst.setResult(s.nextInt());
		
		return dst;
	}*/

	// About login request
	// Encode login request





	// Decode login response packet data
	/*public static LoginAck decodeLoginAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginAck dst = new LoginAck();
		
		dst.setResult(s.nextInt());
		if(dst.getResult() == Packet.SUCCESS)
		{
			dst.setName(s.next());
			dst.setJob(s.next());
			dst.setGender(s.next());
			dst.setCountry(s.next());
			dst.setProfile_img(s.next());
		}
		return dst;
	}*/


}
