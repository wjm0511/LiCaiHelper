package com.example.wjm.licaihelper;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.pm.ActivityInfo;
        import android.os.Bundle;
        import android.text.TextPaint;
        import android.text.TextUtils;
        import android.view.Gravity;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.RadioGroup;
        import android.widget.Spinner;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.zip.Inflater;

        import static com.example.wjm.licaihelper.Constant.*;
/**
 * Created by wjm on 2016/5/10.
 */
public class ShouzhiActivity extends Activity {
    private String isubject;
    private String imode;
    private String idate;
    private String iamount;
    private String iplace;
    private String inote;
    private String dateStr;
    private String yearStr;
    private String lastyearStr;
    private String monthStr;
    private String lastmonthStr;
    private String lastthreemonthStr;
    private String lastmonthdayStr;
    private Spinner szCategory;
    private List<String[]> bl=new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(R.layout.shouzhi);
        szCategory=(Spinner)findViewById(R.id.xuanzeSpinner);
        SimpleDateFormat   sDateFormat=new SimpleDateFormat("  yyyy-MM-dd");
        dateStr=sDateFormat.format(new java.util.Date()).trim(); //获取当前日期
        SimpleDateFormat   sMonthFormat=new SimpleDateFormat("  yyyy-MM");
        SimpleDateFormat   sYearFormat=new SimpleDateFormat("  yyyy");
        yearStr=sYearFormat.format(new java.util.Date()).trim(); //获取当前年份
        lastyearStr=(Integer.parseInt(yearStr)-1)+""; //获取去年年份
        monthStr=sMonthFormat.format(new java.util.Date()).trim(); //获取当前年月
        lastmonthStr=yearStr+"-"+((new java.util.Date().getMonth()+1<10)?"0"+
                new java.util.Date().getMonth():new java.util.Date().getMonth());//获取今年上月日期
        lastthreemonthStr=((new java.util.Date().getMonth()>2)?yearStr+"-"+
                (new java.util.Date().getMonth()-2):lastyearStr+"-"+
                (12+new java.util.Date().getMonth()-2))+"-"+"31";//今年前三个月
        lastmonthdayStr=lastmonthStr+"-"+"31";
        setSpAdapter(szCategory,shouruQK.length,shouruQK);//初始化spinner
        setListener(szCategory,"shouru");
        RadioGroup szRg=(RadioGroup)this.findViewById(R.id.shouzhiRadio);
        szRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //根据选中的单选按钮改变spinner的内容
                if(R.id.shouruRadio==checkedId) {
                    setSpAdapter(szCategory,shouruQK.length,shouruQK);
                    setListener(szCategory,"shouru");
                }
                else{
                    setSpAdapter(szCategory,zhichuQK.length,zhichuQK);
                    setListener(szCategory,"zhichu");
                }
            }
        });
        //返回按钮
        Button returnbutton=(Button)this.findViewById(R.id.fanhui);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShouzhiActivity.this.finish();
            }
        });
    }
    //spinner监听器(从数组)
    public void setSpAdapter(final Spinner setspinner,final int size,final String msgIds[])
    {
        BaseAdapter ba=new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return  size;
            }
            @Override
            public Object getItem(int position)
            {
                return null;
            }
            @Override
            public long getItemId(int arg0)
            {
                return 0;
            }
            @Override
            public View getView(int arg0, View arg1, ViewGroup arg2)
            {
                LinearLayout ll=new LinearLayout(ShouzhiActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                TextView tv=new TextView(ShouzhiActivity.this);
                tv.setText(msgIds[arg0]);
                tv.setPadding(10,5,10,5);
                tv.setTextSize(18);
                ll.addView(tv);
                return ll;
            }
        };
        setspinner.setAdapter(ba);
    }
    public void setListener(Spinner setspinner,final String tableName){
        setspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ListView detaillv = (ListView) findViewById(R.id.shouzhiListView);
                switch (arg2) {
                    case 0:
                        bl = DBUtil.getTodayDetails(tableName, dateStr);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                    case 1:
                        bl = DBUtil.getMonthYearDetails(tableName, monthStr);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                    case 2:
                        bl = DBUtil.getMonthYearDetails(tableName, lastmonthStr);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                    case 3:
                        bl = DBUtil.getLastThreeMonthDetails(tableName, lastthreemonthStr, lastmonthdayStr);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                    case 4:
                        bl = DBUtil.getMonthYearDetails(tableName, yearStr);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                    case 5:
                        bl = DBUtil.getMonthYearDetails(tableName, lastyearStr);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                    case 6:
                        bl = DBUtil.getAllDetails(tableName);
                        setListAdapter(detaillv, bl,tableName);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    public void setListAdapter(final ListView lv,final List<String[]> bl, final String tableName){
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
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view=getLayoutInflater().inflate(R.layout.listadapter,null);
                TextView tv1=(TextView)view.findViewById(R.id.tv1);
                TextView tv2=(TextView)view.findViewById(R.id.tv2);
                TextView tv3=(TextView)view.findViewById(R.id.tv3);
                TextView tv4=(TextView)view.findViewById(R.id.tv4);


                tv1.setText(bl.get(position)[0]);
                tv2.setText(bl.get(position)[1]);
                tv3.setText(bl.get(position)[2]);
                tv4.setText(bl.get(position)[3]);

                return view;
            }
        };
        lv.setAdapter(ba);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                isubject = bl.get(position)[0];
                iamount = bl.get(position)[1];
                imode = bl.get(position)[2];
                idate = bl.get(position)[3];
                iplace = bl.get(position)[5];
                inote = bl.get(position)[6];

                Dialog dialog = new AlertDialog.Builder(ShouzhiActivity.this)
                        .setTitle("详细信息")
                        .setMessage("  科目：" + isubject + "\n" +
                                "  金额：" + iamount + "\n" +
                                "  方式：" + imode + "\n" +
                                "  时间：" + idate + "\n" +
                                "  地点：" + iplace + "\n" +
                                "  备注：" + inote)
                        .setNegativeButton("返回", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBUtil.deleteZhangwu(tableName, isubject, idate, imode,
                                        Double.parseDouble(iamount), iplace, inote);
                                bl.remove(position);
                                setListAdapter(lv, bl, tableName);
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        ba.notifyDataSetChanged();
    }
}
