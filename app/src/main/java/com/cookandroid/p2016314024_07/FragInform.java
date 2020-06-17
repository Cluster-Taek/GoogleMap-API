package com.cookandroid.p2016314024_07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FragInform extends Fragment {
    public static FragInform newInstance(String title, String image) {
        FragInform fragment = new FragInform();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("image", image);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View view = inflater.inflate(R.layout.fragment_frag_inform, null);
        ImageView iv = (ImageView)view.findViewById(R.id.imageView);
        TextView tv = (TextView)view.findViewById(R.id.textView2);
        Bundle bundle = getArguments();
        String str = null;
        try {
            str = new Task().execute(bundle.getString("title").toString()).get();
            String url = "http://192.168.35.179:8080/0604/images/" + bundle.getString("image");
            ImageLoadTask task = new ImageLoadTask(url, iv);
            task.execute();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tv.setText(str);
        return view;
    }

    public class Task extends AsyncTask<String, Void, String> {

        String sendMsg="", receiveMsg;
        String serverIp = "http://192.168.35.179:8080/0604/map/selectByName.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String ... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "name="+strings[0];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }

    }

    public class ImageLoadTask extends AsyncTask <Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;
        private HashMap<String, Bitmap> hashMap = new HashMap<>();

        // 어떤 url 로 요청할 지, 응답을 받은 후 어떤 이미지뷰에 설정할 지 전달받음
        public ImageLoadTask(String urlStr, ImageView imageView){
            this.urlStr = urlStr;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // 웹서버의 이미지 데이터를 받아 비트맵 객체로 만든다.
            Bitmap bitmap = null;
            try {
                // 메모리에 만들어진 후 해제되지 않으면 메모리에 계속 남아있는다.
                // 여러 이미지를 로딩하게 되면 메모리가 부족해지는 문제가 발생할 수 있으므로
                // 사용하지 않는 비트맵 객체는 recycle() 메소드를 이용해 즉시 해제시킨다.
                if(hashMap.containsKey(urlStr)){  // 요청 주소가 들어있다면 비트맵을 꺼냄
                    Bitmap oldBitmap = hashMap.remove(urlStr);
                    if(oldBitmap != null){
                        oldBitmap.recycle();    // 들어왔던 비트맵을 메모리에 제거
                        oldBitmap = null;
                    }
                }

                URL url = new URL(urlStr);
                // 주소로 접속하여 이미지 스트림을 받고, decodeStream 을 통해 비트맵으로 바꿈
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                hashMap.put(urlStr, bitmap); // 새 비트맵을 넣음
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);   // 비트맵을 이미지뷰에 설정
            imageView.invalidate(); // 이미지를 다시 그림
        }
    }

}
