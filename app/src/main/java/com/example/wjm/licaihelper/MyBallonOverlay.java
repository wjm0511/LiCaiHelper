package com.example.wjm.licaihelper;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.graphics.*;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.inner.GeoPoint;

class MyBallonOverlay extends Overlay {
	final static int picWidth=20;  //气球图的宽度
	final static int picHeight=34; //气球图的高度
	final static int arcR=8;//信息窗口的圆角半径
	
	static MyBallonOverlay currentBallon=null;//表示当前选中的气球
	String msg;	//此气球对应的文字信息
	String jdmc;//此气球对应的景点名称
	GeoPoint gp;//此气球对应的经纬度
	
	boolean showWindow=false;//是否显示文字信息窗口的标志位     为true显示文字信息窗口

	public MyBallonOverlay(GeoPoint gp,String msg,String jdmc)
	{
		this.gp=gp;
		this.msg=msg;
		this.jdmc=jdmc;
	}

    public boolean onTouchEvent(MotionEvent event, MapView mv) {
        /*if(currentBallon!=null&&currentBallon!=this)
        {
        	//若当前气球不为空且不是自己，什么都不做
        	return false;
        }
    	
    	if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
        	//若在气球上按下则设置当前气球为自己，且当前状态为在气球上
        	int x=(int)event.getX();
            int y=(int)event.getY();
            Point p= getPoint(mv); 
            
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;
            
            if(x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {
            	//若在自己这个气球上按下则设置自己为当前气球
            	currentBallon=this;
            	return true;
            }
        }
    	else if (event.getAction() == MotionEvent.ACTION_MOVE) 
    	{
    		//移动事件返回当前气球状态 若当前在气球上则返回true 屏蔽其他移动事件
    		return currentBallon!=null;
    	}    		
        else if (event.getAction() == MotionEvent.ACTION_UP) 
        {
        	//获取触控笔位置
            int x=(int)event.getX();
            int y=(int)event.getY();
            
            //获取气球在屏幕上的坐标范围
            //Point p= getPoint(mv);
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;           
            
            if(currentBallon==this&&x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {
            	//若当前气球为自己且在当前气球上抬起触控则显示当前气球内容
            	//显示完内容后清空当前气球
            	currentBallon=null;     
            	showWindow=!showWindow;
            	
            	List<Overlay> overlays = mv.getOverlays();
            	overlays.remove(this); //删除此气球再添加
            	overlays.add(this);    //此气球就位于最上面了
            	for(Overlay ol:overlays)
            	{
            	    //清除其他气球的showWindow标记
            		if(ol instanceof MyBallonOverlay)
            		{
            			if(ol!=this)
            			{
            				((MyBallonOverlay)ol).showWindow=false;
            			}
            		}
            	}
            	return true;
            }
            else if(currentBallon==this)
            {
            	//若当前气球为自己但抬起触控不再自己上则清空气球状态并返回true
            	currentBallon=null;
            	return true;            	
            }            
        }*/
        return false;
    }

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    	//将经纬度翻译成屏幕上的XY坐标
    	Point p= getPoint(mapView);
		//在坐标指定位置绘制气球
		canvas.drawBitmap(ZujiActivity.bitmap, p.x-picWidth/2, p.y-picHeight, null);
		
		if(showWindow)
		{
			//如果showWindow为true则显示信息窗口
			drawWindow(canvas,p,180);
		}
	}
    
    public Point getPoint(MapView mapView)
    {
    	//将经纬度翻译成屏幕上的XY坐标
    	//Projection projetion = mapView.getProjection();
		//Point p = new Point();
		//projetion.toPixels(gp, p);
		return null;
    }
    
	public void drawWindow(Canvas canvas,Point p,int winWidth) 
	{
		//绘制信息窗口的方法
		int charSize=15;
		int textSize=16;
		int leftRightPadding=2;
		
		//为信息分行
		int lineWidth=winWidth-leftRightPadding*2;//每行的宽度
		int lineCharCount=lineWidth/(charSize);	//每行字符数
		//扫描整个信息字符串进行分行
		ArrayList<String> alRows=new ArrayList<String>();//记录所有行的ArrayList
		String currentRow="";//当前行的字符串
		for(int i=0;i<msg.length();i++)
		{			
			char c=msg.charAt(i);
			if(c!='\n')
			{
				//若当前字符不为换行符则加入到当前行中
				currentRow=currentRow+c;
			}
			else
			{
				//若当前字符为换行符则检查当前行长度，若当前行长度大于零
				//则将当前行加入记录所有行的ArrayList
				if(currentRow.length()>0)
				{
					alRows.add(currentRow);
				}				
				currentRow="";//清空当前行
			}
			if(currentRow.length()==lineCharCount)	
			{
				//若当前行的长度达到一行规定的字符数则
		     	//将当前行加入记录所有行的ArrayList
				alRows.add(currentRow);
				currentRow="";//清空当前行
			}
		}
		if(currentRow.length()>0)
		{
			//若当前行长度大于零则将当前行加入记录所有行的ArrayList
			alRows.add(currentRow);
		}
		int lineCount=alRows.size();//获得总行数
		int winHeight=lineCount*(charSize)+2*arcR;//自动计算信息窗体高度
		//创建paint对象
		Paint paint=new Paint();
		paint.setAntiAlias(true);//打开抗锯齿
		paint.setTextSize(textSize);//设置文字大小	
		//绘制信息窗体圆角矩形
		paint.setARGB(255, 255,251,239);
		int x1=p.x-winWidth/2;
		int y1=p.y-picHeight-winHeight-1;
		int x2=x1+winWidth;
		int y2=y1+winHeight;		
		RectF r=new RectF(x1,y1,x2,y2);		
		canvas.drawRoundRect(r, arcR, arcR, paint);
		//绘制 信息窗体圆角矩形边框
		paint.setARGB(255,198,195,198);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRoundRect(r, arcR, arcR, paint);
		
		//循环绘制每行文字
		paint.setStrokeWidth(0);
		paint.setARGB(255, 10, 10, 10);		
		int lineY=y1+arcR+charSize;
		for(String lineStr:alRows)
		{
			//对每一行进行循环
			for(int j=0;j<lineStr.length();j++)
			{
				//对一行中的每个字循环
				String colChar=lineStr.charAt(j)+"";				
				canvas.drawText(colChar, x1+leftRightPadding+j*charSize, lineY, paint);
			}
			lineY=lineY+charSize;	//y坐标移向下一行
		}
	}
}


