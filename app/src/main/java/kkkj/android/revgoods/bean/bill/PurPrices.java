package kkkj.android.revgoods.bean.bill;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean.bill
 * Author: Admin
 * Time: 2019/8/8 15:20
 * Describe: 计价明细
 */
public class PurPrices {
    /**
     * NormsID : 4703a9fb-01f1-49c6-8989-9f10fa76b408
     * Amount : 2.0
     * Price : 3.0
     * Menoy : 4.0
     * Ratio : 5.0
     */

    private String NormsID; //规格Id
    private double Amount; //重量
    private double Price; //单价
    private double Menoy; //金额
    private double Ratio; //占比

    public String getNormsID() {
        return NormsID;
    }

    public void setNormsID(String NormsID) {
        this.NormsID = NormsID;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public double getMenoy() {
        return Menoy;
    }

    public void setMenoy(double Menoy) {
        this.Menoy = Menoy;
    }

    public double getRatio() {
        return Ratio;
    }

    public void setRatio(double Ratio) {
        this.Ratio = Ratio;
    }
}
