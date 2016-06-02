package com.example.wjm.licaihelper.shezhi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjm.licaihelper.DBUtil;
import com.example.wjm.licaihelper.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.wjm.licaihelper.Constant.fangshi;

/**
 * Created by Yao on 2016/5/16.
 */
public class ZhanghuActivity extends Activity {
    private List<String> al=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.shourukm);

        Button back=(Button)findViewById(R.id.fanhui);

        goSZView("szmode",fangshi);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhanghuActivity.this.finish();
            }
        });
    }

    public void goSZView(final String tableName,String[] kemu){
        al= DBUtil.getSubjects(tableName);//初始化
        if(al.size()==0){
            for(int i=0;i<kemu.length;i++){
                DBUtil.insertSubjects(kemu[i],tableName);
            }
        }
        ListView lv=(ListView)findViewById(R.id.ListView01);
        setAdapter(lv, al, tableName);

        final ImageButton add=(ImageButton)findViewById(R.id.add);
        final EditText addEdit=(EditText)findViewById(R.id.addEdit);
        add.setOnClickListener(new View.OnClickListener() {
            String str;
            @Override
            public void onClick(View v) {
                str=addEdit.getText().toString().trim();
                al=DBUtil.getSubjects(tableName);
                if(al.contains(str))
                    Toast.makeText(ZhanghuActivity.this, "不能重复插入", Toast.LENGTH_SHORT).show();
                else if(str.length()==0)
                    Toast.makeText(ZhanghuActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                else {
                    DBUtil.insertSubjects(str, tableName);
                    al.add(str);
                    addEdit.setText("");
                    ListView lv=(ListView)findViewById(R.id.ListView01);
                    setAdapter(lv,al,tableName);
                }
            }
        });
    }

    private void setAdapter(final ListView lv,final List<String> al, final String tableName){
        final BaseAdapter baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return al.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view= LayoutInflater.from(ZhanghuActivity.this).inflate(R.layout.shourukemulist,null);
                TextView item=(TextView)view.findViewById(R.id.item);
                ImageButton deleteBtn=(ImageButton)view.findViewById(R.id.deletebBtn);
                item.setText(al.get(position));
                final String del_str=item.getText().toString().trim();
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(ZhanghuActivity.this)
                                .setTitle("删除")
                                .setMessage("确认删除科目"+"\""+del_str+"\"?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DBUtil.deleteFromTable(del_str,"szmode");
                                        al.remove(del_str);
                                        setAdapter(lv, al, tableName);
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .create()
                                .show();
                    }
                });
                return view;
            }
        };
        lv.setAdapter(baseAdapter);
        baseAdapter.notifyDataSetChanged();
    }
}
