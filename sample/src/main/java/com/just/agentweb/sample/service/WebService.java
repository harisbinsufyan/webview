package com.just.agentweb.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;

/**
 * @author xiaozhongcen
 * @date 20-8-18
 * @since 1.0.0
 * Pre-initialize process to reduce white screen
 */
public class WebService extends Service {

    private static final String TAG = WebService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Initialize process");
        try {
            new WebView(this.getApplicationContext());
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
