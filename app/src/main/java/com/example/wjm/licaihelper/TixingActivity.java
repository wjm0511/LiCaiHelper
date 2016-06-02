package com.example.wjm.licaihelper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by wjm on 2016/5/10.
 */
public class TixingActivity extends Activity{
    private List<String[]> al=new ArrayList<String[]>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(R.layout.tixing);

        ListView tixingLv=(ListView)findViewById(R.id.txListView);
        al=DBUtil.getTiXing();
        setAdapter(tixingLv,al);
        Button returnButton=(Button)findViewById(R.id.fanhui);
        returnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(TixingActivity.this,LiCaiActivity.class);
                startActivity(intent);
            }
        });
        Button addButton=(Button)this.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(TixingActivity.this, addTixingActivity.class);
                startActivity(intent);
            }
        });
    }

    //按下back键
    public void onBackPressed(){
        Intent intent=new Intent();
        intent.setClass(TixingActivity.this,LiCaiActivity.class);
        startActivity(intent);
    }

    public void setAdapter(final ListView lv,final List<String[]> al){
        final BaseAdapter ba=new BaseAdapter(){
            @Override
            public int getCount() {
                return al.size();
            }
            @Override
            public Object getItem(int position) {
                return al.get(position);
            }
            @Override
            public long getItemId(int position) {
                return position;
            }
            @Override
            public View getView(final int position,View convertView, ViewGroup parent) {
                View view= LayoutInflater.from(TixingActivity.this).inflate(R.layout.tixinglist,null);
                TextView titleText=(TextView)view.findViewById(R.id.titleText);
                TextView dateText=(TextView)view.findViewById(R.id.dateText);
                TextView timeText=(TextView)view.findViewById(R.id.timeText);
                String[] str=new String[4];
                str[0]=al.get(position)[0];
                str[1]=al.get(position)[1];
                str[2]=al.get(position)[2];
                str[3]=al.get(position)[3];

                titleText.setText(str[0]);
                dateText.setText(str[1]);
                timeText.setText(str[2]+"   "+str[3]);
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(TixingActivity.this)
                                .setTitle("确认删除")
                                .setMessage("确定删除本条提醒？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String[] str = new String[4];
                                        str[0] = al.get(position)[0];
                                        str[1] = al.get(position)[1];
                                        str[2] = al.get(position)[2];
                                        str[3] = al.get(position)[3];
                                        DBUtil.deleteTixing(str[0], str[1], str[2], str[3], "tixingcontent");
                                        List<String[]> al_=new ArrayList<String[]>();
                                        al_=DBUtil.getTiXing();
                                        setAdapter(lv,al_);

                                    }
                                }).setNegativeButton("取消", null)
                                .create()
                                .show();
                        return true;
                    }
                });
                return view;
            }
        };
        lv.setAdapter(ba);
        ba.notifyDataSetChanged();
    }
}
