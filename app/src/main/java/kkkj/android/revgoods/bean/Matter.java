package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

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
    private String Name;

    private String KeyID;

    /**
     * 计价方式
     * ValuationType = 1;根据规格计算
     * ValuationType = 2;根据规格占比计算
     */
    private int ValuationType = 0;

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }

    public int getValuationType() {
        return ValuationType;
    }

    public void setValuationType(int valuationType) {
        this.ValuationType = valuationType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    @Override
    public String toString() {
        return "Matter{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", KeyID='" + KeyID + '\'' +
                ", ValuationType=" + ValuationType +
                '}';
    }
}
