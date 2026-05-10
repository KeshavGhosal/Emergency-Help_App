package com.example.help_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {
    private EditText etName, etBlood, etContact1, etContact2;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("EmergencyPrefs", MODE_PRIVATE);

        if (prefs.contains("name") && !getIntent().getBooleanExtra("isAddingNew", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        etName = findViewById(R.id.etName);
        etBlood = findViewById(R.id.etBlood);
        etContact1 = findViewById(R.id.etContact1);
        etContact2 = findViewById(R.id.etContact2);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String blood = etBlood.getText().toString().trim();
            String c1 = etContact1.getText().toString().trim();
            String c2 = etContact2.getText().toString().trim();

            if (name.isEmpty() || c1.isEmpty()) {
                Toast.makeText(this, "Name and Primary Contact required", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.insertUser(name, blood, c1, c2)) {
                prefs.edit().putString("name", name).putString("blood", blood)
                        .putString("contact1", c1).putString("contact2", c2).apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }
}