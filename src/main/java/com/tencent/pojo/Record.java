package com.tencent.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;


@TableName("record")
public class Record implements Serializable {
    private int userid;
    private int newsid;
    private String time;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
