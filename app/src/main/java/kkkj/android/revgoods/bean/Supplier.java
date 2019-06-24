package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.bean
 * 创建者:   Bpldbt
 * 创建时间: 2019/6/24 15:25
 * 描述:    供应商
 */
public class Supplier extends LitePalSupport {

    private int id;

    /**
     * 名称
     */
    private String name;

    /**
     * 该供应商下所有品类
     * 必须先初始化
     */
    private List<Matter> mMatters = new ArrayList<>();

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

    public List<Matter> getMatters() {
        return mMatters;
    }

    public void setMatters(List<Matter> matters) {
        mMatters = matters;
    }
}
