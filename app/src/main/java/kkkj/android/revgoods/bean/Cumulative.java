package kkkj.android.revgoods.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * 累计
 */
public class Cumulative extends LitePalSupport {

    private int id;

    private int count;
    /**
     * 类别
     */
    private String category;

    /**
     * 重量
     */
    private String weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
