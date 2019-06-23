package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 扣重
 */
public class Deduction extends LitePalSupport {
    /**
     * 扣重类别
     */
    private DeductionCategory category;

    /**
     * 重量
     */
    private String weight;

    public DeductionCategory getCategory() {
        return category;
    }

    public void setCategory(DeductionCategory category) {
        this.category = category;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
