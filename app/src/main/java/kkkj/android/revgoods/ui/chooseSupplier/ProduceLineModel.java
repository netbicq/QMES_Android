package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.Dict;
import kkkj.android.revgoods.bean.ProduceLine;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSupplier
 * Author: Admin
 * Time: 2019/8/7 13:38
 * Describe: describe
 */
public class ProduceLineModel {

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {

        private List<ProduceLine> data;

        public List<ProduceLine> getData() {
            return data;
        }

        public void setData(List<ProduceLine> data) {
            this.data = data;
        }
    }

}
