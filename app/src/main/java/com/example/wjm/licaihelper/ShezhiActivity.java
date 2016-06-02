package com.example.wjm.licaihelper;

import com.example.wjm.licaihelper.shezhi.*;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by wjm on 2016/5/10.
 */

public class ShezhiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //强制竖屏
        setContentView(R.layout.shezhi);

        //收入科目
        Button incomebutton=(Button)findViewById(R.id.incomeButton);
        incomebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShezhiActivity.this, ShouruKmActivity.class);
                startActivity(intent);
            }
        });

        //支出科目
        Button spendButton=(Button)this.findViewById(R.id.spendButton);
        spendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShezhiActivity.this, ZhichuKmActivity.class);
                startActivity(intent);
            }
        });

        //账户
        Button zhanghuButton=(Button)this.findViewById(R.id.zhanghuButton);
        zhanghuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShezhiActivity.this, ZhanghuActivity.class);
                startActivity(intent);
            }
        });

        //提醒设置
        Button tixingButton=(Button)this.findViewById(R.id.tixingButton);
        tixingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShezhiActivity.this, TixingShezhiActivity.class);
                startActivity(intent);
            }
        });

        //反馈建议
        Button jianyiButton=(Button)findViewById(R.id.jianyiButton);
        jianyiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShezhiActivity.this, JianyiActivity.class);
                startActivity(intent);
            }
        });

        //关于
        Button aboutButton=(Button)this.findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShezhiActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //返回
        Button returnbutton=(Button)this.findViewById(R.id.back);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShezhiActivity.this.finish();
            }
        });
    }
}
