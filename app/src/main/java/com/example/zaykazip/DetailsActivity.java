package com.example.zaykazip;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.zaykazip.databinding.ActivityDetailsBinding;
import com.example.zaykazip.model.CartItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    private String foodName;
    private String foodImage;
    private String foodDescription;
    private String foodIngredient;
    private String foodPrice;

    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        // 🔹 Get data from intent
        foodName = getIntent().getStringExtra("MenuItemName");
        foodDescription = getIntent().getStringExtra("MenuItemDescription");
        foodIngredient = getIntent().getStringExtra("MenuItemIngredients");
        foodPrice = getIntent().getStringExtra("MenuItemPrice");
        foodImage = getIntent().getStringExtra("MenuItemImage");

        // 🔹 Bind data to views
        binding.detailFoodName.setText(foodName);
        binding.detailDescription.setText(foodDescription);
        binding.detailIngredients.setText(foodIngredient);

        Glide.with(this)
                .load(foodImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_delete)
                .into(binding.detailFoodImage);

        // 🔹 Add to cart button
        binding.addItemButton.setOnClickListener(v -> addItemToCart());

        // 🔹 Back button
        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    private void addItemToCart() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String foodQuantity = "1";

        // ✅ Create cart item with unique push key
        String key = database.child(userId).child("CartItems").push().getKey();
        CartItems cartItem = new CartItems(foodName, foodPrice, foodDescription, foodImage, foodQuantity, foodIngredient);
        cartItem.setItemId(key);

        database.child(userId).child("CartItems").child(key).setValue(cartItem)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Item added to cart successfully", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
