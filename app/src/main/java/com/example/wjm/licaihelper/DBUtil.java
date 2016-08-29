package com.example.wjm.licaihelper;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBUtil {
    public static SQLiteDatabase createOrOpenDatabase()
    {
        SQLiteDatabase sld=null;
        try
        {
            sld=SQLiteDatabase.openDatabase
                    (
                            "/data/data/com.example.wjm.licaihelper/mydb",
                            null,
                            SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY
                    );
            String sql01="create table if not exists shouru" +
                    "("+
                    "id INTEGER PRIMARY KEY autoincrement," +
                    "isubject varchar(20),"+
                    "idate char(10)," +
                    "imode Varchar(20)," +
                    "iamount double," +
                    "iplace varchar(20)," +
                    "inote varchar(50)" +
                    ")";
            String sql02="create table if not exists zhichu"+
                    "("+
                    "id integer primary key AUTOINCREMENT,"+
                    "isubject varchar(20),"+
                    "idate varchar(10),"+
                    "imode varchar(20),"+
                    "iamount double,"+
                    "iplace varchar(20)," +
                    "inote varchar(50)"+
                    ")";
            String sql03="create table if not exists incomesubject"+
                    "("+

                    "subject varchar(20) primary key "+
                    ")";
            String sql04="create table if not exists spendsubject"+
                    "("+

                    "subject varchar(20) primary key"+

                    ")";
            String sql05="create table if not exists szmode"+
                    "("+

                    "subject varchar(20) primary key "+

                    ")";
            String sql06="create table if not exists yusuan"+
                    "("+
                    "id integer primary key,"+
                    "amount double  "+
                    ")";
            String sql07="create table if not exists yusuanyue"+
                    "("+
                    "id integer primary key ,"+
                    "amount double  "+

                    ")";
            String sql08="create table if not exists tixingtitle"+
                    "("+
                    "title varchar(20) primary key "+
                    ")";
            String sql09="create table if not exists tixingcontent"+
                    "("+
                    "id integer primary key ,"+
                    "title varchar(20) ,"+
                    "dtime varchar(10),"+
                    "cycle varchar(30),"+
                    "time varchar(5),"+
                    "note varchar(50)" +
                    ")";
            String sql10="create table if not exists dengebenjindetail"+
                    "("+
                    "id integer primary key ,"+
                    "benxi long,"+
                    "lixi long,"+
                    "benjin long,"+
                    "yubenjin long" +
                    ")";
            String sql11="create table if not exists dengebenxidetail"+
                    "("+
                    "id integer primary key ,"+
                    "benxi long,"+
                    "lixi long,"+
                    "benjin long,"+
                    "yubenjin long" +
                    ")";

            sld.execSQL(sql01);
            sld.execSQL(sql02);
            sld.execSQL(sql03);
            sld.execSQL(sql04);
            sld.execSQL(sql05);
            sld.execSQL(sql06);
            sld.execSQL(sql07);
            sld.execSQL(sql08);
            sld.execSQL(sql09);
            sld.execSQL(sql10);
            sld.execSQL(sql11);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sld;
    }

    public static void closeDatabase(SQLiteDatabase sld)
    {
        try
        {
            sld.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> searchKeMuZhichu(String start_date,String end_date)
    {
        SQLiteDatabase sld=null;
        ArrayList<String[]> list=new ArrayList<String[]>();
        try
        {
            sld=createOrOpenDatabase();//打开数据库
            String sql="select isubject,sum(iamount) from zhichu where idate>=? and idate<=? group by isubject";
            Cursor cur=sld.rawQuery(sql, new String[]{start_date,end_date});
            while(cur.moveToNext())
            {
                String str[]=new String[2];
                str[0]=cur.getString(0);//地点
                str[1]=cur.getString(1);//金额
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }

    public static ArrayList<String[]> searchZhanghuZhichu(String start_date,String end_date)
    {
        SQLiteDatabase sld=null;
        ArrayList<String[]> list=new ArrayList<String[]>();
        try
        {
            sld=createOrOpenDatabase();//打开数据库
            String sql="select imode,sum(iamount) from zhichu where idate>=? and idate<=? group by imode";
            Cursor cur=sld.rawQuery(sql, new String[]{start_date,end_date});
            while(cur.moveToNext())
            {
                String str[]=new String[2];
                str[0]=cur.getString(0);//支付方式
                str[1]=cur.getString(1);//金额
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }

    public static ArrayList<String[]> searchAddressZhichu(String start_date,String end_date)
    {
        SQLiteDatabase sld=null;
        ArrayList<String[]> list=new ArrayList<String[]>();
        try
        {
            sld=createOrOpenDatabase();//打开数据库
            String sql="select iplace,sum(iamount) from zhichu where idate>=? and idate<=? group by iplace";
            Cursor cur=sld.rawQuery(sql, new String[]{start_date,end_date});
            while(cur.moveToNext())
            {
                String str[]=new String[2];
                str[0]=cur.getString(0);//支付地点
                str[1]=cur.getString(1);//金额
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }

    public static ArrayList<String[]> searchAllAddressZhichu()
    {
        SQLiteDatabase sld=null;
        ArrayList<String[]> list=new ArrayList<String[]>();
        try
        {
            sld=createOrOpenDatabase();//打开数据库
            String sql="select iplace,sum(iamount) from zhichu";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[2];
                str[0]=cur.getString(0);//支付方式
                str[1]=cur.getString(1);//金额
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    /*============================================================================*/
    //计算器详情
    public static void insertDetail(String tablename,int id,long benxi,long lixi,long benjin,long yubenjin)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="insert into '"+tablename+"' values('"+id+"','"+benxi+"','"+lixi+"','"+benjin+"','"+yubenjin+"')";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static List<String[]> getDetail(String tablename){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select id,benxi,lixi,benjin,yubenjin from '"+tablename+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[5];
                str[0]=cur.getString(0);
                str[1]=cur.getString(1);
                str[2]=cur.getString(2);
                str[3]=cur.getString(3);
                str[4]=cur.getString(4);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    public static void updateDetail(int id,long benxi,long lixi,long benjin,long yubenjin,String tableName)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="update '"+tableName+"' set benxi='"+benxi+"',lixi='"+lixi+"',benjin='"+benjin+"',yubenjin='"+yubenjin+"' where id='"+id+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }

    /*=============================================================================*/
    //提醒
    public static void insertTiXing(int id,String title,String date,String cycle,String time,String note)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="insert into tixingcontent values('"+id+"','"+title+"','"+date+"','"+cycle+"','"+time+"','"+note+"')";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static List<String[]> getTiXing(){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select title,dtime,cycle,time from tixingcontent;";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[4];
                str[0]=cur.getString(0);
                str[1]=cur.getString(1);
                str[2]=cur.getString(2);
                str[3]=cur.getString(3);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    public static String  getTiXingTitle(int eventid){
        SQLiteDatabase sld=null;
        String titleStr="";
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select title from tixingcontent where id='"+eventid+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                titleStr=cur.getString(0);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return titleStr;
    }
    public static void insertSubjects(String str,String tableName)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="insert into '"+tableName+"' values('"+str+"')";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    //预算
    public static List<String> getAmount(String tablename)
    {
        SQLiteDatabase sld=null;
        List<String> subjectlist=new ArrayList<String>();
        try
        {
            sld=createOrOpenDatabase();
            String sql="select * from '"+tablename+"';" ;
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                subjectlist.add(cur.getString(1));
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return subjectlist;
    }
    public static void insertYusuan(int id,double amount,String tableName)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="insert into '"+tableName+"' values('"+id+"','"+amount+"');";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static void updateYusuan(int id,double amount,String tableName)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="update '"+tableName+"' set amount='"+amount+"' where id='"+id+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static double zongYusuan(String tableName)
    {   SQLiteDatabase sld=null;
        double sum=0;
        try
        {   sld=createOrOpenDatabase();
            String sql="select sum(amount) from '"+tableName+"' ;";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                sum=Double.parseDouble(cur.getString(0));
            }
            cur.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return sum;
    }
    //记账
    public static void insertZhangWu(String tableName,String isubject,String idate,String imode,double iamount,String iplace,String inote)
    {
        SQLiteDatabase sld=null;
        try{
            sld=createOrOpenDatabase();
            String sql="insert into '"+tableName+"' values(null,'"+isubject+"',"+
                    "'"+idate+"','"+imode+"','"+iamount+"','"+iplace+"','"+inote+"')";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static void deleteZhangwu(String tableName,String isubject,String idate,String imode,double iamount,String iplace,String inote){
        SQLiteDatabase sld=null;
        try{
            sld=createOrOpenDatabase();
            String sql="delete from '"+tableName+"' where isubject='"+isubject+"' and idate='"+idate+"' and imode='"+
                    imode+"' and iamount='"+iamount+"' and iplace='"+iplace+"' and inote='"+inote+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static List<String> getSubjects(String tableName)
    {
        SQLiteDatabase sld=null;
        List<String> subjectlist=new ArrayList<String>();
        try
        {
            sld=createOrOpenDatabase();
            String sql="select * from '"+tableName+"';" ;
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                subjectlist.add(cur.getString(0));
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return subjectlist;
    }
    public static List<String[]> getTodayDetails(String tablename,String idate){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select * from '"+tablename+"' where idate='"+idate+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[7];
                str[0]=cur.getString(1);
                str[1]=cur.getString(4);
                str[2]=cur.getString(3);
                str[3]=cur.getString(2);
                str[4]=cur.getString(0);//id
                str[5]=cur.getString(5);
                str[6]=cur.getString(6);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    public static List<String[]> getMonthYearDetails(String tablename,String idate){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select * from '"+tablename+"' where idate like '"+idate+"%"+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[7];
                str[0]=cur.getString(1);
                str[1]=cur.getString(4);
                str[2]=cur.getString(3);
                str[3]=cur.getString(2);
                str[5]=cur.getString(5);
                str[6]=cur.getString(6);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    public static List<String[]> getLastThreeMonthDetails(String tablename,String lastthreemonth,String lastmonthday){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select * from '"+tablename+"' where idate  between '"+lastthreemonth+"' and '"+lastmonthday+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[7];
                str[0]=cur.getString(1);
                str[1]=cur.getString(4);
                str[2]=cur.getString(3);
                str[3]=cur.getString(2);
                str[5]=cur.getString(5);
                str[6]=cur.getString(6);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    public static List<String[]> getAllDetails(String tablename){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select * from '"+tablename+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[7];
                str[0]=cur.getString(1);
                str[1]=cur.getString(4);
                str[2]=cur.getString(3);
                str[3]=cur.getString(2);
                str[5]=cur.getString(5);
                str[6]=cur.getString(6);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    //预算
    public static double getYuAmountAllDetails(String km){
        SQLiteDatabase sld=null;
        double sum=0;
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select sum(iamount) from zhichu group by isubject having isubject='"+km+"';";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                sum=Double.parseDouble(cur.getString(0));
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return sum;
    }
    public static List<String[]> getAmountAllDetails(String tablename){
        SQLiteDatabase sld=null;
        List<String[]> list=new ArrayList<String[]>();
        try{
            sld=createOrOpenDatabase();//打开数据库
            String sql="select distinct isubject,sum(iamount) from '"+tablename+"'group by isubject order by id asc;";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                String str[]=new String[2];
                str[0]=cur.getString(0);
                str[1]=cur.getString(1);
                list.add(str);
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return list;
    }
    //insert提醒标题
    public static void insertTiXingTitle(String biaoti,String tablename)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="insert into '"+tablename+"' values('"+biaoti+"');";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static List<String> getTiXingTitle(String tablename)
    {
        SQLiteDatabase sld=null;
        List<String> titlelist=new ArrayList<String>();
        try
        {
            sld=createOrOpenDatabase();
            String sql="select * from '"+tablename+"';" ;
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                titlelist.add(cur.getString(0));
            }
            cur.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
        return titlelist;
    }
    public static void deleteFromTable(String str0,String tableName)
    {
        SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="delete from '"+tableName+"' where subject='"+str0+"';";

            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static void deleteTixing(String str0,String str1,String str2,String str3,String tableName){
        SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="delete from '"+tableName+"' where title='"+str0+"' and dtime='"+
                    str1+"' and cycle='"+str2+"' and time='"+str3+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }

    }
    public static void deleteTixingTitle(String title,String tableName){
        SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="delete from '"+tableName+"' where title='"+title+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }

    }
    public static void deleteAllFromTable(String tableName)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="delete from '"+tableName+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
    public static void dropAllFromTable(String tableName)
    {   SQLiteDatabase sld=null;
        try
        {   sld=createOrOpenDatabase();
            String sql="drop table '"+tableName+"';";
            sld.execSQL(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
        }
    }
}


