package kkkj.android.revgoods.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.util.SharedUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.adapter.BillAdapter;
import kkkj.android.revgoods.bean.Bill;
import kkkj.android.revgoods.bean.BillDetails;
import kkkj.android.revgoods.bean.Cumulative;
import kkkj.android.revgoods.bean.Deduction;
import kkkj.android.revgoods.bean.Matter;
import kkkj.android.revgoods.bean.MatterLevel;
import kkkj.android.revgoods.bean.Path;
import kkkj.android.revgoods.bean.SamplingBySpecs;
import kkkj.android.revgoods.bean.SamplingDetails;
import kkkj.android.revgoods.bean.Specs;
import kkkj.android.revgoods.bean.Student;
import kkkj.android.revgoods.bean.Supplier;
import kkkj.android.revgoods.bean.bill.BillMaster;
import kkkj.android.revgoods.bean.bill.DelWeights;
import kkkj.android.revgoods.bean.bill.PurPrices;
import kkkj.android.revgoods.bean.bill.PurSamples;
import kkkj.android.revgoods.bean.bill.Scales;
import kkkj.android.revgoods.customer.MyToasty;
import kkkj.android.revgoods.event.DeviceEvent;
import kkkj.android.revgoods.mvpInterface.MvpModel;
import kkkj.android.revgoods.ui.ShowBillDetailsActivity;
import kkkj.android.revgoods.ui.saveBill.BillModel;
import kkkj.android.revgoods.ui.saveBill.SaveBillDetailsActivity;
import kkkj.android.revgoods.utils.DoubleCountUtils;
import kkkj.android.revgoods.utils.ExcelUtils;
import kkkj.android.revgoods.utils.NetUtils;
import kkkj.android.revgoods.utils.ShareFile;

/**
 * ????????????
 */
public class BillListFragment extends BaseDialogFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BillAdapter billAdapter;
    private List<Bill> mBills;
    private QMUITipDialog mQMUITipDialog;

    private ArrayList<ArrayList<String>> recordList;
    private List<Student> students;
    private static String[] title = { "??????","??????","??????","??????","??????","??????","??????","??????" };
    private File file;
    private String fileName;

    @Override
    public void initData() {
        mBills = new ArrayList<>();
        mBills = LitePal.findAll(Bill.class, true);

        //??????????????????
        students = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            students.add(new Student("??????"+i,"???","12","1"+i,"??????","85","77","98"));
            students.add(new Student("??????"+i,"???","14","2"+i,"??????","65","57","100"));
        }

    }

    /**
     * ??????excel
     */
    public void exportExcel() {
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/Record");
        if (!file.exists()) {
           file.mkdir();
        }

        ExcelUtils.initExcel(Environment.getExternalStorageDirectory().getPath() + "/Record" + "/test.xls", title);
        fileName = Environment.getExternalStorageDirectory().getPath() + "/Record/test.xls";
        ExcelUtils.writeObjListToExcel(getRecordData(), fileName, getActivity());
    }

    /**
     * ??????????????? ?????????ArrayList<ArrayList<String>>
     * @return
     */
    private  ArrayList<ArrayList<String>> getRecordData() {
        recordList = new ArrayList<>();
        for (int i = 0; i <students.size(); i++) {
            Student student = students.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(student.id);
            beanList.add(student.name);
            beanList.add(student.sex);
            beanList.add(student.age);
            beanList.add(student.classNo);
            beanList.add(student.math);
            beanList.add(student.english);
            beanList.add(student.chinese);
            recordList.add(beanList);
        }
        return recordList;
    }

    @Override
    public void initView(View view) {
        tvTitle.setText(R.string.bill_list);

        mQMUITipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getResources().getString(R.string.is_uploading))
                .create();

        mRecyclerView = view.findViewById(R.id.id_device_recyclerView);
        billAdapter = new BillAdapter(R.layout.item_bill_list, mBills);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setAdapter(billAdapter);

        billAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ShowBillDetailsActivity.newInstance(getActivity(),mBills.get(position).getId());
                startActivity(intent);
            }
        });

        billAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //????????????
                //??????????????????
                switch (view.getId()) {
                    case R.id.tv_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("???????????????")
                                .setMessage("?????????????????????")
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        DeviceEvent deviceEvent = new DeviceEvent();
                                        deviceEvent.setResetUploadCount(true);
                                        EventBus.getDefault().post(deviceEvent);

                                        int id = mBills.get(position).getId();
                                        LitePal.delete(Bill.class, id);
                                        mBills.remove(position);
                                        billAdapter.notifyDataSetChanged();

                                        Logger.d("??????");
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Logger.d("??????");
                                    }
                                }).show();

                        break;

                    case R.id.tv_upload:

                        Bill bill = mBills.get(position);
                        BillModel.Request request = getBillRequest(bill);
                        if (!NetUtils.checkNetWork()) {
                            myToasty.showWarning("???????????????????????????????????????????????????");
                            return;
                        }

                        mQMUITipDialog.show();

                        MvpModel.apiApp.addBill(request)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<BillModel.Response>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(BillModel.Response response) {
                                        if (response.isData()) {
                                            myToasty.showSuccess("????????????!");
                                            bill.setIsUpload(0);
                                            bill.save();
                                            adapter.notifyDataSetChanged();
                                        }else {
                                            myToasty.showError("???????????????" + response.getMsg());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (mQMUITipDialog.isShowing()) {
                                            mQMUITipDialog.dismiss();
                                        }
                                        myToasty.showError("???????????????" + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        if (mQMUITipDialog.isShowing()) {
                                            mQMUITipDialog.dismiss();
                                        }
                                    }
                                });

                        break;

                    case R.id.tv_share://????????????
