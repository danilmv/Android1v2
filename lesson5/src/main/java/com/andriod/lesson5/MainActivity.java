package com.andriod.lesson5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.text_expression);
        findViewById(R.id.button_start).setOnClickListener(v -> {
            String expression = editText.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("calculator://process/" + expression));
            if (intent.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(this, "Calculator not found", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(intent);
            }
        });
    }
}