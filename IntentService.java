package com.utils.ServiceExp;
/*
	��̨���� ��ʱ����һ���ص�������
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


public class ServicePool extends IntentService {		//�̳��� intentService ��������˳���� intent ����
    private static final String TAG="ServicePool";
    //1 ���ӵ�ʱ����
    private static final long INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public static final String ACTION_SHOW_NOTIFICATION = "com.utils.SHOW_NOTIFICATION";

    public static void setServiceAlarm(Context context,boolean isOn){
        Intent intent = ServicePool.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,intent,0);		//ʹ�� pending intent ���һ��service intent�����͸� alarm manager  ����һ���������������� intent��context��

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);	//alarm manager ϵͳ���������� intent 

        if(isOn){
            //setRepeating �����ظ���ʱ����ʱ������
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,    //����׼��ʱ�������������Լ��ٻ����豸�Ŀ��������������
                    SystemClock.elapsedRealtime(),INTERVAL_MS,pi);
        }else{
            alarmManager.cancel(pi);    //������ʱ��
            pi.cancel();                //���� intent
        }
    }
    private static Intent newIntent(Context context){
        return new Intent(context, ServicePool.class);
    }

    public ServicePool(){
        super(TAG);
    }

    // ����Ҫ���������������
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if( !isNetworkAvailableAndConnected()){
            Log.i(TAG,"No network ...");
            return;
        }
        Log.i(TAG,"Handling intent ...");
        Resources resources = getResources();
        Intent intent1 = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent1,0);		// �� pending intent ���һ�� activity intent 

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.notify_ticker))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)  // ʹ�� ��׿�Դ�����Դ
                .setContentTitle(resources.getString(R.string.notify_title))         //����
                .setContentText(resources.getString(R.string.notify_text))             //����
                .setContentIntent(pi)           //���û���� notification ��Ϣʱ���ͻ�������Ӧ�� Pending intent
                .setAutoCancel(true)            //�û�����󣬸� notification �Զ���ʧ��ɾ����
                .build();

        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(this);    //��ȡһ�� notification managerʵ��
        notificationManager.notify(0,notification);     //���� notification

        sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION));    //���͹㲥
    }

    //�ж������Ƿ����
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
/* manifest ���Ȩ�ޣ�
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

���� service ��ǩ��
<service android:name=".ServiceExp.ServicePool"></service>


*/