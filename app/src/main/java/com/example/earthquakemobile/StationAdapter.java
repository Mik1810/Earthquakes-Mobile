package com.example.earthquakemobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earthquakemobile.databinding.AdapterCityBinding;
import com.example.earthquakemobile.model.Station;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {
    private List<Station> data;

    public StationAdapter(List<Station> data){
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterCityBinding binding = AdapterCityBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Station stat = data.get(position);
        holder.onBind(stat);
    }

    @Override
    public int getItemCount() {
        return this.data != null ?this.data.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        AdapterCityBinding binding;
        public ViewHolder(AdapterCityBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Station stat){
            binding.setStation(stat);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Station station = data.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailActivity.EXTRA_STATION, station);
            Navigation.findNavController(v).navigate(R.id.action_menu_list_to_detailActivity,bundle);
        }
    }
}
