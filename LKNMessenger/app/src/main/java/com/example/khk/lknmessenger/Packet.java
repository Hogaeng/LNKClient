package com.example.khk.lknmessenger;

public class Packet {
	public static final String JOIN_REQ = "JOIN_REQ";
	public static final String JOIN_ACK = "JOIN_ACK";
	
	public static final String LOG_REQ = "LOG_REQ";
	public static final String LOG_ACK = "LOG_ACK";
	
	public static final String MSS_REQ = "MSS_REQ";
	public static final String MSS_ACK = "MSS_ACK";
	
	public static final String FIELD_DELIM = "|";
	public static final String PK_DELIM = "?";
	
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
