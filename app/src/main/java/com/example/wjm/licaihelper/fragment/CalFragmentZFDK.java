package com.example.wjm.licaihelper.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjm.licaihelper.DBUtil;
import com.example.wjm.licaihelper.DEBJDetailActivity;
import com.example.wjm.licaihelper.DEBXDetailActivity;
import com.example.wjm.licaihelper.R;

/**
 * Created by Wjm on 2016/5/20.
 */
public class CalFragmentZFDK extends Fragment{

    private final int nianDialog=1;
    private final int yueDialog=2;
    private final int shangdaiHKmode=3;
    private final int JijinHKmode=4;

    private int dYear,dMonth;

    private Button daikuan,nian,yue,hkmode;
    private EditText lilv,jine;
    private Button zfdkCal;

    public CalFragmentZFDK() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //设置初始情况为商业房贷
        final View view = inflater.inflate(R.layout.fg_zfdk_content,container,false);

        daikuan=(Button)view.findViewById(R.id.strB);
        jine=(EditText)view.findViewById(R.id.jine);
        nian=(Button)view.findViewById(R.id.nian);
        yue=(Button)view.findViewById(R.id.yue);
        lilv=(EditText)view.findViewById(R.id.lilv);
        hkmode=(Button)view.findViewById(R.id.hkmode);
        zfdkCal=(Button)view.findViewById(R.id.zfdkCal);

