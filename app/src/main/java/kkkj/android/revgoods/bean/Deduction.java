package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 扣重
 */
public class Deduction extends LitePalSupport {
    private int id;

    private int hasBill = -1;
    /**
     * 扣重类别
     */
    private String category;

    /**
     * 重量
     */
    private double weight;
    /**
     * 扣重类别ID
     */
    private String KeyID;

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }

    public int getHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
