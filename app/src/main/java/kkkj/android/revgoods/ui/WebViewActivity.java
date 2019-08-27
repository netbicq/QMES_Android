package kkkj.android.revgoods.ui;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import kkkj.android.revgoods.R;
import kkkj.android.revgoods.mvpInterface.MvpPresenter;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.bar_container)
    RelativeLayout barContainer;
    @BindView(R.id.id_pb)
    ProgressBar mProgressbar;
    @BindView(R.id.id_web_view)
    WebView mWebView;

    private WebSettings webSettings;
    private String url = "";
    private static final int PROGRESS_MAX = 100;

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        tvTitle.setText("帮助文档");
        ivBack.setOnClickListener(this);

        mWebView.canGoBack();
        webSettings = mWebView.getSettings();

        //支持与JS交互的接口
        mWebView.addJavascriptInterface(WebViewActivity.this, "android");

        //设置自适应屏幕，两者合用
        //webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        //webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setJavaScriptEnabled(true);//让webView支持JS
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存


        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == PROGRESS_MAX) {
                    mProgressbar.setVisibility(View.GONE);
                } else {
                    mProgressbar.setVisibility(View.VISIBLE);
                    mProgressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }});


        mWebView.setWebViewClient(new WebViewClient() {
            //该界面打开更多链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);

                String title = webView.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    tvTitle.setText(title);
                }

            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                /**
                 * i:errorCode
                 * s:description
                 * s1:failingUrl
                 */
                Logger.d("errorCode" + i);
            }

        });

        mWebView.loadUrl(url);
    }

    @Override
    protected void initData() {
        url = " https://www.showdoc.cc/476646167832007?page_id=2788800705764101";

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.id_iv_back) {

            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }else {
                finish();
            }

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        webSettings.setLightTouchEnabled(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        webSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
