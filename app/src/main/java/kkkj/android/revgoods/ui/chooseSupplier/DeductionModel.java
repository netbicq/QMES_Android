package kkkj.android.revgoods.ui.chooseSupplier;

import java.util.List;

import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.http.RevGRequest;
import kkkj.android.revgoods.http.RevGResponse;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSupplier
 * Author: Admin
 * Time: 2019/8/8 13:34
 * Describe: 扣重类型
 */
public class DeductionModel {

    public static class Request extends RevGRequest {

    }

    public static class Response extends RevGResponse {

        private List<DeductionCategory> data;

        public List<DeductionCategory> getData() {
            return data;
        }

        public void setData(List<DeductionCategory> data) {
            this.data = data;
        }
    }
}
