/*
AsyncTask 与 UI线程分离， 一般用 AsyncTask 做网络请求，当完成请求后， 在 onPostExecute 主线程，

更新界面。 
*/

import android.net.Uri;
import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

//异步过程调用，类似子线程
//最后一个参数表示 doInBackground 方法的返回值以及 onPostExecute 方法的参数
//第一个类型参数表示 可以传递给execute 方法的参数，以及传递给 doInBackground 方法的参数
//第二个类型参数用来传递进度
public class AsyncHttpTask extends AsyncTask<AsyncHttpTask.Get, Void , Document> {
    private static final String TAG="AsyncHttpTask";

    //封装一个 get 请求
    public class Get{
        public String url;
        public HashMap<String,String> mParams;
        public Get(String urlSpec,HashMap<String,String> hashMap){
            url=urlSpec;
            mParams=hashMap;
        }
    }
    @Override
    protected Document doInBackground(Get ... params) {	//子线程将要执行的方法，返回的是Document 类型的对象
        try {
            HttpWorker worker=new HttpWorker(params[0]);

            return worker.work();
        }catch (Exception e){
            return null;
        }
    }
    @Override
    protected void onPostExecute(Document result){
        //在 UI 类中实现这个方法，这样才能刷新 UI

    }
    private class HttpWorker{
        private String uri;

        //解析并构造 get 请求
        public HttpWorker(Get request){
            Uri.Builder builder=Uri.parse(request.url).buildUpon();
            if( request.mParams != null){
                for(String param : request.mParams.keySet()){
                    builder.appendQueryParameter(param,request.mParams.get(param));
                }
            }
            uri=builder.build().toString();
            Log.i(TAG,"get "+uri);
        }
        //发起 get 请求 wrapper
        public Document work(){
            try {
                return Jsoup.connect(uri).get();
            }catch (Exception e){

            }
            return null;
        }
        //真正的 get 请求，返回的是原始 byte
        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }
        //封装的get请求，把返回的数据转为byte
        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }
    }
}

public class FragmentSearchResultList extends Fragment {
	
	//在 UI层继承  异步请求，当完成请求后，可以在 onPostExecute 更新界面 
    private class SearchEngine extends AsyncHttpTask{
        private static final String searchUrl="https://www.nvshens.net/girl/search.aspx";
        private  Get get;
        public SearchEngine(String name){
            HashMap map=new HashMap<String,String>();
            try{
                map.put("name", name);
            }catch (Exception e){
                Log.e(TAG,"encode "+name+" with UTF-8 failed ");
            }
           get= new Get(searchUrl,map);   //构造 get 请求
        }
        public void search(){
            this.execute(get);			//调用 AsyncTask 的 execute 方法，开始子线程执行，同时把参数 get 传递给它 
        }
        @Override
        protected void onPostExecute(Document result){		//在UI层更新界面
            parseSearchResultPage(result);
            //Log.i(TAG,"html :"+result);
            setupAdapter();
        }
        //解析搜索返回的页面,把每个mm的信息解析到一个 GirlInfo ，并加入到列表中
        private void parseSearchResultPage(Document html){
            //Document document= Jsoup.parse(html);
            //mGirlInfos = new ArrayList<>();
            mGirlInfos.clear();
            Document document= html;
            try{
                if( document == null){
                    Log.i(TAG,"the returned html can not be parsed");
                    return;
                }
                Log.i(TAG,"Let us work with returned html ");
                Element title = document.getElementsByTag("title").first();
                if(title != null){
                    Log.i(TAG,"title is "+title);
                }
                Element tbody  = document.getElementsByTag("tbody").first();	//获取 body 元素
                
                    Elements tds = tbody.getElementsByTag("td");	//获取所有 td 标签 
                    for(Element td : tds){
                        if(td == null)
                            break;
                        //获取缩略图
                        Element img = td.getElementsByTag("img").first();	//获取第一个 img 标签 
                        String thumbnailUrl = img.attr("src");
                        if(thumbnailUrl!=null)
                            Log.i(TAG,"Found thumbnail : "+thumbnailUrl);

                        //获取名字
                        Element a = td.getElementsByTag("a").get(1);
                        String name = a.text();
                        if(name != null)
                            Log.i(TAG,"name is "+name);

                        //获取主页
                        a = td.getElementsByTag("a").first();
                        String mainPageUrl = a.attr("href");
                        if(mainPageUrl != null)
                            Log.i(TAG,"main page "+mainPageUrl);

                        //构造模型层数据
                        GirlInfo girlInfo=new GirlInfo();
                        girlInfo.name=name;
                        girlInfo.thumbNail=thumbnailUrl;
                        girlInfo.mainPage="https://www.nvshens.net" + mainPageUrl;
                        mGirlInfos.add(girlInfo);

                    }
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }
        }
    }

}

