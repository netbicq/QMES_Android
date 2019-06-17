package kkkj.android.revgoods.relay.bean;


import android.text.TextUtils;

/**
 * 继电器实体类
 */
public class RelayBean {
    private String name;
    private String state;
    public String getName() {
        if(TextUtils.isEmpty(name))
        {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        if(TextUtils.isEmpty(state))
        {
            return "0";
        }
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
