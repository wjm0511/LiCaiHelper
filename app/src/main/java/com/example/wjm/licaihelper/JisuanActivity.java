package com.example.wjm.licaihelper;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class JisuanActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private RadioGroup tab_bar;
    private RadioButton zfdk;
    private RadioButton tqhk;
    private RadioButton hqck;
    private RadioButton lczq;
    private RadioButton zclq;
    private RadioButton zczq;
    private Button back;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;
    public static final int PAGE_SIX = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jisuanqimain);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        zfdk.setChecked(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JisuanActivity.this.finish();
            }
        });

    }

    private void bindViews() {
        back = (Button) findViewById(R.id.back);
        tab_bar = (RadioGroup) findViewById(R.id.tab_bar);
        zfdk = (RadioButton) findViewById(R.id.zfdk);
        tqhk = (RadioButton) findViewById(R.id.tqhk);
        hqck = (RadioButton) findViewById(R.id.hqck);
        lczq = (RadioButton) findViewById(R.id.lczq);
        zclq = (RadioButton) findViewById(R.id.zclq);
        zczq = (RadioButton) findViewById(R.id.zczq);
        tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.zfdk:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.tqhk:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.hqck:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.lczq:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
            case R.id.zclq:
                vpager.setCurrentItem(PAGE_FIVE);
                break;
            case R.id.zczq:
                vpager.setCurrentItem(PAGE_SIX);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    zfdk.setChecked(true);
                    break;
                case PAGE_TWO:
                    tqhk.setChecked(true);
                    break;
                case PAGE_THREE:
                    hqck.setChecked(true);
                    break;
                case PAGE_FOUR:
                    lczq.setChecked(true);
                    break;
                case PAGE_FIVE:
                    zclq.setChecked(true);
                    break;
                case PAGE_SIX:
                    zczq.setChecked(true);
                    break;
            }
        }
    }
}