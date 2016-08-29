package com.example.wjm.licaihelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Wjm on 2016/5/12.
 */
public class WelcomeActivity extends Activity {
    private final long SPLASH_LENGTH = 1200;
    Handler handler = new Handler();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        handler.postDelayed(new Runnable() {  //使用handler的postDelayed实现延时跳转

            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LiCaiActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_LENGTH);//1.2秒后跳转至应用主界面MainActivity

    }
}
