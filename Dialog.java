package com.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AboutFragment extends DialogFragment {	//�̳��� dialog Fragment 
    private String mMsg;
    private static final String ARG_MSG="ARG_MSG";
    private TextView mTVMsg;

    public static AboutFragment newInstance(String msg){
        Bundle args=new Bundle();
        args.putSerializable(ARG_MSG,msg);			//����Ҫ��ʾ����Ϣ
        AboutFragment aboutFragment=new AboutFragment();	//���� �¶���
        aboutFragment.setArguments(args);			//���ݲ��� 
        return aboutFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about,null);	//ʵ������Դ inflate 
        mTVMsg=v.findViewById(R.id.tv_aboutMsg);
        mMsg=(String)getArguments().getSerializable(ARG_MSG);		//��ȡ Fragment ���� 
        mTVMsg.setText(mMsg);
        return new AlertDialog.Builder(getActivity())
                .setView(v)									//������ʾ�� view 
                .setTitle(R.string.about)
                .setPositiveButton(R.string.ok,null)		//��һ����ť
                .create();
    }
}

/* �����ļ���  R.layout.dialog_about.xml 
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/tv_aboutMsg"
        android:layout_width="match_parent"
        android:layout_height="169dp"
        android:layout_gravity="center"
        android:gravity="center"
        android:textFontWeight="20dp"
        />
</LinearLayout>
*/
