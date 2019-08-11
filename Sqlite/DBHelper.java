package com.e.capstone;
/*
创建数据库文件、初始化数据库表、字段的wrapper 
*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.e.capstone.DB.DocTable;
import com.e.capstone.DB.JinfoTB;
import com.e.capstone.DB.NurseTB;
import com.e.capstone.DB.PatientTB;
import com.e.capstone.DB.SibTB;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME= "hospital.db";	//数据库文件名

    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {			//创建所有的表和字段
        db.execSQL("create table " + NurseTB.Name + "(" +
                " _id integer primary key autoincrement, " +
                NurseTB.Cols.UUID + ", " +
                NurseTB.Cols.NAME + ", " +
                NurseTB.Cols.UNAME + ", " +
                NurseTB.Cols.PWD + ", " +
                NurseTB.Cols.ADDR + ", " +
                NurseTB.Cols.PHONE + ", " +
                NurseTB.Cols.WTIME + ")");
        db.execSQL("create table " + DocTable.Name+ "(" +
                " _id integer primary key autoincrement, " +
                DocTable.Cols.UUID + ", " +
                DocTable.Cols.NAME + ", " +
                DocTable.Cols.UNAME + ", " +
                DocTable.Cols.PWD + ", " +
                DocTable.Cols.DEP + ", " +
                DocTable.Cols.PHONE + ", " +
                DocTable.Cols.TITLE + ")");
        db.execSQL("create table " + PatientTB.Name + "(" +
                " _id integer primary key autoincrement, " +
                //PatientTB.Cols.UUID + " , " +
                PatientTB.Cols.NAME + " , " +
                PatientTB.Cols.AGE  + " , " +
                PatientTB.Cols.SEX  + " , " +
                PatientTB.Cols.ADDR + " , " +
                PatientTB.Cols.PID  + " , " +
                PatientTB.Cols.DID  + " , "+
                PatientTB.Cols.DESC  + ")");
        db.execSQL("create table " + JinfoTB.Name + "(" +
                " _id integer primary key autoincrement, " +
                JinfoTB.Cols.UUID + ", " +
                JinfoTB.Cols.PID  + ", " +
                //JinfoTB.Cols.NID  + ", " +
                JinfoTB.Cols.DID  + ", " +
                JinfoTB.Cols.WID + ", " +
                JinfoTB.Cols.TIME + ", " +
                JinfoTB.Cols.DESCP  + ")");
        db.execSQL("create table " + SibTB.Name + "(" +
                " _id integer primary key autoincrement, " +
                SibTB.Cols.PWD + ", " +
                SibTB.Cols.UNAME  + ", " +
                //JinfoTB.Cols.NID  + ", " +
                SibTB.Cols.UUID  + ", " +
                SibTB.Cols.NAME + ", " +
                SibTB.Cols.PHONE + ", " +
                SibTB.Cols.ADDR + ", " +
                SibTB.Cols.PID  + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}