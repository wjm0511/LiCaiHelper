package com.example.wjm.licaihelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.wjm.licaihelper.Constant.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wjm on 2016/5/10.
 */
public class JizhangActivity extends Activity{
    private String subject;
    private String mode;
    private String date;
    private double amount;
    private String note;
    private String place;
    private String szStr; //日常收入，日常支出

    private EditText amountEdit;
    private EditText noteEdit;
    private TextView dateEdit;
    private EditText placeEdit;
    private Spinner s;

    Calendar calendar=null;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.jizhang);

        amountEdit=(EditText)findViewById(R.id.jineEdit);
        dateEdit=(TextView)findViewById(R.id.riqiEdit);
        placeEdit=(EditText)findViewById(R.id.didianEdit);
        noteEdit=(EditText)findViewById(R.id.beizhuEdit);

        //获得从语音记账那里得来的数据
        Intent intent=this.getIntent();
        final String inote=intent.getStringExtra("content");
        noteEdit.setText(inote);

        //日常收入、日常支出
        Spinner shouzhiSpinner=(Spinner)findViewById(R.id.shouzhiSpinner);
        setSpinnerAdapter(shouzhiSpinner, shouzhiIds.length, shouzhiIds);
        shouzhiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll = (LinearLayout) view;
                TextView tv = (TextView) ll.getChildAt(0);
                szStr = tv.getText().toString();
                if (szStr.equals("日常收入")) {
                    Spinner kemuSpinner = (Spinner) findViewById(R.id.kemuSpinner);
                    setSpinnerAdapter(kemuSpinner, shouruSubjects.length, shouruSubjects);
                } else {
                    Spinner kemuSpinner = (Spinner) findViewById(R.id.kemuSpinner);
                    setSpinnerAdapter(kemuSpinner, zhichuSubjects.length, zhichuSubjects);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //科目
        Spinner kemuSp=(Spinner)this.findViewById(R.id.kemuSpinner);
        kemuSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                LinearLayout ll=(LinearLayout)view;
                TextView tvn=(TextView)ll.getChildAt(0);
                subject=tvn.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Spinner fangshiSpinner=(Spinner)findViewById(R.id.fangshiSpinner);
        setSpinnerAdapter(fangshiSpinner, fangshi.length, fangshi);
        fangshiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll = (LinearLayout) view;
                TextView tv = (TextView) ll.getChildAt(0);
                mode = tv.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //返回
        Button returnBtn=(Button)findViewById(R.id.fanhui);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JizhangActivity.this.finish();
            }
        });
        //日期
        SimpleDateFormat sdf=new SimpleDateFormat(" yyyy-MM-dd ");
        final String dateStr=sdf.format(new Date()).trim();
        dateEdit.setText(dateStr);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(DATE_DIALOG).show();  //弹出系统日期对话框
            }
        });
        //保存
        Button saveBtn=(Button)findViewById(R.id.savebtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iamount=amountEdit.getText().toString().trim();
                amount=Double.parseDouble(iamount);
                date=dateEdit.getText().toString().trim();
                place=placeEdit.getText().toString();
                note=noteEdit.getText().toString();
                if(iamount.equals(""))
                    Toast.makeText(JizhangActivity.this,"请输入金额",Toast.LENGTH_SHORT).show();
                else{
                    if(szStr.equals("日常收入"))
                        DBUtil.insertZhangWu("shouru",subject,date,mode,amount,place,note);
                    else
                        DBUtil.insertZhangWu("zhichu",subject,date,mode,amount,place,note);
                }
                Toast.makeText(JizhangActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
        //再记一笔
        Button againBtn=(Button)findViewById(R.id.againbtn);
        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iamount=amountEdit.getText().toString().trim();
                amount=Double.parseDouble(iamount);
                date=dateEdit.getText().toString();
                place=placeEdit.getText().toString();
                note=noteEdit.getText().toString();
                if(iamount.equals(""))
                    Toast.makeText(JizhangActivity.this,"请输入金额",Toast.LENGTH_SHORT).show();
                else{
                    if(szStr.equals("日常收入"))
                        DBUtil.insertZhangWu("shouru",subject,date,mode,amount,place,note);
                    else
                        DBUtil.insertZhangWu("zhichu",subject,date,mode,amount,place,note);
                }
                Toast.makeText(JizhangActivity.this,"保存成功,请继续添加",Toast.LENGTH_SHORT).show();
                amountEdit.setText("");
                placeEdit.setText("");
                noteEdit.setText("");
            }
        });
    }

    private Dialog createDialog(int id){
        Dialog dialog=null;
        switch (id){
            case DATE_DIALOG:
                calendar=Calendar.getInstance();
                dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateEdit=(TextView)findViewById(R.id.riqiEdit);
                        dateEdit.setTextSize(18);
                        dateEdit.setText(" "+year+"-"+((monthOfYear+1)<10?"0"+(monthOfYear+1):""+
                                (monthOfYear+1))+"-"+(dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth));
                    }
                },
                calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                break;
        }
        return dialog;
    }


    //Spinner适配器,从数组读入
    private void setSpinnerAdapter(Spinner spinner, final int length, final String[] arr) {
        BaseAdapter baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return length;
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
                LinearLayout ll=new LinearLayout(JizhangActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv=new TextView(JizhangActivity.this);
                tv.setText(arr[position]);
                tv.setTextSize(18);
                tv.setPadding(10,5,10,5);
                ll.addView(tv);
                return ll;
            }
        };
        spinner.setAdapter(baseAdapter);
    }
    //Spinner适配器，从数据库表中读入
    public void setSpinner(Spinner spinner, final String tableName){
        BaseAdapter baseAdapter=new BaseAdapter() {
            List<String> result=DBUtil.getSubjects(tableName);
            @Override
            public int getCount() {
                return result.size();
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
                LinearLayout ll=new LinearLayout(JizhangActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv=new TextView(JizhangActivity.this);
                tv.setText(result.get(position));
                result.get(position);
                tv.setTextSize(18);
                tv.setPadding(10,5,10,5);
                ll.addView(tv);
                return ll;
            }
        };
        spinner.setAdapter(baseAdapter);
    }

}
