package kkkj.android.revgoods.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/1 17:01
 * Describe: 品类等级
 */
public class MatterLevel extends LitePalSupport {

    private int id;

    private String name;

    private String level;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
