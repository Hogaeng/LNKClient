package com.example.khk.lknmessenger;

/**
 * Created by KHK on 2015-12-07.
 */
public class LobbyAck {
    private int answer;
    private int RoomNum;
    private String RoomName;
    private int FriendNum;
    private String FriendName;

    public int getAnswer() {
        return answer;
    }

    public void setAnswerOk() {
        this.answer = Packet.SUCCESS;
    }
    public void setAnswerFail()
    {
        this.answer = Packet.FAIL;
    }
    public int getRoomNum() {
        return RoomNum;
    }
    public void setRoomNum(int num) {
        RoomNum = num;
    }

    public String getRoomName() {
        return RoomName;
    }
    public void setRoomName(String name) {
        RoomName = name;
    }

    public int getFriendNum() {
        return FriendNum;
    }
    public void setFriendNum(int num) {
        FriendNum = num;
    }

    public String getFriendName() {
        return FriendName;
    }
    public void setFriendName(String name) {
        FriendName = name;
    }

}
