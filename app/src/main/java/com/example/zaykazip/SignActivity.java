package com.example.zaykazip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zaykazip.databinding.ActivitySignBinding;
import com.example.zaykazip.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignActivity extends AppCompatActivity {

    private ActivitySignBinding binding;
    private String email, password, username;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Firebase setup
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // ✅ Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        if (task.isSuccessful()) {
                            GoogleSignInAccount account = task.getResult();
                            firebaseAuthWithGoogle(account);
                        } else {
                            Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // ✅ Google Button
        binding.GoogleButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // ✅ Create Account Button
        binding.createAccountButton.setOnClickListener(v -> {
            username = binding.userName.getText().toString().trim();
            email = binding.emailAddress.getText().toString().trim();
            password = binding.password.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details\uD83E\uDDD1", Toast.LENGTH_SHORT).show();
            } else {
                createAccount(email, password);
            }
        });

        // ✅ Navigate to Login
        binding.haveaccount.setOnClickListener(view ->
                startActivity(new Intent(SignActivity.this, LoginActivity.class)));
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Google Sign-In Successful\uD83D\uDE00", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Google Sign-In Failed☹\uFE0F", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account Created Successfully\uD83D\uDE00", Toast.LENGTH_SHORT).show();
                        saveUserData();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Account Creation Failed☹\uFE0F \uD83D\uDE23 ", Toast.LENGTH_SHORT).show();
                        Log.e("SignUp", "createAccount failed☹\uFE0F", task.getException());
                    }
                });
    }

    private void saveUserData() {
        username = binding.userName.getText().toString().trim();
        email = binding.emailAddress.getText().toString().trim();
        password = binding.password.getText().toString().trim();

        UserModel userModel = new UserModel(username, email, password);

        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            database.child("users").child(userId).setValue(userModel)
                    .addOnSuccessListener(unused -> Log.d("SignUp", "User data saved successfully"))
                    .addOnFailureListener(e -> Log.e("SignUp", "Failed to save user data", e));
        }
    }
}
