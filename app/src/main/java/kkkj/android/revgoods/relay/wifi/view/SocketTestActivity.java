package kkkj.android.revgoods.relay.wifi.view;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import butterknife.BindView;
import io.reactivex.Observable;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.conn.socket.ModbusProtocol;
import kkkj.android.revgoods.conn.socket.WriteData;
import kkkj.android.revgoods.mvpInterface.MvpBaseActivity;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;
import kkkj.android.revgoods.relay.wifi.model.Order;

public class SocketTestActivity extends MvpBaseActivity implements View.OnClickListener {
    @BindView(R.id.connect)
    Button btn_connect;
    @BindView(R.id.send)
    Button btn_send;
    @BindView(R.id.ed_content)
    EditText ed_content;
    @BindView(R.id.tv_content)
    TextView tv_content;
    IConnectionManager manager;
    StringBuffer sb;
    Observable<String> readOb;
    Observable<String> writeOb;
    SocketTestListener listener;

    @Override
    protected int getLayout() {
        return R.layout.socket_test_activity;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initMonitorAndData() {
        action_bar_title.setText("socket测试");
        sb = new StringBuffer();
        btn_send.setOnClickListener(this);
        btn_connect.setOnClickListener(this);
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
        listener = new SocketTestListener(tv_content, manager);
        manager.registerReceiver(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        manager.
        if (manager.isConnect()) {
            manager.disconnect();
        }

    }

    @Override
    protected void onDestroy() {
        manager.unRegisterReceiver(listener);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect:
                //调用通道进行连接
                if (manager != null) {
                    manager.connect();
                }
                break;
            case R.id.send:
                if (manager.isConnect()) {
                    manager.send(new WriteData(Order.TURN_OFF_1));
                }
                break;
        }
    }
}
