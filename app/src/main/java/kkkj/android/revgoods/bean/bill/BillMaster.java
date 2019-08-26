package kkkj.android.revgoods.bean.bill;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean.bill
 * Author: Admin
 * Time: 2019/8/8 15:15
 * Describe: describe
 */
public class BillMaster {
    /**
     *     "Code": "sample string 1",
     *     "PurchaseDate": "2019-08-09 16:23:12",
     *     "SupplierID": "82ff0743-41ce-443a-81d9-a4f89127ab80",
     *     "NormID": "731ba94f-ca87-4242-a3dd-5b085e94c980",
     *     "CategoryID": "d371f9b1-8b83-4da2-84c7-949c30ce705f",
     *     "CategoryLv": "cf9832b1-3147-4d9a-8427-9311bda34893",
     *     "Price": 6.0,
     *     "Amount": 7.0,
     *     "Money": 8.0,
     *     "Memo": "sample string 9",
     *     "DelWeightRate": 10.0
     */
    private String Code;//UUID
    private String PurchaseDate;//日期
    private String SupplierID;
    private String NormID; //规格Id
    private String CategoryID;//品类Id
    private String CategoryLv;
    private double Price;
    private double Amount;//重量
    private double Money;//金额
    private String Memo = "null";//备注
    private double DelWeightRate;//扣重率

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public double getDelWeightRate() {
        return DelWeightRate;
    }

    public void setDelWeightRate(double delWeightRate) {
        DelWeightRate = delWeightRate;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String PurchaseDate) {
        this.PurchaseDate = PurchaseDate;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String SupplierID) {
        this.SupplierID = SupplierID;
    }

    public String getNormID() {
        return NormID;
    }

    public void setNormID(String NormID) {
        this.NormID = NormID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getCategoryLv() {
        return CategoryLv;
    }

    public void setCategoryLv(String CategoryLv) {
        this.CategoryLv = CategoryLv;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double Money) {
        this.Money = Money;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String Memo) {
        this.Memo = Memo;
    }
}
