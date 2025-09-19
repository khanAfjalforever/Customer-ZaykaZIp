package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zaykazip.PayOutActivity;
import com.example.zaykazip.databinding.FragmentCartBinding;
import com.example.zaykazip.model.CartItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

import adapter.CartAdapter;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference database;

    private List<CartItems> cartItemsList;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        cartItemsList = new ArrayList<>();
        cartAdapter = new CartAdapter(requireContext(), cartItemsList);

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(cartAdapter);

        retrieveCartItems();

        binding.proceedButton.setOnClickListener(v -> {
            sendOrderData();
        });
    }

    private void sendOrderData() {
        ArrayList<String> foodName = new ArrayList<>();
        ArrayList<String> foodPrice = new ArrayList<>();
        ArrayList<String> foodDescription = new ArrayList<>();
        ArrayList<String> foodImage = new ArrayList<>();
        ArrayList<String> foodIngredient = new ArrayList<>();
        ArrayList<String> foodQuantities = cartAdapter.getUpdatedItemsQuantities();

        for (CartItems item : cartItemsList) {
            foodName.add(item.getFoodName());
            foodPrice.add(item.getFoodPrice());
            foodDescription.add(item.getFoodDescription());
            foodImage.add(item.getFoodImage());
            foodIngredient.add(item.getFoodIngredient());
        }

        Intent intent = new Intent(requireContext(), PayOutActivity.class);
        intent.putStringArrayListExtra("foodItemName", foodName);
        intent.putStringArrayListExtra("foodItemPrice", foodPrice);
        intent.putStringArrayListExtra("foodItemDescription", foodDescription);
        intent.putStringArrayListExtra("foodItemImage", foodImage);
        intent.putStringArrayListExtra("foodItemIngredient", foodIngredient);
        intent.putStringArrayListExtra("foodItemQuantities", foodQuantities);


        startActivity(intent);
    }

    private void retrieveCartItems() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        database.child(userId).child("CartItems").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartItemsList.clear();
                        for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                            CartItems item = foodSnapshot.getValue(CartItems.class);
                            if (item != null) {
                                item.setItemId(foodSnapshot.getKey()); //
                                cartItemsList.add(item);
                            }
                        }
                        cartAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
