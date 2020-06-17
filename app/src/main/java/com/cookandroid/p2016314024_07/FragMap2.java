package com.cookandroid.p2016314024_07;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class FragMap2 extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    private MapView mapView = null;

    public static FragMap2 newInstance(List<MapData> mapData) {
        FragMap2 fragment = new FragMap2();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) mapData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View view = inflater.inflate(R.layout.fragment_frag_map2,  null);
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng seoul = new LatLng(37.3424128,126.7269632);
        mMap.addMarker(new MarkerOptions().position(seoul).title("서울시청"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        Bundle bundle = getArguments();
        ArrayList<? extends MapData> list = bundle.getParcelableArrayList("list" );

        for (MapData data : list) {
            LatLng latlng = new LatLng(data.getLatitude(), data.getLongitude());
            mMap.addMarker(new MarkerOptions().snippet(data.getImage()).position(latlng).title(data.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.MAPS_RECEIVE)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ((MainPage)getActivity()).replaceFragInformation(FragInform.newInstance(marker.getTitle(), marker.getSnippet()));
                return false;
            }
        });
    }
}
