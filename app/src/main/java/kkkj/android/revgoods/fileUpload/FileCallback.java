package kkkj.android.revgoods.fileUpload;

import java.util.List;

import kkkj.android.revgoods.bean.SamplingDetails;

/**
 * 项目名:   RevGoods
 * 包名:     kkkj.android.revgoods.fileUpload
 * 创建者:   Bpldbt
 * 创建时间: 2019/8/14 20:39
 * 描述:    TODO
 */
public interface FileCallback {

    void onCompleted(List<SamplingDetails> samplingDetailsList);
}
