package com.example.khk.lknmessenger;

/**
 * Created by KHK on 2015-12-07.
 */
public class RoomAck {
    private int mauNum ;
    private String mau;
    private int memberNum;
    private String member;

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
