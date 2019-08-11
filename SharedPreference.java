
import android.content.SharedPreferences;

public class MySharePrefs {
    SharedPreferences sp ;
    public MySharePrefs(Context context){
        sp = context.getSharedPreferences("mySP",Context.MODE_PRIVATE);		//sp 文件名 mySP 
    }
    public void setValue(String key,String value){		//写入 sp 
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value).commit();
    }

    public String getValue(String key){			//读取 sp  
        return sp.getString(key,"");
    }
}