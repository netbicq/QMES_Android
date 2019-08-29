package kkkj.android.revgoods.bean.bill;

import org.litepal.crud.LitePalSupport;

import kkkj.android.revgoods.bean.Bill;

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
     * InitialPrice: 3.0,
     * RevisionPrice: 4.0,
     * Price : 3.0
     * Menoy : 4.0
     * Ratio : 5.0
     */

    private Bill bill;

    private String NormsID; //规格Id
    private double Amount; //重量
    private double InitialPrice;//初始单价
    private double RevisionPrice;//调整单价
    private double Price; //最终单价
    private double Menoy; //金额
    private double Ratio; //占比



    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

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

    public double getInitialPrice() {
        return InitialPrice;
    }

    public void setInitialPrice(double InitialPrice) {
        this.InitialPrice = InitialPrice;
    }

    public double getRevisionPrice() {
        return RevisionPrice;
    }

    public void setRevisionPrice(double RevisionPrice) {
        this.RevisionPrice = RevisionPrice;
    }
}
