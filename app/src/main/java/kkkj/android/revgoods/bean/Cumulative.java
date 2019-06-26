package kkkj.android.revgoods.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * 累计
 */
public class Cumulative extends LitePalSupport {

    private int id;

    private int hasBill = -1;

    private int count;
    /**
     * 类别
     */
    private String category;

    /**
     * 重量
     */
    private String weight;

    /**
     *单价
     */
    private String price = "0";
    /**
     * 所属单据
     */
    private Bill bill;

    public int isHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
