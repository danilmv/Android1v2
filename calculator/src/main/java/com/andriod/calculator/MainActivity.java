package com.andriod.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private final String KEY = "CALC";

    private final int[] buttonIds = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9,
            R.id.button_point,
            R.id.button_c,
            R.id.button_sign, R.id.button_percent,
            R.id.button_divide, R.id.button_multiply, R.id.button_subtract, R.id.button_add, R.id.button_equal};

    private final Action[] actions = {Action.ZERO, Action.ONE, Action.TWO, Action.THREE,
            Action.FOUR, Action.FIVE, Action.SIX, Action.SEVEN, Action.EIGHT, Action.NINE,
            Action.DECIMAL,
            Action.CANCEL,
            Action.SIGN, Action.PERCENT,
            Action.DIVIDE, Action.MULTIPLY, Action.SUBTRACT, Action.ADD, Action.EQUAL};

    private Calculate calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            calculate = new Calculate(findViewById(R.id.text_output), findViewById(R.id.text_output_operation));
        else
            restoreCalculate(savedInstanceState);

        Button button;
        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;
            button = findViewById(buttonIds[i]);
            if (button != null) {
                button.setOnClickListener((View.OnClickListener) v -> {
                    calculate.process(actions[index]);
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY, calculate);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restoreCalculate(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    private void restoreCalculate(@NonNull Bundle savedInstanceState) {
        calculate = (Calculate) savedInstanceState.getSerializable(KEY);
        calculate.setTextView(findViewById(R.id.text_output), findViewById(R.id.text_output_operation));
        calculate.show();
    }
}