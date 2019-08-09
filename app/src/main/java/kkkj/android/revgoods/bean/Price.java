package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/8 12:02
 * Describe: 价格配置
 */
public class Price extends LitePalSupport {

    private int id;

    /**
     * KeyID : 8726fa28-7982-4710-9cc3-bd33a50d6f38
     * SupplierID : b2fb8d68-9b07-4645-b482-2aa559ecca24
     * CategoryID : f9a859dd-398a-48b9-b3f4-6502bd5952b4
     * CategoryLv : 1af82f42-7bf0-4eca-b331-b9cb4b9cb93c
     * NormsID : 80d6339f-e049-4e70-bea5-9dab27238564
     * StartDate : 2019-08-08 12:01:39
     * EndDate : 2019-08-08 12:01:39
     * Price : 8.0
     */

    private String KeyID;
    private String SupplierID;
    private String CategoryID; //品类
    private String CategoryLv; //品类等级
    private String NormsID;    //规格
    private String StartDate;
    private String EndDate;
    private double Price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String KeyID) {
        this.KeyID = KeyID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String SupplierID) {
        this.SupplierID = SupplierID;
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

    public String getNormsID() {
        return NormsID;
    }

    public void setNormsID(String NormsID) {
        this.NormsID = NormsID;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", KeyID='" + KeyID + '\'' +
                ", SupplierID='" + SupplierID + '\'' +
                ", CategoryID='" + CategoryID + '\'' +
                ", CategoryLv='" + CategoryLv + '\'' +
                ", NormsID='" + NormsID + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", Price=" + Price +
                '}';
    }
}
