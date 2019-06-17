package kkkj.android.revgoods.relay.wifi.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.IAction;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.conn.socket.Message;
import kkkj.android.revgoods.conn.socket.ModbusProtocol;
import kkkj.android.revgoods.conn.socket.PulseData;
import kkkj.android.revgoods.conn.socket.SocketListener;
import kkkj.android.revgoods.conn.socket.WriteData;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.relay.adapter.RelayAdapter;
import kkkj.android.revgoods.relay.bean.RelayBean;
import kkkj.android.revgoods.relay.wifi.model.Order;
import static com.xuhao.didi.core.iocore.interfaces.IOAction.ACTION_READ_COMPLETE;
import static com.xuhao.didi.socket.client.sdk.client.action.IAction.ACTION_READ_THREAD_SHUTDOWN;
import static kkkj.android.revgoods.utils.GetMacAddress.getMacFromArpCache;

/**
 * Wifi继电器
 */
public class WifiRelayActivity extends MvpBaseActivity implements View.OnClickListener {
    SocketListener listener;
    IConnectionManager manager;
    private PulseData mPulseData = new PulseData();
    @BindView(R.id.tv_menu1)
    TextView tv_menu1;
    @BindView(R.id.tv_menu2)
    TextView tv_menu2;
    @BindView(R.id.tv_menu3)
    TextView tv_menu3;
    @BindView(R.id.tv_menu4)
    TextView tv_menu4;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RelayAdapter adapter;
    List<RelayBean> mList;
    private CompositeDisposable compositeDisposable;


