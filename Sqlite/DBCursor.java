package com.e.capstone;
/*
查询数据库，并构造模型层数据的 wrapper
*/
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.e.capstone.DB.DocTable;
import com.e.capstone.DB.JinfoTB;
import com.e.capstone.DB.NurseTB;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBCursor extends CursorWrapper {
    //读取当前记录，转为特定对象
    Cursor mCursor;
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public DBCursor(Cursor cursor) {
        super(cursor);
        mCursor=cursor;
        mCursor.moveToFirst();
    }

    public boolean isEmptyRet(){
        return mCursor.getCount()==0;
    }

    //获取所有 病人信息
    public List<Patient> GetPatiens(){
        List<Patient> patients=new ArrayList<>();
        if(isEmptyRet())
            return patients;
        try {
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast()){
                patients.add(GetPatient());
                mCursor.moveToNext();
            }
        }finally {
            mCursor.close();
        }
        return patients;
    }

    //获取一个病人信息
    public Patient GetPatient(){
        Patient patient=new Patient(null,null,null);
        if(isEmptyRet())
            return patient;
        patient.setName(mCursor.getString(
                mCursor.getColumnIndex(DB.PatientTB.Cols.NAME
        )));
        patient.setAge(mCursor.getInt(
                mCursor.getColumnIndex(DB.PatientTB.Cols.AGE
                )));
        patient.setSex(mCursor.getString(
                mCursor.getColumnIndex(DB.PatientTB.Cols.SEX
                )));
        patient.setAddr(mCursor.getString(
                mCursor.getColumnIndex(DB.PatientTB.Cols.ADDR
                )));
        patient.setpID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(DB.PatientTB.Cols.PID
                ))));
        /*patient.setBedID(mCursor.getString(
                mCursor.getColumnIndex(DB.PatientTB.Cols.BID
                )));*/
        patient.setdID(mCursor.getString(
                mCursor.getColumnIndex(DB.PatientTB.Cols.DID
                )));

        return patient;
    }

    //获取当前 doctor 的信息
    public Doctor GetDoctor(){
        Doctor doctor = new Doctor(null,null,null);
        if(isEmptyRet())
            return doctor;
        doctor.setName(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.NAME)
        ));
        doctor.setuName(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.UNAME)
        ));
        doctor.setuPwd(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.PWD)
        ));
        doctor.setDep(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.DEP)
        ));
        doctor.setTitle(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.TITLE)
        ));
        doctor.setPhone(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.PHONE)
        ));
        doctor.setdID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(DocTable.Cols.UUID))
        ));
        return doctor;
    }

    //获取所有 护工信息
    public List<Nurse> GetAllNurse(){
        List<Nurse> nurses=new ArrayList<>();
        if(isEmptyRet())
            return nurses;
        try{
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast()){
                nurses.add(GetNurse());
                mCursor.moveToNext();
            }
        }finally {
            mCursor.close();
        }
        return nurses;
    }

    //获取当前 护工信息
    public Nurse GetNurse(){
        Nurse nurse = new Nurse(null,null,null);
        if(isEmptyRet())
            return nurse;
        nurse.setName(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.NAME)
        ));
        nurse.setuName(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.UNAME)
        ));
        nurse.setuPwd(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.PWD)
        ));
        nurse.setAddr(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.ADDR)
        ));
        nurse.setWorkTime(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.WTIME)
        ));
        nurse.setnID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.UUID))
        ));
        nurse.setPhone(mCursor.getString(
                mCursor.getColumnIndex(NurseTB.Cols.PHONE)
        ));
        return nurse;
    }

    //获取家属信息
    public  Sib GetSib(){
        Sib sib= new Sib(null,null,null);
        if(isEmptyRet())
            return sib;
        sib.setAddr(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.ADDR)
        ));
        sib.setPhone(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.PHONE)
        ));
        sib.setPid(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.PID))));
        sib.setSid(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.UUID))));
        sib.setName(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.NAME)
        ));
        sib.setuPwd(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.PWD)
        ));
        sib.setuName(mCursor.getString(
                mCursor.getColumnIndex(DB.SibTB.Cols.UNAME)
        ));
        return sib;
    }

    //获取所有 工作信息
    public List<jobInfo> GetJobInfos(){
        List<jobInfo> infos = new ArrayList<>();
        if(isEmptyRet())
            return infos;
        try{
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast()){
                infos.add(GetSingleJobInfo());
                mCursor.moveToNext();
            }
        }finally {
            mCursor.close();
        }
        return infos;
    }

    //获取当前 工作信息
    private jobInfo GetSingleJobInfo(){
        jobInfo info = new jobInfo();
        if(isEmptyRet())
            return info;
        info.setDesc(mCursor.getString(
                mCursor.getColumnIndex(JinfoTB.Cols.DESCP)
        ));
        info.setdID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(JinfoTB.Cols.DID)
        )));
        info.setwID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(JinfoTB.Cols.WID)
        )));
        /*info.setnID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(JinfoTB.Cols.NID)
        )));*/
        info.setpID(UUID.fromString(mCursor.getString(
                mCursor.getColumnIndex(JinfoTB.Cols.PID)
        )));
        //工作时间段安排如何实现呢？
        info.setWorkTime(mCursor.getString(
                mCursor.getColumnIndex(JinfoTB.Cols.TIME)
        ));
        return info;
    }
}
