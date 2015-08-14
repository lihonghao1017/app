package com.jin91.preciousmetal.ui.mine;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2015/5/1.
 * 我要开户里面的每一个item
 */
@SuppressLint("NewApi")
public class OpenAccountPage implements View.OnClickListener {

    private static final String APP_CACAHE_DIRNAME = "/webviewCacheChromium";
    private static final String TAG = "OpenAccountPage";

    @ViewInject(R.id.webView)
    WebView webView;
    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    private View view;

    private EmptyLayout emptyLayout;
    private String url; // 加载的url
    private Context mContext;

    private int isError;

    public OpenAccountPage(Context mContext, String url) {
        view = LayoutInflater.from(mContext).inflate(R.layout.open_account_page, null);
        ViewUtils.inject(this, view);
        this.url = url;
        this.mContext = mContext;
        clearWebViewCache();
        initView();
    }

    public void initView() {
        emptyLayout = new EmptyLayout(mContext, fl_price_content);
        emptyLayout.setErrorButtonClickListener(this);
        WebSettings settings = webView.getSettings();
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        webView.setScrollbarFadingEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        String cacheDirPath = mContext.getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
//        settings.setDatabasePath(cacheDirPath);
//        settings.setAppCachePath(cacheDirPath);
//        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);// 支持JavaScript
        settings.setJavaScriptCanOpenWindowsAutomatically(true);// JavaScript自动窗口执行
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.getSettings().setBlockNetworkImage(true);
        webView.loadUrl(url);
        Logger.i("url", "url-----" + url);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(new WebViewClient() {


            @Override
            public android.webkit.WebResourceResponse shouldInterceptRequest(WebView view, String url) {

//                sv.scrollTo(0, 0);
                return null;

            }

            ;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //调用拨号程序
                if (url.startsWith("tel:")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //当有新连接时，使用当前的 WebView
                    view.loadUrl(url);
                }
                return true;

            }

            ;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                emptyLayout.showLoading();
                if (isError == 1) {
                    isError++;
                } else {
                    isError = 0;
                }
//                System.out.println("onPageStarted---"+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.getSettings().setBlockNetworkImage(false);
                if (isError == 2) {
                    emptyLayout.showError();
                    fl_price_content.setVisibility(View.VISIBLE);
                } else {
                    emptyLayout.hideLoadinAnim();
                    fl_price_content.setVisibility(View.GONE);
                }
//                System.out.println("onPageFinished---"+url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                emptyLayout.showError();
                isError = 1;
//                System.out.println("onReceivedError---"+url);
            }

        });
    }

    public View getContentView() {
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error:
                webView.loadUrl(url);
                break;
        }
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            mContext.deleteDatabase("webview.db");
            mContext.deleteDatabase("webviewCache.db");
            mContext.deleteDatabase("webviewCookiesChromium.db");
            mContext.deleteDatabase("webviewCookiesChromiumPrivate.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(mContext.getCacheDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        Logger.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(mContext.getCacheDir().getAbsolutePath() + "/webviewCacheChromiumStaging");
        Logger.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Logger.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
               boolean flag =  file.delete();
                Logger.i(TAG, "flag" + flag);
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Logger.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

}
