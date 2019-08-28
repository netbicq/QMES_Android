package kkkj.android.revgoods.utils;

import java.math.BigDecimal;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.utils
 * Author: Admin
 * Time: 2019/8/12 10:38
 * Describe: 双精度值计算
 *
 * /**浮点数
 *          * 相加：b1.add(b2).doubleValue();
 *          * 相减：b1.subtract(b2).doubleValue();
 *          * 相乘：b1.multiply(b2).doubleValue();
 *          * 相除：b1.divide(b2).doubleValue();
 *
 *
 */
public class DoubleCountUtils {

    private BigDecimal b1;
    private BigDecimal b2;

    public DoubleCountUtils(double b1,double b2) {
        this.b1 = new BigDecimal(b1);
        this.b2 = new BigDecimal(b2);

    }

    /**
     * 相加
     * @return
     */
    public double add() {
        return keep(b1.add(b2).doubleValue());
    }

    /**
     * 相减
     */
    public double subtract() {
        return keep(b1.subtract(b2).doubleValue());
    }

    /**
     * 相乘
     */
    public double multiply() {
        return keep(b1.multiply(b2).doubleValue());
    }

    /**
     * 相除
     */
    public double divide() {
        return keep(b1.divide(b2).doubleValue());
    }


    /**
     * 四舍五入，保留两位小数
     * double   f   =   111231.5585;
     * BigDecimal   b   =   new   BigDecimal(f);
     * double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
     */
    public static double keep(double d) {

        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 保留4位小数
     * @param d
     * @return
     */
    public static double keep4(double d) {

        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();

    }


}
