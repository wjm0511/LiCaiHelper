package com.example.wjm.licaihelper.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjm.licaihelper.R;

import java.util.Calendar;

/**
 * Created by Wjm on 2016/5/20.
 */
public class CalFragmentZCLQ extends Fragment {

    private Button savetime; //存款时间
    private EditText jine;   //存款金额
    private EditText lilv;   //存款利率
    private Button frequency;  //支取频度

    private Button zclqCal;

    private double durdate; //存款持续时间
    private int pindu;      //提取频度
    private float times;
    private double inputmoney;
    private double yearrate;

    public CalFragmentZCLQ(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_zclq_content,container,false);

        savetime=(Button)view.findViewById(R.id.oneyear);
        jine=(EditText)view.findViewById(R.id.jine);
        lilv=(EditText)view.findViewById(R.id.lilv);
        frequency=(Button)view.findViewById(R.id.everymonth);
        zclqCal=(Button)view.findViewById(R.id.zclqCal);


        savetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.item)//设置图标
                        .setTitle(R.string.savetime)//设置标题
                        .setItems(
                                R.array.sl_array,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //对话框关闭时的业务代码
                                        savetime.setText(getResources().getStringArray(R.array.sl_array)[which]);
                                    }
                                }
                        ).create().show();
            }
        });

        frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.item)//设置图标
                .setTitle(R.string.zqpd) //设置标题
                .setItems(
                        R.array.pd_array,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //对话框关闭时的业务代码
                                frequency.setText(getResources().getStringArray(R.array.pd_array)[which]);
                            }
                        }
                ).create().show();
            }
        });

        zclqCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Double.parseDouble(jine.getText().toString()))<0.1)
                {
                    Toast.makeText(getActivity(), "1000 元起存", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    goZCLQResult();
                }
            }
        });

        return view;
    }

    private void  goZCLQResult(){
        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("计算结果");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.zclqresult);

        Button b1=(Button)dialog.findViewById(R.id.Button01);
        Button b2=(Button)dialog.findViewById(R.id.Button03);
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        pindu=getPinDu(frequency.getText().toString().trim());
        durdate=getMonth(savetime.getText().toString().trim());

        times=(float)durdate/pindu;
        inputmoney=Double.parseDouble(jine.getText().toString())*10000;
        yearrate=(Double.parseDouble(lilv.getText().toString()))/100;
        double lixisum=(inputmoney+inputmoney/times)/2*times*pindu*(yearrate/12);
        double perzhiqu=inputmoney/times;
        String str1=String.valueOf(lixisum);
        String str2=String.valueOf(perzhiqu);
        b1.setText(str1);
        b2.setText(str2);

        dialog.create();
        dialog.show();
    }

    private static int getPinDu(String str){
        if(str.equals("每月"))
            return 1;
        else if(str.equals("每季"))
            return 3;
        else
            return 6;
    }

    private  static int getMonth(String str){
        if(str.equals("一年"))
            return 1*12;
        else if(str.equals("三年"))
            return 3*12;
        else
            return 5*12;
    }
}
