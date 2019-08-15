package kkkj.android.revgoods.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.sdk.TbsVideo;
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.MainActivity;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.PicOrMp4Adapter;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Path;
import kkkj.android.revgoods.bean.Price;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.GetPicOrMP4Activity;
import kkkj.android.revgoods.common.getpic.PhotoViewActivity;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.http.RetrofitServiceManager;
import kkkj.android.revgoods.http.api.APIAttachfile;
import kkkj.android.revgoods.http.api.UploadCallbacks;
import kkkj.android.revgoods.ui.chooseSupplier.UpLoadFileModel;
import kkkj.android.revgoods.ui.saveBill.SaveBillWithoutSamplingActivity;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.NetUtils;

import static kkkj.android.revgoods.mvpInterface.MvpModel.apiAttachfile;

/**
 * 采样
 */

public class SamplingFragment extends BaseDialogFragment implements View.OnClickListener {

    private Button mSaveButton;
    private Button mEnterButton;
    private EditText mEtNumber;
    private EditText mEtWeight;
    private EditText mEtPrice;
    private Spinner mSpSpecs;
    private RecyclerView recyclerView;
    private ArrayAdapter specsAdapter;

    private String weight;
    private int supplierId;
    private int matterId;
    private int matterLevelId;
    private Supplier supplier;
    private Matter matter;
    private MatterLevel matterLevel;
    private String tempPrice = "";

    //单重
    private double singalWeight;

    private List<GetPicModel> mList;
    private List<Specs> specsList;
    private List<String> specsNameList;

    private Specs specs;
    private Specs tempSpecs;
    private int position = -1;
    private PicOrMp4Adapter picOrMp4Adapter;
    private MyToasty myToasty;

    private QMUITipDialog qmuiTipDialog;

    //返回的路径集合
    private List<Path> pathList = new ArrayList<>();


    public static SamplingFragment newInstance(String weight, int supplierId, int matterId, int matterLevelId) {
        Bundle args = new Bundle();
        args.putString("weight", weight);
        args.putInt("supplierId", supplierId);
        args.putInt("matterId", matterId);
        args.putInt("matterLevelId", matterLevelId);

        SamplingFragment fragment = new SamplingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_sampling;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weight = (String) getArguments().getSerializable("weight");
        supplierId = (int) getArguments().getSerializable("supplierId");
        matterId = (int) getArguments().getSerializable("matterId");
        matterLevelId = (int) getArguments().getSerializable("matterLevelId");
        supplier = LitePal.find(Supplier.class, supplierId);
        matter = LitePal.find(Matter.class, matterId);
        matterLevel = LitePal.find(MatterLevel.class, matterLevelId);

    }

    @Override
    public void initData() {
        myToasty = new MyToasty(getContext());
        mList = new ArrayList<>();
        specsList = new ArrayList<>();
        specsNameList = new ArrayList<>();
        //specs = new Specs();
        tempSpecs = new Specs();
        tempSpecs.setValue(getResources().getString(R.string.choose_specs));

        specsList.add(tempSpecs);
        specsList.addAll(LitePal.findAll(Specs.class));

        for (int i = 0; i < specsList.size(); i++) {
            specsNameList.add(specsList.get(i).getValue());
        }
        specsAdapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_spinner_item, specsNameList);
        specsAdapter.setDropDownViewResource(R.layout.item_spinner);


