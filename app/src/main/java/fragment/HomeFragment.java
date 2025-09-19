package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.zaykazip.MenuBootomSheetFragment;
import com.example.zaykazip.R;
import com.example.zaykazip.databinding.FragmentHomeBinding;
import com.example.zaykazip.model.MenuItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.MenuAdapter;
import adapter.PopularAdapter;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseReference foodRef;
    private List<MenuItem> menuItems = new ArrayList<>();
    private PopularAdapter popularAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Firebase reference
        foodRef = FirebaseDatabase.getInstance().getReference("menu");

        // Open bottom sheet
        binding.viewAllMenu.setOnClickListener(v -> {
            MenuBootomSheetFragment bottomSheetDialog = MenuBootomSheetFragment.newInstance();
            bottomSheetDialog.show(getParentFragmentManager(), "MenuBottomSheet");
        });

        // Load and display random popular items
        retrieveAndDisplayPopularItems();

        return binding.getRoot();
    }

    private void retrieveAndDisplayPopularItems() {
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItem item = foodSnapshot.getValue(MenuItem.class);
                    if (item != null) {
                        menuItems.add(item);
                    }
                }

                // Now show random items
                randomPopularItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void randomPopularItems() {
        if (menuItems.isEmpty()) return;

        // Shuffle the list
        List<MenuItem> shuffled = new ArrayList<>(menuItems);
        Collections.shuffle(shuffled);

        // Pick first 6 or less
        int numItemsToShow = Math.min(6, shuffled.size());
        List<MenuItem> subset = shuffled.subList(0, numItemsToShow);

        setPopularItemAdapter(subset);
    }

    private MenuAdapter menuAdapter;

    private void setPopularItemAdapter(List<MenuItem> subsetMenuItems) {
        menuAdapter = new MenuAdapter(subsetMenuItems, requireContext());
        binding.PopularRecylerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        );
        binding.PopularRecylerView.setAdapter(menuAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Banner images
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT);

        // ImageSlider item click listener
        binding.imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void doubleClick(int i) { }

            @Override
            public void onItemSelected(int position) {
                String message = "Selected Image " + position;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
