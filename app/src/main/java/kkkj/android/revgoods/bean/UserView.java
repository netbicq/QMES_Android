package kkkj.android.revgoods.bean;

import android.text.TextUtils;

public class UserView {
    private Auth_User UserInfo;
    private Auth_UserProfile UserProfile;
    private String AccountID;
    private String StateStr;
    private String AccountCode;
    private String AccountName;
    private String ShortName;
    private String Principal;
    private String Tel;

    public Auth_User getUserInfo() {
        if(UserInfo==null)
        {
            return new Auth_User();
        }
        return UserInfo;
    }

    public Auth_UserProfile getUserProfile() {
        if(UserProfile==null)
        {
            return new Auth_UserProfile();
        }
        return UserProfile;
    }

    public String getAccountID() {
        if(TextUtils.isEmpty(AccountID))
        {
            return "";
        }
        return AccountID;
    }

    public String getStateStr() {
        if(TextUtils.isEmpty(StateStr))
        {
            return "";
        }
        return StateStr;
    }

    public String getAccountCode() {
        if(TextUtils.isEmpty(AccountCode))
        {
            return "";
        }
        return AccountCode;
    }

    public String getAccountName() {
        if(TextUtils.isEmpty(AccountName))
        {
            return "";
        }
        return AccountName;
    }

    public String getShortName() {
        if(TextUtils.isEmpty(ShortName))
        {
            return "";
        }
        return ShortName;
    }

    public String getPrincipal() {
        if(TextUtils.isEmpty(Principal))
        {
            return "";
        }
        return Principal;
    }

    public String getTel() {
        if(TextUtils.isEmpty(Tel))
        {
            return "";
        }
        return Tel;
    }
}
