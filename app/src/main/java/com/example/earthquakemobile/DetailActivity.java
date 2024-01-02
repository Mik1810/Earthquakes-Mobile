package com.example.earthquakemobile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.earthquakemobile.databinding.DetailBinding;
import com.example.earthquakemobile.model.Earthquake;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_EARTHQUAKE = "extra";
    private final int MAX_ZOOM = 7;
    private final int RED_ZONE = Color.argb(75,255,0,0);
    private final int ORANGE_ZONE = Color.argb(60,200,100,0);
    private final int YELLOW_ZONE = Color.argb(50,255,255,0);
    private final float EARTHQUAKE_LOCATION_OFFSET = 0.3f;
    private DetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Earthquake earthquake = (Earthquake) getIntent().getSerializableExtra(EXTRA_EARTHQUAKE);
        if(earthquake != null){
            binding.setEarthquake(earthquake);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView3);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        initMap(googleMap);
        MarkerOptions options = getEarthquakeMarker();
        googleMap.addMarker(options);
        CircleOptions[] affectedArea = getAffectedArea(binding.getEarthquake());
        googleMap.addCircle(affectedArea[0]);
        googleMap.addCircle(affectedArea[1]);
        googleMap.addCircle(affectedArea[2]);
    }

    private void initMap(GoogleMap googleMap){
        googleMap.clear();
        Earthquake earthquake = binding.getEarthquake();
        LatLng latlng = new LatLng(earthquake.getLatitudine(),earthquake.getLongitudine());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,MAX_ZOOM));
        googleMap.setMaxZoomPreference(MAX_ZOOM);
        googleMap.setMinZoomPreference(MAX_ZOOM);
        LatLng notheastBound = new LatLng(earthquake.getLatitudine()-EARTHQUAKE_LOCATION_OFFSET,earthquake.getLongitudine()-EARTHQUAKE_LOCATION_OFFSET);
        LatLng southwestBound = new LatLng(earthquake.getLatitudine()+EARTHQUAKE_LOCATION_OFFSET,earthquake.getLongitudine()+EARTHQUAKE_LOCATION_OFFSET);
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(notheastBound,southwestBound));
    }
    private MarkerOptions getEarthquakeMarker(){
        Earthquake earthquake = binding.getEarthquake();
        MarkerOptions options = new MarkerOptions();
        options.title(earthquake.getTitle());
        options.position(new LatLng(earthquake.getLatitudine(),earthquake.getLongitudine()));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        return options;
    }

    private CircleOptions getAreaMarker(int radius){
        Earthquake earthquake = binding.getEarthquake();
        LatLng position = new LatLng(earthquake.getLatitudine(),earthquake.getLongitudine());
        CircleOptions circle = new CircleOptions();
        circle.center(position);
        circle.radius(radius);
        return circle;
    }

    private CircleOptions[] getAffectedArea(Earthquake earthquake){
        int redZoneArea = calculateRadius(earthquake.getMagnitude(),earthquake.getDepth());
        int orangeZoneArea = redZoneArea+15000;
        int yellowZoneArea = orangeZoneArea+20000;
        CircleOptions[] options = new CircleOptions[3];
        options[0] = getAreaMarker(redZoneArea);
        options[0].fillColor(RED_ZONE);
        options[0].strokeColor(RED_ZONE);
        options[1] = getAreaMarker(orangeZoneArea);
        options[1].fillColor(ORANGE_ZONE);
        options[1].strokeColor(ORANGE_ZONE);
        options[2] = getAreaMarker(yellowZoneArea);
        options[2].fillColor(YELLOW_ZONE);
        options[2].strokeColor(YELLOW_ZONE);
        return options;
    }

    private int calculateRadius(float magnitude, double depth){
        double radius = (Math.exp(magnitude)/depth)*2000;
        System.out.println(radius);
        return (int)radius;
    }
}
