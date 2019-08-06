package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;


/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/6 14:18
 * Describe: describe
 */
public class BillDetails extends LitePalSupport {

    private int id;

    /**
     * 规格
     */
    private String specs;

    /**
     * 单价
     */
    private String price;

    /**
     * 占比
     */
    private String proportion;

    /**
     * 重量
     */
    private double weight;

    /**
     * 价格
     */
    private double totalPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
