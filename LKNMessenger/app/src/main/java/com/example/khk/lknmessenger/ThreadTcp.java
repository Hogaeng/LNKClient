package com.example.khk.lknmessenger;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadTcp implements Runnable {


	//private Base64Codec bs64 = new Base64Codec();//
	private Socket clientSocket = null;//

	private PrintWriter out = null;
	private BufferedReader in = null;

	private boolean isContinous = false;
	private int user_id = 0;

	String inputData;
	Packet rcvPacket;

	public ThreadTcp(Socket clientSocket, boolean isContinous) throws IOException {
		this.clientSocket = clientSocket;
		this.isContinous = isContinous;

		System.out.println("Client Connect");
	}

	public ThreadTcp(ServerSocket serverSocket, boolean isContinous) throws IOException {
		clientSocket = serverSocket.accept();
		this.isContinous = isContinous;

		System.out.println("Client Connect");
	}


	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			while (isContinous) {
				rcvPacket = PacketCodec.decodeHeader(in);//
				if (rcvPacket == null)
					continue;
				isContinous = clienthandler(rcvPacket, out);//
			}//
			in.close();
			out.close();
			clientSocket.close();//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean clienthandler(Packet src, PrintWriter out) throws IOException
	{
		boolean isContinuous = true;

		switch(src.getType())
		{
			case Packet.LOG_ACK:
				LoginAck l_ack = PacketCodec.decodeLoginAck(src.getData());
				if(l_ack.getAnswer() == Packet.SUCCESS)
					Log.d("Success","Login Success!");
				else
					Log.d("Fail", "Login Failed!");
				break;

			case Packet.JOIN_ACK:
				break;

			case Packet.MSS_ACK:
				break;

			default:
				System.out.println("Not Defined Packet Type!!!!");

		}
		return isContinuous;

	}
}
