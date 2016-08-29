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
public class CalFragmentZCZQ extends Fragment {

    private Button savetime; //存款时间
    private EditText jine; //存款金额
    private EditText lilv; //年利率

    private double inputmoney;
    private double yearrate;

    private float durdate; //存款时间

    private Button zczqCal;


    public CalFragmentZCZQ() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_zczq_content,container,false);

        savetime=(Button)view.findViewById(R.id.threemonth);
        jine=(EditText)view.findViewById(R.id.jine);
        lilv=(EditText)view.findViewById(R.id.lilv);
        zczqCal=(Button)view.findViewById(R.id.zczqCal);

        savetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.item)//设置图标
                        .setTitle(R.string.savetime)//设置标题
                        .setItems(
                                R.array.ll_array,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //对话框关闭时的业务代码
                                        savetime.setText(getResources().getStringArray(R.array.ll_array)[which]);
                                    }
                                }
                        ).create().show();
            }
        });

        zczqCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Double.parseDouble(jine.getText().toString()))*10000<50)
                {
                    Toast.makeText(getActivity(), "50元起存", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    goZCZQResult();
                }
            }
        });

        return view;
    }

    private void goZCZQResult(){
        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("计算结果");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.zczqresult);
        Button b1=(Button)dialog.findViewById(R.id.Button02);
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        durdate=getYear(savetime.getText().toString().trim());
        inputmoney=Double.parseDouble(jine.getText().toString())*10000;
        yearrate=(Double.parseDouble(lilv.getText().toString()))/100;
        double benxi=inputmoney*(1+yearrate*durdate);
        String str=String.valueOf(benxi);
        b1.setText(str);
        dialog.create();
        dialog.show();
    }

    private static float getYear(String str){
        if (str.equals("三个月"))
            return (float)0.25*1;
        else if (str.equals("半年"))
            return (float)0.5*1;
        else if (str.equals("一年"))
            return 1;
        else if (str.equals("两年"))
            return 2;
        else if (str.equals("三年"))
            return 3;
        else
            return 5;
    }
}
