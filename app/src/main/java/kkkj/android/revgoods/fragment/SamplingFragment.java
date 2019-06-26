package kkkj.android.revgoods.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.sdk.TbsVideo;
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.PicOrMp4Adapter;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.GetPicOrMP4Activity;
import kkkj.android.revgoods.common.getpic.PhotoViewActivity;
import kkkj.android.revgoods.event.DeviceEvent;

/**
 * 采样
 */

public class SamplingFragment extends DialogFragment implements View.OnClickListener {

    private Button mSaveButton;
    private EditText mEtNumber;
    private EditText mEtWeight;
    private EditText mEtSpecs;
    private ImageView mBackImageView;
    private ImageView mTakePictureImageView;
    private RecyclerView recyclerView;
    private ArrayAdapter adapter;
    private String weight;
    private List<GetPicModel> mList;
    private PicOrMp4Adapter picOrMp4Adapter;


    public static SamplingFragment newInstance(String weight) {
        Bundle args = new Bundle();
        args.putString("weight", weight);

        SamplingFragment fragment = new SamplingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weight = (String) getArguments().getSerializable("weight");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sampling, container, false);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initData();
        initView(view);
        return view;

    }


    private void initData() {

        mList = new ArrayList<>();

        picOrMp4Adapter = new PicOrMp4Adapter(R.layout.item_picormp4, mList);
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
                                Toast.makeText(getActivity(), "TBS视频播放器异常", Toast.LENGTH_LONG);
                            }
                        }
                        break;
                    case R.id.iv_delete:
                        mList.remove(position);
                        picOrMp4Adapter.notifyDataSetChanged();
                        break;

                    case R.id.ed_content:

                        break;

                    default:
                        break;
                }
            }
        });

    }


    private void initView(View view) {
        mEtNumber = view.findViewById(R.id.id_et_number);
        mEtWeight = view.findViewById(R.id.id_et_weight);
        mEtSpecs = view.findViewById(R.id.id_et_specs);
        mSaveButton = view.findViewById(R.id.button);

        mEtWeight.setText(weight);

        mBackImageView = view.findViewById(R.id.iv_sampling_back);
        mTakePictureImageView = view.findViewById(R.id.id_iv_sampling_takePicture);
        mBackImageView.setOnClickListener(this);
        mTakePictureImageView.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.id_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(picOrMp4Adapter);


    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_sampling);
        dialog.setCanceledOnTouchOutside(true);

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;

        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        wlp.width = (2 * width) / 3;
        wlp.height = (3 * height) / 4;
        // wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        return dialog;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sampling_back:
                dismiss();
                break;

            case R.id.id_iv_sampling_takePicture:
                takePicture();
                break;

            case R.id.button:
                if (!TextUtils.isEmpty(mEtWeight.getText().toString().trim())
                        && !TextUtils.isEmpty(mEtNumber.getText().toString().trim())
                        && !TextUtils.isEmpty(mEtSpecs.getText().toString().trim())) { //不能为空

                    if (Double.parseDouble(mEtWeight.getText().toString().trim()) != 0 && Integer.parseInt(mEtNumber.getText().toString().trim()) != 0) { //不能为零
                        LitePal.saveAll(mList);

                        SamplingDetails samplingDetails = new SamplingDetails();
                        samplingDetails.setWeight(mEtWeight.getText().toString().trim());
                        samplingDetails.setNumber(mEtNumber.getText().toString().trim());
                        Specs specs = new Specs();
                        specs.setName(mEtSpecs.getText().toString().trim());
                        specs.save();
                        samplingDetails.setSpecs(specs);
                        for (int i = 0;i<mList.size();i++) {
                            samplingDetails.getModelList().add(mList.get(i));
                        }

                        DeviceEvent deviceEvent = new DeviceEvent();

                        if (!LitePal.isExist(SamplingDetails.class)) {
                            samplingDetails.setCount(1);
                            deviceEvent.setSamplingNumber(1);
                        } else {
                            SamplingDetails lastDetails = LitePal.findLast(SamplingDetails.class);
                            samplingDetails.setCount(lastDetails.getCount() + 1);
                            deviceEvent.setSamplingNumber(lastDetails.getCount() + 1);
                        }

                        EventBus.getDefault().post(deviceEvent);
                        samplingDetails.save();
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), "输入框不能为零！请重新输入！", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "输入框不能为空！", Toast.LENGTH_LONG).show();
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
            mList = picOrMp4Adapter.getData();
            GetPicModel picOrMp4 = new GetPicModel();
            picOrMp4 = (GetPicModel) data.getSerializableExtra("result");
            Logger.d(picOrMp4.getMp4Path());
            mList.add(picOrMp4);
            picOrMp4Adapter.notifyDataSetChanged();
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

}
