package kkkj.android.revgoods.http;

/**
 * 响应报文公共报文头
 */
public class RevGResponse {
    //状态码 200 = 成功
    private int state;
    //错误码
    private String msg;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
