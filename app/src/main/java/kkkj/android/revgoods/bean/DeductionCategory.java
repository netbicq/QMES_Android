package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 扣重类别
 */
public class DeductionCategory extends LitePalSupport {
    /**
     * 类别
     */
    private String category;

    /**
     * 单价
     */
    private String price;

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
}
