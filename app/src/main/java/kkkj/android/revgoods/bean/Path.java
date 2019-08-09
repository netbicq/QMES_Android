package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/9 16:59
 * Describe: describe
 */
public class Path extends LitePalSupport {

    private int id;

    private String path;

    private SamplingDetails samplingDetails;

    public SamplingDetails getSamplingDetails() {
        return samplingDetails;
    }

    public void setSamplingDetails(SamplingDetails samplingDetails) {
        this.samplingDetails = samplingDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
