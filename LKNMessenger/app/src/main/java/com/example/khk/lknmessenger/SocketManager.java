package com.example.khk.lknmessenger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by KHK on 2015-12-05.
 */
public class SocketManager extends Activity {

    public final static String HOST = "115.23.216.223";
    public final static int PORT = 9193;
    //public static Thread worker;

    public static Socket socket;
    public static PrintWriter socket_out;
    public static BufferedReader socket_in;
    //  variables about socket

    public static String result;
    //  String variable received from the server

    public static boolean isConnected = false;
    public static boolean isGetEnd = false;
    //  boolean variables for determining whether
    //  the socket is connected or not and the
    //  received string is full or not

    public static LoginActivity loginActivity;
    public static ProgressDialog dialog;

    public static void getSocket() throws IOException {
        //  method for initializing socket and
        //  connecting to the server

        Thread worker = new Thread() {
            /*  Since the Android 4.4(Kitkat), the android system
             *  doesn't allow thread tasks which requires lots of time,
             *  including socket thread, in main thread. So we have to
             *  create other threads for executing these thread tasks.  */
            public void run() {
                //super.run();
                if (socket == null )
                    socket = new Socket();
                if( !socket.isConnected() )
                    try {
                        socket.connect( new InetSocketAddress(HOST, PORT) );
                        socket_in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                        socket_out = new PrintWriter( socket.getOutputStream(), true );
                        isConnected = true;
                        //throw new IOException();
                        //  allocating variables about socket
                   } catch ( IOException e ) {

                        //e.printStackTrace();
                    }
            }
        };
        worker.start();
    }

    public static void closeSocket() throws IOException {
        //  method for closing socket
        if( socket != null )
            socket.close();
    }

    public static void sendMsg( String message ) throws IOException {
        //  method for sending a packet to the server
        socket_out.println( message );
    }

    public static String receiveMsg() throws IOException {
        //  method for receiving a packet from the server
        Thread receiver = new Thread() {
            /*  Unlike sending to the server, receiving a data from the
             *  server needs thread tasking. So we have to create another
             *  thread for receiving from the server.   */
            public void run() {
                try {
                    result = PacketCodec.readDelimiter(socket_in);
                    isGetEnd = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        isGetEnd = false;
        receiver.start();
        while( !isGetEnd );
        return result;
    }

    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            dialog.dismiss();
        }
    };
}
