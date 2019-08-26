package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/26 16:46
 * Describe: 当选择的品类决定通过规格而不是占比计价的时候，需要将多次采样求平均值。
 *            这个最终的结果保存在这个实体类
 */
public class SamplingBySpecs extends LitePalSupport {

    private int id;

    /**
     * 是否已保存为单据 默认-1；未保存
     */
    private int hasBill = -1;

    /**
     * 采样的总数量
     */
    private int count;

    /**
     * 采样的总重量
     */
    private double weiht;

    /**
     * 采样的规格ID
     */
    private int specsId;

    /**
     * 单价
     */
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWeiht() {
        return weiht;
    }

    public void setWeiht(double weiht) {
        this.weiht = weiht;
    }

    public int getSpecsId() {
        return specsId;
    }

    public void setSpecsId(int specsId) {
        this.specsId = specsId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