//                        exportExcel();
//                        String path = Environment.getExternalStorageDirectory().getPath() + "/Record/test.xls";
//                        File file = new File(path);
//                        ShareFile.shareFile(getActivity(),file);

                        break;

                    default:
                        break;

                }

            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_device_list;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //????????????
    private BillModel.Request getBillRequest(Bill bill) {

        BillModel.Request request = new BillModel.Request();
        //?????????
        double money = 0d;

        List<Deduction> deductionList = bill.getDeductionList();
        List<SamplingDetails> samplingDetailsList = bill.getSamplingDetailsList();
        List<Cumulative> cumulativeList = bill.getCumulativeList();
        List<BillDetails> billDetailsList = bill.getBillDetailsList();

        int supplierId = bill.getSupplierId();
        int matterId = bill.getMatterId();
        Supplier supplier = LitePal.find(Supplier.class,supplierId);
        Matter matter = LitePal.find(Matter.class,matterId);
        /**
         * ????????????
         * ValuationType = 1;??????????????????
         * ValuationType = 2;????????????????????????
         */
        int ValuationType = matter.getValuationType();

        //???????????????
        double mWeight = bill.getWeight();
        // ???????????????
        double deductionWeight = 0d;
        for (int i = 0; i < deductionList.size(); i++) {
            BigDecimal b1 = new BigDecimal(Double.toString(deductionWeight));
            BigDecimal b2 = new BigDecimal(deductionList.get(i).getWeight());
            deductionWeight = b1.add(b2).doubleValue();
        }
        //?????????
        BigDecimal b1 = new BigDecimal(Double.toString(mWeight));
        BigDecimal b2 = new BigDecimal(Double.toString(deductionWeight));
        mWeight = b1.subtract(b2).doubleValue();
        //????????????  :???????????????????????????????????????
        double realWeight = DoubleCountUtils.keep(mWeight * (100 - bill.getDeductionMix()) * 0.01);

        //???????????????????????????
        SamplingDetails maxSamplingDetails;
        if (samplingDetailsList.size() == 1) {
            maxSamplingDetails = samplingDetailsList.get(0);
        }else {
            //???????????????????????????
            maxSamplingDetails = Collections.max(samplingDetailsList, new Comparator<SamplingDetails>() {
                @Override
                public int compare(SamplingDetails samplingDetails, SamplingDetails t1) {
                    if (samplingDetails.getSpecsProportion() > t1.getSpecsProportion()) {
                        return 1;
                    } else if (samplingDetails.getSpecsProportion() == t1.getSpecsProportion()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        //?????????????????????
        Specs specs = LitePal.find(Specs.class,maxSamplingDetails.getSpecsId());

        /**   ????????????
         * NormsID : 4703a9fb-01f1-49c6-8989-9f10fa76b408 ??????ID
         * Amount : 2.0
         * Price : 3.0
         * Menoy : 4.0 ??????
         * Ratio : 5.0
         */
        List<PurPrices> purPricesList = new ArrayList<>();//????????????

        if (ValuationType == 1) {
            //????????????
            SamplingBySpecs samplingBySpecs = LitePal.find(SamplingBySpecs.class,bill.getSamplingBySpecsId());

            int id = samplingBySpecs.getSpecsId();
            Specs specsFinal = LitePal.find(Specs.class,id);

            PurPrices purPrices = new PurPrices();
            purPrices.setNormsID(specsFinal.getKeyID());//??????ID

            purPrices.setAmount(realWeight);//?????????????????????
            purPrices.setInitialPrice(samplingBySpecs.getPrice());//????????????
            purPrices.setRevisionPrice(samplingBySpecs.getAdjustPrice());//????????????
            purPrices.setPrice(samplingBySpecs.getFinalPrice());//????????????
            purPrices.setMenoy(DoubleCountUtils.keep(realWeight * samplingBySpecs.getPrice()));//??????
            purPrices.setRatio(100);//????????????
            purPricesList.add(purPrices);

        } else {

            //??????????????????

            for (int i = 0; i < billDetailsList.size(); i++) {

                BillDetails billDetails = billDetailsList.get(i);
                //????????????
                double finalPrice = Double.valueOf(billDetails.getFinalPrice());
                //????????????
                double weight = billDetails.getWeight();//?????????????????????
                //????????????
                double totalPrice = billDetails.getTotalPrice();

                double ratio = Double.valueOf(billDetails.getProportion()) * 100;//????????????

                PurPrices purPrices = new PurPrices();

                money = totalPrice + money;//?????????
                money = DoubleCountUtils.keep(money);

                purPrices.setNormsID(billDetails.getSpecsKeyId());//??????ID

                purPrices.setAmount(weight);//?????????????????????
                purPrices.setInitialPrice(Double.valueOf(billDetails.getPrice()));//????????????
                purPrices.setRevisionPrice(Double.valueOf(billDetails.getAdjustPrice()));//????????????
                purPrices.setPrice(finalPrice);//????????????
                purPrices.setMenoy(totalPrice);//??????
                purPrices.setRatio(ratio);//????????????
                purPricesList.add(purPrices);
            }

        }

        request.setPurPrices(purPricesList);//????????????

        /**
         * PurchaseDate : 2019-08-08 15:07:06
         * SupplierID : cd529f25-5fae-40a2-ac49-1df6627fe769
         * NormID : f1a26ea8-d60f-4874-a8b2-abc5d8387b4d
         * CategoryID : 4bdfa721-cc93-4588-b55e-270865614f6c
         * CategoryLv : 312387db-e6f9-4ae5-8715-a888c53184de
         * Price : 6.0
         * Amount : 7.0
         * Money : 8.0
         * Memo : sample string 9
         * "DelWeightRate": 10.0 //?????????
         */
        BillMaster billMasterBean = new BillMaster();

        billMasterBean.setCode(bill.getUUID());//Code
        billMasterBean.setPurchaseDate(bill.getTime());//??????
        billMasterBean.setSupplierID(supplier.getKeyID());//?????????ID
        billMasterBean.setNormID(specs.getKeyID());//??????ID
        billMasterBean.setCategoryID(matter.getKeyID());//??????ID

        MatterLevel matterLevel = LitePal.find(MatterLevel.class,maxSamplingDetails.getMatterLevelId());
        billMasterBean.setCategoryLv(matterLevel.getKeyID());//????????????

        billMasterBean.setPrice(DoubleCountUtils.keep(money / realWeight));//????????????
        billMasterBean.setAmount(realWeight);//?????????
        billMasterBean.setDelWeightRate(bill.getDeductionMix());

        billMasterBean.setMoney(money);//?????????

        request.setBillMaster(billMasterBean);

        /**
         * Weight : 1.0
         * DelWeightType : a641ee4c-e57c-40ef-bd62-fae0705a284a
         */
        List<DelWeights> delWeightsList = new ArrayList<>();//????????????
        for (int i = 0; i < deductionList.size(); i++) {
            DelWeights delWeights = new DelWeights();
            delWeights.setDelWeightType(deductionList.get(i).getKeyID());//????????????ID
            delWeights.setWeight(deductionList.get(i).getWeight());//??????
            delWeightsList.add(delWeights);
        }

        request.setDelWeights(delWeightsList);//????????????


        /**
         * Weigth : 1.0
         * Amount : 2.0
         * SingalWeight : 3.0
         * NormsID : b8b62085-43a5-488e-9cfa-659f4c73927e
         * Ratio : 5.0
         * Files : ["sample string 1","sample string 2"]
         */
        List<PurSamples> purSamplesList = new ArrayList<>();//????????????
        for (int i = 0; i < samplingDetailsList.size(); i++) {
            PurSamples purSamples = new PurSamples();
            double ratio = samplingDetailsList.get(i).getSpecsProportion() * 100;//????????????
            double amount = Double.valueOf(samplingDetailsList.get(i).getNumber());//?????????????????????

            purSamples.setWeigth(DoubleCountUtils.keep(Double.valueOf(samplingDetailsList.get(i).getWeight())));//?????????????????????
            purSamples.setAmount(DoubleCountUtils.keep(amount));//?????????????????????
            purSamples.setSingalWeight(samplingDetailsList.get(i).getSingalWeight());//??????????????????

            Specs specs1 = LitePal.find(Specs.class,samplingDetailsList.get(i).getSpecsId());
            purSamples.setNormsID(specs1.getKeyID());//??????ID

            purSamples.setRatio(ratio);//????????????

            int  samplingDetailsId = samplingDetailsList.get(i).getId();
            SamplingDetails samplingDetails = LitePal.find(SamplingDetails.class,samplingDetailsId,true);

            //????????????
            List<Path> pathList = samplingDetails.getPathList();
            List<String> stringList = new ArrayList<>();
            for (int j = 0; j < pathList.size(); j++) {
                stringList.add(pathList.get(j).getPath());
            }
            purSamples.setFiles(stringList);//????????????

            for (int j=0;j < stringList.size();j++) {
                Logger.d(stringList.get(j));
            }

            purSamplesList.add(purSamples);
        }
        request.setPurSamples(purSamplesList);//????????????

        /**
         * Weight : 1.0
         */
        List<Scales> scalesList = new ArrayList<>();
        for (int i = 0; i < cumulativeList.size(); i++) {
            Scales scales = new Scales();
            scales.setWeight(Double.valueOf(cumulativeList.get(i).getWeight()));
            scalesList.add(scales);
        }
        request.setScales(scalesList);//????????????

        return request;

    }


    //???????????????????????????
    private List<SamplingDetails> getSamplingDetails(List<SamplingDetails> samplingDetailsList){

        int size = samplingDetailsList.size();

        for (int i=0;i<size;i++) {

            SamplingDetails samplingDetails = samplingDetailsList.get(i);
            double proportion = samplingDetails.getSpecsProportion();

            for (int j = i+1;j<size;j++) {
                SamplingDetails samplingDetailsNext = samplingDetailsList.get(j);
                if (samplingDetails.getSpecsId() == samplingDetailsNext.getSpecsId()) {
                    proportion = DoubleCountUtils.keep4(proportion + samplingDetailsNext.getSpecsProportion());
                    samplingDetailsList.remove(samplingDetailsNext);
                    size = size - 1;
                }
            }
            samplingDetails.setSpecsProportion(proportion);

        }

        return samplingDetailsList;
    }

}
