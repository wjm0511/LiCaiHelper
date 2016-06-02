package com.example.wjm.licaihelper.shezhi;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.example.wjm.licaihelper.R;

/**
 * Created by Yao on 2016/5/16.
 */
public class AboutActivity extends Activity {
    protected void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.about);
    }
}
