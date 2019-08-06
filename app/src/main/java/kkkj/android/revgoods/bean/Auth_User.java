package kkkj.android.revgoods.bean;

import android.text.TextUtils;

public class Auth_User {
    private String Login;
    private String Pwd;
    private String Token;
    private boolean OtherView;
    private boolean OtherEdit;
    private String TokenValidTime;
    private String CreateDate;
    private String CreateMan;
    private int State;
    private String ID;

    public String getLogin() {
        if(TextUtils.isEmpty(Login))
        {
            return "";
        }
        return Login;
    }

    public String getPwd() {
        if(TextUtils.isEmpty(Pwd))
        {
            return "";
        }
        return Pwd;
    }

    public String getToken() {
        if(TextUtils.isEmpty(Token))
        {
            return "";
        }
        return Token;
    }

    public boolean isOtherView() {
        return OtherView;
    }

    public boolean isOtherEdit() {
        return OtherEdit;
    }

    public String getTokenValidTime() {
        if(TextUtils.isEmpty(TokenValidTime))
        {
            return "";
        }
        return TokenValidTime;
    }

    public String getCreateDate() {
        if(TextUtils.isEmpty(CreateDate))
        {
            return "";
        }
        return CreateDate;
    }

    public String getCreateMan() {
        if(TextUtils.isEmpty(CreateMan))
        {
            return "";
        }
        return CreateMan;
    }

    public int getState() {
        return State;
    }

    public String getID() {
        if(TextUtils.isEmpty(ID))
        {
            return "";
        }
        return ID;
    }
}
