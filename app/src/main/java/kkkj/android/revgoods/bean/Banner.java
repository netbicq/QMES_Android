package kkkj.android.revgoods.bean;

import java.util.List;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.bean
 * Author: Admin
 * Time: 2019/8/1 11:35
 * Describe: describe
 */
public class Banner {


    /**
     * data :[
     * {"desc":"Android高级进阶直播课免费学习\r\n",
     * "id":22,
     * "imagePath":"https://wanandroid.com/blogimgs/c3615a24-79ef-45c9-9ae6-5adfe5437d32.jpeg",
     * "isVisible":1,
     * "order":0,
     * "title":"Android高级进阶直播课免费学习",
     * "type":0,
     * "url":"https://url.163.com/4bj"}
     * ]
     * errorCode : 0
     * errorMsg :
     */

    private int errorCode;
    private String errorMsg;
    private List<DataBean> data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * desc : Android高级进阶直播课免费学习
         * id : 22
         * imagePath : https://wanandroid.com/blogimgs/c3615a24-79ef-45c9-9ae6-5adfe5437d32.jpeg
         * isVisible : 1
         * order : 0
         * title : Android高级进阶直播课免费学习
         * type : 0
         * url : https://url.163.com/4bj
         */

        private String desc;
        private int id;
        private String imagePath;
        private int isVisible;
        private int order;
        private String title;
        private int type;
        private String url;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public int getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(int isVisible) {
            this.isVisible = isVisible;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @Override
    public String toString() {
        return "Banner{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
