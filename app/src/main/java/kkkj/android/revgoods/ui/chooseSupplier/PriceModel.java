package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.Price;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSupplier
 * Author: Admin
 * Time: 2019/8/8 12:06
 * Describe: describe
 */
public class PriceModel {

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {

        private List<Price> data;

        public List<Price> getData() {
            return data;
        }

        public void setData(List<Price> data) {
            this.data = data;
        }
    }
}
