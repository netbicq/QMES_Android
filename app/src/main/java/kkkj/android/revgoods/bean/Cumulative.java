package kkkj.android.revgoods.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * 累计
 */
public class Cumulative extends LitePalSupport {

    private int id;
    /**
     * 是否已保存为单据 默认-1；未保存
     */
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
     * 记录时间
     */
    private String time;

    /**
     * 所属单据
     */
    private Bill bill;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int isHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
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
