package kkkj.android.revgoods.bean;

/**
 * 累计
 */
public class Cumulative {
    private int count;
    /**
     * 毛重
     */
    private String mweight;

    /**
     * 扣重
     */
    private String kweight;

    /**
     * 净重
     */
    private String jweight;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMweight() {
        return mweight;
    }

    public void setMweight(String mweight) {
        this.mweight = mweight;
    }

    public String getKweight() {
        return kweight;
    }

    public void setKweight(String kweight) {
        this.kweight = kweight;
    }

    public String getJweight() {
        return jweight;
    }

    public void setJweight(String jweight) {
        this.jweight = jweight;
    }
}
