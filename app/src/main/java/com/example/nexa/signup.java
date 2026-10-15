
package com.example.nexa;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends Activity {

    EditText etName, etEmail, etPassword, etConfirmPassword;
    CheckBox cbTerms;
    Button btnSignup;
    TextView btnGoogle, tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        cbTerms = findViewById(R.id.cbTerms);
        btnSignup = findViewById(R.id.btnSignup);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword =
                    etConfirmPassword.getText().toString();

            if (name.isEmpty()) {
                etName.setError("Enter your full name");
                etName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                etEmail.setError("Enter your email");
                etEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Enter a valid email");
                etEmail.requestFocus();
                return;
            }

            if (password.length() < 6) {
                etPassword.setError(
                        "Password must be at least 6 characters");
                etPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError(
                        "Passwords do not match");
                etConfirmPassword.requestFocus();
                return;
            }

            if (!cbTerms.isChecked()) {
                Toast.makeText(
                        signup.this,
                        "Please accept the Terms & Privacy Policy",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Toast.makeText(
                    signup.this,
                    "Form validated! Account creation needs Firebase setup.",
                    Toast.LENGTH_LONG
            ).show();
        });

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(
                        signup.this,
                        "Google Sign-Up needs Firebase setup",
                        Toast.LENGTH_SHORT
                ).show()
        );

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(signup.this, login.class);
            startActivity(intent);
            finish();
        });
    }
}
