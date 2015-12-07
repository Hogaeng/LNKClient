package com.example.khk.lknmessenger;

/**
 * Created by KHK on 2015-12-07.
 */
public class MssAck {
    private int answer;
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
}

