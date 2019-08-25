package kkkj.android.revgoods.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.TbsVideo;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.PicOrMp4Adapter;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.common.getpic.GetPicModel;
import kkkj.android.revgoods.common.getpic.PhotoViewActivity;

/**
 *
 */
public class ShowSamplingPictureFragment extends BaseDialogFragment implements View.OnClickListener {

    private int id;
    private Button mButton;
    private RecyclerView mRecyclerView;
    private List<GetPicModel> mList;
    private PicOrMp4Adapter picOrMp4Adapter;

    public static ShowSamplingPictureFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        ShowSamplingPictureFragment fragment = new ShowSamplingPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = (int) getArguments().getSerializable("id");
    }

    public void initView(View view) {
        tvTitle.setText(R.string.sampling_details);

        mButton = view.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.id_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(picOrMp4Adapter);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_show_sampling_picture;
    }

    public void initData() {
        mList = new ArrayList<>();
        SamplingDetails samplingDetails = LitePal.find(SamplingDetails.class, id, true);
        mList = samplingDetails.getModelList();
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

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            String path = mList.get(position).getMp4Path();
                            File file = new File(path);
                            Uri uri;
                            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
                                uri = FileProvider.getUriForFile(getActivity(),"kkkj.android.revgoods.fileprovider",file);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }else {
                                uri = Uri.fromFile(file);
                            }
                            intent.setDataAndType(uri, "video/*");
                            startActivity(intent);


                            //用腾讯TBS播放视频
                            //判断当前是否可用
//                            if (TbsVideo.canUseTbsPlayer(getActivity().getApplicationContext())) {
//                                //播放视频
//                                TbsVideo.openVideo(getActivity().getApplicationContext(),
//                                        mList.get(position).getMp4Path());
//                            } else {
//                                Toast.makeText(getActivity(), "TBS视频播放器异常", Toast.LENGTH_LONG);
//                            }
                        }
                        break;
                    case R.id.iv_delete:

                        break;

                    case R.id.ed_content:

                        break;

                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                dismiss();
                break;

            default:
                break;
        }
    }
}
