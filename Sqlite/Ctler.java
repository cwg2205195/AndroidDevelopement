package com.e.capstone;
/*
控制器，从UI层构造模型层对象，然后再写入到数据库的表中。
*/
import android.app.job.JobInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.e.capstone.DB.DocTable;
import com.e.capstone.DB.JinfoTB;
import com.e.capstone.DB.NurseTB;
import com.e.capstone.DB.PatientTB;
import com.e.capstone.DB.SibTB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ctler implements Serializable {
    Human mHuman;
    Context mContext;
    private static SQLiteDatabase mDatabase;



    public void CloseDB(){
        mDatabase.close();
    }

    public static void insertDb(String tableName,String param,ContentValues contentValues){
        mDatabase.insert(tableName,param,contentValues);
    }

    public static Cursor QueryFromTable(String tableName,String whereClause,String [] whereArgs){
        // 执行 query ，返回 cursor 对象，供 DBCursor 生成特定的对象
        Cursor cursor = mDatabase.query(tableName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return cursor;

    }

    public void deleteRecord(String tableName,String whereClause,String [] whereArgs){
        mDatabase.delete(tableName,whereClause,whereArgs);
    }

    public void updateRecord(String tableName,ContentValues values,String whereClause,String []whereArgs){
        mDatabase.update(tableName,values,whereClause,whereArgs);
    }

    public Ctler(Human human,Context context) {
        mHuman = human;
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(mContext).getWritableDatabase();
    }



    public Ctler(Doctor d){
        mHuman=d;
    }
    public  Ctler(Nurse n){
        mHuman=n;
    }
    public Ctler(Patient p){
        mHuman=p;
    }

    public boolean WriteDb(){
        return true;
    }
    public boolean ReadDb(){
        return true;
    }

    //写数据库时需要进行转换
    public static ContentValues getContentValues(jobInfo info){
        ContentValues values = new ContentValues();
        values.put(JinfoTB.Cols.UUID , info.getpID().toString());
        values.put(JinfoTB.Cols.PID,info.getpID().toString());
//        values.put(JinfoTB.Cols.NID,info.getnID().toString());
        values.put(JinfoTB.Cols.DID,info.getdID().toString());
        values.put(JinfoTB.Cols.WID,info.getwID().toString());
        values.put(JinfoTB.Cols.TIME,info.getWorkTime());
        values.put(JinfoTB.Cols.DESCP,info.getDesc());

        return values;
    }
    public static ContentValues getContentValues(Doctor doctor){
        ContentValues values = new ContentValues();
        values.put(DocTable.Cols.UUID , doctor.getdID().toString());
        values.put(DocTable.Cols.NAME,doctor.getName());
        values.put(DocTable.Cols.UNAME,doctor.getuName());
        values.put(DocTable.Cols.PWD,doctor.getuPwd());
        values.put(DocTable.Cols.DEP,doctor.getDep());
        values.put(DocTable.Cols.PHONE,doctor.getPhone());
        values.put(DocTable.Cols.TITLE,doctor.getTitle());

        return values;
    }
    public static ContentValues getContentValues(Nurse nurse){
        ContentValues values = new ContentValues();
        values.put(NurseTB.Cols.UUID , nurse.getnID().toString());
        values.put(NurseTB.Cols.NAME,nurse.getName());
        values.put(NurseTB.Cols.UNAME,nurse.getuName());
        values.put(NurseTB.Cols.PWD,nurse.getuPwd());
        values.put(NurseTB.Cols.ADDR,nurse.getAddr());
        values.put(NurseTB.Cols.PHONE,nurse.getPhone());
        values.put(NurseTB.Cols.WTIME,nurse.getWorkTime());

        return values;
    }
    public static ContentValues getContentValues(Patient patient){
        ContentValues values = new ContentValues();
        values.put(PatientTB.Cols.NAME,patient.getName());
        values.put(PatientTB.Cols.AGE,patient.getAge());
        values.put(PatientTB.Cols.SEX,patient.getSex());
        values.put(PatientTB.Cols.ADDR,patient.getAddr());
        values.put(PatientTB.Cols.PID,patient.getpID().toString());           //空指针= =
        //values.put(PatientTB.Cols.PID,patient.hashCode());
       // values.put(PatientTB.Cols.BID,patient.getBedID());
        values.put(PatientTB.Cols.DID,patient.getdID());
        values.put(PatientTB.Cols.DESC,patient.getDesc());

        return values;
    }
    public static ContentValues getContentValues(Sib sib){
        ContentValues values = new ContentValues();
        values.put(SibTB.Cols.PWD,sib.getuPwd());
        values.put(SibTB.Cols.UNAME,sib.getuName());
        values.put(SibTB.Cols.UUID,sib.getSid().toString());
        values.put(SibTB.Cols.NAME,sib.getName());
        values.put(SibTB.Cols.PHONE,sib.getPhone());
        values.put(SibTB.Cols.ADDR,sib.getAddr());
        values.put(SibTB.Cols.PID,sib.getPid().toString());

        return values;
    }

    public Patient GetPatientByPID(UUID pid){
        String []args={pid.toString()};
        DBCursor dbCursor=new DBCursor(QueryFromTable(PatientTB.Name,PatientTB.Cols.PID+ "=?",args));
        return dbCursor.GetPatient();

    }
}

//用登录、注册控制器类，用于响应注册或登录用户
//2019.2.1- 新增病人家属登录注册功能
class SignInUpControler extends Ctler implements Serializable {
    //注册时要写数据库，因此需要对象转记录
    public SignInUpControler(Human human,Context context){
        super(human,context);           //只要是人就可以登录，除了病人
    }

    //登录验证函数，成功返回对应的控制器对象，失败返回 null 对象
    public Ctler SignIn(){
        return CheckUser();
    }

    /*登录验证函数，私有，验证成功返回 对应的控制器对象
     根据内部对象human，判断他的类型，执行 sql 查询语句，查询对应的表*/
    private Ctler CheckUser(){
        if( mHuman.getClass().equals(Doctor.class)){
            //医生登录，查询医生表
            DBCursor dbCursor=new DBCursor(QueryFromTable(DocTable.Name,
                    DocTable.Cols.UNAME + " = ? and "+
                    DocTable.Cols.PWD + " = ?",
                    new String[]{mHuman.getuName(),mHuman.getuPwd()}));
            Doctor doctor=dbCursor.GetDoctor();
            if((doctor.uName!=null && doctor.uPwd!=null)){
                mHuman = doctor;        //必须把结果赋值给 mHuman，否则返回的控制器对象将不能访问 真正的对象信息
                return new DocControler(mHuman,mContext);
            }
        }else if (mHuman.getClass().equals(Nurse.class)){
            //护工登录，查询护工表
            DBCursor dbCursor=new DBCursor(QueryFromTable(NurseTB.Name,
                    NurseTB.Cols.UNAME + " = ? and "+
                            NurseTB.Cols.PWD + " = ?",
                    new String[]{mHuman.getuName(),mHuman.getuPwd()}));
            Nurse nurse=dbCursor.GetNurse();
            if(nurse.uName!=null && nurse.uPwd!=null ){
                mHuman = nurse;         //必须把结果赋值给 mHuman，否则返回的控制器对象将不能访问 真正的对象信息
                return new NurControler(mHuman,mContext);
            }
        }else if(mHuman.getClass().equals(Sib.class)){
            //家属登录,查询家属表
            DBCursor dbCursor=new DBCursor(QueryFromTable(SibTB.Name,
                    SibTB.Cols.UNAME + " = ? and " +
                    SibTB.Cols.PWD + " = ?",
                    new String[]{mHuman.getuName(),mHuman.getuPwd()}));
            Sib sib=dbCursor.GetSib();
            if(sib.uName!=null && sib.uPwd!=null ){
                mHuman=sib;
                return new SibControler(mHuman,mContext);
            }

        }
        return null;
    }

    //注册，成功返回true
    public boolean SignUp(Human sb){
        ContentValues values ;
        if (sb.getClass().equals(Nurse.class )){
            //护士注册
            values = getContentValues((Nurse)sb);
            insertDb(NurseTB.Name,null,values);
        }else if(sb.getClass().equals(Doctor.class)){
            //医生注册
            values = getContentValues((Doctor)sb);
            insertDb(DocTable.Name,null,values);
        }else if(sb.getClass().equals(Sib.class)){
            //家属注册
            values = getContentValues((Sib)sb);
            insertDb(SibTB.Name,null,values);
        }else{
            return false;
        }
        return true;
    }
    //对象转数据库记录

}

//医生控制器类，用于响应医生的请求
class DocControler extends Ctler  {

    public Doctor GetMyFullInfo(String uname,String pwd){
       // Doctor doctor=new Doctor(name,uname,pwd);
        String args[]={uname,pwd};
        DBCursor dbCursor=new DBCursor(QueryFromTable(DocTable.Name,"uname=? and pwd=?",args));
        return dbCursor.GetDoctor();
    }

    public void UpdateInfo(Doctor doctor){
        String args[]={doctor.getdID().toString()};
        updateRecord(DocTable.Name,getContentValues(doctor)," uuid=? ",args);
    }

    public String GetMyName(){
        return mHuman.getName();
    }
    //医生可以添加病人信息，添加护理信息，查询护工信息
    public DocControler(Human human,Context context) {
        super(human,context);
    }

    //医生主界面，获取所有病人信息
    public List<Patient> GetPatients(){
        DBCursor dbCursor=new DBCursor(QueryFromTable(PatientTB.Name,null,null));
        return dbCursor.GetPatiens();
    }

    public boolean AddPatient(Patient patient){
        ContentValues values = getContentValues(patient);
        insertDb(PatientTB.Name,null,values);
        return true;
    }

    //获取所有的护工信息
    public static List<Nurse> GetNurses(){
        //List<Nurse> nurses=null;
        DBCursor dbCursor = new DBCursor( QueryFromTable(NurseTB.Name,null,null));
        return dbCursor.GetAllNurse();
        /*nurses = dbCursor.GetAllNurse();
        return nurses;*/
    }

    public static List<Nurse> QueryAvaNurse(String jobTime){
        //查询所有护工，选择工作时间段符合的护工，返回 护工列表，用 DBCursor 解析出护工对象
        //jobTime 格式"yyyy-mm-dd hh:mm-hh:mm"
        List<Nurse> nurses;
        DBCursor dbCursor = new DBCursor( QueryFromTable(NurseTB.Name,null,null));
        nurses = dbCursor.GetAllNurse();
        for(int i =0;i<nurses.size();){
            if(!StringNDate.CanWork(jobTime,nurses.get(i).getWorkTime())){//移除所有不符合工作时间的护工数据
                nurses.remove(i);
                continue;
            }
            i++;
        }
        return nurses;
    }

    //根据病人 uuid查找sib表，如果sib表有结果，则返回与病人关联的 家属信息
    public static Sib GetPatientSib(UUID uuidPatient){
        String [] args={uuidPatient.toString()};
        DBCursor dbCursor=new DBCursor(QueryFromTable(SibTB.Name,"pid=?",args));
        return dbCursor.GetSib();
        //Sib sib=dbCursor.GetSib();

        //return sib;
    }

    public boolean AddJobInfo(jobInfo info){
        ContentValues values =getContentValues(info);
        insertDb(JinfoTB.Name,null,values);
        return true;
    }

}

//护士控制器类，用于响应护士的请求
class NurControler extends Ctler  {
    //护士可以查询工作内容，修改自己的工作时间
    public NurControler(Human human,Context context) {
        super(human,context);
        DBCursor dbCursor=new DBCursor(QueryFromTable(NurseTB.Name,
                NurseTB.Cols.NAME + " = ? ",
                new String[]{human.getName()}));
        mHuman=dbCursor.GetNurse();
    }
    public void UpdateInfo(Nurse nurse){
        String args[]={nurse.getnID().toString()};
        updateRecord(NurseTB.Name,getContentValues(nurse)," uuid=? ",args);
    }

    public List<jobInfo> GetJobInfo(){
        //根据护工自己的 UUID，查询工作安排表，返回工作安排对象列表，用于展示给护工
        DBCursor dbCursor = new DBCursor( QueryFromTable(JinfoTB.Name,
                NurseTB.Cols.UUID + " = ? ",
                 new String[]{((Nurse)mHuman).getnID().toString()}));
        return dbCursor.GetJobInfos();
    }

    public boolean AddWorkTime(String workTime){
        //由视图层 把 Calendar 对象转为 String， 在此处添加工作时间到护工的工作时间段中,更新数据库
        Nurse myself = (Nurse)mHuman;

        myself.setWorkTime(myself.getWorkTime()+workTime);
        ContentValues values =  getContentValues(myself);
        updateRecord(NurseTB.Name,
                values,
                NurseTB.Cols.UUID + " = ? ",
                new String[]{myself.getnID().toString()});

        return true;
    }

}

//家属专用控制器
class SibControler extends Ctler{
    SibControler(Human human,Context context){
        super(human,context);
        DBCursor dbCursor=new DBCursor(QueryFromTable(SibTB.Name,
                            SibTB.Cols.NAME + " = ? ",
                        new String[]{human.getName()}));
        mHuman=dbCursor.GetSib();
    }


}