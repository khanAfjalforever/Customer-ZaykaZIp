package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zaykazip.databinding.CartItemBinding;
import com.example.zaykazip.model.CartItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    private final List<CartItems> cartItems;

    public CartAdapter(Context context, List<CartItems> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ✅ Helper method to get updated quantities
    public ArrayList<String> getUpdatedItemsQuantities() {
        ArrayList<String> quantities = new ArrayList<>();
        for (CartItems item : cartItems) {
            quantities.add(item.getFoodQuantity());
        }
        return quantities;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItems item) {
            binding.cartfoodname.setText(item.getFoodName());
            binding.cartItemPrice.setText(item.getFoodPrice());
            binding.quantity.setText(item.getFoodQuantity());

            Glide.with(context)
                    .load(item.getFoodImage())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(binding.cartImage);

            // Plus button
            binding.plusbutton.setOnClickListener(v -> {
                int qty = Integer.parseInt(item.getFoodQuantity());
                qty++;
                item.setFoodQuantity(String.valueOf(qty));
                binding.quantity.setText(item.getFoodQuantity());
            });

            // Minus button
            binding.minusbutton.setOnClickListener(v -> {
                int qty = Integer.parseInt(item.getFoodQuantity());
                if (qty > 1) {
                    qty--;
                    item.setFoodQuantity(String.valueOf(qty));
                    binding.quantity.setText(item.getFoodQuantity());
                }
            });

            // Delete button (remove from Firebase + adapter)
            binding.deletebutton.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

                    if (userId != null && item.getItemId() != null) {
                        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(userId)
                                .child("CartItems")
                                .child(item.getItemId());

                        dbRef.removeValue()
                                .addOnSuccessListener(unused -> {
                                    cartItems.remove(pos);
                                    notifyItemRemoved(pos);
                                    Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    }
                }
            });
        }
    }
}
