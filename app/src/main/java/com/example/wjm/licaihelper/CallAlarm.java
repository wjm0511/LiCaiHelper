package com.example.wjm.licaihelper;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;

/**
 * Created by wjm on 2016/5/13.
 */

public class CallAlarm extends BroadcastReceiver
{
  @Override
  public void onReceive(Context context, Intent intent)
  {
    int eventid=intent.getIntExtra("eventid", -1);
    int modeIndex=intent.getIntExtra("modeIndex",0);
    int bellIndex=intent.getIntExtra("bellIndex",2);
    String title=DBUtil.getTiXingTitle(eventid);
    Intent i = new Intent(context, AlarmActivity.class);
    i.putExtra("titleContent",title);
    i.putExtra("modeIndex", modeIndex);
    i.putExtra("bellIndex", bellIndex);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }
}
