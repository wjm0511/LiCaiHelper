package com.example.wjm.licaihelper.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.wjm.licaihelper.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wjm on 2016/5/20.
 */
public class CalFragmentHQCK extends Fragment {

    private Button indate;
    private Button outdate;
    private Button hqckCal;
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
        lilv=(EditText)view.findViewById(R.id.lilv);
        jine=(EditText)view.findViewById(R.id.jine);
        outdate=(Button)view.findViewById(R.id.outdate);
        hqckCal=(Button)view.findViewById(R.id.hqckCal);

        indate.setText(curdate);
        outdate.setText(curdate);

        indate.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          //c=Calendar.getInstance();//获取日期对象
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
                        //c=Calendar.getInstance();//获取日期对象
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

        hqckCal.setOnClickListener(new View.OnClickListener() {
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

        inputdate=StringToDate(indate.getText().toString(),"yyyy-MM-dd");
        inputmoney=(Double.parseDouble(jine.getText().toString().trim()))*10000;
        yearrate=(Double.parseDouble(lilv.getText().toString().trim()))/100;
        outputdate=StringToDate(outdate.getText().toString(),"yyyy-MM-dd");

        System.out.println(inputdate);
        System.out.println(outputdate);
        System.out.println(inputmoney);
        System.out.println(yearrate);

        long dayNumber=(outputdate.getTime()-inputdate.getTime())/86400000;
        System.out.println(dayNumber);

        double benxi=inputmoney+inputmoney*yearrate*dayNumber/360;
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
