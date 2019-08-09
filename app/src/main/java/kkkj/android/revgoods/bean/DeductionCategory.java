package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 扣重类别
 */
public class DeductionCategory extends LitePalSupport {

    private int id;
    /**
     * KeyID : 17570c09-1727-4bbd-a100-29da26eb571c
     * Name : sample string 2
     */

    private String KeyID;
    private String Name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String KeyID) {
        this.KeyID = KeyID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public String toString() {
        return "DeductionCategory{" +
                "id=" + id +
                ", KeyID='" + KeyID + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
