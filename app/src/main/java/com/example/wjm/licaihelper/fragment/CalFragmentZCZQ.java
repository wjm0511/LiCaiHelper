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
 * Created by Yao on 2016/5/20.
 */
public class CalFragmentZCZQ extends Fragment {

    private Button savetime; //存款时间
    private EditText jine; //存款金额
    private EditText lilv; //年利率

    private double inputmoney;
    private double yearrate;

    private float durdate; //存款时间

    private TextView jsCal;


    public CalFragmentZCZQ() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_zczq_content,container,false);

        savetime=(Button)view.findViewById(R.id.threemonth);
        jine=(EditText)view.findViewById(R.id.jine);
        lilv=(EditText)view.findViewById(R.id.lilv);
        jsCal=(TextView)getActivity().findViewById(R.id.jscal);

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
                                        switch (which) {
                                            case 0:
                                                durdate = (float) 3 / 12;
                                                break;
                                            case 1:
                                                durdate = (float) 6 / 12;
                                                break;
                                            case 2:
                                                durdate = 1;
                                                break;
                                            case 3:
                                                durdate = 2;
                                                break;
                                            case 4:
                                                durdate = 3;
                                                break;
                                            case 5:
                                                durdate = 5;
                                                break;
                                        }
                                    }
                                }
                        ).create().show();
            }
        });

        jsCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Double.parseDouble(jine.getText().toString()))*10000<50)
                {
                    Toast.makeText(getActivity(), "50元起存", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    inputmoney=Double.parseDouble(jine.getText().toString())*10000;
                    yearrate=(Double.parseDouble(lilv.getText().toString()))/100;
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
        double benxi=inputmoney*(1+yearrate*durdate);
        String str=String.valueOf(benxi);
        b1.setText(str);
        dialog.create();
        dialog.show();
    }
}
