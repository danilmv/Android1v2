package com.andriod.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Calculate.ShowValuesListener {

    private final String KEY = "CALCULATOR_DATA_KEY";

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

    private TextView textMain;
    private TextView textOperation;
    private TextView textHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMain = findViewById(R.id.text_output);
        textOperation = findViewById(R.id.text_output_operation);
        textHistory = findViewById(R.id.text_output_history);

        if (savedInstanceState == null) {
            calculate = new Calculate(this);
        } else {
            restoreCalculate(savedInstanceState);
        }

        Button button;
        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;
            button = findViewById(buttonIds[i]);
            if (button != null) {
                button.setOnClickListener(v -> calculate.process(actions[index]));
            }
        }

        ToggleButton toggleButtonTheme = findViewById(R.id.toggle_button_theme_switch);
        toggleButtonTheme.setChecked((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        toggleButtonTheme.setOnCheckedChangeListener(
                (buttonView, isChecked) -> AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO));

        updateTitle();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY, calculate);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restoreCalculate(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    private void restoreCalculate(@NonNull Bundle savedInstanceState) {
        calculate = savedInstanceState.getParcelable(KEY);
        calculate.setListener(this);
        calculate.show();
    }

    private void updateTitle() {
        Configuration config = getResources().getConfiguration();
        setTitle(String.format(Locale.getDefault(), "%s: %sdpi, %s, %s",
                getTitle(),
                config.densityDpi,
                config.orientation == Configuration.ORIENTATION_PORTRAIT ? "portrait" : "landscape",
                (config.screenHeightDp / 4 * 3 >= config.screenWidthDp - 1) ? "long" : "notlong"));
    }

    @Override
    public void setMainText(String value) {
        textMain.setText(value);
    }

    @Override
    public void appendMainText(String value) {
        textMain.append(value);
    }

    @Override
    public String getMainText() {
        return textMain.getText().toString();
    }

    @Override
    public void setOperationText(String value) {
        textOperation.setText(value);
    }

    @Override
    public void setHistoryText(String value) {
        if (textHistory != null) textHistory.setText(value);
    }

    @Override
    public void appendHistoryText(String value) {
        if (textHistory != null) textHistory.append(value);
    }
}