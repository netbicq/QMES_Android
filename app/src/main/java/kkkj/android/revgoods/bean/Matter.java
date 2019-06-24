package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.bean
 * 创建者:   Bpldbt
 * 创建时间: 2019/6/24 15:18
 * 描述:    品类
 */
public class Matter extends LitePalSupport {

    private int id;

    /**
     * 名称
     */
    private String name;

    /**
     * 所属的供应商
     */
    private Supplier supplier;

    /**
     * 品类下所有规格
     * 必须先初始化
     */
    private List<Specs> mSpecs = new ArrayList<>();

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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<Specs> getSpecs() {
        return mSpecs;
    }

    public void setSpecs(List<Specs> specs) {
        mSpecs = specs;
    }
}
