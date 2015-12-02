package com.example.khk.lknmessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadTcp implements Runnable{


	
	//private Base64Codec bs64 = new Base64Codec();//�̹��� ����
	private Socket clientSocket = null;//Ŭ���̾�Ʈ ���������� ���� ���ϵ� ����
	
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private boolean isContinous = false;
	private int user_id = 0;
	
	String inputData;
	//Packet rcvPacket;

	public ThreadTcp(Socket clientSocket, boolean isContinous) throws IOException{
		this.clientSocket = clientSocket;
		this.isContinous = isContinous;
		
		inputData = "";

		System.out.println("Client Connect");
	}

	public ThreadTcp(ServerSocket serverSocket, boolean isContinous) throws IOException{
		clientSocket = serverSocket.accept();
		this.isContinous = isContinous;
		
		inputData = "";

		System.out.println("Client Connect");
	}

	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//Ŭ���̾�Ʈ�� ������ ��ǲ ��Ʈ���� ������ �о�鿩 ���ο� ���� ������ �ִ´�.
			out = new PrintWriter(clientSocket.getOutputStream(), true);//�����̰�.
			
			while(isContinous){
				// read packet and decode
				//inputData = PacketCodec.readDelimiter(in);//Ŭ���̾�Ʈ�� �Է°�(BufferedReader)�� ��Ŷ�ڵ��� �ְ� �м��Ͽ� ���� String�� ��ǲ�� �ִ´�.
				//rcvPacket = PacketCodec.decodeHeader(inputData);//�׷��� ���� ��Ŷ������(String)�� �ѹ� �� �ڵ��� �־� ����� ���ڵ� �Ѵ�. ���°��� Packet.
				//isContinous = handler(rcvPacket, out);//���������� handler�� Packet�� PrintWriter�� �־� ������ ������ ���θ� Ȯ���Ѵ�. 
			}//������ true�� �������� false�� ������������ ��� �����Ѵ�.

			in.close();
			out.close();
			clientSocket.close();//TCP�� ������.
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
