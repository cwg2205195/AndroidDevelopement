package com.asynchttpjson.OkHttpTool;
/*
ok http 基本使用， get 和 post 请求。
*/
import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpTool  {
    private static final String TAG = "HttpTool";
    private String ReqUrl;
    private String ReqData;
    private String ReqUA;
    private String ReqHost;
    private String ReqReferer;
    private Map<String,String> dataMap;
    private Map<String,String> mParamMap;
    private Context mContext;

    public void setResponseListener(ResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

    private ResponseListener mResponseListener;

    //构造一个 请求（Get|Post），包括 URL 和 参数 以及 数据（Post请求）
    public static HttpTool Request(Context context,String url,Map<String,String> params,Map<String,String> data){
        return new HttpTool(context,url,params,data);
    }

    private HttpTool(Context context,String url,Map<String,String> params,Map<String,String> data){
        mContext = context;
        ReqUrl = url;
        mParamMap = params;
        dataMap  = data;
    }

    //定义一个处理 HTTP 响应的接口，由发起 HTTP 请求的类自己做解析工作
    public interface ResponseListener<T>{
        void onResponse(Response response);
    }

    //根据是否传入 data 进行构造 Get | Post 请求
    private Request buildRequest(){
        try{
            Request.Builder reqBuilder = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(ReqUrl).newBuilder();
            if(mParamMap != null){            //Url 参数构造
                for(String param : mParamMap.keySet()){
                    urlBuilder.addQueryParameter(param, mParamMap.get(param));
                }
            }

            FormBody.Builder formBody=null;
            if( dataMap != null){               //若是 Post 请求，构造 data
                formBody=new FormBody.Builder();
                for(Map.Entry<String,String> entry: dataMap.entrySet()){    //若 Post的是不可显示字符串，则需要先编码，这是一个缺陷
                    formBody.add(entry.getKey() ,entry.getValue());
                }
            }

            // 替换 UA
            reqBuilder.url(urlBuilder.build()).removeHeader("User-Agent").addHeader("User-Agent",SystemUtil.getUserAgent(mContext));

            if( formBody == null)       //Get 请求直接返回 build
                return reqBuilder.build();
            return reqBuilder.post(formBody.build()).build();   //返回 Post 请求
        }catch (Exception e){
            Log.i(TAG,e.toString());
            return null;
        }
    }

    //发起请求，必须先设置响应器！！！
    public void makeRequest(){
        try {
            OkHttpClient client=new OkHttpClient();
            client.newCall(buildRequest()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        mResponseListener.onResponse(response);     //处理响应
                    }

                }
            });
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }
}
