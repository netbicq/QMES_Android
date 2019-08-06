package kkkj.android.revgoods.bean;

import android.text.TextUtils;

public class Auth_UserProfile {
    private String Login;
    private String CNName;
    private String Tel;
    private String HeadIMG;
    private String ID;

    public String getLogin() {
        if(TextUtils.isEmpty(Login))
        {
            return "";
        }
        return Login;
    }

    public String getCNName() {
        if(TextUtils.isEmpty(CNName))
        {
            return "";
        }
        return CNName;
    }

    public String getTel() {
        if(TextUtils.isEmpty(Tel))
        {
            return "";
        }
        return Tel;
    }

    public String getHeadIMG() {
        if(TextUtils.isEmpty(HeadIMG))
        {
            return "";
        }
        return HeadIMG;
    }

    public String getID() {
        if(TextUtils.isEmpty(ID))
        {
            return "";
        }
        return ID;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public void setCNName(String CNName) {
        this.CNName = CNName;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public void setHeadIMG(String headIMG) {
        HeadIMG = headIMG;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
