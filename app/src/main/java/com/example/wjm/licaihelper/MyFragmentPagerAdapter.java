package com.example.wjm.licaihelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.wjm.licaihelper.fragment.CalFragmentHQCK;
import com.example.wjm.licaihelper.fragment.CalFragmentLCZQ;
import com.example.wjm.licaihelper.fragment.CalFragmentTQHK;
import com.example.wjm.licaihelper.fragment.CalFragmentZCLQ;
import com.example.wjm.licaihelper.fragment.CalFragmentZCZQ;
import com.example.wjm.licaihelper.fragment.CalFragmentZFDK;

/**
 * Created by Yao on 2016/5/20.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 6;
    private CalFragmentZFDK fragmentZFDK = null;
    private CalFragmentTQHK fragmentTQHK = null;
    private CalFragmentHQCK fragmentHQCK = null;
    private CalFragmentLCZQ fragmentLCZQ = null;
    private CalFragmentZCZQ fragmentZCZQ = null;
    private CalFragmentZCLQ fragmentZCLQ = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentZFDK = new CalFragmentZFDK();
        fragmentTQHK = new CalFragmentTQHK();
        fragmentHQCK = new CalFragmentHQCK();
        fragmentLCZQ = new CalFragmentLCZQ();
        fragmentZCZQ=new CalFragmentZCZQ();
        fragmentZCLQ=new CalFragmentZCLQ();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case JisuanActivity.PAGE_ONE:
                fragment = fragmentZFDK;
                break;
            case JisuanActivity.PAGE_TWO:
                fragment = fragmentTQHK;
                break;
            case JisuanActivity.PAGE_THREE:
                fragment = fragmentHQCK;
                break;
            case JisuanActivity.PAGE_FOUR:
                fragment = fragmentLCZQ;
                break;
            case JisuanActivity.PAGE_FIVE:
                fragment = fragmentZCZQ;
                break;
            case JisuanActivity.PAGE_SIX:
                fragment = fragmentZCLQ;
                break;
        }
        return fragment;
    }
}
