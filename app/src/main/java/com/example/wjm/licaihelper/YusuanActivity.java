package com.example.wjm.licaihelper;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by wjm on 2016/5/10.
 */
public class YusuanActivity extends Activity {
    private List<String> spendSubjectal=new ArrayList<String>();  //支出科目
    static List<String> yusuanAmountal=new ArrayList<String>();   //预算总额
    static List<String> yueAmountal=new ArrayList<String>();      //预算余额

    private int index;
    private double sum;
    private double yuesum;
    private double zongyue;
    private double spendAmount;
    private double zongSpendAmount;
    TextView zongyusuanTv;
    TextView zongyusuanTv2;
    private NumberDialog numDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yusuan);
        numDlg = new NumberDialog(YusuanActivity.this);
        final ListView yusuanlv=(ListView)this.findViewById(R.id.ListView01);

        //返回
        Button returnbutton=(Button) this.findViewById(R.id.fanhui);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YusuanActivity.this.finish();
            }
        });

        spendSubjectal=DBUtil.getSubjects("spendsubject");// 支出科目
        yusuanAmountal=DBUtil.getAmount("yusuan");  // 得到当前的预算总额
        yueAmountal=DBUtil.getAmount("yusuanyue");  //预算余额

        if (yusuanAmountal.size()!=spendSubjectal.size()) {
            DBUtil.deleteAllFromTable("yusuan");
            for (int i=0;i<spendSubjectal.size();i++)  //这里将各个科目的总预算值设置为1000，初始情况下
                DBUtil.insertYusuan(i,1000,"yusuan");
            yusuanAmountal=DBUtil.getAmount("yusuan");  // 得到当前的预算总额
        }

        //计算余额情况
        double temp;
        double temp_yue;
        if(yueAmountal.size()!=spendSubjectal.size()) {
            DBUtil.dropAllFromTable("yusuanyue");
            for(int i=0;i<spendSubjectal.size();i++) {
                temp=DBUtil.getYuAmountAllDetails(spendSubjectal.get(i));
                temp_yue=Double.parseDouble(yusuanAmountal.get(i))-temp;
                DBUtil.insertYusuan(i,temp_yue,"yusuanyue");
            }
        }else{
            for(int i=0;i<spendSubjectal.size();i++) {
                temp=DBUtil.getYuAmountAllDetails(spendSubjectal.get(i));
                DBUtil.updateYusuan(i,temp,"yusuanyue");
            }
        }

        yueAmountal=DBUtil.getAmount("yusuanyue");  //预算余额
        setYuSuanAdapter(yusuanlv,spendSubjectal,yusuanAmountal,yueAmountal);
        sum=DBUtil.zongYusuan("yusuan");
        zongyusuanTv = (TextView) this.findViewById(R.id.TextView02);//总预算额度
        for(int i=0;i<spendSubjectal.size();i++){
            if(Double.parseDouble(yusuanAmountal.get(i))!=0) {
                zongSpendAmount+=DBUtil.getYuAmountAllDetails(spendSubjectal.get(i));
            }
        }
        yuesum=sum-zongSpendAmount;
        zongyusuanTv2=(TextView)this.findViewById(R.id.TextView03);

        zongyusuanTv.setText(sum+"");
        zongyusuanTv2.setText("余额："+yuesum);

        ProgressBar pb=(ProgressBar)this.findViewById(R.id.progress_horizontal);
        pb.setMax(100);
        if (sum!=0) {
            pb.setSecondaryProgress(100);
            pb.setProgress((int)(100*(yuesum/sum)));
        } else {
            pb.setSecondaryProgress(0);
            pb.setProgress(0);
        }

        yusuanlv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index=position;
                numDlg.show();
                numDlg.addJineListener(
                        new NumberListener() {
                            @Override
                            public void doAction(String numStr) {
                                double newYusuanAmount=Double.parseDouble(numStr);
                                DBUtil.updateYusuan(index, newYusuanAmount,"yusuan"); //更新预算表
                                spendSubjectal=DBUtil.getSubjects("spendsubject");// 支出科目
                                yusuanAmountal=DBUtil.getAmount("yusuan");  // 得到当前的预算总额
                                yueAmountal=DBUtil.getAmount("yusuanyue");  //预算余额
                                sum=DBUtil.zongYusuan("yusuan");
                                for(int i=0;i<spendSubjectal.size();i++){
                                    if(Double.parseDouble(yusuanAmountal.get(i))!=0) {
                                        zongSpendAmount+=DBUtil.getYuAmountAllDetails(spendSubjectal.get(i));
                                    }
                                }
                                yuesum=sum-zongSpendAmount;
                                zongyusuanTv.setText(sum+"");
                                zongyusuanTv2.setText("余额："+yuesum);
                                setYuSuanAdapter(yusuanlv,spendSubjectal,yusuanAmountal,yueAmountal);
                            }
                        }
                );
            }
        });
    }
    public void setYuSuanAdapter(final ListView lv,final List<String> cl,final List<String> dl,final List<String> dl2){
        BaseAdapter ba=new BaseAdapter(){
            @Override
            public int getCount() {
                return cl.size();
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
                View view= LayoutInflater.from(YusuanActivity.this).inflate(R.layout.yusuanlist,null);
                TextView kemu=(TextView)view.findViewById(R.id.TextView01);
                ProgressBar progressBar=(ProgressBar)view.findViewById(R.id.progress_horizontal);
                TextView yusuan=(TextView)view.findViewById(R.id.TextView02);
                TextView yue=(TextView)view.findViewById(R.id.TextView03);
                kemu.setText(cl.get(position));
                double yuesum,sum;
                sum=Double.parseDouble(dl.get(position));
                yuesum=Double.parseDouble(dl2.get(position));
                progressBar.setMax(100);
                if (Double.parseDouble(dl.get(position))!=0) {
                    progressBar.setSecondaryProgress(100);
                    progressBar.setProgress((int) (100-100*(yuesum/sum)));
                } else {
                    progressBar.setSecondaryProgress(0);
                    progressBar.setProgress(0);
                }
                yusuan.setText(dl.get(position));
                yue.setText("余额："+(sum-Integer.parseInt(dl2.get(position))));
                return view;
            }
        };
        lv.setAdapter(ba);
        ba.notifyDataSetChanged();
    }

    private  void initUi(){
        spendSubjectal=DBUtil.getSubjects("spendsubject");
        yusuanAmountal=DBUtil.getAmount("yusuan");
        List<String[]> fl=DBUtil.getAmountAllDetails("zhichu");  //预算科目，总额
        for (int i=0;i<fl.size();i++) {
            if ((fl.get(i)[0]+"").equals(spendSubjectal.get(index)+"")) {
                spendAmount=DBUtil.getYuAmountAllDetails( spendSubjectal.get(index));
                break;
            } else
                spendAmount=0;
        }
        if (Double.parseDouble( yusuanAmountal.get(index))==0) {
            zongyue=0;
        } else
            zongyue=Double.parseDouble(yusuanAmountal.get(index))-spendAmount;
        DBUtil.updateYusuan(index,zongyue,"yusuanyue");
        spendSubjectal=DBUtil.getSubjects("spendsubject");
        yusuanAmountal=DBUtil.getAmount("yusuan");
        yueAmountal=DBUtil.getAmount("yusuanyue");
        ListView yusuanlv=(ListView) findViewById(R.id.ListView01);
        setYuSuanAdapter(yusuanlv, spendSubjectal, yusuanAmountal, yueAmountal);
    }
}
