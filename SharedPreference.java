
import android.content.SharedPreferences;

public class MySharePrefs {
    SharedPreferences sp ;
    public MySharePrefs(Context context){
        sp = context.getSharedPreferences("mySP",Context.MODE_PRIVATE);		//sp �ļ��� mySP 
    }
    public void setValue(String key,String value){		//д�� sp 
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value).commit();
    }

    public String getValue(String key){			//��ȡ sp  
        return sp.getString(key,"");
    }
}