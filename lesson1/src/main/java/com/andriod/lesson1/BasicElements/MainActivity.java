package com.andriod.lesson1.BasicElements;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editNumber1;
    private EditText editNumber2;
    private ToggleButton buttonOperator;
    private Button buttonResult;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNumber1 = findViewById(R.id.editNumber1);
        editNumber2 = findViewById(R.id.editNumber2);
        buttonOperator = findViewById(R.id.buttonOperator);
        buttonResult = findViewById(R.id.buttonResult);
        textResult = findViewById(R.id.textResult);

        buttonResult.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {

            double number1 = Double.parseDouble(editNumber1.getText().toString());
            double number2 = Double.parseDouble(editNumber2.getText().toString());
            boolean operator = buttonOperator.isChecked();
            double result = 0;
            if (operator)
                result = number1 + number2;
            else
                result = number1 - number2;

            textResult.setText(String.format(Locale.getDefault(), "%.0f", result));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите числа", Toast.LENGTH_SHORT).show();
        }
    }
}