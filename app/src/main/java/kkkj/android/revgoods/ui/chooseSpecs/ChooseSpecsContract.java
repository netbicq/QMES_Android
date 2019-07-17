package kkkj.android.revgoods.ui.chooseSpecs;

import java.util.List;

import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.mvpInterface.MvpView;

/**
 * Name: RevGoods
 * Package Name：kkkj.android.revgoods.ui.chooseSpecs
 * Author: Admin
 * Time: 2019/7/17 13:51
 * Describe: describe
 */
public class ChooseSpecsContract {

    public interface View extends MvpView
    {
        void getSpecsSuc(List<Specs> data);
    }
    public static abstract class Presenter extends MvpPresenter<View> {

        public abstract void getSpecsByMatterId(ChooseSpecsModel.Request request);
    }
}
