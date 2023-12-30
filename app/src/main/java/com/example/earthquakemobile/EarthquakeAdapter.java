package com.example.earthquakemobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earthquakemobile.databinding.AdapterCityBinding;
import com.example.earthquakemobile.databinding.AdapterEarthquakeBinding;
import com.example.earthquakemobile.model.Earthquake;
import com.example.earthquakemobile.model.Station;

import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {
    private List<Earthquake> data;

    public EarthquakeAdapter(List<Earthquake> data){
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterEarthquakeBinding binding = AdapterEarthquakeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Earthquake earthquake = data.get(position);
        holder.onBind(earthquake);
    }

    @Override
    public int getItemCount() {
        return this.data != null ?this.data.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        AdapterEarthquakeBinding binding;
        public ViewHolder(AdapterEarthquakeBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Earthquake earthquake){
            binding.setEarthquake(earthquake);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Earthquake earthquake = data.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailActivity.EXTRA_STATION, earthquake);
            Navigation.findNavController(v).navigate(R.id.action_menu_list_to_detailActivity,bundle);
        }
    }
}
