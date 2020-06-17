package com.cookandroid.p2016314024_07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import java.util.List;

public class FragMap extends Fragment {
    public static FragMap newInstance() {
        return new FragMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View view = inflater.inflate(R.layout.fragment_frag_map, null);
        Button button1 = (Button)view.findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            List<MapData> mapData = new ArrayList<MapData>();
            @Override
            public void onClick(View v) {
                try {
                    String rst = String.valueOf(new Task().execute().get());
                    JSONObject json = new JSONObject(rst);
                    JSONArray jArr = json.getJSONArray("List");

                    int mapId=0;
                    double latitude = 0;
                    double longitude = 0;
                    String name="";
                    String image="";
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);
                        mapId = Integer.parseInt(json.getString("mapId"));
                        latitude = Double.parseDouble(json.getString("latitude"));
                        longitude = Double.parseDouble(json.getString("longitude"));
                        name = json.getString("name");
                        image = json.getString("image");
                        mapData.add(new MapData(mapId,latitude,longitude, name, image));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((MainPage)getActivity()).replaceFragMap(FragMap2.newInstance(mapData));
            }
        });

        return view;
    }
    public class Task extends AsyncTask<Void, Void, String> {

        String sendMsg="", receiveMsg;
        String serverIp = "http://192.168.35.179:8080/0604/map/selectList.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(Void ... voids) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

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
}
