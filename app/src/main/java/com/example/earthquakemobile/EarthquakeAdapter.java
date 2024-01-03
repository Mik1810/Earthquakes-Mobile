package com.example.earthquakemobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earthquakemobile.databinding.AdapterEarthquakeBinding;
import com.example.earthquakemobile.model.Earthquake;

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

        Context context = holder.itemView.getContext();

        int lineColor = getLineColor(earthquake, context);
        holder.binding.tickDanger.setBackgroundColor(lineColor);

        holder.onBind(earthquake);

    }

    private int getLineColor(Earthquake earthquake, Context context) {

        switch (earthquake.getDanger()) {
            case RED:
                return ContextCompat.getColor(context, R.color.lineRed);
            case ORANGE:
                return ContextCompat.getColor(context, R.color.lineOrange);
            case YELLOW:
                return ContextCompat.getColor(context, R.color.lineYellow);
            default:
                return ContextCompat.getColor(context, R.color.lineDefault);
        }
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
            bundle.putSerializable(DetailActivity.EXTRA_EARTHQUAKE, earthquake);
            NavController navController = Navigation.findNavController(v);
            int currentFragID = navController.getCurrentDestination().getId();
            if(currentFragID == R.id.menu_search){
                navController.navigate(R.id.action_menu_search_to_detailActivity,bundle);
            }
            if(currentFragID == R.id.menu_list){
                navController.navigate(R.id.action_menu_list_to_detailActivity,bundle);
            }
        }

    }
}
