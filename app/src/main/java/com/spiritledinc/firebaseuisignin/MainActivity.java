package com.spiritledinc.firebaseuisignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String FIRE_RES_ID_KEY = "com.spiritledinc.firebaseuisignin.firebaseuser";
    private FirebaseAuth mAuth;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //hide spinner
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        //allow submit from password edittext
        EditText edit_txt = findViewById(R.id.fieldPassword);
        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signInEmailPassword(findViewById(android.R.id.content).getRootView());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, String.valueOf(currentUser));
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, MainHome.class);
            startActivity(intent);
        }
    }



    public void register(View view){
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

    public void signInEmailPassword(View view) {
        //check user input
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }

        //set spinner to visible
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        //get email
        EditText emailEditText = findViewById(R.id.fieldEmail);
        String email = emailEditText.getText().toString();

        //get password
        EditText passwordEditText = findViewById(R.id.fieldPassword);
        String password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            spinner.setVisibility(View.GONE);
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, MainHome.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            spinner.setVisibility(View.GONE);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean validate() {
        boolean valid = true;

        EditText emailEditText = findViewById(R.id.fieldEmail);
        String email = emailEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.fieldPassword);
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("enter a valid email address");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            passwordEditText.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}
