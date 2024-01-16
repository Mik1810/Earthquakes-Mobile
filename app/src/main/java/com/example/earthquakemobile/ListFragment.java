package com.example.earthquakemobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.earthquakemobile.database.DB;
import com.example.earthquakemobile.databinding.FragmentListBinding;
import com.example.earthquakemobile.model.Earthquake;
import com.example.earthquakemobile.service.MainViewModel;

import java.util.List;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainViewModel model = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();
        swipeRefreshLayout = rootView.findViewById(R.id.menu_list);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Dati aggiornati", Toast.LENGTH_SHORT).show();
            // Aggiorna i dati quando si effettua uno swipe down chiamando il metodo refreshData() del MainViewModel
            new Thread(() -> {
                this.model.refreshData();
            }).start();

            // Chiude l'indicatore di aggiornamento
            swipeRefreshLayout.setRefreshing(false);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            this.model = new ViewModelProvider(requireActivity())
                    .get(MainViewModel.class);
            binding.earthquakeList.setLayoutManager(new LinearLayoutManager(requireContext()));
            this.model.getEarthquakes().observe(getViewLifecycleOwner(), new Observer<List<Earthquake>>() {
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
