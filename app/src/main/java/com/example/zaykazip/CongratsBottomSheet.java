package com.example.zaykazip;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.zaykazip.databinding.FragmentCongratsBottomSheetBinding;

public class CongratsBottomSheet extends BottomSheetDialogFragment {

    private FragmentCongratsBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout with ViewBinding
        binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false);

        // Button click listener (Java style)
        binding.gohome.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            dismiss(); // close bottom sheet after navigating
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoid memory leaks
    }
}
