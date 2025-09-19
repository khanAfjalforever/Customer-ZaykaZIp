package com.example.zaykazip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zaykazip.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String email, password;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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

        // ✅ Google Sign-In result handler
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        if (task.isSuccessful()) {
                            GoogleSignInAccount account = task.getResult();
                            firebaseAuthWithGoogle(account);
                        } else {
                            Toast.makeText(this, "Google Sign-In Failed☹\uFE0F", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // ✅ Normal login button
        binding.loginbutton.setOnClickListener(view -> {
            email = binding.emailAddres.getText().toString().trim();
            password = binding.passWord.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details\uD83D\uDE00", Toast.LENGTH_SHORT).show();
            } else {
                loginOrCreateUser(email, password);
            }
        });

        // ✅ Google Button
        binding.googlebutton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // ✅ Go to SignUp if needed (optional)
        binding.donthavebutton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignActivity.class);
            startActivity(intent);
        });
    }

    // ✅ Login or Create User if not exists
    private void loginOrCreateUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateUi(auth.getCurrentUser());
            } else {
                // If sign-in fails, try creating the account
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(createTask -> {
                    if (createTask.isSuccessful()) {
                        updateUi(auth.getCurrentUser());
                    } else {
                        Toast.makeText(this, "Login/Sign-up failed:☹\uFE0F " +
                                        (createTask.getException() != null ? createTask.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // ✅ Google Firebase Authentication
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                updateUi(auth.getCurrentUser());
            } else {
                Toast.makeText(this, "Google Authentication Failed ☹\uFE0F", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Navigate to MainActivity
    private void updateUi(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
