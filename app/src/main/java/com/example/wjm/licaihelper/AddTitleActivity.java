package com.example.wjm.licaihelper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Yao on 2016/5/13.
 */
public class AddTitleActivity extends Activity {
    private String titleStr;
    private EditText newtitleEt;
    private List<String> al=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addtitle);
        al=DBUtil.getTiXingTitle("tixingtitle");
        ImageButton okButton=(ImageButton)this.findViewById(R.id.ImageButton02);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newtitleEt=(EditText)findViewById(R.id.EditText01);
                titleStr=newtitleEt.getText().toString().trim();
                if(al.contains(titleStr)){
                    Toast.makeText(AddTitleActivity.this,"标题已存在，不能重复插入",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(AddTitleActivity.this, addTixingActivity.class);
                    startActivity(intent);
                }
                else if(titleStr.equals("")){
                    Toast.makeText(AddTitleActivity.this,"不能插入空格",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(AddTitleActivity.this, addTixingActivity.class);
                    startActivity(intent);
                }
                else{
                    DBUtil.insertTiXingTitle(titleStr,"tixingtitle");
                    Intent intent=new Intent();
                    intent.setClass(AddTitleActivity.this, addTixingActivity.class);
                    intent.putExtra("title",titleStr);
                    startActivity(intent);}
            }
        });
        ImageButton cancelButton=(ImageButton)this.findViewById(R.id.ImageButton01);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(AddTitleActivity.this, addTixingActivity.class);
                startActivity(intent);
            }
        });
    }

}
