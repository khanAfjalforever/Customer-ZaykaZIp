package com.example.zaykazip;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zaykazip.databinding.ActivityPayOutBinding;
import com.example.zaykazip.model.OrderDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class PayOutActivity extends AppCompatActivity {

    private ActivityPayOutBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private String name, address, phone, userId, totalAmount;

    private ArrayList<String> foodItemName;
    private ArrayList<String> foodItemPrice;
    private ArrayList<String> foodItemDescription;
    private ArrayList<String> foodItemIngredient;
    private ArrayList<String> foodItemQuantities;
    private ArrayList<String> foodItemImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //  Load user data
        setUserData();

        //  Get cart data from intent
        foodItemName = getIntent().getStringArrayListExtra("foodItemName");
        foodItemPrice = getIntent().getStringArrayListExtra("foodItemPrice");
        foodItemDescription = getIntent().getStringArrayListExtra("foodItemDescription");
        foodItemIngredient = getIntent().getStringArrayListExtra("foodItemIngredient");
        foodItemQuantities = getIntent().getStringArrayListExtra("foodItemQuantities");
        foodItemImages = getIntent().getStringArrayListExtra("foodItemImage");

        //  Show total amount
        int total = calculateTotalAmount();
        totalAmount = total + "₹";
        binding.totalAmount.setText(totalAmount);

        //  Place order button click
        binding.PlaceMyOrder.setOnClickListener(v -> placeOrder());
    }
    private void placeOrder() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        userId = user.getUid();
        String itemPushKey = databaseReference.child("OrderDetails").push().getKey();
        long time = System.currentTimeMillis();

        if (itemPushKey == null) {
            Toast.makeText(this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderDetails orderDetails = new OrderDetails(
                userId,
                name,
                foodItemName,
                foodItemPrice,
                foodItemImages,
                foodItemQuantities,
                address,
                totalAmount,
                phone,
                itemPushKey,
                time,
                false,
                false
        );

        // Save order under global "OrderDetails"
        databaseReference.child("OrderDetails").child(itemPushKey)
                .setValue(orderDetails)
                .addOnSuccessListener(unused -> {
                    // Also save under user's order history
                    addOrderToHistory(orderDetails);

                    Toast.makeText(this, "Order placed successfully 🎉", Toast.LENGTH_SHORT).show();
                    removeItemFromCart();

                    // Show Congrats BottomSheet
                    CongratsBottomSheet bottomSheetDialog = new CongratsBottomSheet();
                    bottomSheetDialog.show(getSupportFragmentManager(), "Test");

                 
                    new android.os.Handler().postDelayed(() -> {
                        finish();
                    }, 3000);

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show());
    }


    private void addOrderToHistory(OrderDetails orderDetails) {
        if (userId != null) {
            databaseReference.child("users")
                    .child(userId)
                    .child("BuyHistory")
                    .child(orderDetails.getItemPushKey())
                    .setValue(orderDetails)
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to save to history", Toast.LENGTH_SHORT).show());
        }
    }

    private void removeItemFromCart() {
        if (userId != null) {
            databaseReference.child("users").child(userId).child("CartItems").removeValue();
        }
    }

    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            DatabaseReference userReference = databaseReference.child("users").child(userId);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").getValue(String.class);
                        address = snapshot.child("address").getValue(String.class);
                        phone = snapshot.child("phone").getValue(String.class);

                        binding.name.setText(name != null ? name : "");
                        binding.address.setText(address != null ? address : "");
                        binding.phone.setText(phone != null ? phone : "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PayOutActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int calculateTotalAmount() {
        int totalAmount = 0;

        if (foodItemPrice != null && foodItemQuantities != null) {
            for (int i = 0; i < foodItemPrice.size(); i++) {
                String priceStr = foodItemPrice.get(i);
                int priceValue;

                //  Remove ₹ symbol if present
                if (priceStr.endsWith("₹")) {
                    priceStr = priceStr.substring(0, priceStr.length() - 1);
                }

                try {
                    priceValue = Integer.parseInt(priceStr.trim());
                } catch (NumberFormatException e) {
                    priceValue = 0;
                }

                int quantity = 1;
                try {
                    quantity = Integer.parseInt(foodItemQuantities.get(i));
                } catch (NumberFormatException e) {
                    quantity = 1;
                }

                totalAmount += priceValue * quantity;
            }
        }

        return totalAmount;
    }
}
