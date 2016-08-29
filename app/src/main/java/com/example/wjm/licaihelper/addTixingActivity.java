package com.example.wjm.licaihelper;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static com.example.wjm.licaihelper.Constant.*;

/**
 * Created by Wjm on 2016/5/13.
 */
public class addTixingActivity extends Activity {
    private LinearLayout linearLayout;
    private DatePicker dateDp;
    Dialog dialogCycle;   //提醒周期
    Dialog titleDialog;   //提醒标题
    private String titleStr="";
    private TextView titleTv;
    Calendar c;
    int hour=8;
    int minute=0;
    int year2;
    int month;
    int day;
    private Date date;
    private String timeStr;
    private String dateStr;
    AlarmManager am;
    private  int id;
    private int index;
    private int yearDays;
    private int monthDays;
    private int modeIndex;
    private int bellIndex;
    private TextView cycleTv;
    private TextView timeTv;
    List<String> temp=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.addtixing);

        SharedPreferences bellSp=getSharedPreferences("actm", Context.MODE_PRIVATE);
        SharedPreferences modeSp=getSharedPreferences("actm2", Context.MODE_PRIVATE);

        modeIndex=modeSp.getInt("modeIndex",2);
        bellIndex=bellSp.getInt("bellIndex", 0);
        dateDp=(DatePicker)this.findViewById(R.id.DatePicker01);
        linearLayout=(LinearLayout)findViewById(R.id.dateLinear);
        linearLayout.setVisibility(View.GONE);

        c =Calendar.getInstance();
        int inityear =c.get(Calendar.YEAR);  //得到当前年份
        int initmonth=c.get(Calendar.MONTH);  //得到当前月份
        int initday=c.get(Calendar.DAY_OF_MONTH);  //得到当前在月份中的哪一天
        year2=inityear;
        month=initmonth;
        day=initday;


        dateDp.init(inityear, initmonth, initday, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                year2 = year;
                month = monthOfYear;
                day = dayOfMonth;
            }
        });


        //初始化提醒数据库
        temp= DBUtil.getTiXingTitle("tixingtitle");
       if(temp==null){
            for(int i=0;i<titleContent.length;i++){
                DBUtil.insertTiXingTitle(titleContent[i],"tixingtitle");
            }
        }


        //标题
        titleTv=(TextView)this.findViewById(R.id.TextView04);
        if(this.getIntent().getStringExtra("title")!=null){
            Intent titleintent=getIntent();
            titleStr=titleintent.getStringExtra("title");
        }
        titleTv.setText(titleStr);
        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleDialog=new Dialog(addTixingActivity.this);
                titleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                titleDialog.setContentView(R.layout.titledialog);
                ListView titlelv=(ListView)titleDialog.findViewById(R.id.ListView01);
                TextView newTitle=(TextView)titleDialog.findViewById(R.id.newTitle);
                newTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(addTixingActivity.this, AddTitleActivity.class);
                        startActivity(intent);
                        titleDialog.dismiss();
                    }
                });
                setListViewAdapter(titlelv);
                titleDialog.show();
            }
        });

        //时间
        timeTv=(TextView)findViewById(R.id.TextView01);
        timeTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG);
            }
        });

        //提醒周期
        cycleTv=(TextView)findViewById(R.id.TextView02);
        cycleTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(CYCLE_DIALOG);
            }
        });

        //返回
        Button returnButton=(Button)this.findViewById(R.id.fanhui);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTixingActivity.this.finish();

            }
        });

        //保存
        Button saveButton=(Button)this.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title=titleTv.getText().toString();
                Toast.makeText(addTixingActivity.this, title, Toast.LENGTH_SHORT).show();
                date=new Date(year2-1900,month,day,hour,minute);
                EditText noteEt=(EditText)findViewById(R.id.EditText01);
                String note=noteEt.getText().toString();
                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateStr=sDateFormat.format(date);
                timeStr=timeTv.getText().toString();
                String cycle=cycleTv.getText().toString();
                SharedPreferences sp=getSharedPreferences("actm", Context.MODE_PRIVATE); //获取提醒方式
                id=sp.getInt
                        (
                                "eventid",   //键值
                                0   //默认值
                        );
                SharedPreferences.Editor editor=sp.edit();
                editor.putInt("eventid", ++id);
                editor.commit();
                DBUtil.insertTiXing(id,title,dateStr,cycle,timeStr,note);
                yearDays=getYearDays(year2);
                monthDays=getDays(year2,month+1);
                setAlarm(date);
                //提醒周期
                Intent intent=new Intent();
                intent.setClass(addTixingActivity.this,TixingActivity.class);
                startActivity(intent);
            }
        });
    }
    //对话框

    private void setListViewAdapter(final ListView lv){
        final BaseAdapter baseAdapter=new BaseAdapter() {
            List<String> result=DBUtil.getTiXingTitle("tixingtitle");
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
            public View getView(final int position, final View convertView, ViewGroup parent) {
                View view= LayoutInflater.from(addTixingActivity.this).inflate(R.layout.choosetitlelist,null);
                final TextView tv=(TextView)view.findViewById(R.id.choose);
                ImageButton Btn=(ImageButton)view.findViewById(R.id.deletebBtn);
                tv.setText(result.get(position));
                tv.setTextSize(20);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titleStr = tv.getText().toString();
                        titleTv.setText(titleStr);
                        titleDialog.dismiss();
                    }
                });
                Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBUtil.deleteTixingTitle(result.get(position),"tixingtitle");
                        result.remove(position);
                        setListViewAdapter(lv);
                    }
                });
                return view;
            }
        };
        lv.setAdapter(baseAdapter);
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public Dialog onCreateDialog(int id)
    {
        Dialog dialog=null;

        switch(id)
        {
            case TIME_DIALOG:
                c=Calendar.getInstance();//获取日期对象
                int mHour=c.get(Calendar.HOUR_OF_DAY);
                int mMinute=c.get(Calendar.MINUTE);
                dialog=new TimePickerDialog(
                        this,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            @Override
                            public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

                                c=Calendar.getInstance();//获取日期对象
                                c.setTimeInMillis(System.currentTimeMillis());
                                c.set(Calendar.HOUR_OF_DAY, arg1);
                                c.set(Calendar.MINUTE, arg2);
                                c.set(Calendar.SECOND,0);
                                c.set(Calendar.MILLISECOND,0);
                                hour=arg1;
                                minute=arg2;
                                if(arg1>=10)
                                    timeTv.setText((arg2<10)?(arg1+":"+"0"+arg2):(arg1+":"+arg2));
                                else{
                                    timeTv.setText((arg2<10)?("0"+arg1+":"+"0"+arg2):("0"+arg1+":"+arg2));
                                }
                            }
                        },
                        mHour,
                        mMinute,
                        true
                );
                break;
            case CYCLE_DIALOG:
                final CharSequence[] cycleitems = {"仅一次", "每天", "工作日","每月","每年"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(addTixingActivity.this);
                builder.setTitle("设置提醒周期");
                builder.setSingleChoiceItems(cycleitems, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        index=item;
                        switch (item){
                            case 0:
                            case 1:
                            case 2:
                                linearLayout.setVisibility(View.GONE);
                                break;
                            case 3:
                            case 4:
                                linearLayout.setVisibility(View.VISIBLE);
                                break;
                        }
                        cycleTv=(TextView)findViewById(R.id.TextView02);
                        dialogCycle.dismiss();
                        cycleTv.setText(cycleitems[item]);
                        //Toast.makeText(addTixingActivity.this,index+"",Toast.LENGTH_SHORT).show();
                    }});
                dialogCycle= builder.create();
                dialog=dialogCycle;
                break;
        }
        return dialog;
    }


    public void setAlarm(Date date)
    {
        switch(index){
            case 0:
                if(date.before(new Date(System.currentTimeMillis()))){
                    cancelAlarm();
                }
                else{
                    Intent intent=new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am=(AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime(),
                            sender
                    );
                }
                break;
            case 1:
                if(date.before(new Date(System.currentTimeMillis()))){
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime()+AlarmManager.INTERVAL_DAY,
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime()+AlarmManager.INTERVAL_DAY,AlarmManager.INTERVAL_DAY, sender);
                }
                else{
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime(),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime(),AlarmManager.INTERVAL_DAY, sender);
                }
                break;
            case 2:
                if(date.before(new Date(System.currentTimeMillis()))){
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime()+7*(AlarmManager.INTERVAL_DAY),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime()+(7*AlarmManager.INTERVAL_DAY),7*AlarmManager.INTERVAL_DAY, sender);
                }
                else{
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime(),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime(),7*AlarmManager.INTERVAL_DAY, sender);
                }
                break;
            case 3:
                if(date.before(new Date(System.currentTimeMillis()))){
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime()+monthDays*(AlarmManager.INTERVAL_DAY),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime()+(monthDays*AlarmManager.INTERVAL_DAY),monthDays*AlarmManager.INTERVAL_DAY, sender);
                }
                else{
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime(),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime(),monthDays*AlarmManager.INTERVAL_DAY, sender);
                }
                break;
            case 4:
                if(date.before(new Date(System.currentTimeMillis()))){
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime()+yearDays*(AlarmManager.INTERVAL_DAY),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime()+(yearDays*AlarmManager.INTERVAL_DAY),yearDays*AlarmManager.INTERVAL_DAY, sender);
                }
                else{
                    Intent intent = new Intent(addTixingActivity.this, CallAlarm.class);
                    intent.putExtra("eventid",id);
                    intent.putExtra("modeIndex",modeIndex);
                    intent.putExtra("bellIndex",bellIndex);
                    PendingIntent sender=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            date.getTime(),
                            sender
                    );
                    am.setRepeating(AlarmManager.RTC_WAKEUP,date.getTime(),yearDays*AlarmManager.INTERVAL_DAY, sender);
                }
                break;
        }

    }
    //取消闹钟方法
    public void cancelAlarm()
    {
        //获取闹钟管理的对象
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //找对应的接受者
        Intent intent=new Intent(addTixingActivity.this,CallAlarm.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(addTixingActivity.this,id, intent, 0);
        //取消闹钟
        am.cancel(pendingIntent);
    }
    public boolean isLeap(int year)
    {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))
            return true;
        else
            return false;
    }
    public int getYearDays(int year){
        int yearDays;
        if(isLeap(year))
        {yearDays=366;}
        else
        {yearDays=365;}
        return yearDays;
    }
    //返回当月天数
    public int getDays(int year, int month)
    {
        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

}
