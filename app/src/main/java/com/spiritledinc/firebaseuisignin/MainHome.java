package com.spiritledinc.firebaseuisignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserInfo;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainHome extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainHome";
    private GoogleSignInClient mGoogleSignInClient;
    private TextView userNameHome;
    private TextView emailHome;
    private TextView validEmailHome;
    private ImageView userPhotoHome;

    private String strMessage;
    Button revokeGoogle;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        revokeGoogle = findViewById(R.id.button_revoke_access);
        revokeGoogle.setVisibility(View.GONE);


        //set spinner to visible
        spinner = findViewById(R.id.progressBarHome);
        spinner.setVisibility(View.VISIBLE);

        fireBaseLogin();
        googleLogin();




    }

    private void fireBaseLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Uri photoUrl = user.getPhotoUrl();
            Log.d(TAG, "Firebase Photo: "+photoUrl);
            userPhotoHome = findViewById(R.id.userPhotoHome);
            userPhotoHome.setImageURI(photoUrl);

            String name = user.getDisplayName();
            Log.d(TAG, "Firebase Name: "+name);
            userNameHome = findViewById(R.id.userNameHome);
            userNameHome.setText(name);

            String email = user.getEmail();
            Log.d(TAG, "Firebase Email: "+email);
            emailHome = findViewById(R.id.emailHome);
            emailHome.setText(email);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            Log.d(TAG, "Firebase Email Verified: "+emailVerified);
            validEmailHome = findViewById(R.id.validEmailHome);
            String email_not_verified = getResources().getString(R.string.email_verified, String.valueOf(emailVerified));
            validEmailHome.setText(email_not_verified);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid(); //asC3tPpbJwawyDIbtcUNwqtTdG63
//            Log.d(TAG, "Firebase uid: "+uid);

//            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
//                @Override
//                public void onSuccess(GetTokenResult result) {
//                    String idToken = result.getToken(); //eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg4ODQ4YjVhZmYyZDUyMDEzMzFhNTQ3ZDE5MDZlNWFhZGY2NTEzYzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZmlyLXVpc2lnbmluLTQ2MGVjIiwiYXVkIjoiZmlyLXVpc2lnbmluLTQ2MGVjIiwiYXV0aF90aW1lIjoxNTg4ODcwNTIxLCJ1c2VyX2lkIjoiYXNDM3RQcGJKd2F3eURJYnRjVU53cXRUZEc2MyIsInN1YiI6ImFzQzN0UHBiSndhd3lESWJ0Y1VOd3F0VGRHNjMiLCJpYXQiOjE1ODg4NzEwODQsImV4cCI6MTU4ODg3NDY4NCwiZW1haWwiOiJteXJlYWxuYW1laXNibGF6ZUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJteXJlYWxuYW1laXNibGF6ZUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.GPn3eZhaKuwaNZIHID_IAj3WgI6UeIJLtp7Ld008tjgfQdJbrgyG8uaam5piz--BlmEtxWf6jIluGS9AWo-PN8zE_0pG0PBPw0-1rvvU9357nsCYRe_6ffXK-ExbshtO-grz4xp3nyIGg304ln8IanDxmP9HzHQsvCTHOAXToC-sytlHhmY3iP1O-vLXoUjhXD9z_zbxP1QhupX0oXVCd8vZMmz9Dv4BFj96QaVCG_uadHPX_kmAQaKyDKhjBk4PekGEDf8qBhS7WGXt43kZ2TQRSk8CjH7RVArR6FW9Ob_j1kQpX02aTqTr8LQIPdHqEKqDI6SqXsm6WKMZVvUm1Q
//                    Log.d(TAG, "Firebase GetTokenResult result = " + idToken);
//                }
//            });

//            for (UserInfo profile : user.getProviderData()) {
//                // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId(); //firebase, password
//                Log.d(TAG, "Firebase providerId: "+providerId);
//                // UID specific to the provider
//                uid = profile.getUid(); //asC3tPpbJwawyDIbtcUNwqtTdG63, myrealnameisblaze@gmail.com
//                Log.d(TAG, "Firebase uid: "+uid);
//                // Name, email address, and profile photo Url
//                name = profile.getDisplayName();
//                Log.d(TAG, "Firebase getDisplayName(): "+name);
//                email = profile.getEmail();
//                Log.d(TAG, "Firebase Email: "+email);
//                photoUrl = profile.getPhotoUrl();
//                Log.d(TAG, "Firebase Photo URL: "+String.valueOf(photoUrl));
//
//            }
            //hide spinner
            spinner = findViewById(R.id.progressBarHome);
            spinner.setVisibility(View.GONE);
        }
    }

    private void googleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //get values of signed in user and set to UI
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            revokeGoogle.setVisibility(View.VISIBLE);

            Uri personPhoto = acct.getPhotoUrl();
            Log.d(TAG, "Google Photo: "+personPhoto);
            strMessage = String.valueOf(personPhoto);
            new getUserHeadshot().execute();

            String personName = acct.getDisplayName();
            Log.d(TAG, "Google Name: "+personName);
            userNameHome = findViewById(R.id.userNameHome);
            userNameHome.setText(personName);

//            String personGivenName = acct.getGivenName();
//            Log.d(TAG, "Google Given Name: "+personGivenName);
//            String personFamilyName = acct.getFamilyName();
//            Log.d(TAG, "Google Family Name: "+personFamilyName);

            String personEmail = acct.getEmail();
            Log.d(TAG, "Google Email: "+personEmail);
            emailHome = findViewById(R.id.emailHome);
            emailHome.setText(personEmail);

            // Check if user's email is verified
            String emailVerified = "Yes";
            validEmailHome = findViewById(R.id.validEmailHome);
            String email_not_verified = getResources().getString(R.string.email_verified, emailVerified);
            validEmailHome.setText(email_not_verified);

//            String personId = acct.getId();
//            Log.d(TAG, "Google Id: "+personId);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_out:
                signOut(v);
            case R.id.button_revoke_access:
                revokeGoogleAccess(v);
            break;
            // add other onclicks here
        }
    }

    public void signOut(View v) {

        //if user logged in with email/password log them out
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, String.valueOf(currentUser));
        if(currentUser != null){
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(MainHome.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        //if user logged in with google log them out
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainHome.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }


    public void revokeGoogleAccess(View view) {
        //set spinner to visible
        spinner = findViewById(R.id.progressBarHome);
        spinner.setVisibility(View.VISIBLE);

        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainHome.this, "App Access Revoked.",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainHome.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private class getUserHeadshot extends AsyncTask<Void, Void, Void> {
        Drawable result;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InputStream is = (InputStream) new URL(strMessage).getContent();
                result = Drawable.createFromStream(is, "src name");
            } catch (IOException e){
                e.printStackTrace();
                Log.d(TAG, "error: "+e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            userPhotoHome = findViewById(R.id.userPhotoHome);
            userPhotoHome.setImageDrawable(result);

            //hide spinner
            spinner = findViewById(R.id.progressBarHome);
            spinner.setVisibility(View.GONE);

            Log.d(TAG, "finished onPostExecute");
            super.onPostExecute(aVoid);
        }
    }


}