        picOrMp4Adapter = new PicOrMp4Adapter(R.layout.item_picormp4_upload, mList);
        picOrMp4Adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mList = picOrMp4Adapter.getData();
                switch (view.getId()) {
                    case R.id.img:
                    case R.id.mp4:
                        Logger.d("图片路径:" + mList.get(position).getImagePath());
                        Logger.d("MP4路径:" + mList.get(position).getMp4Path());
                        if (mList.get(position).getType() == 0) {
                            startActivityForResult(new Intent(getActivity(), PhotoViewActivity.class).putExtra(
                                    "picUrl", mList.get(position).getImagePath()), 200);
                        } else {
                            //用腾讯TBS播放视频
                            //判断当前是否可用
                            if (TbsVideo.canUseTbsPlayer(getActivity().getApplicationContext())) {
                                //播放视频
                                TbsVideo.openVideo(getActivity().getApplicationContext(),
                                        mList.get(position).getMp4Path());
                            } else {
                                myToasty.showError("TBS视频播放器异常");
                            }
                        }
                        break;
                    case R.id.iv_delete:
                        mList.remove(position);
                        picOrMp4Adapter.notifyDataSetChanged();
                        break;

                    case R.id.ed_content:

                        break;

                    case R.id.ic_upload:
                        if (!NetUtils.checkNetWork()) {
                            myToasty.showWarning("当前网络连接不可用，请联网后重试！");
                            return;
                        }
                        GetPicModel getPicModel = mList.get(position);
                        uploadFiles(getPicModel);
                        break;

                    default:
                        break;
                }
            }
        });

    }


    @Override
    public void initView(View view) {
        qmuiTipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传...")
                .create();

        ivRight.setImageResource(R.drawable.ic_camera);
        tvTitle.setText(R.string.sampling);
        mEtNumber = view.findViewById(R.id.id_et_number);
        mEtWeight = view.findViewById(R.id.id_et_weight);
        mEtPrice = view.findViewById(R.id.id_et_price);
        mSpSpecs = view.findViewById(R.id.id_sp_specs);
        mSpSpecs.setAdapter(specsAdapter);

        mSaveButton = view.findViewById(R.id.button);
        mEnterButton = view.findViewById(R.id.button_enter);
        mEtWeight.setText(weight);

        ivRight.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mEnterButton.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.id_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(picOrMp4Adapter);

        mSpSpecs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                specs = specsList.get(i);
                position = i;

                /**
                 * 根据supplierId，matterId ,specs 和当前品类等级  查找价格配置表   是否匹配
                 * 匹配：mEtPrice.setText(当前配置的价格)
                 * 未匹配：0
                 */

                List<Price> priceList = LitePal.where("SupplierID = ? and CategoryID = ? and CategoryLv = ? and NormsID = ?",
                        supplier.getKeyID(), matter.getKeyID(), matterLevel.getKeyID(), specs.getKeyID()).find(Price.class);
                //比交时间
                //获取当前时间 HH:mm:ss
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date nowDate = new Date(System.currentTimeMillis());//当前时间
                List<Price> tempPriceList = new ArrayList<>();//所有满足条件的集合
                for (int j = 0; j < priceList.size(); j++) {
                    String start = priceList.get(j).getStartDate();
                    String end = priceList.get(j).getEndDate();
                    Date startDate = new Date();
                    Date endDate = new Date();
                    try {
                        startDate = dateFormat.parse(start);//开始时间
                        endDate = dateFormat.parse(end);//截止时间
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                        tempPriceList.add(priceList.get(j));
                    }
                }

                if (tempPriceList.size() > 0) {
                    Price lastPrice = tempPriceList.get(tempPriceList.size() - 1);//最新一个价格
                    mEtPrice.setText(String.valueOf(lastPrice.getPrice()));
                }else {
                    myToasty.showInfo("当前未配置价格，请手动填写！");
                    mEtPrice.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //specs = tempSpecs;
                position = 0;
            }
        });

        mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tempPrice = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_right:
                takePicture();
                break;

            case R.id.button://提交
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setIsDwon(1);//不可编辑
                }
                LitePal.saveAll(mList);
                //上传附件
                //uploadFiles(mList);

                SamplingDetails samplingDetails = new SamplingDetails();

                if (tempPrice.length() == 0) {
                    myToasty.showWarning("请输入单价！");
                    return;
                }

                if (DoubleCountUtils.keep(Double.valueOf(tempPrice)) <= 0) {
                    myToasty.showWarning("单价不能为零！");
                    return;
                }

                //最终的单价
                if (position != 0) {
                    samplingDetails.setPrice(DoubleCountUtils.keep(Double.valueOf(tempPrice)));
                    samplingDetails.setSpecsId(specs.getId());
                } else {
                    myToasty.showWarning("请选择系统默认提供的规格！");
                    return;
                }

                samplingDetails.setWeight(mEtWeight.getText().toString().trim());
                samplingDetails.setNumber(mEtNumber.getText().toString().trim());
                samplingDetails.setSingalWeight(singalWeight);
                samplingDetails.setPathList(pathList);
                samplingDetails.setSupplierId(supplierId);
                samplingDetails.setMatterId(matterId);

                //品类等级
                samplingDetails.setMatterLevelId(matterLevel.getId());

                samplingDetails.setModelList(mList);

                DeviceEvent deviceEvent = new DeviceEvent();

                if (LitePal.where("hasBill < ?", "0").find(SamplingDetails.class).size() > 0) {
                    SamplingDetails lastDetails = LitePal.where("hasBill < ?", "0").findLast(SamplingDetails.class);
                    samplingDetails.setCount(lastDetails.getCount() + 1);
                    deviceEvent.setSamplingNumber(lastDetails.getCount() + 1);
                } else {
                    samplingDetails.setCount(1);
                    deviceEvent.setSamplingNumber(1);
                }

                EventBus.getDefault().post(deviceEvent);
                samplingDetails.save();

                dismiss();

                break;

            case R.id.button_enter://计算
                String weight = mEtWeight.getText().toString().trim();
                String number = mEtNumber.getText().toString().trim();

                if (!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(number)) {
                    if (Double.parseDouble(weight) != 0 && Integer.parseInt(number) != 0) {

                        double specs = Double.parseDouble(weight) / Integer.parseInt(number);
                        //单重
                        singalWeight = DoubleCountUtils.keep(specs);
                        specsNameList.set(0, String.valueOf(specs));
                        specsAdapter.notifyDataSetChanged();

                    } else {
                        myToasty.showError("输入不能为零！请重新输入！");
                    }
                } else {
                    myToasty.showError("请填写相关信息！");
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //拍照回调
        if (resultCode == Activity.RESULT_OK) {
            //mList = picOrMp4Adapter.getData();
            GetPicModel picOrMp4 = new GetPicModel();
            picOrMp4 = (GetPicModel) data.getSerializableExtra("result");
            Logger.d(picOrMp4.getMp4Path());
            mList.add(picOrMp4);
            Logger.d("-------------------->" + picOrMp4.getImagePath());
            picOrMp4Adapter.notifyDataSetChanged();


//            int type = picOrMp4.getType();//0照片  1视频
//            switch (type) {
//                case 0://照片
//                    String path = picOrMp4.getImagePath();
//                    UpLoadFileModel.Request request = new UpLoadFileModel.Request();
//                    request.setFile(new File(path));
//                    request.setMediaType("image");
//                    UploadCallbacks mListener = new UploadCallbacks() {
//                        @Override
//                        public void onProgressUpdate(int percentage) {
////                        Logger.d(percentage);
//                        }
//
//                        @Override
//                        public void onError() {
//                            Logger.d("错误");
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            //qmuiTipDialog.dismiss();
//                        }
//                    };
//                    request.setListener(mListener);
//                    apiAttachfile.uploadfile(request.getFiles())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<UpLoadFileModel.Response>() {
//                                @Override
//                                public void onSubscribe(Disposable d) {
//
//                                }
//
//                                @Override
//                                public void onNext(UpLoadFileModel.Response response) {
//                                    if (response.getState() == 200) {
//                                        Path path1 = new Path();
//                                        path1.setPath(response.getData());
//                                        boolean b = path1.save();
//                                        Logger.d("是否保存成功：" + b);
//                                        pathList.add(path1);
//                                    }
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//
//                                @Override
//                                public void onComplete() {
//                                    if (qmuiTipDialog.isShowing()) {
//                                        qmuiTipDialog.dismiss();
//                                    }
//                                }
//                            });
//
//                    break;
//
//                case 1:
//
//                    String pathVideo = picOrMp4.getMp4Path();
//                    UpLoadFileModel.Request requestVideo = new UpLoadFileModel.Request();
//                    requestVideo.setFile(new File(pathVideo));
//                    requestVideo.setMediaType("video");
//                    UploadCallbacks mListenerVideo = new UploadCallbacks() {
//                        @Override
//                        public void onProgressUpdate(int percentage) {
////                        Logger.d(percentage);
//                        }
//
//                        @Override
//                        public void onError() {
//                            Logger.d("错误");
//                        }
//
//                        @Override
//                        public void onFinish() {
//
//                        }
//                    };
//                    requestVideo.setListener(mListenerVideo);
//                    apiAttachfile.uploadfile(requestVideo.getFiles())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<UpLoadFileModel.Response>() {
//                                @Override
//                                public void onSubscribe(Disposable d) {
//
//                                }
//
//                                @Override
//                                public void onNext(UpLoadFileModel.Response response) {
//                                    if (response.getState() == 200) {
//                                        Path path1 = new Path();
//                                        path1.setPath(response.getData());
//                                        boolean b = path1.save();
//                                        Logger.d("是否保存成功：" + b);
//                                        pathList.add(path1);
//                                    }
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//
//                                @Override
//                                public void onComplete() {
//                                    if (qmuiTipDialog.isShowing()) {
//                                        qmuiTipDialog.dismiss();
//                                    }
//                                }
//                            });
//                    break;
//
//                default:
//                    break;
//            }

        }

    }

    /**
     * 拍照
     */
    private void takePicture() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        startActivityForResult(new Intent(getContext(), GetPicOrMP4Activity.class), 200);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //有至少一个权限没有同意
                        //showToast("请同意全部权限");
                    } else {
                        //有至少一个权限没有同意且勾选了不在提示
                        //showToast("请在权限管理中打开相关权限");
                    }
                });
    }


    /**
     * 上传附件
     */
    private void uploadFiles(GetPicModel picOrMp4) {

        //上传附件
        APIAttachfile apiAttachfile = RetrofitServiceManager.getInstance().create(APIAttachfile.class);
        if (!qmuiTipDialog.isShowing()) {
            qmuiTipDialog.show();
        }

        int type = picOrMp4.getType();//0照片  1视频
        switch (type) {
            case 0://照片
                String path = picOrMp4.getImagePath();
                UpLoadFileModel.Request request = new UpLoadFileModel.Request();
                request.setFile(new File(path));
                request.setMediaType("image");
                UploadCallbacks mListener = new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(int percentage) {
//                        Logger.d(percentage);
                    }

                    @Override
                    public void onError() {
                        Logger.d("错误");
                    }

                    @Override
                    public void onFinish() {
                        //qmuiTipDialog.dismiss();
                    }
                };
                request.setListener(mListener);
                apiAttachfile.uploadfile(request.getFiles())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UpLoadFileModel.Response>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(UpLoadFileModel.Response response) {
                                if (response.getState() == 200) {
                                    Path path1 = new Path();
                                    path1.setPath(response.getData());
                                    boolean b = path1.save();
                                    Logger.d("是否保存成功：" + b);
                                    pathList.add(path1);
                                    myToasty.showSuccess("上传成功！");
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                if (qmuiTipDialog.isShowing()) {
                                    qmuiTipDialog.dismiss();
                                }
                            }
                        });

                break;

            case 1:

                String pathVideo = picOrMp4.getMp4Path();
                UpLoadFileModel.Request requestVideo = new UpLoadFileModel.Request();
                requestVideo.setFile(new File(pathVideo));
                requestVideo.setMediaType("video");
                UploadCallbacks mListenerVideo = new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(int percentage) {
//                        Logger.d(percentage);
                    }

                    @Override
                    public void onError() {
                        Logger.d("错误");
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                requestVideo.setListener(mListenerVideo);
                apiAttachfile.uploadfile(requestVideo.getFiles())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UpLoadFileModel.Response>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(UpLoadFileModel.Response response) {
                                if (response.getState() == 200) {
                                    Path path1 = new Path();
                                    path1.setPath(response.getData());
                                    boolean b = path1.save();
                                    Logger.d("是否保存成功：" + b);
                                    pathList.add(path1);
                                    myToasty.showSuccess("上传成功！");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                if (qmuiTipDialog.isShowing()) {
                                    qmuiTipDialog.dismiss();
                                }
                            }
                        });
                break;

            default:
                break;
        }


    }

}
