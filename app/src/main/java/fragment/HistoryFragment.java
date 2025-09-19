package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.zaykazip.R;
import com.example.zaykazip.databinding.FragmentHistoryBinding;
import com.example.zaykazip.model.OrderDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.BuyAgainAdapter;

public class HistoryFragment extends Fragment {

    private BuyAgainAdapter buyAgainAdapter;
    private FragmentHistoryBinding binding;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String userId;
    private List<OrderDetails> listOfOrderItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        listOfOrderItem = new ArrayList<>();

        setupRecyclerView();
        retrieveBuyHistory();

        return binding.getRoot();
    }

    private void retrieveBuyHistory() {
        if (userId == null) return;

        binding.recentBuyItem.setVisibility(View.INVISIBLE);

        DatabaseReference buyItemReference = database.getReference()
                .child("user")
                .child(userId);

        buyItemReference.orderByChild("currentTime")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listOfOrderItem.clear();
                        for (DataSnapshot buySnapshot : snapshot.getChildren()) {
                            OrderDetails buyHistoryItem = buySnapshot.getValue(OrderDetails.class);
                            if (buyHistoryItem != null) {
                                listOfOrderItem.add(buyHistoryItem);
                            }
                        }

                        // Reverse list so latest is first
                        Collections.reverse(listOfOrderItem);

                        if (!listOfOrderItem.isEmpty()) {
                            setDataInRecentBuyItem();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });
    }

    private void setDataInRecentBuyItem() {
        binding.recentBuyItem.setVisibility(View.VISIBLE);

        OrderDetails recentOrderItem = listOfOrderItem.get(0); // first item (latest)

        // Show first food item from order
        if (recentOrderItem.getFoodNames() != null && !recentOrderItem.getFoodNames().isEmpty()) {
            binding.buyAgainFoodName.setText(recentOrderItem.getFoodNames().get(0));
        }

        if (recentOrderItem.getFoodPrices() != null && !recentOrderItem.getFoodPrices().isEmpty()) {
            binding.buyAgainFoodPrice.setText(recentOrderItem.getFoodPrices().get(0));
        }

        if (recentOrderItem.getFoodImages() != null && !recentOrderItem.getFoodImages().isEmpty()) {
            String imageUrl = recentOrderItem.getFoodImages().get(0);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Uri uri = Uri.parse(imageUrl);
                Glide.with(requireContext())
                        .load(uri)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_delete)
                        .into(binding.buyAgainFoodImage);
            }
        }
    }

    private void setupRecyclerView() {
        ArrayList<String> buyAgainFoodName = new ArrayList<>();
        buyAgainFoodName.add("Food1");
        buyAgainFoodName.add("Food2");
        buyAgainFoodName.add("Food3");

        ArrayList<String> buyAgainFoodPrice = new ArrayList<>();
        buyAgainFoodPrice.add("$5");
        buyAgainFoodPrice.add("$6");
        buyAgainFoodPrice.add("$7");

        ArrayList<Integer> buyAgainFoodImage = new ArrayList<>();
        buyAgainFoodImage.add(R.drawable.menu1);
        buyAgainFoodImage.add(R.drawable.menu2);
        buyAgainFoodImage.add(R.drawable.menu3);

        buyAgainAdapter = new BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage);
        binding.buyAgainRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.buyAgainRecyclerView.setAdapter(buyAgainAdapter);
    }
}
