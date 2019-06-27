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
     * 扣重单价
     */
    private String price;
    /**
     * 重量
     */
    private String weight;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
