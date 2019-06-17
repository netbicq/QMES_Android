package kkkj.android.revgoods.conn.socket;

import android.text.TextUtils;
//自定义消息
public class Message {
    private String action;
    private String data;

    public String getAction() {
        if(TextUtils.isEmpty(action))
        {
            return "";
        }
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        if(TextUtils.isEmpty(data))
        {
            return "";
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
