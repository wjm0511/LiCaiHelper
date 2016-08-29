package com.example.wjm.licaihelper;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;


import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wjm on 2016/5/10.
 */
public class ZujiActivity extends Activity implements View.OnClickListener {


    private MapView mapView;
    private LinearLayout menu;//由LinearLayout布局的菜单
    private Boolean menushowed;//是否显示菜单
    private BaiduMap bdMap;

    Calendar c= Calendar.getInstance();
    int cur_year=c.get(Calendar.YEAR);
    int cur_month=c.get(Calendar.MONTH)+1;
    String curdate;//当前日期

    static Bitmap bitmap;//气球图片
    String addresses[];  //支出地点
    float money[];       //支出金额
    String date;
    int year;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zuji);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mapView=(MapView)findViewById(R.id.myMapView);
        MapStatusUpdate msu=MapStatusUpdateFactory.zoomTo(15.0f);
        bdMap=mapView.getMap();
        bdMap.setMapStatus(msu);


        curdate=cur_year+"-"+(cur_month>9?cur_month:"0"+cur_month);//当前日期
        menushowed=false;
        date=curdate;

        //得到LenearLayout的引用
        menu=(LinearLayout)findViewById(R.id.lmenu);
        menu.setVisibility(View.GONE);

        Button back=(Button)findViewById(R.id.back);
        Button bmenu=(Button)findViewById(R.id.bmenu);

        //menu中的各个控件
        Button menuleft=(Button)findViewById(R.id.bleft);
        TextView menudate=(TextView)menu.findViewById(R.id.datemenu);
        Button menuright=(Button)findViewById(R.id.bright);
        Button menuall=(Button)findViewById(R.id.all);

        menudate.setText(curdate);
        menuright.setVisibility(View.GONE);

        back.setOnClickListener(this);
        bmenu.setOnClickListener(this);
        menuleft.setOnClickListener(this);
        menuright.setOnClickListener(this);
        menuall.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        TextView menudate=(TextView)findViewById(R.id.datemenu);
        Button menuall=(Button)findViewById(R.id.all);
        Button menuleft=(Button)findViewById(R.id.bleft);
        Button menuright=(Button)findViewById(R.id.bright);
        switch(v.getId())
        {
            case R.id.back:
                ZujiActivity.this.finish();
                break;
            case R.id.bmenu:
                if(menushowed)
                {
                    menu.setVisibility(View.GONE);
                    menushowed=false;
                }
                else
                {
                    menu.setVisibility(View.VISIBLE);
                    menushowed=true;
                    searchDBUtil();
                    initMap();
                }
                break;

            case R.id.bleft:
                date=menudate.getText().toString();
                minusMonth(date);
                date=year+"-"+(month>9?month:"0"+month);
                menudate.setText(date);

                menuright.setVisibility(View.VISIBLE);
                searchDBUtil();
                initMap();
                break;
            case R.id.bright:
                date=menudate.getText().toString();
                addMonth(date);
                date=year+"-"+(month>9?month:"0"+month);
                menudate.setText(date);
                if(date.equals(curdate))
                {
                    menuright.setVisibility(View.GONE);
                }
                searchDBUtil();
                initMap();
                break;
            case R.id.all:
                if((menuall.getText().toString()).equals("全部"))
                {
                    menuall.setText("返回");
                    menuleft.setVisibility(View.GONE);
                    menuright.setVisibility(View.GONE);
                    menudate.setText("全部交易");
                    menudate.setPadding(10, 0, 0, 0);
                }
                else
                {
                    menuall.setText("全部");
                    menuleft.setVisibility(View.VISIBLE);
                    if(!(date.equals(curdate)))
                    {
                        menuright.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        menuright.setVisibility(View.GONE);
                    }
                    menudate.setText(date);
                }
                searchDBUtil();
                initMap();
                break;
        }

    }

    public void searchDBUtil()
    {
        TextView dateText=(TextView)findViewById(R.id.datemenu);
        String datetemp=dateText.getText().toString();
        List<String[]> addresszhichu=null;
        if(!(datetemp.equals("全部交易")))
        {
            int yeartemp=Integer.parseInt(date.substring(0,4));
            int monthtemp=Integer.parseInt(datetemp.substring(5));
            int days=getMonthDays(yeartemp, monthtemp);
            String datefirst=datetemp+"-"+01;
            String dateend=datetemp+"-"+days;
            addresszhichu=DBUtil.searchAddressZhichu(datefirst, dateend);
        }
        else
        {
            addresszhichu=DBUtil.searchAllAddressZhichu();
        }

        if(addresszhichu.size()!=0)
            Toast.makeText(getBaseContext(),"共找到"+addresszhichu.size()+"个结果",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getBaseContext(),"没有找到相应的结果",Toast.LENGTH_SHORT).show();

        addresses=new String[addresszhichu.size()];
        money=new float[addresszhichu.size()];

        String[] str=null;
        for(int i=0;i<addresszhichu.size();i++){
            str=addresszhichu.get(i);
            addresses[i]=str[0];
            money[i]=Float.parseFloat(str[1]);
        }
    }

    public void initMap()
    {

        //对地图进行初始化
        mapView.showZoomControls(true);  //设置地图上要缩放控制条

        for(int i=0;i<addresses.length;i++)
        {
            //通过Geocoder查找指定名称景点的经纬度
            Geocoder gc=new Geocoder(ZujiActivity.this);
            try
            {
                //通过Geocoder查找指定名称景点的经纬度列表
                List<Address> addressList=gc.getFromLocationName(addresses[i], 3);
                if(addressList.size()>0) {
                    //如果成功获取了经纬度取列表中的第一条
                    Address tempa = addressList.get(0);
                    int latE6 = (int) (tempa.getLatitude() * 1000000);
                    int longE6 = (int) (tempa.getLongitude() * 1000000);
                    GeoPoint gpp = new GeoPoint(latE6, longE6);

                    double latVal = Math.round(gpp.getLatitudeE6() / 1000.00) / 1000.0;//纬度
                    double longVal = Math.round(gpp.getLongitudeE6() / 1000.00) / 1000.0;//经度
                    final LatLng point = new LatLng(latVal, longVal);


                    //获得要显示图标的图片资源
                    BitmapDescriptor bitmap=BitmapDescriptorFactory
                            .fromResource(R.drawable.zj_ballon);


                    OverlayOptions option=new MarkerOptions()
                            .position(point)
                            .icon(bitmap);

                    Marker marker=(Marker)bdMap.addOverlay(option);
                    //将该点信息存入Bundle中

                    Info info=new Info(addresses[i],money[i]);
                    System.out.println(info.add + " " + info.spend);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", info);
                    marker.setExtraInfo(bundle);

                    bdMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            //得到传来的Bundle信息
                            Bundle bundle1 = marker.getExtraInfo();
                            Info info1 = (Info) bundle1.get("info");
                            String add = info1.add;
                            float spend = info1.spend;

                            TextView location = new TextView(getApplicationContext());
                            location.setPadding(15, 15, 8, 35);
                            location.setTextColor(Color.BLUE);
                            location.setBackground(getResources().getDrawable(R.drawable.editsharp));

                            location.setLayoutParams(new ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
                            location.setGravity(Gravity.CENTER | Gravity.LEFT);
                            location.setTextSize(14);
                            location.setText("地点:" + add + "\n支出:" + spend+"元");

                            // 将marker所在的经纬度的信息转化成屏幕上的坐标
                            LatLng ll = marker.getPosition();
                            Point p = bdMap.getProjection().toScreenLocation(ll);
                            LatLng llInfo = bdMap.getProjection().fromScreenLocation(p);
                            // 为弹出的InfoWindow添加点击事件
                            InfoWindow m = new InfoWindow(location, llInfo, -100);
                            // 显示最后一条的InfoWindow
                            bdMap.showInfoWindow(m);
                            return true;
                        }
                    });
                    //将地图移到最后一个经纬度位置
                    LatLng latLng=new LatLng(latVal,longVal);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                    bdMap.animateMapStatus(u);
                }
                else
                {
                    Toast.makeText(ZujiActivity.this,"对不起，您要找的地点没有找到！", Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void minusMonth(String date)
    {
        year=Integer.parseInt(date.substring(0,4));
        month=Integer.parseInt(date.substring(5));
        if(month==1)
        {
            month=12;
            minusYear(date);
        }
        else
        {
            month=month-1;
        }
    }

    private void minusYear(String date)
    {
        year=Integer.parseInt(date.substring(0,4));
        year=year-1;
    }

    private void addMonth(String date)
    {
        year=Integer.parseInt(date.substring(0,4));
        month=Integer.parseInt(date.substring(5));
        if(month==12)
        {
            month=1;
            addYear(date);
        }
        else
        {
            month=month+1;
        }
    }

    private void addYear(String date)
    {
        year=Integer.parseInt(date.substring(0,4));
        year=year+1;
    }

    private static int getMonthDays(int year, int month) //计算每个月的天数
    {
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            {
                return 31;
            }
            case 4:
            case 6:
            case 9:
            case 11:
            {
                return 30;
            }
            case 2:
            {
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
                    return 29;
                else
                    return 28;
            }
        }
        return 0;
    }


}

class Info implements Serializable {
    public String add;
    public float spend;
    public Info(String add,float spend){
        this.add=add;
        this.spend=spend;
    }
}
