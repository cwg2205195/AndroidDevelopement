package hunter.shell;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="hunter.Shell.Main";
    private Button btn_exec;
    private EditText et_cmd;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_exec=findViewById(R.id.btn_exec);
        et_cmd=findViewById(R.id.et_cmd);
        linearLayout = findViewById(R.id.ll_container);

        Handler handler=new Handler();                      //获取主线程的 Handler
        final CmdHandler<TextView> cmdHandler=new CmdHandler<>(handler);  //把主线程 Handler 传递给子线程，同时创建子线程 HandlerThread
        cmdHandler.setListener(new CmdHandler.ResultListener<TextView>() {
            @Override
            public void onCmdExecute(TextView target, String result) {      //主线程的消息回调将会调用这个方法，把结果展示出来
                target.setText(result);
            }
        });
        cmdHandler.start();         //启动子线程
        cmdHandler.getLooper();     //创建子线程的 Looper 消息队列
        btn_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String cmd = et_cmd.getText().toString();
                    if(cmd == null || cmd.isEmpty()){
                        return;
                    }
                    TextView textView=new TextView(getApplicationContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    textView.setTextColor(Color.parseColor("#A25CC2"));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    linearLayout.addView(textView);
                    //发送 Message 执行命令
                    cmdHandler.enqueueTask(textView,cmd);

                    //然后把命令的结果显示出来---》 这里是主线程的消息回调函数显示
                    //清除 edittext 的文字
                    et_cmd.setText("");
                }catch (Exception e){
                    Log.i(TAG,e.toString());
                }

            }
        });
        et_cmd.setHint(R.string.hint);
    }
}
