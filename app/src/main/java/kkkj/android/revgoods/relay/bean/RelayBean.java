package kkkj.android.revgoods.relay.bean;


import android.text.TextUtils;

/**
 * 继电器实体类
 */
public class RelayBean {

    private int leftImageView;
    private int rightImageView;
    private String name;
    private String state;

    public int getLeftIamgeView() {
        return leftImageView;
    }

    public void setLeftIamgeView(int leftIamgeView) {
        this.leftImageView = leftIamgeView;
    }

    public int getRightImageView() {
        return rightImageView;
    }

    public void setRightImageView(int rightImageView) {
        this.rightImageView = rightImageView;
    }

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
