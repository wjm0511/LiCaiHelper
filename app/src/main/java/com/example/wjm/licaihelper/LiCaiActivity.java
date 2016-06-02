package com.example.wjm.licaihelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LiCaiActivity extends Activity implements View.OnClickListener {
    int list;

    final Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:
                    goJizhangView();
                    break;
                case 1:
                    goShouzhiView();
                    break;
                case 2:
                    goBaobiaoView();
                    break;
                case 3:
                    goYuyinView();
                    break;
                case 4:
                    goYusuanView();
                    break;
                case 5:
                    goZujiView();
                    break;
                case 6:
                    goJisuanView();
                    break;
                case 7:
                    goTixingView();
                    break;
                case 8:
                    goBianqianView();
                    break;
                case 9:
                    goShezhiView();
                    break;
                case 10:
                    goJianyiView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_li_cai);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //强制竖屏

        findViewById(R.id.jizhang).setOnClickListener(this);
        findViewById(R.id.shouzhi).setOnClickListener(this);
        findViewById(R.id.baobiao).setOnClickListener(this);
        findViewById(R.id.yuyin).setOnClickListener(this);
        findViewById(R.id.yusuan).setOnClickListener(this);
        findViewById(R.id.zuji).setOnClickListener(this);
        findViewById(R.id.jisuan).setOnClickListener(this);
        findViewById(R.id.tixing).setOnClickListener(this);
        findViewById(R.id.bianqian).setOnClickListener(this);
        findViewById(R.id.shezhi).setOnClickListener(this);
    }

    public void goJizhangView(){
        System.out.println("Jizhang--------");
        Intent intent=new Intent(LiCaiActivity.this,JizhangActivity.class);
        startActivity(intent);
    }

    public void goShouzhiView(){
        System.out.println("Shouzhi--------");
        Intent intent=new Intent(LiCaiActivity.this,ShouzhiActivity.class);
        startActivity(intent);
    }

    public void goBaobiaoView(){
        System.out.println("Baobiao--------");
        Intent intent=new Intent(LiCaiActivity.this,BaobiaoActivity.class);
        startActivity(intent);
    }

    public void goYuyinView(){
        System.out.println("Yuyin--------");
        Intent intent=new Intent(LiCaiActivity.this,YuyinActivity.class);
        startActivity(intent);
    }

    public void goYusuanView(){
        System.out.println("Yusuan--------");
        Intent intent=new Intent(LiCaiActivity.this,YusuanActivity.class);
        startActivity(intent);
    }

    public void goZujiView(){
        System.out.println("Zuji--------");
        Intent intent=new Intent(LiCaiActivity.this,ZujiActivity.class);
        startActivity(intent);
    }

    public void goJisuanView(){
        System.out.println("Jisuan--------");
        Intent intent=new Intent(LiCaiActivity.this,JisuanActivity.class);
        startActivity(intent);
    }

    public void goTixingView(){
        System.out.println("Tixing--------");
        Intent intent=new Intent(LiCaiActivity.this,TixingActivity.class);
        startActivity(intent);
    }

    public void goBianqianView(){
        System.out.println("Bianqian--------");
        Intent intent=new Intent(LiCaiActivity.this,BianqianActivity.class);
        startActivity(intent);
    }

    public void goShezhiView(){
        System.out.println("Shezhi--------");
        Intent intent=new Intent(LiCaiActivity.this,ShezhiActivity.class);
        startActivity(intent);
    }

    public void goJianyiView(){
        System.out.println("Jianyi--------");
        Intent intent=new Intent(LiCaiActivity.this,JianyiActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jizhang:
                handler.sendEmptyMessage(0);
                break;
            case R.id.shouzhi:
                handler.sendEmptyMessage(1);
                break;
            case R.id.baobiao:
                handler.sendEmptyMessage(2);
                break;
            case R.id.yuyin:
                handler.sendEmptyMessage(3);
                break;
            case R.id.yusuan:
                handler.sendEmptyMessage(4);
                break;
            case R.id.zuji:
                handler.sendEmptyMessage(5);
                break;
            case R.id.jisuan:
                handler.sendEmptyMessage(6);
                break;
            case R.id.tixing:
                handler.sendEmptyMessage(7);
                break;
            case R.id.bianqian:
                handler.sendEmptyMessage(8);
                break;
            case R.id.shezhi:
                handler.sendEmptyMessage(9);
                break;
        }
    }
}
