package com.asynchttpjson.OkHttpTool;
/*
ok http ����ʹ�ã� get �� post ����
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

    //����һ�� ����Get|Post�������� URL �� ���� �Լ� ���ݣ�Post����
    public static HttpTool Request(Context context,String url,Map<String,String> params,Map<String,String> data){
        return new HttpTool(context,url,params,data);
    }

    private HttpTool(Context context,String url,Map<String,String> params,Map<String,String> data){
        mContext = context;
        ReqUrl = url;
        mParamMap = params;
        dataMap  = data;
    }

    //����һ������ HTTP ��Ӧ�Ľӿڣ��ɷ��� HTTP ��������Լ�����������
    public interface ResponseListener<T>{
        void onResponse(Response response);
    }

    //�����Ƿ��� data ���й��� Get | Post ����
    private Request buildRequest(){
        try{
            Request.Builder reqBuilder = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(ReqUrl).newBuilder();
            if(mParamMap != null){            //Url ��������
                for(String param : mParamMap.keySet()){
                    urlBuilder.addQueryParameter(param, mParamMap.get(param));
                }
            }

            FormBody.Builder formBody=null;
            if( dataMap != null){               //���� Post ���󣬹��� data
                formBody=new FormBody.Builder();
                for(Map.Entry<String,String> entry: dataMap.entrySet()){    //�� Post���ǲ�����ʾ�ַ���������Ҫ�ȱ��룬����һ��ȱ��
                    formBody.add(entry.getKey() ,entry.getValue());
                }
            }

            // �滻 UA
            reqBuilder.url(urlBuilder.build()).removeHeader("User-Agent").addHeader("User-Agent",SystemUtil.getUserAgent(mContext));

            if( formBody == null)       //Get ����ֱ�ӷ��� build
                return reqBuilder.build();
            return reqBuilder.post(formBody.build()).build();   //���� Post ����
        }catch (Exception e){
            Log.i(TAG,e.toString());
            return null;
        }
    }

    //�������󣬱�����������Ӧ��������
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
                        mResponseListener.onResponse(response);     //������Ӧ
                    }

                }
            });
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }
}
