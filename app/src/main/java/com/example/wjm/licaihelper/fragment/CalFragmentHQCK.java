package com.example.wjm.licaihelper.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.example.wjm.licaihelper.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yao on 2016/5/20.
 */
public class CalFragmentHQCK extends Fragment {

    private Button indate;
    private Button outdate;
    private TextView jsCal;
    private EditText jine;
    private EditText lilv;

    private double inputmoney;
    private Date inputdate,outputdate;
    private double yearrate;

    Calendar c=Calendar.getInstance();
    String curdate=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"
            +c.get(Calendar.DAY_OF_MONTH);


    public CalFragmentHQCK() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_hqck_content,container,false);
        indate=(Button)view.findViewById(R.id.indate);
        indate.setText(curdate);
        lilv=(EditText)view.findViewById(R.id.lilv);
        jine=(EditText)view.findViewById(R.id.jine);
        outdate=(Button)view.findViewById(R.id.outdate);
        outdate.setText(curdate);
        jsCal=(TextView)getActivity().findViewById(R.id.jscal);

        indate.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          c=Calendar.getInstance();//获取日期对象
                                          new DatePickerDialog(
                                                  getActivity(),
                                                  new DatePickerDialog.OnDateSetListener()
                                                  {
                                                      @Override
                                                      public void onDateSet(DatePicker arg0, int arg1, int arg2,int arg3) {
                                                          //设置日期同时要做的工作
                                                          c=Calendar.getInstance();//获取日期对象
                                                          c.set(arg1, arg2, arg3);
                                                          //设置系统时间
                                                          indate.setText(""+arg1+"-"+(arg2+1)+"-"+arg3);
                                                      }
                                                  },
                                                  c.get(Calendar.YEAR),
                                                  c.get(Calendar.MONTH),
                                                  c.get(Calendar.DAY_OF_MONTH)
                                          ).show();
                                      }
                                  }
        );
        outdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        c=Calendar.getInstance();//获取日期对象
                        new DatePickerDialog(
                                getActivity(),
                                new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker arg0, int arg1, int arg2,int arg3) {
                                        //设置日期同时要做的工作
                                        c=Calendar.getInstance();//获取日期对象
                                        c.set(arg1, arg2, arg3);
                                        //设置系统时间
                                        outdate.setText(""+arg1+"-"+(arg2+1)+"-"+arg3);
                                    }
                                },
                                c.get(Calendar.YEAR),
                                c.get(Calendar.MONTH),
                                c.get(Calendar.DAY_OF_MONTH)
                        ).show();
                    }
                }
        );

        inputdate=StringToDate(indate.getText().toString(),"yyyy-MM-dd");
        inputmoney=Double.parseDouble(jine.getText().toString())*10000;
        yearrate=(Double.parseDouble(lilv.getText().toString()))/100;
        outputdate=StringToDate(outdate.getText().toString(),"yyyy-MM-dd");

        jsCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHQCKResult();
            }
        });
        return view;
    }

    private void goHQCKResult(){
        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("计算结果");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.hqcxresult);

        Button b2=(Button)dialog.findViewById(R.id.Button02);
        Button close=(Button)dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        double benxi=inputmoney+inputmoney*yearrate*(((outputdate.getTime()
                -inputdate.getTime())/1000/60/60/24/365));
        String str=String.valueOf(benxi);
        b2.setText(str);

        dialog.create();
        dialog.show();
    }

    //日期转换格式
    public static Date StringToDate(String dateStr,String formatStr){
        DateFormat sdf=new SimpleDateFormat(formatStr);
        Date date=null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
