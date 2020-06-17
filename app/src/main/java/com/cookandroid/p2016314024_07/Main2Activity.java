package com.cookandroid.p2016314024_07;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback {

    static final LatLng SEOUL = new LatLng(37.5665, 126.9780);
    static final LatLng INCHEON = new LatLng(37.4000, 126.6100);
    private GoogleMap map;
    Marker seoul, incheon, here;
    GroundOverlayOptions videoMarks;
    LatLng myPosition;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            myPosition = new LatLng(latitude, longitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                videoMarks = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.presence_video_busy)).position(latLng, 100f, 100f);
                map.addGroundOverlay(videoMarks);
            }
        });
        seoul = map.addMarker(new MarkerOptions().position(SEOUL).title("서울").snippet("도시"));
        incheon = map.addMarker(new MarkerOptions().position(INCHEON).title("인천").snippet("도시"));
        //here = map.addMarker(new MarkerOptions().position(myPosition).title("여기"));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                String title = marker.getTitle();
                Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);
                intent.putExtra("title", title);
                startActivity(intent);
                //Toast.makeText(Main2Activity.this, "위도:" + latLng.latitude + "경도:" + latLng.longitude, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10),2000, null);
    }
}
