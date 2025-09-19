package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zaykazip.databinding.FragmentProfileBinding;
import com.example.zaykazip.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        setUserData();

        binding.saveInfoButton.setOnClickListener(v -> updateUserData());
    }

    private void setUserData() {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                if (user != null) {
                    binding.name.setText(user.getName());
                    binding.email.setText(user.getEmail());
                    binding.phone.setText(user.getPhone() != null ? user.getPhone() : "");
                    binding.address.setText(user.getAddress() != null ? user.getAddress() : "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData() {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();

        UserModel updatedUser = new UserModel(
                binding.name.getText().toString().trim(),
                binding.email.getText().toString().trim(),
                "", // password not updated here
                binding.phone.getText().toString().trim(),
                binding.address.getText().toString().trim()
        );

        database.child(userId).setValue(updatedUser)
                .addOnSuccessListener(unused ->
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