        jine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jine.setCursorVisible(true);
            }
        });

        lilv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lilv.setCursorVisible(true);
            }
        });

        daikuan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                        daikuan.setText(getResources().getStringArray(R.array.houselt_array)[0]);
                                                        break;
                                                    case 1:
                                                        daikuan.setText(getResources().getStringArray(R.array.houselt_array)[1]);
                                                        break;
                                                }
                                            }
                                        }
                                )
                                .create().show();
                    }
                }
        );

        String daikuanStyle=daikuan.getText().toString().trim();
        if (daikuanStyle == "商业贷款") {
            goShangDaiCal();
        } else {
            goJiJinCal();
        }

        return view;
    }

    private void goShangDaiCal(){
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
        hkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(shangdaiHKmode);
            }
        });
        zfdkCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goShangDaiResult();
            }
        });

    }

    private void goJiJinCal(){
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
        hkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(JijinHKmode);
            }
        });
        zfdkCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goJiJinResult();
            }
        });
    }

    private void goShangDaiResult(){
        double inputmoney=Double.parseDouble(jine.getText().toString().trim())*10000;
        float yearrate=Float.parseFloat(lilv.getText().toString().trim())/100;
        double ratesum; //累计支付利息
        double sum; //累计还款总额
        String dYearStr=nian.getText().toString().trim();
        String dMonthStr=yue.getText().toString().trim();
        dYear=getNumber(dYearStr);
        dMonth=getNumber(dMonthStr);
        int durdate=dYear*12+dMonth;

        if(hkmode.getText().toString().trim()=="等额本息")
        {
            DBUtil.deleteAllFromTable("dengebenxidetail");
            for(int i=1;i<=durdate;i++){

                DBUtil.insertDetail("dengebenxidetail", i, 0, 0, 0, 0);
            }

            sum=jsDengeBenxi(inputmoney,yearrate,durdate);
            ratesum=sum-inputmoney;
        }
        else
        {
            DBUtil.deleteAllFromTable("dengebenjindetail");
            for(int i=1;i<=durdate;i++){

                DBUtil.insertDetail("dengebenjindetail", i, 0, 0, 0, 0);
            }
            ratesum=jsDengeBenjin(inputmoney,yearrate,durdate);
            sum=ratesum+inputmoney;
        }
        showResultDialog(ratesum, sum);
    }

    private void goJiJinResult(){
        if(jine.getText().toString()==null||lilv.getText().toString()==null){
            Toast.makeText(getContext(),"请输入完整的计算参数",Toast.LENGTH_SHORT).show();
        }
        double inputmoney=Double.parseDouble(jine.getText().toString().trim())*10000;
        float yearrate=Float.parseFloat(lilv.getText().toString().trim())/100;
        double ratesum;
        double sum;
        String dYearStr=nian.getText().toString().trim();
        String dMonthStr=yue.getText().toString().trim();
        dYear=getNumber(dYearStr);
        dMonth=getNumber(dMonthStr);
        int durdate=dYear*12+dMonth;
        if(hkmode.getText().toString().trim()=="等额本息")
        {
            DBUtil.deleteAllFromTable("dengebenxidetail");
            for(int i=1;i<=durdate;i++){

                DBUtil.insertDetail("dengebenxidetail", i, 0, 0, 0, 0);
            }
            sum=jsDengeBenxi(inputmoney,yearrate,durdate);
            ratesum=sum-inputmoney;
        }
        else
        {
            DBUtil.deleteAllFromTable("dengebenjindetail");
            for(int i=1;i<=durdate;i++){

                DBUtil.insertDetail("dengebenjindetail", i, 0, 0, 0, 0);
            }
            ratesum=jsDengeBenjin(inputmoney,yearrate,durdate);
            sum=ratesum+inputmoney;
        }
        showResultDialog(ratesum,sum);
    }

    private double jsDengeBenxi(double loanAmount,double loanRate,double loanPeroid){
        double sumPayment=0.0;
        double yubenjin2=loanAmount;
        sumPayment=loanAmount*(loanRate/12)*Math.pow(1+loanRate/12, loanPeroid)/(Math.pow(1+loanRate/12, loanPeroid)-1)*loanPeroid;
        for(int i=0;i<loanPeroid;i++){
            double benxi2=loanAmount*(loanRate/12)*Math.pow(1+loanRate/12, loanPeroid)/(Math.pow(1+loanRate/12, loanPeroid)-1);
            double lixi2=yubenjin2*(loanRate/12);
            double benjin2=benxi2-lixi2;
            yubenjin2-=benjin2;
            long benjin=(long)benjin2;
            long yubenjin=(long)yubenjin2;
            long lixi=(long)lixi2;
            long benxi=(long)benxi2;
            DBUtil.updateDetail( i+1, benxi, lixi, benjin, yubenjin,"dengebenxidetail");
        }
        return sumPayment;
    }

    private double jsDengeBenjin(double loanAmount,double loanRate,double loanPeroid){
        double interestPayment=0.0;
        double dengeBenjin2=loanAmount/loanPeroid;
        for(int i=0;i<loanPeroid;i++){
            interestPayment+=(loanAmount-i*dengeBenjin2)*(loanRate/12);//累计利息总额
            double benjin2=dengeBenjin2;
            double yubenjin2=loanAmount-(i+1)*benjin2;
            double lixi2=(loanAmount-i*benjin2)*(loanRate/12);
            double benxi2=benjin2+lixi2;
            long benjin=(long)benjin2;
            long yubenjin=(long)yubenjin2;
            long lixi=(long)lixi2;
            long benxi=(long)benxi2;
            DBUtil.updateDetail(i+1, benxi, lixi, benjin, yubenjin,"dengebenjindetail");
        }

        return interestPayment;
    }

    public static int getNumber(String str){
        int length=str.length();
        String strTemp=str.substring(0, length - 1);
        return Integer.parseInt(strTemp);
    }

    public void showDialog(int dialog){
        switch (dialog){
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
            case shangdaiHKmode:
                new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.item)//设置图标s
                .setTitle(R.string.pt)   //设置标题
                .setItems(
                        R.array.opaytype_array,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //对话框关闭时的业务代码
                                hkmode.setText(getResources().getStringArray(R.array.opaytype_array)[which]);
                            }
                        }
                ).create().show();
                break;
            case JijinHKmode:
                new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.item)//设置图标
                .setTitle(R.string.pt)   //设置标题
                .setItems(
                        R.array.housept_array,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //对话框关闭时的业务代码
                                hkmode.setText(getResources().getStringArray(R.array.housept_array)[which]);
                            }
                        }
                ).create().show();
                break;
        }
    }
    private void showResultDialog(double ratesum,double sum){
        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("计算结果");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.businessfdresult);
        Button b1=(Button)dialog.findViewById(R.id.Button01);
        Button b2=(Button)dialog.findViewById(R.id.Button02);
        Button close=(Button)dialog.findViewById(R.id.close);
        Button detail=(Button)dialog.findViewById(R.id.detail);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hkmode.getText().toString().trim()=="等额本息") {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DEBXDetailActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DEBJDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
        java.text.DecimalFormat df=new java.text.DecimalFormat("#0.00");
        String str1 = df.format(ratesum);
        String str2=df.format(sum);
        b1.setText(str1);
        b2.setText(str2);

        dialog.create();
        dialog.show();
    }
}

