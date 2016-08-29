package com.example.wjm.licaihelper.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import java.util.Date;

/**
 * Created by Yao on 2016/5/20.
 */
public class CalFragmentLCZQ extends Fragment {

    private Button savetime; //储蓄存期
    private EditText jine; //每月存入金额
    private EditText lilv; //利率

    private Button lczqCal;

    private int durdate; //存款时间
    private double inputmoney; //存入金额
    private double yearrate,monthrate;//年、月利率

    public CalFragmentLCZQ() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_lczq_content,container,false);

        savetime=(Button)view.findViewById(R.id.year);
        jine=(EditText)view.findViewById(R.id.jine);
        lilv=(EditText)view.findViewById(R.id.lilv);
        lczqCal=(Button)view.findViewById(R.id.lczqCal);

        savetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.item)//设置图标
                .setTitle(R.string.savetime)//设置标题
                .setItems(
                        R.array.sl_array,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //对话框关闭时的业务代码
                                savetime.setText(getResources().getStringArray(R.array.sl_array)[which]);
                            }
                        }
                ).create().show();
            }
        });

        lczqCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Double.parseDouble(jine.getText().toString()))*10000<5)
                {
                    Toast.makeText(getActivity(), "5元起存", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    goLCZQResult();
                }
            }
        });
        return view;
    }

    private void goLCZQResult(){
        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("计算结果");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.lczqresult);
        Button b1=(Button)dialog.findViewById(R.id.Button01);
        Button b2=(Button)dialog.findViewById(R.id.Button03);

        inputmoney=Double.parseDouble(jine.getText().toString())*10000;
        yearrate=(Double.parseDouble(lilv.getText().toString()))/100;
        monthrate=yearrate/12;

        String temp=savetime.getText().toString().trim();
        durdate=parseToMonth(temp);

        System.out.println(temp);
        System.out.println(durdate);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        double inputsum=inputmoney*durdate;
        double benxi=inputsum+inputmoney*((durdate+1)/2*durdate)*monthrate;
        String str1=String.valueOf(inputsum);
        String str2=String.valueOf(benxi);
        b1.setText(str1);
        b2.setText(str2);
        dialog.create();
        dialog.show();
    }

    private static int parseToMonth(String str){
        if(str.equals("一年"))
            return 1*12;
        else if(str.equals("三年"))
            return 3*12;
        else if(str.equals("五年"))
            return 5*12;
        return 0;
    }
}
