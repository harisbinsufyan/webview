package com.just.agentweb.sample.common;

import android.app.Activity;
import android.os.Build;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebListenerManager;

/**
 * Created by cenxiaozhong on 2017/5/26.
 * Source code: https://github.com/Justson/AgentWeb
 */
public class CustomSettings extends AbsAgentWebSettings {

    public CustomSettings(Activity activity) {
        super();
        this.mActivity = activity;
    }

    private AgentWeb mAgentWeb;
    private Activity mActivity;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }


    @Override
    public IAgentWebSettings toSetting(WebView webView) {
        super.toSetting(webView);

        getWebSettings().setBlockNetworkImage(false); // Whether to block loading network images, protocol http or https
        getWebSettings().setAllowFileAccess(false); // Allow loading local file html file protocol, this may cause insecurity, recommend rewriting to close
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWebSettings().setAllowFileAccessFromFileURLs(false); // Javascript loaded through file URL reads other local files. Recommend closing
            getWebSettings().setAllowUniversalAccessFromFileURLs(false); // Allow Javascript loaded through file URL to access other sources, including other files and http, https and other sources
        }
        getWebSettings().setNeedInitialFocus(true);
        getWebSettings().setDefaultTextEncodingName("gb2312"); // Set encoding format
        getWebSettings().setDefaultFontSize(16);
        getWebSettings().setMinimumFontSize(12); // Set the minimum font size supported by WebView, default is 8
        getWebSettings().setGeolocationEnabled(true);
        getWebSettings().setUserAgentString(getWebSettings().getUserAgentString().concat("agentweb/3.1.0"));
        return this;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        return super.setDownloader(webView,
                DefaultDownloadImpl.create(this.mActivity
                        , webView, mAgentWeb.getPermissionInterceptor()));
    }
}
