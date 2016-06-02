package com.example.wjm.licaihelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yao on 2016/5/23.
 */
public class DEBJDetailActivity extends Activity {

    private List<String[]> datailList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail);
        Button back = (Button) findViewById(R.id.fanhui);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DEBJDetailActivity.this.finish();
                    }
                }
        );
        ListView datailLv = (ListView) findViewById(R.id.ListView01);
        datailList = DBUtil.getDetail("dengebenjindetail");
        if(datailList.size()!=0){
            setListAdapter(datailLv, datailList);
        }else
            Toast.makeText(DEBJDetailActivity.this,"木有数据的啦！",Toast.LENGTH_SHORT).show();

    }

    private  void setListAdapter(final ListView lv,final List<String[]> bl){
        BaseAdapter ba=new BaseAdapter(){
            @Override
            public int getCount() {
                return bl.size();
            }
            @Override
            public Object getItem(int position) {
                return null;
            }
            @Override
            public long getItemId(int position) {
                return 0;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view= LayoutInflater.from(DEBJDetailActivity.this).inflate(R.layout.detaillsit1,null);
                TextView tv1=(TextView)view.findViewById(R.id.tv1);
                TextView tv2=(TextView)view.findViewById(R.id.tv2);
                TextView tv3=(TextView)view.findViewById(R.id.tv3);
                TextView tv4=(TextView)view.findViewById(R.id.tv4);
                TextView tv5=(TextView)view.findViewById(R.id.tv5);

                tv1.setText(bl.get(position)[0]);
                tv2.setText(bl.get(position)[1]);
                tv3.setText(bl.get(position)[2]);
                tv4.setText(bl.get(position)[3]);
                tv5.setText(bl.get(position)[4]);
                return view;
            }
        };
        lv.setAdapter(ba);
    }
}

