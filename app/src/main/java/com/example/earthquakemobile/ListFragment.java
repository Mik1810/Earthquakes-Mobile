package com.example.earthquakemobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.earthquakemobile.databinding.FragmentListBinding;
import com.example.earthquakemobile.model.Earthquake;
import com.example.earthquakemobile.model.Station;
import com.example.earthquakemobile.service.MainViewModel;

import java.util.List;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            MainViewModel mainViewModel = new ViewModelProvider(requireActivity())
                    .get(MainViewModel.class);
            binding.earthquakeList.setLayoutManager(new LinearLayoutManager(requireContext()));
            mainViewModel.getEarthquakes().observe(getViewLifecycleOwner(), new Observer<List<Earthquake>>() {
                @Override
                public void onChanged(List<Earthquake> earthquakes) {
                    binding.earthquakeList.setAdapter(new EarthquakeAdapter(earthquakes));
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Ciao");
        }
    }
}
