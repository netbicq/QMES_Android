package kkkj.android.revgoods.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
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
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.NetUtils;
import kkkj.android.revgoods.utils.SharedPreferenceUtil;

/**
 * ??????
 */

public class SamplingFragment extends BaseDialogFragment implements View.OnClickListener {

    private Button mSaveButton;
    private Button mEnterButton;
    private Button mBtnChangeUnit;
    private TextView mTvUnit;
    private TextView mTvSpecsName;
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


    //??????
    private double singalWeight;

    private List<GetPicModel> mList;
    private List<Specs> specsList;
    private List<String> specsNameList;

    private Specs specs;
    private Specs tempSpecs;
    private int position = 0;
    private PicOrMp4Adapter picOrMp4Adapter;

    private QMUITipDialog qmuiTipDialog;

    /**
     * ????????????
     * 1. ??????????????????kg
     * 2. g
     */
    private int samplingUnit;

    //?????????????????????
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

        samplingUnit = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);
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

        specsNameList.add(getResources().getString(R.string.choose_specs));
        for (int i = 1; i < specsList.size(); i++) {
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
                        Logger.d("????????????:" + mList.get(position).getImagePath());
                        Logger.d("MP4??????:" + mList.get(position).getMp4Path());
                        if (mList.get(position).getType() == 0) {
                            startActivityForResult(new Intent(getActivity(), PhotoViewActivity.class).putExtra(
                                    "picUrl", mList.get(position).getImagePath()), 200);
                        } else {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            String path = mList.get(position).getMp4Path();
                            File file = new File(path);
                            Uri uri;
                            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
                                uri = FileProvider.getUriForFile(getActivity(), "kkkj.android.revgoods.fileprovider", file);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                                uri = Uri.fromFile(file);
                            }
                            intent.setDataAndType(uri, "video/*");
                            startActivity(intent);

                            //?????????TBS????????????
                            //????????????????????????
//                            if (TbsVideo.canUseTbsPlayer(getActivity().getApplicationContext())) {
//                                //????????????
//                                TbsVideo.openVideo(getActivity().getApplicationContext(),
//                                        mList.get(position).getMp4Path());
//                            } else {
//                                myToasty.showError("TBS?????????????????????");
//                            }
                        }
                        break;
                    case R.id.iv_delete:
                        mList.remove(position);
                        picOrMp4Adapter.notifyDataSetChanged();
                        //??????????????????


                        break;

                    case R.id.ed_content:

                        break;

                    case R.id.ic_upload:
                        if (!NetUtils.checkNetWork()) {
                            myToasty.showWarning("???????????????????????????????????????????????????");
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
                .setTipWord(getResources().getString(R.string.is_uploading))
                .create();

        ivRight.setImageResource(R.drawable.ic_camera);
        ivRight.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.sampling);

        mEtWeight = view.findViewById(R.id.id_et_weight);

        mTvUnit = view.findViewById(R.id.tv_unit);
        mTvSpecsName = view.findViewById(R.id.tv_specs_name);
        switch (samplingUnit) {
            case 1://kg
                mTvUnit.setText("??????(kg)");
                mEtWeight.setText(weight);
                break;

            case 2://g
                mTvUnit.setText("??????(g)");
                if (weight != null && weight.length() > 0) {
                    double d = Double.valueOf(weight);
                    d = d * 1000;
                    mEtWeight.setText(String.valueOf(d));
                }else {
                    mEtWeight.setText("0");
                }
                break;
                default:
                    break;
        }

        mEtNumber = view.findViewById(R.id.id_et_number);
        mEtPrice = view.findViewById(R.id.id_et_price);
        mSpSpecs = view.findViewById(R.id.id_sp_specs);
        mSpSpecs.setAdapter(specsAdapter);

        mSaveButton = view.findViewById(R.id.button);
        mEnterButton = view.findViewById(R.id.button_enter);
        mBtnChangeUnit = view.findViewById(R.id.btn_change_unit);


        ivRight.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mEnterButton.setOnClickListener(this);
        mBtnChangeUnit.setOnClickListener(this);

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
                mTvSpecsName.setText(specs.getName());

                /**
                 * ??????supplierId???matterId ,specs ?????????????????????  ?????????????????????   ????????????
                 * ?????????mEtPrice.setText(?????????????????????)
                 * ????????????0
                 */

                List<Price> priceList = LitePal.where("SupplierID = ? and CategoryID = ? and CategoryLv = ? and NormsID = ?",
                        supplier.getKeyID(), matter.getKeyID(), matterLevel.getKeyID(), specs.getKeyID()).find(Price.class);
                //????????????
                //?????????????????? HH:mm:ss
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date nowDate = new Date(System.currentTimeMillis());//????????????
                List<Price> tempPriceList = new ArrayList<>();//???????????????????????????
                for (int j = 0; j < priceList.size(); j++) {
                    String start = priceList.get(j).getStartDate();
                    String end = priceList.get(j).getEndDate();
                    Date startDate = new Date();
                    Date endDate = new Date();
                    try {
                        startDate = dateFormat.parse(start);//????????????
                        endDate = dateFormat.parse(end);//????????????
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                        tempPriceList.add(priceList.get(j));
                    }
                }

                if (tempPriceList.size() > 0) {
                    Price lastPrice = tempPriceList.get(tempPriceList.size() - 1);//??????????????????
                    mEtPrice.setText(String.valueOf(lastPrice.getPrice()));
                } else {
                    myToasty.showWarning("??????????????????????????????????????????");
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

        if (view.getId() == R.id.btn_change_unit) {
            String[] items = new String[2];
            items[0] = "?????? -> ???";
            items[1] = "??? -> ??????";
            QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(getContext());
            builder.addItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,2);
                            mTvUnit.setText("??????(g)");
                            if (weight != null && weight.length() > 0) {
                                double d = Double.valueOf(weight);
                                d = d * 1000;
                                mEtWeight.setText(String.valueOf(d));
                            }

                            break;

                        case 1:
                            SharedPreferenceUtil.setInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);
                            mTvUnit.setText("??????(kg)");
                            if (weight != null && weight.length() > 0) {
                                double d = Double.valueOf(weight);
                                d = d * 0.001;
                                mEtWeight.setText(String.valueOf(d));
                            }

                            break;
                        default:
                            break;
                    }
                    dialogInterface.dismiss();

                }
            }).show();

            return;
        }

        String weight = mEtWeight.getText().toString().trim();
        String number = mEtNumber.getText().toString().trim();


        switch (view.getId()) {

            case R.id.iv_right:
                takePicture();
                break;

            case R.id.button://??????

                if (!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(number)) {
                    if (Double.parseDouble(weight) != 0 && Integer.parseInt(number) != 0) {

                    } else {
                        myToasty.showWarning("???????????????????????????????????????");
                        return;
                    }
                } else {
                    myToasty.showWarning("???????????????????????????");
                    return;
                }

                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setIsDwon(1);//????????????
                }
                LitePal.saveAll(mList);
                //????????????
                //uploadFiles(mList);

                SamplingDetails samplingDetails = new SamplingDetails();


                if (position == 0) {
                    myToasty.showWarning("???????????????????????????????????????");
                    return;
                }

                if (tempPrice.length() == 0) {
                    myToasty.showWarning(getResources().getString(R.string.input_price));
                    return;
                }

                if (DoubleCountUtils.keep(Double.valueOf(tempPrice)) <= 0) {
                    myToasty.showWarning("?????????????????????");
                    return;
                }
                //???????????????
                samplingDetails.setPrice(DoubleCountUtils.keep(Double.valueOf(tempPrice)));
                samplingDetails.setSpecsId(specs.getId());


                samplingDetails.setWeight(mEtWeight.getText().toString().trim());
                samplingDetails.setNumber(mEtNumber.getText().toString().trim());
                samplingDetails.setSingalWeight(singalWeight);
                samplingDetails.setPathList(pathList);
                samplingDetails.setSupplierId(supplierId);
                samplingDetails.setMatterId(matterId);

                //????????????
                samplingDetails.setMatterLevelId(matterLevel.getId());

                samplingDetails.setModelList(mList);


                if (LitePal.where("hasBill < ?", "0").find(SamplingDetails.class).size() > 0) {
                    SamplingDetails lastDetails = LitePal.where("hasBill < ?", "0").findLast(SamplingDetails.class);
                    samplingDetails.setCount(lastDetails.getCount() + 1);
                } else {
                    samplingDetails.setCount(1);
                }

                samplingDetails.save();

                dismiss();

                break;

            case R.id.button_enter://??????

                if (!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(number)) {
                    if (Double.parseDouble(weight) != 0 && Integer.parseInt(number) != 0) {

                    } else {
                        myToasty.showWarning("???????????????????????????????????????");
                        return;
                    }
                } else {
                    myToasty.showWarning("???????????????????????????");
                    return;
                }

                double specs = Double.parseDouble(weight) / Integer.parseInt(number);
                specs = DoubleCountUtils.keep(specs);

                int postion = 0;

                int unit = SharedPreferenceUtil.getInt(SharedPreferenceUtil.SP_SAMPLING_UNIT,1);

                for (int i=1;i<specsList.size();i++) {
                    Specs specs1 = specsList.get(i);
                    /**
                     * unit = 1:??????????????????
                     * unit = 2:???????????????
                     */
                    if (unit == 1) {
                        //????????????????????????kg,??????????????????????????????g?????????????????????kg,???????????????????????????
                        if (specs1.getUnit().equals("g")) {
                            //???g??????????????????
                            if (specs1.getMinWeight() * 0.001 < specs && specs < specs1.getMaxWeight() * 0.001) {
                                postion = i;
                            }
                        }else {
                            //???kg???????????????????????????????????????????????????
                            if (specs1.getMinWeight() < specs && specs < specs1.getMaxWeight()) {
                                postion = i;
                            }
                        }
                    } else {
                        //????????????????????????g,??????????????????????????????kg?????????????????????g,???????????????????????????
                        if (specs1.getUnit().equals("kg")) {
                            //???kg??????????????????
                            if (specs1.getMinWeight() * 1000 < specs && specs < specs1.getMaxWeight() * 1000) {
                                postion = i;
                            }
                        }else {
                            //???g???????????????????????????????????????????????????
                            if (specs1.getMinWeight() < specs && specs < specs1.getMaxWeight()) {
                                postion = i;
                            }
                        }
                    }

                }
                //??????????????????????????????
                if (postion != 0) {
                    mSpSpecs.setSelection(postion,true);
                }else {
                    myToasty.showWarning("???????????????????????????????????????????????????????????????????????????????????????");
                }


                //??????
                singalWeight = DoubleCountUtils.keep(specs);
                if (unit ==1) {
                    specsNameList.set(0, specs + "kg");
                }else {
                    specsNameList.set(0, specs + "g");
                }

                specsAdapter.notifyDataSetChanged();

                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //????????????
        if (resultCode == Activity.RESULT_OK) {
            //mList = picOrMp4Adapter.getData();
            GetPicModel picOrMp4 = new GetPicModel();
            picOrMp4 = (GetPicModel) data.getSerializableExtra("result");
            Logger.d(picOrMp4.getMp4Path());
            mList.add(picOrMp4);
            Logger.d("-------------------->" + picOrMp4.getImagePath());
            picOrMp4Adapter.notifyDataSetChanged();

        }

    }

    /**
     * ??????
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
                        //?????????????????????????????????
                        //showToast("?????????????????????");
                    } else {
                        //?????????????????????????????????????????????????????????
                        //showToast("???????????????????????????????????????");
                    }
                });
    }


    /**
     * ????????????
     */
    private void uploadFiles(GetPicModel picOrMp4) {

        //????????????
        APIAttachfile apiAttachfile = RetrofitServiceManager.getInstance().create(APIAttachfile.class);
        if (!qmuiTipDialog.isShowing()) {
            qmuiTipDialog.show();
        }

        int type = picOrMp4.getType();//0??????  1??????
        switch (type) {
            case 0://??????
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
                        Logger.d("??????");
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
                                    Logger.d("?????????????????????" + b);
                                    pathList.add(path1);
                                    myToasty.showSuccess("???????????????");
                                } else {
                                    myToasty.showError("???????????????" + response.getMsg());
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                myToasty.showError("???????????????" + e.getMessage());
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
                        Logger.d("??????");
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
                                    Logger.d("?????????????????????" + b);
                                    pathList.add(path1);
                                    myToasty.showSuccess("???????????????");
                                } else {
                                    myToasty.showError("???????????????" + response.getMsg());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                myToasty.showError("???????????????" + e.getMessage());
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
