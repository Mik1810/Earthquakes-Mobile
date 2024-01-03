package com.example.earthquakemobile;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.earthquakemobile.databinding.FragmentMapBinding;
import com.example.earthquakemobile.model.Earthquake;
import com.example.earthquakemobile.service.LocationHelper;
import com.example.earthquakemobile.service.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private GoogleMap map;
    private FragmentMapBinding binding;
    private MainViewModel mainViewModel;
    private List<Earthquake> earthquakes = new ArrayList<Earthquake>();
    private Marker marker;
    private List<Marker> markers = new ArrayList<Marker>();
    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean granted) {
            if(granted)
                LocationHelper.start(requireContext(),MapFragment.this);
            else{
                Toast.makeText(requireContext(), R.string.location_needed, Toast.LENGTH_SHORT).show();
            }
        }
    });
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);
        this.mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        int fineLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(fineLocation == PackageManager.PERMISSION_DENIED){
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        int fineLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(fineLocation == PackageManager.PERMISSION_GRANTED){
            LocationHelper.start(requireContext(),MapFragment.this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationHelper.stop(requireContext(),MapFragment.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Earthquake earthquake = (Earthquake)marker.getTag();
                if(earthquake != null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DetailActivity.EXTRA_EARTHQUAKE, earthquake);
                    System.out.println(earthquake);
                    Navigation.findNavController(requireView()).navigate(R.id.action_menu_map_to_detailActivity,bundle);
                }
            }
        });
        showMarkers();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng current = new LatLng(location.getLatitude(),location.getLongitude());
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        bounds.include(current);
        if(this.marker == null){
            MarkerOptions opt = new MarkerOptions();
            opt.title("You");
            opt.position(current);
            opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            this.marker = map.addMarker(opt);
        }else{
            marker.setPosition(current);
        }

        if(!markers.isEmpty()) {
            for(Marker marker : markers) {
                marker.remove();
            }
        }
        new Thread(()->{
           if(!earthquakes.isEmpty()){
               for(Earthquake earthquake : earthquakes){
                   Location eLoc = new Location("Earthquake");
                   eLoc.setLatitude(earthquake.getLatitudine());
                   eLoc.setLongitude(earthquake.getLongitudine());
                   //if(eLoc.distanceTo(location) >= 10000) continue;
                   bounds.include(new LatLng(earthquake.getLatitudine(),earthquake.getLongitudine()));
                   createEarthquake(earthquake);
               }
           }
           binding.getRoot().post(()->{
               map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20));
           });
       }).start();
    }

    private void showMarkers(){
        mainViewModel.getEarthquakes().observe(getViewLifecycleOwner(), earthquakes -> {

            MapFragment.this.earthquakes = earthquakes;
            map.clear();
            earthquakes.forEach(this::createEarthquake);
        });
    }

    private void createEarthquake(Earthquake earthquake) {
        MarkerOptions options  = new MarkerOptions();
        options.title(earthquake.getTitle());
        options.position(new LatLng(earthquake.getLatitudine(), earthquake.getLongitudine()));
        if(earthquake.getDanger() == Earthquake.Danger.RED) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else if(earthquake.getDanger() == Earthquake.Danger.ORANGE) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        } else {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }
        requireActivity().runOnUiThread(() -> {
            Marker marker = map.addMarker(options);
            marker.setTag(earthquake);
            markers.add(marker);
        });
    }
}
