package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.Dict;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSupplier
 * Author: Admin
 * Time: 2019/8/7 9:23
 * Describe: describe
 */
public class DictModel {

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {

        private List<Dict> data;

        public List<Dict> getData() {
            return data;
        }

        public void setData(List<Dict> data) {
            this.data = data;
        }
    }
}
