package com.example.wjm.licaihelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.wjm.licaihelper.bianqian.AtyEditNote;


public class BianqianActivity extends Activity {
    private SimpleCursorAdapter adapter=null;
    private NotesDB db;
    private SQLiteDatabase dbRead,dbWrite;

    public static final int REQUEST_CODE_ADD_NOTE=1;
    public static final int REQUEST_CODE_EDIT_NOTE=2;


    //实现OnClickListener接口，添加日志按钮的监听
    private OnClickListener btnAddNote_clickHandler=new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 有返回结果的开启编辑日志的Activity，
            startActivityForResult(new Intent(BianqianActivity.this,
                    AtyEditNote.class), REQUEST_CODE_ADD_NOTE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianqian);

        // 操作数据库
        db=new NotesDB(this);
        dbRead=db.getReadableDatabase();
        dbWrite=db.getWritableDatabase();

        // 查询数据库并将数据显示在ListView上。
        final ListView list=(ListView)findViewById(R.id.list);
        adapter=new SimpleCursorAdapter(this, R.layout.notes_list_cell, null,
                new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
                        NotesDB.COLUMN_NAME_NOTE_DATE }, new int[] {
                R.id.tvName, R.id.tvDate });

        list.setAdapter(adapter);

        refreshNotesListView();

        findViewById(R.id.newNote).setOnClickListener(
                btnAddNote_clickHandler);
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BianqianActivity.this.finish();
            }
        });


    //复写方法，笔记列表中的笔记条目被点击时被调用，打开编辑笔记页面，同事传入当前笔记的信息

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前笔记条目的Cursor对象
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);

                // 显式Intent开启编辑笔记页面
                Intent i = new Intent(BianqianActivity.this, AtyEditNote.class);

                // 传入笔记id，name，content
                i.putExtra(AtyEditNote.EXTRA_NOTE_ID,
                        c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
                i.putExtra(AtyEditNote.EXTRA_NOTE_NAME,
                        c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
                i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT,
                        c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT)));

                // 有返回的开启Activity
                startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前笔记条目的Cursor对象
                final Cursor c=adapter.getCursor();
                c.moveToPosition(position);

                new AlertDialog.Builder(BianqianActivity.this)
                        .setIcon(R.drawable.item)
                        .setTitle("删除")
                        .setMessage("确认要删除这条信息？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbWrite.delete(NotesDB.TABLE_NAME_NOTES,NotesDB.COLUMN_NAME_ID+"=?",
                                        new String[]{c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))});
                                dbWrite.delete(NotesDB.TABLE_NAME_MEDIA, NotesDB.COLUMN_NAME_ID + "=?",
                                        new String[]{c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))});
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null)
                        .create()
                        .show();

                return true;
            }
        });
    }


    //当被开启的Activity存在并返回结果时调用的方法

     //当从编辑笔记页面返回时调用，刷新笔记列表

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_ADD_NOTE:
            case REQUEST_CODE_EDIT_NOTE:
                if (resultCode == Activity.RESULT_OK) {
                    refreshNotesListView();
                }
                break;

            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //刷新笔记列表，内容从数据库中查询

    public void refreshNotesListView() {
        adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null,
                null, null, null, null));
    }
}

