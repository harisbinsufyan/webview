/*
 * Copyright (C)  Justson(https://github.com/Justson/AgentWeb)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.just.agentweb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author cenxiaozhong
 * @since 1.0.0
 */
public final class AgentWebUtils {

    private static final String TAG = AgentWebUtils.class.getSimpleName();

    private AgentWebUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runInUiThread(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (isUIThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean checkNetwork(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            return info != null && info.isConnected();
        } catch (Exception e) {
            LogUtils.e(TAG, "Error checking network", e);
            return false;
        }
    }

    public static int checkNetworkType(Context context) {
        if (context == null) {
            return 0;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return 0;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    return 1;
                } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return 2;
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Error checking network type", e);
        }
        return 0;
    }

    public static void clearWebViewAllCache(Context context, WebView webView) {
        try {
            if (webView != null) {
                webView.clearHistory();
                webView.clearCache(true);
                webView.clearFormData();
                webView.clearSslPreferences();
            }
            if (context != null) {
                AgentWebConfig.clearDiskCache(context);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Error clearing WebView cache", e);
        }
    }

    public static void clearWebViewAllCache(Context context) {
        clearWebViewAllCache(context, null);
    }

    public static void clearWebView(WebView webView) {
        if (webView == null) {
            return;
        }
        try {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroyDrawingCache();
        } catch (Exception e) {
            LogUtils.e(TAG, "Error clearing WebView", e);
        }
    }

    public static void clearCacheFolder(File dir, long numDays) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        try {
            File[] files = dir.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    clearCacheFolder(file, numDays);
                } else {
                    if (System.currentTimeMillis() - file.lastModified() > numDays * 24 * 60 * 60 * 1000) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Error clearing cache folder", e);
        }
    }

    public static String getAgentWebFilePath(Context context) {
        if (context == null) {
            return null;
        }
        try {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                return externalCacheDir.getAbsolutePath() + File.separator + AgentWebConfig.FILE_CACHE_PATH;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Error getting file path", e);
        }
        return null;
    }

    public static boolean hasPermission(@NonNull Activity activity, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermission(@NonNull Activity activity, @NonNull List<String> permissions) {
        return hasPermission(activity, permissions.toArray(new String[0]));
    }

    public static List<String> getDeniedPermissions(@NonNull Activity activity, @NonNull String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return deniedPermissions;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    public static AbsAgentWebUIController getAgentWebUIControllerByWebView(WebView webView) {
        if (webView == null) {
            return null;
        }
        try {
            if (webView.getParent() instanceof WebParentLayout) {
                WebParentLayout webParentLayout = (WebParentLayout) webView.getParent();
                return webParentLayout.provide();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Error getting UI controller", e);
        }
        return null;
    }

    public static boolean isJson(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }
        try {
            new JSONObject(target);
            return true;
        } catch (JSONException e) {
            try {
                new JSONArray(target);
                return true;
            } catch (JSONException e1) {
                return false;
            }
        }
    }

    public static Method isExistMethod(Object o, String method, Class<?>... clazzs) {
        if (o == null) {
            return null;
        }
        try {
            Class<?> clazz = o.getClass();
            return clazz.getMethod(method, clazzs);
        } catch (Exception e) {
            return null;
        }
    }

    public static File createImageFile(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (storageDir == null) {
                storageDir = new File(activity.getCacheDir(), "images");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
            }
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            LogUtils.e(TAG, "Error creating image file", e);
            return null;
        }
    }

    public static File createVideoFile(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String videoFileName = "MP4_" + timeStamp + "_";
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (storageDir == null) {
                storageDir = new File(activity.getCacheDir(), "videos");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
            }
            return File.createTempFile(videoFileName, ".mp4", storageDir);
        } catch (IOException e) {
            LogUtils.e(TAG, "Error creating video file", e);
            return null;
        }
    }

    public static Intent getIntentCaptureCompat(Activity activity, File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (file != null) {
            Uri photoURI = getUriFromFile(activity, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }
        return intent;
    }

    public static Intent getIntentVideoCompat(Activity activity, File file) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (file != null) {
            Uri videoURI = getUriFromFile(activity, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
        }
        return intent;
    }

    private static Uri getUriFromFile(Activity activity, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(activity, activity.getPackageName() + ".AgentWebFileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static String[] uriToPath(Activity activity, Uri[] uris) {
        if (activity == null || uris == null || uris.length == 0) {
            return null;
        }
        String[] paths = new String[uris.length];
        for (int i = 0; i < uris.length; i++) {
            paths[i] = uriToPath(activity, uris[i]);
        }
        return paths;
    }

    public static String uriToPath(Activity activity, Uri uri) {
        if (activity == null || uri == null) {
            return null;
        }
        // For file:// URIs
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        // For content:// URIs, return the URI string as path
        return uri.toString();
    }

    public static boolean showFileChooserCompat(Activity activity,
                                                WebView webView,
                                                ValueCallback<Uri[]> valueCallbacks,
                                                WebChromeClient.FileChooserParams fileChooserParams,
                                                PermissionInterceptor permissionInterceptor,
                                                ValueCallback<Uri> valueCallback,
                                                String acceptType,
                                                Handler.Callback jsChannelCallback) {
        try {
            com.just.agentweb.filechooser.FileChooser.Builder builder = 
                com.just.agentweb.filechooser.FileChooser.newBuilder(activity, webView)
                    .setPermissionInterceptor(permissionInterceptor);

            if (valueCallbacks != null) {
                builder.setUriValueCallbacks(valueCallbacks);
            }
            if (valueCallback != null) {
                builder.setUriValueCallback(valueCallback);
            }
            if (fileChooserParams != null) {
                builder.setFileChooserParams(fileChooserParams);
            }
            if (!TextUtils.isEmpty(acceptType)) {
                builder.setAcceptType(acceptType);
            }
            if (jsChannelCallback != null) {
                builder.setJsChannelCallback(jsChannelCallback);
            }

            builder.build().openFileChooser();
            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "Error showing file chooser", e);
            return false;
        }
    }

    public static void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LogUtils.e(TAG, "Error closing IO", e);
            }
        }
    }
}