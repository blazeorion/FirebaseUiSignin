package com.spiritledinc.firebaseuisignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "CreateAccount";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }

        EditText emailEditText = findViewById(R.id.email);
        String email = emailEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.password);
        String password = passwordEditText.getText().toString();

        createAccount(email,password);
    }


    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(CreateAccount.this, MainHome.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public boolean validate() {
        boolean valid = true;

        EditText nameEditText = findViewById(R.id.name);
        String name = nameEditText.getText().toString();

        EditText emailEditText = findViewById(R.id.email);
        String email = emailEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.password);
        String password = passwordEditText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError("at least 3 characters");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

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
