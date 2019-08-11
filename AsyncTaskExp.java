/*
AsyncTask �� UI�̷߳��룬 һ���� AsyncTask ���������󣬵��������� �� onPostExecute ���̣߳�

���½��档 
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

//�첽���̵��ã��������߳�
//���һ��������ʾ doInBackground �����ķ���ֵ�Լ� onPostExecute �����Ĳ���
//��һ�����Ͳ�����ʾ ���Դ��ݸ�execute �����Ĳ������Լ����ݸ� doInBackground �����Ĳ���
//�ڶ������Ͳ����������ݽ���
public class AsyncHttpTask extends AsyncTask<AsyncHttpTask.Get, Void , Document> {
    private static final String TAG="AsyncHttpTask";

    //��װһ�� get ����
    public class Get{
        public String url;
        public HashMap<String,String> mParams;
        public Get(String urlSpec,HashMap<String,String> hashMap){
            url=urlSpec;
            mParams=hashMap;
        }
    }
    @Override
    protected Document doInBackground(Get ... params) {	//���߳̽�Ҫִ�еķ��������ص���Document ���͵Ķ���
        try {
            HttpWorker worker=new HttpWorker(params[0]);

            return worker.work();
        }catch (Exception e){
            return null;
        }
    }
    @Override
    protected void onPostExecute(Document result){
        //�� UI ����ʵ�������������������ˢ�� UI

    }
    private class HttpWorker{
        private String uri;

        //���������� get ����
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
        //���� get ���� wrapper
        public Document work(){
            try {
                return Jsoup.connect(uri).get();
            }catch (Exception e){

            }
            return null;
        }
        //������ get ���󣬷��ص���ԭʼ byte
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
        //��װ��get���󣬰ѷ��ص�����תΪbyte
        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }
    }
}

public class FragmentSearchResultList extends Fragment {
	
	//�� UI��̳�  �첽���󣬵��������󣬿����� onPostExecute ���½��� 
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
           get= new Get(searchUrl,map);   //���� get ����
        }
        public void search(){
            this.execute(get);			//���� AsyncTask �� execute ��������ʼ���߳�ִ�У�ͬʱ�Ѳ��� get ���ݸ��� 
        }
        @Override
        protected void onPostExecute(Document result){		//��UI����½���
            parseSearchResultPage(result);
            //Log.i(TAG,"html :"+result);
            setupAdapter();
        }
        //�����������ص�ҳ��,��ÿ��mm����Ϣ������һ�� GirlInfo �������뵽�б���
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
                Element tbody  = document.getElementsByTag("tbody").first();	//��ȡ body Ԫ��
                
                    Elements tds = tbody.getElementsByTag("td");	//��ȡ���� td ��ǩ 
                    for(Element td : tds){
                        if(td == null)
                            break;
                        //��ȡ����ͼ
                        Element img = td.getElementsByTag("img").first();	//��ȡ��һ�� img ��ǩ 
                        String thumbnailUrl = img.attr("src");
                        if(thumbnailUrl!=null)
                            Log.i(TAG,"Found thumbnail : "+thumbnailUrl);

                        //��ȡ����
                        Element a = td.getElementsByTag("a").get(1);
                        String name = a.text();
                        if(name != null)
                            Log.i(TAG,"name is "+name);

                        //��ȡ��ҳ
                        a = td.getElementsByTag("a").first();
                        String mainPageUrl = a.attr("href");
                        if(mainPageUrl != null)
                            Log.i(TAG,"main page "+mainPageUrl);

                        //����ģ�Ͳ�����
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

