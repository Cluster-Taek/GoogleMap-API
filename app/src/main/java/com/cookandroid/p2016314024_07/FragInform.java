package com.cookandroid.p2016314024_07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragInform extends Fragment {
    public static FragInform newInstance() {
        return new FragInform();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View view = inflater.inflate(R.layout.fragment_frag_inform, null);
        ImageView iv = (ImageView)view.findViewById(R.id.imageView);
        TextView tv = (TextView)view.findViewById(R.id.textView);
        return view;
    }

}
