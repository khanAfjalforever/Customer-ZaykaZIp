package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zaykazip.R;
import com.example.zaykazip.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.MenuAdapter;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private MenuAdapter adapter;

    // Original full menu data
    private final List<String> originalMenuFoodName = new ArrayList<>();
    private final List<String> originalMenuItemPrices = new ArrayList<>();
    private final List<Integer> originalMenuImages = new ArrayList<>();

    // Filtered data (shown in RecyclerView)
    private final List<String> filterMenuFoodName = new ArrayList<>();
    private final List<String> filterMenuItemPrice = new ArrayList<>();
    private final List<Integer> filterMenuImage = new ArrayList<>();

    public SearchFragment() {
        // Initialize menu data
        originalMenuFoodName.add("Cheese Burger");
        originalMenuFoodName.add("Veg Sandwich");
        originalMenuFoodName.add("Chicken Momos");
        originalMenuFoodName.add("Cheese Burger");
        originalMenuFoodName.add("Veg Sandwich");
        originalMenuFoodName.add("Chicken Momos");

        originalMenuItemPrices.add("$5");
        originalMenuItemPrices.add("$4");
        originalMenuItemPrices.add("$6");
        originalMenuItemPrices.add("$5");
        originalMenuItemPrices.add("$4");
        originalMenuItemPrices.add("$6");

        originalMenuImages.add(R.drawable.menu1);
        originalMenuImages.add(R.drawable.menu2);
        originalMenuImages.add(R.drawable.menu3);
        originalMenuImages.add(R.drawable.menu1);
        originalMenuImages.add(R.drawable.menu2);
        originalMenuImages.add(R.drawable.menu3);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        // Set up RecyclerView
       // adapter = new MenuAdapter(filterMenuFoodName, filterMenuItemPrice, filterMenuImage, requireContext());
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        // Setup SearchView
        setupSearchView();

        // Show all items initially
        showAllMenu();

        return binding.getRoot();
    }

    private void showAllMenu() {
        filterMenuFoodName.clear();
        filterMenuItemPrice.clear();
        filterMenuImage.clear();

        filterMenuFoodName.addAll(originalMenuFoodName);
        filterMenuItemPrice.addAll(originalMenuItemPrices);
        filterMenuImage.addAll(originalMenuImages);

        adapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItem(newText);
                return true;
            }
        });
    }

    private void filterMenuItem(String query) {
        filterMenuFoodName.clear();
        filterMenuItemPrice.clear();
        filterMenuImage.clear();

        if (query == null || query.trim().isEmpty()) {
            showAllMenu();
            return;
        }

        for (int i = 0; i < originalMenuFoodName.size(); i++) {
            String foodName = originalMenuFoodName.get(i);
            if (foodName.toLowerCase().contains(query.toLowerCase())) {
                filterMenuFoodName.add(foodName);
                filterMenuItemPrice.add(originalMenuItemPrices.get(i));
                filterMenuImage.add(originalMenuImages.get(i));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
}
