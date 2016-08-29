package com.example.wjm.licaihelper.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

import com.example.wjm.licaihelper.DBUtil;
import com.example.wjm.licaihelper.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Wjm on 2016/5/20.
 */
public class CalFragmentTQHK extends Fragment {

    private Button tqhkCal;

    private Button ydklx;
    private Button yhkmode;
    private Button nian;
    private Button yue;
    private Button hktime;
    private Button tqhktime;

    private EditText jine,lilv;

    private final int daikuanDialog=1;
    private final int daikuanmodeDialog=2;
    private final int nianDialog=3;
    private final int yueDialog=4;
    private final int hkdateDialog=5;
    private final int tqhkdateDialog=6;

    Calendar c=Calendar.getInstance();

    private int dyear,dmonth;   //定义原先选择的贷款年限
    private int hkyear,hkmonth;  //定义第一次还款年月
    private int tqhkyear,tqhkmonth;  //定义提前还款年月

    private double inputmoney;  //获取贷款金额
    private int durdate; //获取总的贷款月份
    private double yearrate; //获取贷款年利率
    private int timeinterval; //第一次还款时间到提前还款时间的时间间隔

    List<Double> tqList=new ArrayList<Double>();

    public CalFragmentTQHK() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_tqhk_content,container,false);

        ydklx=(Button)view.findViewById(R.id.ydklx);//原贷款类型
        yhkmode=(Button)view.findViewById(R.id.ydkmode);//原贷款方式
        jine=(EditText)view.findViewById(R.id.jine); //原贷款金额
        nian=(Button)view.findViewById(R.id.nian);   //年份
        yue=(Button)view.findViewById(R.id.yue);     //月份
        lilv=(EditText)view.findViewById(R.id.lilv);   //贷款利率
        hktime=(Button)view.findViewById(R.id.hktime); //第一次还款时间
        tqhktime=(Button)view.findViewById(R.id.tqhktime);//预计提前还款时间
        tqhkCal=(Button)view.findViewById(R.id.tqhkCal);

        String str=c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月";
        hktime.setText(str);
        tqhktime.setText(str);


        ydklx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(daikuanDialog);
            }
        });

        yhkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(daikuanmodeDialog);
            }
        });


        nian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(nianDialog);
            }
        });

        yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(yueDialog);
            }
        });

        hktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(hkdateDialog);
            }
        });

        tqhktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(tqhkdateDialog);
            }
        });

        tqhkCal.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         inputmoney = (Double.parseDouble(jine.getText().toString())) * 10000;
                                         dyear=CalFragmentZFDK.getNumber(nian.getText().toString().trim());
                                         dmonth=CalFragmentZFDK.getNumber(yue.getText().toString().trim());
                                         durdate = dyear * 12 + dmonth;
                                         yearrate = (Double.parseDouble(lilv.getText().toString())) / 100;
                                         if (tqhkyear > hkyear) {
                                             timeinterval = (tqhkyear - hkyear) * 12 + tqhkmonth - hkmonth;
                                         } else if (tqhkyear == hkyear) {
                                             if (tqhkmonth < hkmonth) {
                                                 timeinterval = 612;
                                             } else {
                                                 timeinterval = tqhkmonth - hkmonth;
                                             }
                                         } else {
                                             timeinterval = 612;
                                         }
                                         if (timeinterval > durdate) {
                                             Toast.makeText(getActivity(), "请检查选择的日期", Toast.LENGTH_SHORT).show();
                                         } else {
                                             goHuanKuanResult();
                                         }
                                     }
                                 }
        );

        return view;
    }

    public void goHuanKuanResult()
    {
        double returnratesum=0.0;
        double returnsum=0.0;
        double saveratesum=0.0;
        double monthsum=0.0;
        String str1;
        String str2;
        String str3;
        String str4;
        if(yhkmode.getText().toString().trim()=="等额本息"){
            tqList=jsTqDengeBenxi(inputmoney,yearrate,timeinterval,durdate);

            returnsum=tqList.get(0);
            returnratesum=tqList.get(1);
            saveratesum=jsTqDengeBenxi(inputmoney,yearrate,durdate,durdate).get(1)
                    -jsTqDengeBenxi(inputmoney,yearrate,timeinterval+1,durdate).get(1);
            monthsum=tqList.get(3)*(1+yearrate/12);
        }
        else{
            returnratesum=jsTqDengeBenjin(inputmoney,yearrate,durdate,timeinterval,0);
            returnsum=(inputmoney/durdate)*timeinterval+returnratesum;
            saveratesum=jsTqDengeBenjin(inputmoney,yearrate,durdate,durdate,timeinterval+1);
            monthsum=(inputmoney-(inputmoney/durdate)*timeinterval)*(1+yearrate/12);
        }


        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("计算结果");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.businesshkresult);
        Button b1=(Button)dialog.findViewById(R.id.Button01);
        Button b2=(Button)dialog.findViewById(R.id.Button02);
        Button b3=(Button)dialog.findViewById(R.id.Button03);
        Button b4=(Button)dialog.findViewById(R.id.Button04);
        Button close=(Button)dialog.findViewById(R.id.close);

        java.text.DecimalFormat df=new java.text.DecimalFormat("#0.00");
        str1 = df.format(returnratesum);
        str2=df.format(returnsum);
        str3 = df.format(saveratesum);
        str4=df.format(monthsum);

        b1.setText(str1);
        b2.setText(str2);
        b3.setText(str3);
        b4.setText(str4);

        dialog.create();
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public List<Double> jsTqDengeBenxi(double loanAmount,double loanRate,double interval,double loanPeroid){
        List<Double> tqList=new ArrayList<Double>();
        double benxi2=0.0;
        double lixi2=0.0;
        double benjin2=0.0;
        double yubenjin2=loanAmount;
        double lixi=0.0;
        double benxi=0.0;
        for(int i=0;i<interval;i++){
            benxi2=loanAmount*(loanRate/12)*Math.pow(1+loanRate/12, loanPeroid)/(Math.pow(1+loanRate/12, loanPeroid)-1);
            lixi2=yubenjin2*(loanRate/12);
            benjin2=benxi2-lixi2;
            yubenjin2-=benjin2;
            lixi+=lixi2;
            benxi+=benxi2;
        }
        tqList.clear();
        tqList.add(benxi);
        tqList.add(lixi);
        tqList.add(benjin2);
        tqList.add(yubenjin2);
        //DBUtil.updateTq( 1, benxi, lixi, benjin2, yubenjin2,"tqdengebenxi");

        return tqList;
    }

    public double jsTqDengeBenjin(double loanAmount,double loanRate,double loanPeroid,double interval,int start){
        double interestPayment=0.0;
        double dengeBenjin2=loanAmount/loanPeroid;
        for(int i=start;i<interval;i++){
            interestPayment+=((loanAmount-i*dengeBenjin2)*(loanRate/12));//累计利息总额
        }
        return interestPayment;
    }

    private void showDialog(int dialog){
        switch (dialog){
            case daikuanDialog:
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.item)//设置图标
                        .setTitle(R.string.lt)//设置标题
                        .setItems(
                                R.array.houselt_array,
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch(which)
                                        {
                                            case 0:
                                                ydklx.setText(getResources().getStringArray(R.array.houselt_array)[0]);
                                                break;
                                            case 1:
                                                ydklx.setText(getResources().getStringArray(R.array.houselt_array)[1]);
                                                break;
                                        }
                                    }
                                }
                        )
                        .create().show();
                break;
            case daikuanmodeDialog:
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.item)//设置图标s
                        .setTitle(R.string.pt)   //设置标题
                        .setItems(
                                R.array.opaytype_array,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //对话框关闭时的业务代码
                                        yhkmode.setText(getResources().getStringArray(R.array.opaytype_array)[which]);
                                    }
                                }
                        ).create().show();
                break;

            case nianDialog:
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.item)//设置图标
                        .setTitle(R.string.loadyears)//设置标题
                        .setItems(
                                R.array.year_array,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //对话框关闭时的业务代码
                                        nian.setText(getResources().getStringArray(R.array.year_array)[which]);
                                    }
                                }
                        ).create().show();
                break;
            case yueDialog:
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.item)//设置图标
                        .setTitle(R.string.loadyears)//设置标题
                        .setItems(
                                R.array.month_array,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //对话框关闭时的业务代码
                                        yue.setText(getResources().getStringArray(R.array.month_array)[which]);
                                    }
                                }
                        ).create().show();
                break;
            case hkdateDialog://生成日期对话框
                c= Calendar.getInstance();//获取日期对象
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2,int arg3) {
                        //设置日期同时要做的工作
                        c=Calendar.getInstance();//获取日期对象
                        c.set(arg1, arg2, arg3);
                        //设置系统时间
                        boolean b= SystemClock.setCurrentTimeMillis(c.getTimeInMillis());
                        hktime.setText(""+arg1+"年"+(arg2+1)+"月");
                        hkyear=arg1;
                        hkmonth=arg2+1;
                    }
                },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            case tqhkdateDialog://生成日期对话框
                c=Calendar.getInstance();//获取日期对象
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2,int arg3) {
                        //设置日期同时要做的工作
                        c=Calendar.getInstance();//获取日期对象
                        c.set(arg1, arg2, arg3);
                        //设置系统时间
                        boolean b=SystemClock.setCurrentTimeMillis(c.getTimeInMillis());
                        tqhktime.setText(""+arg1+"年"+(arg2+1)+"月");
                        tqhkyear=arg1;
                        tqhkmonth=arg2+1;
                    }
                },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
        }
    }
}
