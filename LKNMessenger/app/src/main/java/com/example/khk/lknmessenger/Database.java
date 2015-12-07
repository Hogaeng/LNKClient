package com.example.khk.lknmessenger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by KHK on 2015-12-07.
 */
public class Database {
    public static final String memberData = "Member_Data";
    public static final String messBoard = "Mess_Board";
    public static final String roomList = "Room_Id";
    public static final String friendList = "Friend_List";

    Connection con;
    Statement st;
    ResultSet rs;
    PreparedStatement pstmt;

    public Database(){
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        pstmt = null;
    }

    public boolean connect(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/androidDB", "androidDB", "androidDB");
            st = con.createStatement();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            return false;
        }
        return true;
    }

    public Statement getStatement(){
        return st;
    }

    public Connection getConnection(){
        return con;
    }

    public void setPreparedStatement(String query){
        try{
            pstmt = con.prepareStatement(query);
            //psmt.setString(1,test);
            //psmt.clearParameters(); that can recycle psmt;
        }catch(SQLException e){
            printError(e, query);
        }
    }
    public void excuteStatement(String query){
        try{
            getStatement().executeQuery(query);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet excuteStatementReturnRs(String query){
        try{
            rs= getStatement().executeQuery(query);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            return rs;
        }
    }
    public PreparedStatement getPreparedStatement(){
        return pstmt;
    }

    public void printError(SQLException e, String query){
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("Query: " + query);
    }

    public void clear(){
        String query = "";
        try{
            query = "delete from login_data";

            st.executeQuery(query);

            query = "alter table login_data auto_increment = 1";

            st.executeQuery(query);
        }catch(SQLException e){
            printError(e, query);
        }
    }
}
