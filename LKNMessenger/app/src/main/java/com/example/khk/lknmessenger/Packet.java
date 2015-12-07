package com.example.khk.lknmessenger;

public class Packet {
	public static final String JOIN_REQ = "JOIN_REQ";
	public static final String JOIN_ACK = "JOIN_ACK";

	public static final String LOG_REQ = "LOG_REQ";
	public static final String LOG_ACK = "LOG_ACK";

	public static final String MSS_REQ = "MSS_REQ";
	public static final String MSS_ACK = "MSS_ACK";

	public static final String INVIROOM_REQ = "INVIROOM_REQ";
	public static final String INVIROOM_ACK = "INVIROOM_ACK";

	public static final String MAKEROOM_REQ = "MAKEROOM_REQ";
	public static final String MAKEROOM_ACK = "MAKEROOM_ACK";

	public static final String ADDFRIEND_REQ = "ADDFRIEND_REQ";
	public static final String ADDFRIEND_ACK = "ADDFRIEND_ACK";

	public static final String LOBBY_REQ = "LOBBY_REQ";
	public static final String LOBBY_ACK = "LOBBY_ACK";

	public static final String ROOM_REQ = "ROOM_REQ";
	public static final String ROOM_ACK = "ROOM_ACK";

	public static final String GIVEMEM_REQ = "GIVEMEM_REQ";
	public static final String GIVEMEM_ACK = "GIVEMEM_ACK";

	public static final String FIELD_DELIM = "|";
	public static final String PK_DELIM = "?";
	public static final String SMALLDELIM = ":";

	public static final int SUCCESS = 0x01;
	public static final int FAIL = 0x00;

	private String type;
	private String data;

	public Packet(String type, String data){
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
