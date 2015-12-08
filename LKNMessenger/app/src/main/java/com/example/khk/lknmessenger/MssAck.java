package com.example.khk.lknmessenger;

/**
 * Created by KHK on 2015-12-07.
 */


public class MssAck {
    private int answer;
    private String arrTime;
    private int listNum;
    private String list;
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

    public String getArrtime() {
        return arrTime;
    }

    public void setArrtime(String arg) {
        this.arrTime = arg;
    }
    public int getListnum() {
        return listNum;
    }

    public void setListNum(int arg) {
        this.listNum = arg;
    }
    public String getlist() {
        return list;
    }

    public void setlist(String arg) {
        this.list = arg;
    }
}
