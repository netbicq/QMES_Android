package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.bean
 * 创建者:   Bpldbt
 * 创建时间: 2019/6/24 15:20
 * 描述:    规格
 */
public class Specs extends LitePalSupport {

    private int id;

    /**
     * 规格
     */
    private String specs;
    /**
     * 所属的品类
     */
    private Matter matter;
    /**
     * 一对多
     */
    private List<SamplingDetails> detailsList = new ArrayList<>();

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Matter getMatter() {
        return matter;
    }

    public void setMatter(Matter matter) {
        this.matter = matter;
    }
}
