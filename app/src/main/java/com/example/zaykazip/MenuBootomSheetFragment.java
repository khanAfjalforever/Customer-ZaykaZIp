package com.example.zaykazip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zaykazip.databinding.FragmentMenuBootomSheetBinding;
import com.example.zaykazip.model.MenuItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.MenuAdapter;

public class MenuBootomSheetFragment extends BottomSheetDialogFragment {

    private FragmentMenuBootomSheetBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference menuRef;
    private List<MenuItem> menuItems;
    private MenuAdapter adapter;

    public static MenuBootomSheetFragment newInstance() {
        return new MenuBootomSheetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        menuRef = database.getReference("menu");
        menuItems = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater, container, false);

        // Setup RecyclerView
        adapter = new MenuAdapter(menuItems, requireContext());
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        // Load data
        retrieveMenuItems();

        return binding.getRoot();
    }

    private void retrieveMenuItems() {
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItem item = foodSnapshot.getValue(MenuItem.class);
                    if (item != null) {
                        menuItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged(); // refresh UI
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
