
package com.example.nexa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

public class HomeActivity extends Activity {

    private TextView tvHomeEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHomeEmail = findViewById(R.id.tvHomeEmail);
        btnLogout = findViewById(R.id.btnLogout);

        String email = getIntent().getStringExtra("USER_EMAIL");

        if (email != null && !email.isEmpty()) {
            tvHomeEmail.setText(email);
        } else {
            tvHomeEmail.setText("No email available");
        }

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this, login.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
