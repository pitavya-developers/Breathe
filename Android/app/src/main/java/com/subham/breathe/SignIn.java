package com.subham.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class SignIn extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    ConfigPersistanceStorage configPersistanceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Objects.requireNonNull(getSupportActionBar()).hide();

        configPersistanceStorage = new ConfigPersistanceStorage(SignIn.this);

        if (configPersistanceStorage.getGEmail().length() > 0) {
            startActivity(new Intent(SignIn.this, Home.class));
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public int RC_SIGN_IN = 479;

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private String TAG = "SignIn";

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            configPersistanceStorage.setGEmail(account.getEmail());
            configPersistanceStorage.setGName(account.getDisplayName());
            account.getId();
            account.getPhotoUrl();

            startActivity(new Intent(SignIn.this, Home.class));
            this.finish();

            /* VERY SLOW need to deploy on faster Hosting
            HashMap<String, String> signInDetails = new HashMap<>();

            signInDetails.put("emailId", account.getEmail());
            signInDetails.put("userName", account.getDisplayName());
            signInDetails.put("providedId", account.getId());
            signInDetails.put("profilePicUri", account.getPhotoUrl().toString());

            ProgressDialog progressDoalog = new ProgressDialog(SignIn.this);
            progressDoalog.setMessage(getString(R.string.loading_creating_acount));
            progressDoalog.setTitle(getString(R.string.app_name));

            progressDoalog.show();

            JsonObjectRequest signInRequest = new JsonObjectRequest(getString(R.string.signin_url),
                    new JSONObject(signInDetails),
                    (Response.Listener<JSONObject>) response -> {
                        progressDoalog.hide();
                        startActivity(new Intent(SignIn.this, Home.class));
                        this.finish();
                    },
                    (Response.ErrorListener) error -> {

                    }
            );
            signInRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Volley.newRequestQueue(SignIn.this).add(signInRequest);
        */
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
}