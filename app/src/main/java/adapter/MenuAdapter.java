    package adapter;

    import android.content.Context;
    import android.content.Intent;
    import android.net.Uri;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.zaykazip.DetailsActivity;
    import com.example.zaykazip.databinding.MenuItemBinding;
    import com.example.zaykazip.model.MenuItem;

    import java.util.List;

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

        private final List<MenuItem> menuItems;
        private final Context context;

        public MenuAdapter(List<MenuItem> menuItem, Context context) {
            this.menuItems = menuItem;
            this.context = context;
        }

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MenuItemBinding binding = MenuItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new MenuViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
            holder.bind(menuItems.get(position));
        }

        @Override
        public int getItemCount() {
            return menuItems.size();
        }

        public class MenuViewHolder extends RecyclerView.ViewHolder {
            private final MenuItemBinding binding;

            public MenuViewHolder(MenuItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(MenuItem menuItem) {
                binding.menufoodName.setText(menuItem.getFoodName());
                binding.menuprice.setText(menuItem.getFoodPrice());

                // Load image with Glide
                Glide.with(context)
                        .load(menuItem.getFoodImage())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_delete)
                        .into(binding.foodImageView);

                // Click → open details
                binding.getRoot().setOnClickListener(v -> {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("MenuItemName", menuItem.getFoodName());
                    intent.putExtra("MenuItemImage", menuItem.getFoodImage());
                    intent.putExtra("MenuItemDescription", menuItem.getFoodDescription());
                    intent.putExtra("MenuItemIngredients", menuItem.getFoodIngredient());
                    intent.putExtra("MenuItemPrice", menuItem.getFoodPrice());
                    context.startActivity(intent);
                });
            }
        }
    }
