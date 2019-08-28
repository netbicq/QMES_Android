package kkkj.android.revgoods.ui.addDeductionCategory;

import java.util.List;
import kkkj.android.revgoods.bean.DeductionCategory;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.addDeductionCategory
 * Author: Admin
 * Time: 2019/8/5 15:40
 * Describe: describe
 */
public class DeductionCategoryContract {

    public interface View extends MvpView {
        void getDeductionCategorySuc(List<DeductionCategory> data);
        void addDeductionCategorySuc(DeductionCategory data);
        void deleteDeductionCategorySuc(DeleteDeductionCategoryModel.Response data);
        void deleteDeductionCategoryFail(String msg);
    }

    public static abstract class Presenter extends MvpPresenter<DeductionCategoryContract.View> {

        public abstract void getDeductionCategory();
        public abstract void addDeductionCategory(AddDeductionCategoryModel.Request request);
        public abstract void deleteDeductionCategory(DeleteDeductionCategoryModel.Request request);
    }
}
