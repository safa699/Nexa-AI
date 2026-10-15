package com.example.nexa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

public class login extends Activity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView btnGoogle, tvSignup, tvForgot;

    private static final String WEB_CLIENT_ID =
            "530027306515-rneekockgpdn3uf14cece83l5h4q9khm.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgot = findViewById(R.id.tvForgot);

        // Normal Email Login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                etEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Enter a valid email address");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Please enter your password");
                etPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                etPassword.setError(
                        "Password must be at least 6 characters");
                etPassword.requestFocus();
                return;
            }

            openHome(email);
        });

        // Google Sign-In
        btnGoogle.setOnClickListener(v -> signInWithGoogle());

        // Sign Up
        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, signup.class);
            startActivity(intent);
        });

        // Forgot Password
        tvForgot.setOnClickListener(v -> {
            Toast.makeText(
                    login.this,
                    "Password recovery is not configured yet.",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // Google Sign-In Method
    private void signInWithGoogle() {

        CredentialManager credentialManager =
                CredentialManager.create(this);

        GetGoogleIdOption googleIdOption =
                new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(WEB_CLIENT_ID)
                        .setAutoSelectEnabled(false)
                        .build();

        GetCredentialRequest request =
                new GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build();

        credentialManager.getCredentialAsync(
                this,
                request,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(this),
                new CredentialManagerCallback<
                        GetCredentialResponse,
                        GetCredentialException>() {

                    @Override
                    public void onResult(GetCredentialResponse result) {

                        Credential credential = result.getCredential();

                        if (credential instanceof CustomCredential
                                && GoogleIdTokenCredential
                                .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                .equals(credential.getType())) {

                            try {
                                GoogleIdTokenCredential googleCredential =
                                        GoogleIdTokenCredential.createFrom(
                                                ((CustomCredential) credential)
                                                        .getData());

                                String accountId = googleCredential.getId();
                                String email = !accountId.isEmpty() ? accountId : "google.user@gmail.com";

                                Toast.makeText(
                                        login.this,
                                        "Welcome " + email,
                                        Toast.LENGTH_SHORT
                                ).show();

                                openHome(email);

                            } catch (Exception e) {

                                Log.e(
                                        "NexaGoogleSignIn",
                                        "Unable to read Google account",
                                        e
                                );

                                Toast.makeText(
                                        login.this,
                                        "Unable to parse Google account.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {
                            Toast.makeText(
                                    login.this,
                                    "Please select a Google account.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {

                        Log.e(
                                "NexaGoogleSignIn",
                                "Error: " + e.getClass().getSimpleName()
                                        + " | " + e.getMessage(),
                                e
                        );

                        if (e instanceof GetCredentialCancellationException) {
                            Toast.makeText(
                                    login.this,
                                    "Sign-in cancelled.",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        String errorMsg = e.getMessage() != null ? e.getMessage() : "";

                        if (errorMsg.contains("10:") || errorMsg.toLowerCase().contains("developer error")) {
                            Toast.makeText(
                                    login.this,
                                    "Google Sign-In needs SHA-1 added in Google Cloud/Firebase for com.example.nexa",
                                    Toast.LENGTH_LONG
                                    ).show();
                            // Dev fallback to open home
                            openHome("google.user@gmail.com");
                        } else {
                            Toast.makeText(
                                    login.this,
                                    "Google Sign-In Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }
        );
    }

    // Open Home Screen
    private void openHome(String email) {
        Intent intent = new Intent(login.this, HomeActivity.class);
        intent.putExtra("USER_EMAIL", email);
        startActivity(intent);
    }
}
