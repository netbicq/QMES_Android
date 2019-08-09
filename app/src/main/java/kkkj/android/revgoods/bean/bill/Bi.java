package kkkj.android.revgoods.bean.bill;

import java.util.List;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean.bill
 * Author: Admin
 * Time: 2019/8/8 18:21
 * Describe: describe
 */
public class Bi {


    /**
     * BillMaster : {"PurchaseDate":"2019-08-08 17:20:13","SupplierID":"2810ff05-9136-4fa6-b0d7-d27d4993ae77","NormID":"226407e0-6146-4ae2-9e9f-884446cdfd13","CategoryID":"b623c2e3-0983-4685-9fc7-a718fd9a3801","CategoryLv":"4173c686-446d-4065-80be-eef5649fb349","Price":6,"Amount":7,"Money":8,"Memo":"sample string 9"}
     * DelWeights : [{"Weight":1,"DelWeightType":"12baa102-3643-4a14-988b-b37b13840ad3"},{"Weight":1,"DelWeightType":"12baa102-3643-4a14-988b-b37b13840ad3"}]
     * Scales : [{"Weight":1},{"Weight":1}]
     * PurPrices : [{"NormsID":"326b3c1f-b1df-474a-ba5d-c7fa666f3ff5","Amount":2,"Price":3,"Menoy":"sample string 4","Ratio":5},{"NormsID":"326b3c1f-b1df-474a-ba5d-c7fa666f3ff5","Amount":2,"Price":3,"Menoy":"sample string 4","Ratio":5}]
     * PurSamples : [{"Weigth":1,"Amount":2,"SingalWeight":3,"NormsID":"9dde64b5-3e5a-4d8d-b0a5-6581c0c3eff3","Ratio":5,"Files":["sample string 1","sample string 2"]},{"Weigth":1,"Amount":2,"SingalWeight":3,"NormsID":"9dde64b5-3e5a-4d8d-b0a5-6581c0c3eff3","Ratio":5,"Files":["sample string 1","sample string 2"]}]
     */

    private BillMasterBean BillMaster;
    private List<DelWeightsBean> DelWeights;
    private List<ScalesBean> Scales;
    private List<PurPricesBean> PurPrices;
    private List<PurSamplesBean> PurSamples;

    public BillMasterBean getBillMaster() {
        return BillMaster;
    }

    public void setBillMaster(BillMasterBean BillMaster) {
        this.BillMaster = BillMaster;
    }

    public List<DelWeightsBean> getDelWeights() {
        return DelWeights;
    }

    public void setDelWeights(List<DelWeightsBean> DelWeights) {
        this.DelWeights = DelWeights;
    }

    public List<ScalesBean> getScales() {
        return Scales;
    }

    public void setScales(List<ScalesBean> Scales) {
        this.Scales = Scales;
    }

    public List<PurPricesBean> getPurPrices() {
        return PurPrices;
    }

    public void setPurPrices(List<PurPricesBean> PurPrices) {
        this.PurPrices = PurPrices;
    }

    public List<PurSamplesBean> getPurSamples() {
        return PurSamples;
    }

    public void setPurSamples(List<PurSamplesBean> PurSamples) {
        this.PurSamples = PurSamples;
    }

    public static class BillMasterBean {
        /**
         * PurchaseDate : 2019-08-08 17:20:13
         * SupplierID : 2810ff05-9136-4fa6-b0d7-d27d4993ae77
         * NormID : 226407e0-6146-4ae2-9e9f-884446cdfd13
         * CategoryID : b623c2e3-0983-4685-9fc7-a718fd9a3801
         * CategoryLv : 4173c686-446d-4065-80be-eef5649fb349
         * Price : 6.0
         * Amount : 7.0
         * Money : 8.0
         * Memo : sample string 9
         */

        private String PurchaseDate;
        private String SupplierID;
        private String NormID;
        private String CategoryID;
        private String CategoryLv;
        private double Price;
        private double Amount;
        private double Money;
        private String Memo;

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

    public static class DelWeightsBean {
        /**
         * Weight : 1.0
         * DelWeightType : 12baa102-3643-4a14-988b-b37b13840ad3
         */

        private double Weight;
        private String DelWeightType;

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

    public static class ScalesBean {
        /**
         * Weight : 1.0
         */

        private double Weight;

        public double getWeight() {
            return Weight;
        }

        public void setWeight(double Weight) {
            this.Weight = Weight;
        }
    }

    public static class PurPricesBean {
        /**
         * NormsID : 326b3c1f-b1df-474a-ba5d-c7fa666f3ff5
         * Amount : 2.0
         * Price : 3.0
         * Menoy : sample string 4
         * Ratio : 5.0
         */

        private String NormsID;
        private double Amount;
        private double Price;
        private String Menoy;
        private double Ratio;

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

        public String getMenoy() {
            return Menoy;
        }

        public void setMenoy(String Menoy) {
            this.Menoy = Menoy;
        }

        public double getRatio() {
            return Ratio;
        }

        public void setRatio(double Ratio) {
            this.Ratio = Ratio;
        }
    }

    public static class PurSamplesBean {
        /**
         * Weigth : 1.0
         * Amount : 2.0
         * SingalWeight : 3.0
         * NormsID : 9dde64b5-3e5a-4d8d-b0a5-6581c0c3eff3
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
}
