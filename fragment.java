package com.utils.FileUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utils.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.utils.HexUtils.HexTools.bytesToString;

public class FileDetailFragment extends Fragment {
    private static String TAG_FILE_DEATAIL="tag_file_detail";
    private TextView mTvFileName;
    private File mFile;
    private EditText mEtFile;
    private static final String  ARG_FILE_OBJ="file_obj";
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private byte[] mByteRead;
	
	//通过 bundle 传递 参数给 fragment 
    public static FileDetailFragment newInstance(File file){
        Bundle args=new Bundle();
        args.putSerializable(ARG_FILE_OBJ,file);
        FileDetailFragment fragment=new FileDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFile=(File)getArguments().getSerializable(ARG_FILE_OBJ);   //获取传递给fragment的参数
        try {
            mFileInputStream=new FileInputStream(mFile);
        }catch (Exception e){
            Log.e(TAG_FILE_DEATAIL,"File not found");
        }

    }

    @Nullable
    @Override
	//这里初始化界面布局
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_file_detail,container,false);//初始化 fragment 资源
        mTvFileName=view.findViewById(R.id.tv_fileName);
        mTvFileName.setText(mFile.getName());
        mEtFile=view.findViewById(R.id.et_file);
        mByteRead=new byte[(int)mFile.length()];
        try{
            mFileInputStream.read(mByteRead);
            mEtFile.setText(bytesToString(mByteRead));
        }catch (Exception e){
            Log.e(TAG_FILE_DEATAIL,"file input stream failed to init");
        }



        return view;
    }
}
