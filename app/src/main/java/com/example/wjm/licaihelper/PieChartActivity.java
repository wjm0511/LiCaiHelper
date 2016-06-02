package com.example.wjm.licaihelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

/**
 * Created by Yao on 2016/5/15.
 */
public class PieChartActivity extends Activity {

    private PieChart mChart;
    private String[] items;
    private float[] number;
    private int items_len;
    private int number_len;
    private float sum;
    Button btnback;
    ListView detailList;
    TextView sumText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart);

        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        items=bundle.getStringArray("items");
        number=bundle.getFloatArray("amount");
        items_len=items.length;
        number_len=number.length;


        btnback=(Button)findViewById(R.id.back);
        detailList=(ListView)findViewById(R.id.detailList);
        sumText=(TextView)findViewById(R.id.sum);


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PieChartActivity.this.finish();
            }
        });

        setListAdapter(detailList, items, number);

        for(int i=0;i<number_len;i++)
            sum += number[i];
        sumText.setText("消费总额为："+sum);

        PieData mPieData = getPieData(items_len, 100);
        showChart(mChart, mPieData);

    }

    private void setListAdapter(ListView listView, final String[] item, final float[] amount){
        BaseAdapter baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return item.length;
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
            public View getView(int position, View convertView, ViewGroup parent) {
                    View view= LayoutInflater.from(PieChartActivity.this).inflate(R.layout.piechartlist, null);
                    TextView itemsText=(TextView)view.findViewById(R.id.itemsText);
                    TextView numberText=(TextView)view.findViewById(R.id.numberText);
                    itemsText.setText(item[position]);
                    numberText.setText(amount[position]+" ");

                return view;
            }
        };
        listView.setAdapter(baseAdapter);
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(50f);  //半径
        pieChart.setTransparentCircleRadius(54f); //半透明圆
        //pieChart.setHoleRadius(0);  //实心圆

        pieChart.setDescription("消费情况饼状图");
        pieChart.setDescriptionTextSize(14);

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        //pieChart.setCenterText("Quarterly Revenue");//饼状图中间的文字
        //pieChart.setHoleColor(R.color.white);

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);

        pieChart.setDrawHoleEnabled(true);
    }

    /**
     *
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        for (int i = 0; i < count; i++) {
            xValues.add(items[i]);
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼状图分成count部分，4部分按比例分配
         */

        float[] quarterly=new float[number_len];

        for(int i=0;i<number_len;i++){
            quarterly[i]=number[i]/sum*100;
            yValues.add(new Entry(quarterly[i],i));
        }


        //y轴集合
        PieDataSet pieDataSet = new PieDataSet(yValues,""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置每个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(32, 155, 200));
        colors.add(Color.rgb(91, 135, 130));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
}