    @Override
    protected int getLayout() {
        return R.layout.activity_wifi_relay;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        action_bar_title.setText("WIFI继电器");
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        mList = new ArrayList<>();

        adapter = new RelayAdapter(R.layout.item_relay, mList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_menu1:
                        switch (position) { //position 哪个继电器
                            case 0:
                                manager.send(new WriteData(Order.TURN_ON_1));
                                break;
                            case 1:
                                manager.send(new WriteData(Order.TURN_ON_2));
                                break;
                            case 2:
                                manager.send(new WriteData(Order.TURN_ON_3));
                                break;
                            case 3:
                                manager.send(new WriteData(Order.TURN_ON_4));
                                break;
                            case 4:
                                manager.send(new WriteData(Order.TURN_ON_5));
                                break;
                            case 5:
                                manager.send(new WriteData(Order.TURN_ON_6));
                                break;
                            case 6:
                                manager.send(new WriteData(Order.TURN_ON_7));
                                break;
                            case 7:
                                manager.send(new WriteData(Order.TURN_ON_8));
                                break;
                        }
                        break;
                    case R.id.tv_menu2:
                        switch (position) {
                            case 0:
                                manager.send(new WriteData(Order.TURN_OFF_1));
                                break;
                            case 1:
                                manager.send(new WriteData(Order.TURN_OFF_2));
                                break;
                            case 2:
                                manager.send(new WriteData(Order.TURN_OFF_3));
                                break;
                            case 3:
                                manager.send(new WriteData(Order.TURN_OFF_4));
                                break;
                            case 4:
                                manager.send(new WriteData(Order.TURN_OFF_5));
                                break;
                            case 5:
                                manager.send(new WriteData(Order.TURN_OFF_6));
                                break;
                            case 6:
                                manager.send(new WriteData(Order.TURN_OFF_7));
                                break;
                            case 7:
                                manager.send(new WriteData(Order.TURN_OFF_8));
                                break;
                        }
                        break;
                }

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        for (int i = 0; i < 8; i++) {
            RelayBean relayBean = new RelayBean();
            relayBean.setName(i + 1 + "号继电器");
            mList.add(relayBean);
        }

        tv_menu1.setOnClickListener(this);
        tv_menu2.setOnClickListener(this);
        tv_menu3.setOnClickListener(this);
        tv_menu4.setOnClickListener(this);
        //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
        ConnectionInfo info = new ConnectionInfo("192.168.123.105", 10001);
        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        manager = OkSocket.open(info);
        //设置自定义解析头
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setPulseFrequency(10000);
        okOptionsBuilder.setReaderProtocol(new ModbusProtocol());
        //将新的修改后的参配设置给连接管理器
        manager.option(okOptionsBuilder.build());
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        listener = new SocketListener(new Observer<Message>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Message message) {
                Logger.d("Action:" + message.getAction());
                switch (message.getAction()) {
                    case IAction.ACTION_CONNECTION_FAILED:
                        showToast("连接失败");
                        break;

                    case IAction.ACTION_CONNECTION_SUCCESS://连接成功
                        showToast("连接成功");
                        Logger.d("mac地址："+getMacFromArpCache(manager.getRemoteConnectionInfo().getIp()));
                        manager.getPulseManager()
                                .setPulseSendable(mPulseData)//只需要设置一次,下一次可以直接调用pulse()
                                .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
                        tv_menu1.setVisibility(View.GONE);
                        tv_menu2.setVisibility(View.VISIBLE);
                        tv_menu3.setVisibility(View.VISIBLE);
                        tv_menu4.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        manager.send(new WriteData(Order.GET_STATE));
                        break;
                    case IAction.ACTION_DISCONNECTION:
                    case ACTION_READ_THREAD_SHUTDOWN://断开
                        tv_menu1.setVisibility(View.VISIBLE);
                        tv_menu2.setVisibility(View.GONE);
                        tv_menu3.setVisibility(View.GONE);
                        tv_menu4.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        break;
                    case ACTION_READ_COMPLETE:
                        if (manager != null && message.getData().trim().equals("00 02 01 00")) {
                            manager.getPulseManager().feed();
                            return;
                        }
                        if (message.getData().indexOf("01 01 02 ") == 0) {
                            //收到状态
                            String state = message.getData().substring("01 01 02 ".length(), "01 01 02 ".length() + 2);
                            String binaryState = hexString2binaryString(state);
                            char[] bin = binaryState.toCharArray();
                            if (bin.length == 8) {
                                for (int i = 0; i < bin.length; i++) {
                                    mList.get(i).setState(bin[i] + "");
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else if (message.getData().indexOf("01 05 00 ") == 0) {
                            //收到状态
                            //第几个继电器
                            String index = message.getData().substring("01 05 00 ".length(), "01 05 00 ".length() + 2);
                            //继电器状态
                            String state = message.getData().substring("01 05 00 ".length() + 3, "01 05 00 ".length() + 5);
                            Logger.d("---"+index+"---"+state+"---");
                            switch (index) {
                                case "00":
                                    if (!state.equals("00")) {
                                        mList.get(0).setState("1");
                                    } else {
                                        mList.get(0).setState("0");
                                    }
                                    break;
                                case "01":
                                    if (!state.equals("00")) {
                                        mList.get(1).setState("1");
                                    } else {
                                        mList.get(1).setState("0");
                                    }
                                    break;
                                case "02":
                                    if (!state.equals("00")) {
                                        mList.get(2).setState("1");
                                    } else {
                                        mList.get(2).setState("0");
                                    }
                                    break;
                                case "03":
                                    if (!state.equals("00")) {
                                        mList.get(3).setState("1");
                                    } else {
                                        mList.get(3).setState("0");
                                    }
                                    break;
                                case "04":
                                    if (!state.equals("00")) {
                                        mList.get(4).setState("1");
                                    } else {
                                        mList.get(4).setState("0");
                                    }
                                    break;
                                case "05":
                                    if (!state.equals("00")) {
                                        mList.get(5).setState("1");
                                    } else {
                                        mList.get(5).setState("0");
                                    }
                                    break;
                                case "06":
                                    if (!state.equals("00")) {
                                        mList.get(6).setState("1");
                                    } else {
                                        mList.get(6).setState("0");
                                    }
                                    break;
                                case "07":
                                    if (!state.equals("00")) {
                                        mList.get(7).setState("1");
                                    } else {
                                        mList.get(7).setState("0");
                                    }
                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        Logger.d("收到:" + message.getData());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                if (manager != null) {
                    showToast(e.getMessage());
                    tv_menu1.setVisibility(View.VISIBLE);
                    tv_menu2.setVisibility(View.GONE);
                    tv_menu3.setVisibility(View.GONE);
                    tv_menu4.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onComplete() {

            }
        });
        manager.registerReceiver(listener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (manager.isConnect()) {
            manager.disconnect();
        }

    }

    @Override
    protected void onDestroy() {
        manager.unRegisterReceiver(listener);
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_menu1:
                //调用通道进行连接
                if (manager != null) {
                    manager.connect();
                }
                break;
            case R.id.tv_menu2:
                //断开连接
                if (manager != null) {
                    if (manager.isConnect()) {
                        manager.disconnect();
                        tv_menu1.setVisibility(View.VISIBLE);
                        tv_menu2.setVisibility(View.GONE);
                        tv_menu3.setVisibility(View.GONE);
                        tv_menu4.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.tv_menu3:
                if (manager.isConnect()) {
                    manager.send(new WriteData(Order.TURN_ON_ALL));
                }
                break;
            case R.id.tv_menu4:
                if (manager.isConnect()) {
                    manager.send(new WriteData(Order.TURN_OFF_ALL));
                }
                break;
        }
    }

    //查表法，将16进制转为2进制
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "";
        String tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
}
