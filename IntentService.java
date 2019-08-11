package com.utils.ServiceExp;
/*
	后台服务， 定时触发一个回调函数。
*/
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.utils.MainActivity;
import com.utils.R;

import java.util.concurrent.TimeUnit;


public class ServicePool extends IntentService {		//继承自 intentService ，按队列顺序处理 intent 请求。
    private static final String TAG="ServicePool";
    //1 分钟的时间间隔
    private static final long INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public static final String ACTION_SHOW_NOTIFICATION = "com.utils.SHOW_NOTIFICATION";

    public static void setServiceAlarm(Context context,boolean isOn){
        Intent intent = ServicePool.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,intent,0);		//使用 pending intent 打包一个service intent，发送给 alarm manager  ，第一个参数是用来发送 intent的context，

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);	//alarm manager 系统服务来发送 intent 

        if(isOn){
            //setRepeating 设置重复定时器的时间间隔。
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,    //不精准的时间间隔，这样可以减少唤醒设备的开销，电量网络等
                    SystemClock.elapsedRealtime(),INTERVAL_MS,pi);
        }else{
            alarmManager.cancel(pi);    //撤销定时器
            pi.cancel();                //撤销 intent
        }
    }
    private static Intent newIntent(Context context){
        return new Intent(context, ServicePool.class);
    }

    public ServicePool(){
        super(TAG);
    }

    // 服务要做的事情放在这里
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if( !isNetworkAvailableAndConnected()){
            Log.i(TAG,"No network ...");
            return;
        }
        Log.i(TAG,"Handling intent ...");
        Resources resources = getResources();
        Intent intent1 = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent1,0);		// 用 pending intent 打包一个 activity intent 

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.notify_ticker))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)  // 使用 安卓自带的资源
                .setContentTitle(resources.getString(R.string.notify_title))         //标题
                .setContentText(resources.getString(R.string.notify_text))             //内容
                .setContentIntent(pi)           //当用户点击 notification 消息时，就会启动对应的 Pending intent
                .setAutoCancel(true)            //用户点击后，该 notification 自动消失（删除）
                .build();

        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(this);    //获取一个 notification manager实例
        notificationManager.notify(0,notification);     //发送 notification

        sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION));    //发送广播
    }

    //判断网络是否可用
    private boolean isNetworkAvailableAndConnected(){
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
            boolean isNetowrkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
            return isNetowrkConnected;
        }catch (Exception e ){
            Log.e(TAG,e.toString());
        }
        return false;
    }
}
/* manifest 添加权限：
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

还有 service 标签：
<service android:name=".ServiceExp.ServicePool"></service>


*/