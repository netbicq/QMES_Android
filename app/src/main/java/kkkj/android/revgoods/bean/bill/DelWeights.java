package kkkj.android.revgoods.bean.bill;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean.bill
 * Author: Admin
 * Time: 2019/8/8 15:18
 * Describe: 扣重统计
 */
public class DelWeights {
    /**
     * Weight : 1.0
     * DelWeightType : a641ee4c-e57c-40ef-bd62-fae0705a284a
     */

    private double Weight;
    private String DelWeightType;//扣重类型

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double Weight) {
        this.Weight = Weight;
    }

    public String getDelWeightType() {
        return DelWeightType;
    }

    public void setDelWeightType(String DelWeightType) {
        this.DelWeightType = DelWeightType;
    }
}
