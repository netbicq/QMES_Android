package kkkj.android.revgoods.bean.bill;

import java.util.List;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean.bill
 * Author: Admin
 * Time: 2019/8/8 15:25
 * Describe: 采样明细
 */
public class PurSamples {

    /**
     * Weigth : 1.0
     * Amount : 2.0
     * SingalWeight : 3.0
     * NormsID : b8b62085-43a5-488e-9cfa-659f4c73927e
     * Ratio : 5.0
     * Files : ["sample string 1","sample string 2"]
     */

    private double Weigth;
    private double Amount;
    private double SingalWeight;
    private String NormsID;
    private double Ratio;
    private List<String> Files;

    public double getWeigth() {
        return Weigth;
    }

    public void setWeigth(double Weigth) {
        this.Weigth = Weigth;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public double getSingalWeight() {
        return SingalWeight;
    }

    public void setSingalWeight(double SingalWeight) {
        this.SingalWeight = SingalWeight;
    }

    public String getNormsID() {
        return NormsID;
    }

    public void setNormsID(String NormsID) {
        this.NormsID = NormsID;
    }

    public double getRatio() {
        return Ratio;
    }

    public void setRatio(double Ratio) {
        this.Ratio = Ratio;
    }

    public List<String> getFiles() {
        return Files;
    }

    public void setFiles(List<String> Files) {
        this.Files = Files;
    }
}
