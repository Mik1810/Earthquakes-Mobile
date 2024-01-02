package com.example.earthquakemobile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.earthquakemobile.EarthquakeAdapter;
import com.example.earthquakemobile.databinding.FragmentSearchBinding;
import com.example.earthquakemobile.model.Earthquake;
import com.example.earthquakemobile.service.MainViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            MainViewModel mainViewModel = new ViewModelProvider(requireActivity())
                    .get(MainViewModel.class);
            binding.searchList.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.searchList.setAdapter(new EarthquakeAdapter(new ArrayList<Earthquake>()));
            binding.searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() == 0){
                        binding.searchList.setAdapter(new EarthquakeAdapter(new ArrayList<Earthquake>()));
                    }else{
                        String changedText = s.toString();
                        List<Earthquake> searchedEarthquakes = mainViewModel.filterEarthquakesByLocation(changedText);
                        binding.searchList.setAdapter(new EarthquakeAdapter(searchedEarthquakes));
                    }

                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Ciao");
        }
    }
}
