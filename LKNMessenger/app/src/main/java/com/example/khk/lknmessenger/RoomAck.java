package com.example.khk.lknmessenger;

/**
 * Created by KHK on 2015-12-07.
 */

public class RoomAck {
    private int answer;
    private int mauNum ;
    private String mau;
    private int memberNum;
    private String member;

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
    public int getMauNum() {
        return mauNum;
    }
    public void setMauNum(int num) {
        mauNum = num;
    }

    public String getMau() {
        return mau;
    }
    public void setMau(String mau) {

        this.mau = mau ;
    }

    public int getMemberNum() {
        return memberNum;
    }
    public void setMemberNum(int num) {
        memberNum = num;
    }

    public String getMember() {
        return member;
    }
    public void setMember(String member) {
        this.member = member;
    }
}
