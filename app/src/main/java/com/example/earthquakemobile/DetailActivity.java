package com.example.earthquakemobile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.earthquakemobile.databinding.DetailBinding;
import com.example.earthquakemobile.model.Earthquake;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_EARTHQUAKE = "extra";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailBinding binding = DetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Earthquake earthquake = (Earthquake) getIntent().getSerializableExtra(EXTRA_EARTHQUAKE);
        System.out.println(earthquake);
        if(earthquake != null){
            binding.setEarthquake(earthquake);
        }
    }
}
