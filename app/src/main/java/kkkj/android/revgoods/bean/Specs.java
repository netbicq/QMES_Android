package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

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
     * 名称
     */
    private String name;

    /**
     * 规格占比
     */
    private double specsProportion;

    /**
     * 所属的品类
     */
    private Matter matter;

    public double getSpecsProportion() {
        return specsProportion;
    }

    public void setSpecsProportion(double specsProportion) {
        this.specsProportion = specsProportion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Matter getMatter() {
        return matter;
    }

    public void setMatter(Matter matter) {
        this.matter = matter;
    }
}
