package com.example.earthquakemobile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.earthquakemobile.databinding.DetailBinding;
import com.example.earthquakemobile.model.Station;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_STATION = "extra";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailBinding binding = DetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Station station = (Station) getIntent().getSerializableExtra(EXTRA_STATION);
        System.out.println(station);
        if(station != null){
            binding.setStation(station);
        }
    }
}
