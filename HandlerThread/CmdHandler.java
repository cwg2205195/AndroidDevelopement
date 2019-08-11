package hunter.shell;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

public class CmdHandler<T> extends HandlerThread {
    private static final String TAG = "CmdHandler";
    private Handler responseHandler;        //主线程 Handler
    private Handler requestHandler;         //子线程 Handler
    private ConcurrentHashMap<T,String> mconcurrentHashMap = new ConcurrentHashMap<>();  //命令 和 View 关联
    private ResultListener<T> mListener;    //主线程把监听结果的对象传递给子线程
    private boolean mhasQuit;
    private static final int MESSAGE_EXECUTE = 0;   //消息代号

    public void setListener(ResultListener<T> Listener) {
        this.mListener = Listener;
    }

    public interface ResultListener<T>{
        void onCmdExecute(T target,String result);
    }

    public CmdHandler(Handler handler) {
        super(TAG);
        responseHandler = handler;
    }

    @Override
    public boolean quit(){
        mhasQuit = true ;
        return super.quit();
    }

    //在 looper首次处理消息队列前，会调用 该函数，因此在这里设置消息的处理函数 Handler
    @Override
    protected void onLooperPrepared(){
        requestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == MESSAGE_EXECUTE){
                    T target = (T)msg.obj;
                    Log.i(TAG,"Receive a command " + mconcurrentHashMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    //真正处理消息的方法
    private void handleRequest(final T target){
        try {
            final String cmd = mconcurrentHashMap.get(target);  //根据当前View 获取要执行的命令
            if(cmd == null || cmd.isEmpty()){
                return;
            }
            final String result = ExecCmd(cmd);       //执行命令，把结果写到result
            if(result == null){
                return;
            }
            responseHandler.post(new Runnable() {       //消息处理完毕，通知消息发送者可以处理结果了
                @Override
                public void run() {
                    if (mconcurrentHashMap.get(target) != cmd|| mhasQuit){
                        return;
                    }
                    mconcurrentHashMap.remove(target);          //把消息从队列移除
                    mListener.onCmdExecute(target,result);      //显示 result
                }
            });

        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }

    //把消息放入线程消息队列
    public void enqueueTask(T target,String cmd){
        if(cmd == null){
            mconcurrentHashMap.remove(target);
        }else{
            mconcurrentHashMap.put(target,cmd);     //数据关联到UI组件
            requestHandler.obtainMessage(MESSAGE_EXECUTE,target).sendToTarget();    //从当前线程关联的Handler获取消息并发送
        }
    }

    //java 执行 cmd 把结果返回
    private String ExecCmd(String cmd){
        try{
            Runtime runtime= Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb=new StringBuilder();
            String result;
            while((result= bufferedReader.readLine()) != null){
                sb.append(result+"\n");
            }
            return sb.toString();
        }catch (Exception e){
            Log.i(TAG,e.toString());
            return null;
        }
    }
}
