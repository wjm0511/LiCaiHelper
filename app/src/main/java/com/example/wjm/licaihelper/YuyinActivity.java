package com.example.wjm.licaihelper;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wjm on 2016/5/10.
 */
public class YuyinActivity extends Activity implements OnClickListener{
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;

    int num;
    boolean click=true;
    ImageButton ok;
    ImageButton again;
    Button back;
    Button speak;
    EditText et;
    TextView inputnum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(R.layout.yuyin);

        ok=(ImageButton)findViewById(R.id.ImageButton01);  //完成语音输入
        again=(ImageButton)findViewById(R.id.ImageButton02); //重新输入
        speak=(Button)findViewById(R.id.voiceBtnPressSpeak); //语音识别
        back=(Button)findViewById(R.id.fanhui);


        et=(EditText)findViewById(R.id.EditText01);
        et.setCursorVisible(false);//设置是否显示光标

        inputnum=(TextView)findViewById(R.id.inputnum);
        String tvnum=inputnum.getText().toString().trim();
        num=Integer.parseInt(tvnum.substring(6,9));

        ok.setOnClickListener(this);
        again.setOnClickListener(this);
        speak.setOnClickListener(this);
        back.setOnClickListener(this);
        et.setOnClickListener(this);
        et.addTextChangedListener(mTextWatcher);

        //判断当前手机是否支持语音识别功能
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(list.size()!=0)
        {
            speak.setOnClickListener(this);
        }
        else
        {
            speak.setEnabled(false);
            Toast.makeText(this, "当前语音识别设备不可用,请下载语音识别软件...", Toast.LENGTH_SHORT).show();
        }

        et.setTextSize(16);

    }

    //实时计算编辑框中的字符个数的监听方法
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart=et.getSelectionStart();
            editEnd = et.getSelectionEnd();
            num=100-temp.length();
            inputnum.setText("您还可以输入"+num+"字");
            if (num==0) {
                Toast.makeText(YuyinActivity.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                et.setText(s);
                et.setSelection(tempSelection);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.ImageButton01:
                String content=et.getText().toString().trim();
                Intent  goJiZhang=new Intent(YuyinActivity.this,JizhangActivity.class);
                goJiZhang.putExtra("content", content);
                startActivity(goJiZhang);
                break;
            case R.id.ImageButton02:
                Toast.makeText(YuyinActivity.this,"请重新输入...",Toast.LENGTH_SHORT).show();
                et.setText(null);
                break;
            case R.id.fanhui:
                Intent  backintent=new Intent(YuyinActivity.this,LiCaiActivity.class);
                startActivity(backintent);
                break;
            case R.id.voiceBtnPressSpeak:
                et=(EditText)findViewById(R.id.EditText01);
                if(click)
                {
                    click=false;
                    et.setText("");
                }

                //启动手机内置的语言识别功能
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  //设置为当前手机的语言类型
                startActivityForResult(intent,VOICE_RECOGNITION_REQUEST_CODE);
                break;

            case R.id.EditText01:
                et=(EditText)findViewById(R.id.EditText01);
                if(click)
                {
                    click=false;
                    et.setText("");
                }
                et.setCursorVisible(true);//设置是否显示光标
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //回调获取从语音识别软件得到的数据
        if(requestCode==VOICE_RECOGNITION_REQUEST_CODE && resultCode==RESULT_OK)
        {
            //取得语音的字符
            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString;
            resultString=results.get(0);
            et=(EditText)findViewById(R.id.EditText01);
            inputnum=(TextView)findViewById(R.id.inputnum);
            String strtemp=et.getText().toString().trim();
            et.setText(strtemp+resultString+"。");
            strtemp=et.getText().toString().trim();  //最终要返回到JizhangActivity界面的数据
            int size=strtemp.length();
            num=100-size;
            if (num==0) {
                Toast.makeText(YuyinActivity.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
            }
            inputnum.setText("您还可以输入"+num+"字");
            et.addTextChangedListener(mTextWatcher);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
