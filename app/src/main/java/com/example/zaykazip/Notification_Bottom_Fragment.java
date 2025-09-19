package com.example.zaykazip;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.zaykazip.databinding.FragmentNotificationBottomBinding;

import java.util.ArrayList;

import adapter.NotificationAdapter;

public class Notification_Bottom_Fragment extends BottomSheetDialogFragment {

    private FragmentNotificationBottomBinding binding;

    public Notification_Bottom_Fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentNotificationBottomBinding.inflate(inflater, container, false);

        // Create notification data
        ArrayList<String> notifications = new ArrayList<>();
        notifications.add("Your order has been canceled successfully.");
        notifications.add("Order has been taken by driver");
        notifications.add("Congrats! Your order is placed");

        ArrayList<Integer> notificationImages = new ArrayList<>();
        notificationImages.add(R.drawable.sademoji);
        notificationImages.add(R.drawable.truck);
        notificationImages.add(R.drawable.congratulation);

        // Setup RecyclerView
        NotificationAdapter adapter = new NotificationAdapter(notifications, notificationImages);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
