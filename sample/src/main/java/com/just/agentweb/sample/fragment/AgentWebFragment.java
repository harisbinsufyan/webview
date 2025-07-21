package com.just.agentweb.sample.fragment;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.gson.Gson;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.AgentWebUtils;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.filechooser.FileCompressor;
import com.just.agentweb.sample.R;
import com.just.agentweb.sample.app.App;
import com.just.agentweb.sample.client.MiddlewareChromeClient;
import com.just.agentweb.sample.client.MiddlewareWebViewClient;
import com.just.agentweb.sample.common.CommonWebChromeClient;
import com.just.agentweb.sample.common.UIController;
import com.just.agentweb.sample.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.PopupMenu;
import top.zibin.luban.Luban;

/**
 * Created by cenxiaozhong on 2017/5/15.
 * Source code: https://github.com/Justson/AgentWeb
 */

public class AgentWebFragment extends Fragment implements FileCompressor.FileCompressEngine, AgentWebFragment.FragmentKeyDown {

    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private TextView mTitleTextView;
    protected AgentWeb mAgentWeb;
    public static final String URL_KEY = "url_key";
    private ImageView mMoreImageView;
    private androidx.appcompat.widget.PopupMenu mPopupMenu;
    /**
     * For convenient printing and testing
     */
    public static final String TAG = AgentWebFragment.class.getSimpleName();
    private MiddlewareWebClientBase mMiddleWareWebClient;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;
    
    private androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.refresh:
                    if (mAgentWeb != null) {
                        mAgentWeb.getUrlLoader().reload();
                    }
                    return true;
                case R.id.copy:
                    if (mAgentWeb != null) {
                        toCopy(getContext(), mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.default_browser:
                    if (mAgentWeb != null) {
                        openBrowser(mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.error_website:
                    loadErrorWebSite();
                    return true;
                default:
                    return false;
            }
        }
    };

    public interface FragmentKeyDown {
        boolean onFragmentKeyDown(int keyCode, KeyEvent event);
    }

    public static AgentWebFragment getInstance(Bundle bundle) {

        AgentWebFragment mAgentWebFragment = new AgentWebFragment();
        if (bundle != null) {
            mAgentWebFragment.setArguments(bundle);
        }

        return mAgentWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agentweb, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)) // Pass in AgentWeb's parent control.
                .useDefaultIndicator(-1, 3) // Set progress bar color and height, -1 is default value, height is 2, unit is dp.
                .setAgentWebWebSettings(getSettings()) // Set IAgentWebSettings.
                .setWebViewClient(mWebViewClient) // WebViewClient, consistent with WebView usage, but please do not get WebView to call setWebViewClient(xx) method, it will override AgentWeb DefaultWebClient, and corresponding middleware will also fail.
                .setWebChromeClient(new CommonWebChromeClient()) // WebChromeClient