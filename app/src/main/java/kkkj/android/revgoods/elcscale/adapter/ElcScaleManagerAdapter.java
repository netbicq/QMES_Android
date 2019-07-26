package kkkj.android.revgoods.elcscale.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.layout.QMUIButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.elcscale.bean.BluetoothBean;

public class ElcScaleManagerAdapter extends BaseQuickAdapter<BluetoothBean, BaseViewHolder> {
    public ElcScaleManagerAdapter(int layoutResId, @Nullable List<BluetoothBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BluetoothBean item) {
        QMUIButton connect = helper.getView(R.id.connect);//连接按钮
        QMUIButton listen = helper.getView(R.id.listen);//监听按钮
        final TextView weight = helper.getView(R.id.weight);
        if (!TextUtils.isEmpty(item.getBluetoothDevice().getName())) {
            helper.setText(R.id.name, item.getBluetoothDevice().getName());
        } else {
            helper.setText(R.id.name, "未命名");
        }
        helper.setText(R.id.weight, item.getWeight() + "");
        if (item.isConnect()) {
            connect.setText("断开");
            listen.setVisibility(View.VISIBLE);
        } else {
            connect.setText("连接");
            listen.setVisibility(View.GONE);
        }

        if (item.isListen()) {
            listen.setText("停止监听");
        } else {
            listen.setText("开启监听");
        }
        helper.addOnClickListener(R.id.connect);
        helper.addOnClickListener(R.id.listen);
        StringBuilder sb = new StringBuilder();
//        Flowable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//                .takeWhile(new Predicate<Long>() {
//                    @Override
//                    public boolean test(Long integer) throws Exception {
//                        return item.isListen();
//                    }
//                })
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(@NonNull Long aLong) throws Exception {
//                        item.getMyBluetoothManager().getReadOB().subscribe(new Observer<String>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                                if (! item.getMyBluetoothManager().isConnect()) {
//                                    Logger.d("蓝牙断开");
//                                    d.dispose();
//                                }
//                            }
//
//                            @Override
//                            public void onNext(String s) {
//                                Logger.d("读取到数据:"+s);
//                                if(s.length()==8){
//                                    weight.setText(s.replace("=","")+ "");
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Logger.d("读取错误:"+e.getMessage());
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//                    }
//                });
    }
}
